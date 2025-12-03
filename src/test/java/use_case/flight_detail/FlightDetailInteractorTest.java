package use_case.flight_detail;

import entity.FlightDetail;
import interface_adapter.ViewManagerModel;
import interface_adapter.flight_detail.FlightDetailPresenter;
import interface_adapter.flight_detail.FlightDetailState;
import interface_adapter.flight_detail.FlightDetailViewModel;
import interface_adapter.flight_results.FlightResultsViewModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlightDetailInteractorTest {

    static class FakeFlightDetailDAO implements FlightDetailDataAccessInterface {

        public FlightDetail detailToReturn;
        public String lastRequestedId;

        @Override
        public FlightDetail fetchDetail(String flightId) {
            this.lastRequestedId = flightId;
            return detailToReturn;
        }
    }

    private FlightDetail createSampleDetail() {

        // Create a simple baggage object
        FlightDetail.Baggage baggage = new FlightDetail.Baggage(
                1,  // checked bags
                1   // cabin bags
        );

        // One sample amenity list
        List<FlightDetail.Amenity> amenities = List.of(
                new FlightDetail.Amenity("WiFi", "INFLIGHT_WIFI", false),
                new FlightDetail.Amenity("Meal", "MEAL", false)
        );

        FlightDetail.SegmentDetail segment = new FlightDetail.SegmentDetail(
                "YYZ",
                "2026-01-12T17:55:00",
                "T1",
                "PEK",
                "2026-01-14T17:25:00",
                "T2",
                "UA",
                "888",
                "777",
                "20h 30m",
                "ECONOMY",
                baggage,
                amenities
        );

        // Sample price
        FlightDetail.Price price = new FlightDetail.Price(
                501.16,
                "EUR"
        );

        // Construct final FlightDetail object
        return new FlightDetail(
                "21",
                9,
                price,
                "STANDARD",
                List.of(segment)
        );
    }

    @Test
    void testSuccessFlow() {
        // --- Arrange ---
        FakeFlightDetailDAO fakeDao = new FakeFlightDetailDAO();
        fakeDao.detailToReturn = createSampleDetail();

        FlightDetailViewModel viewModel = new FlightDetailViewModel();
        FlightResultsViewModel resultsViewModel = new FlightResultsViewModel();
        ViewManagerModel viewManagerModel = new ViewManagerModel();

        FlightDetailPresenter presenter = new FlightDetailPresenter(
                viewModel,
                resultsViewModel,
                viewManagerModel
        );

        FlightDetailInputBoundary interactor =
                new FlightDetailInteractor(fakeDao, presenter);

        // --- Act ---
        FlightDetailInputData inputData =
                new FlightDetailInputData("21");
        interactor.execute(inputData);

        // --- Assert ViewModel ---
        FlightDetailState state = viewModel.getState();
        assertNotNull(state.getFlightDetail(), "FlightDetail should be set in state!");

        assertEquals("21", state.getFlightDetail().id);
        assertEquals(9, state.getFlightDetail().numberOfBookableSeats);
        assertEquals("STANDARD", state.getFlightDetail().fareOption);

        assertEquals("21", fakeDao.lastRequestedId);

        assertEquals("flight detail", viewManagerModel.getState());
    }

    @Test
    void testFailFlow_whenDaoReturnsNull() {
        // --- Arrange ---
        FakeFlightDetailDAO fakeDao = new FakeFlightDetailDAO();
        fakeDao.detailToReturn = null;

        FlightDetailViewModel viewModel = new FlightDetailViewModel();
        FlightResultsViewModel resultsViewModel = new FlightResultsViewModel();
        ViewManagerModel vm = new ViewManagerModel();

        FlightDetailPresenter presenter =
                new FlightDetailPresenter(viewModel, resultsViewModel, vm);

        FlightDetailInteractor interactor =
                new FlightDetailInteractor(fakeDao, presenter);

        // --- Act ---
        FlightDetailInputData inputData =
                new FlightDetailInputData("99");
        interactor.execute(inputData);

        // --- Assert ---
        assertNull(viewModel.getState().getFlightDetail(),
                "When DAO returns null, no FlightDetail should be set.");

        assertNotNull(viewModel.getState().getErrorMessage());

        assertEquals("Could not retrieve flight details.",
                viewModel.getState().getErrorMessage());

        assertEquals("flight detail", vm.getState());
    }

    @Test
    void testFailFlow_whenFlightIdIsNull() {
        // Arrange
        FakeFlightDetailDAO fakeDao = new FakeFlightDetailDAO();

        FlightDetailViewModel viewModel = new FlightDetailViewModel();
        FlightResultsViewModel resultsViewModel = new FlightResultsViewModel();
        ViewManagerModel vm = new ViewManagerModel();

        FlightDetailPresenter presenter =
                new FlightDetailPresenter(viewModel, resultsViewModel, vm);

        FlightDetailInteractor interactor =
                new FlightDetailInteractor(fakeDao, presenter);

        // Act
        FlightDetailInputData inputData = new FlightDetailInputData(null);
        interactor.execute(inputData);

        // Assert
        assertEquals("Error: Flight is null.", viewModel.getState().getErrorMessage());
        assertEquals("flight detail", vm.getState());
    }

    static class ExceptionThrowingDAO implements FlightDetailDataAccessInterface {
        @Override
        public FlightDetail fetchDetail(String flightId) {
            throw new RuntimeException("DAO failure");
        }
    }

    @Test
    void testFailFlow_whenDaoThrowsException() {
        // Arrange
        FlightDetailDataAccessInterface fakeDao = new ExceptionThrowingDAO();

        FlightDetailViewModel viewModel = new FlightDetailViewModel();
        FlightResultsViewModel resultsViewModel = new FlightResultsViewModel();
        ViewManagerModel vm = new ViewManagerModel();

        FlightDetailPresenter presenter =
                new FlightDetailPresenter(viewModel, resultsViewModel, vm);

        FlightDetailInteractor interactor =
                new FlightDetailInteractor(fakeDao, presenter);

        // Act
        FlightDetailInputData inputData = new FlightDetailInputData("21");
        interactor.execute(inputData);

        // Assert
        assertTrue(
                viewModel.getState().getErrorMessage().contains("An error occurred"),
                "Error message should indicate exception"
        );
        assertEquals("flight detail", vm.getState());
    }
}
