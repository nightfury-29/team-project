package use_case.save_flight;

import data_transfer_objects.FlightDetailDataTransferObject;
import entity.FlightDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.User;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class SaveFlightInteractorTest {

    private MockFlightGateway flightGateway;
    private MockUserGateway userGateway;
    private MockPresenter presenter;

    private SaveFlightInteractor interactor;

    @BeforeEach
    void setup() {
        flightGateway = new MockFlightGateway();
        userGateway = new MockUserGateway();
        presenter = new MockPresenter();

        interactor = new SaveFlightInteractor(
                flightGateway,
                userGateway,
                presenter
        );
    }

    // ---------------------------------------------------------
    // TEST: Null inputData
    // ---------------------------------------------------------
    @Test
    void testNullInputData() {
        interactor.execute(null);

        assertTrue(presenter.failCalled);
        assertEquals("No flight selected to save.", presenter.message);
    }

    // ---------------------------------------------------------
    // TEST: Null FlightDetail DTO
    // ---------------------------------------------------------
    @Test
    void testNullFlightDetail() {
        SaveFlightInputData input = new SaveFlightInputData(null);

        interactor.execute(input);

        assertTrue(presenter.failCalled);
        assertEquals("No flight selected to save.", presenter.message);
    }

    // ---------------------------------------------------------
    // TEST: Missing flight ID
    // ---------------------------------------------------------
    @Test
    void testMissingFlightId() {
        // Arrange: create a DTO with null id
        FlightDetailDataTransferObject dto =
                new FlightDetailDataTransferObject(
                        null,                       // <-- ID is null
                        5,
                        new FlightDetailDataTransferObject.PriceDTO(100.0, "USD"),
                        "STANDARD",
                        Collections.singletonList(
                                new FlightDetailDataTransferObject.SegmentDTO(
                                        "YYZ", "10:00", "T1",
                                        "JFK", "12:00", "T4",
                                        "AC", "123", "A320", "2h",
                                        "ECONOMY",
                                        new FlightDetailDataTransferObject.BaggageDTO(1, 1),
                                        Collections.singletonList(
                                                new FlightDetailDataTransferObject.AmenityDTO("Bag", "BAG", false)
                                        )
                                )
                        )
                );

        SaveFlightInputData input = new SaveFlightInputData(dto);

        // Act
        interactor.execute(input);

        // Assert
        assertTrue(presenter.failCalled);
        assertEquals("Flight has no ID; cannot save.", presenter.message);
    }

    // ---------------------------------------------------------
    // TEST: Null SaveFlightInputData
    // ---------------------------------------------------------
    @Test
    void testFlightObjectIsNull_getFlightIdBranch() {
        SaveFlightInputData input = new SaveFlightInputData(null);

        assertNull(input.getFlightId());
    }


    // ---------------------------------------------------------
    // TEST: flight id is empty string
    // ---------------------------------------------------------
    @Test
    void testFlightIdIsEmpty() {
        FlightDetailDataTransferObject dto = new FlightDetailDataTransferObject(
                "",                     // <-- empty
                5,
                new FlightDetailDataTransferObject.PriceDTO(100.0, "USD"),
                "STANDARD",
                Collections.singletonList(
                        new FlightDetailDataTransferObject.SegmentDTO(
                                "YYZ", "10:00", "T1",
                                "JFK", "12:00", "T4",
                                "AC", "123", "A320", "2h",
                                "ECONOMY",
                                new FlightDetailDataTransferObject.BaggageDTO(1, 1),
                                Collections.emptyList()
                        )
                )
        );

        SaveFlightInputData input = new SaveFlightInputData(dto);
        interactor.execute(input);

        assertTrue(presenter.failCalled);
        assertEquals("Flight has no ID; cannot save.", presenter.message);
    }


    // ---------------------------------------------------------
    // TEST: No logged-in user
    // ---------------------------------------------------------
    @Test
    void testNoLoggedInUser() {
        userGateway.currentUser = null;

        SaveFlightInputData input = new SaveFlightInputData(makeDummyDTO());

        interactor.execute(input);

        assertTrue(presenter.failCalled);
        assertEquals("No logged-in user found.", presenter.message);
    }


    // ---------------------------------------------------------
    // TEST: Username.isEmpty()
    // ---------------------------------------------------------
    @Test
    void testUsernameIsEmpty() {
        userGateway.currentUser = "";   // simulate empty username

        SaveFlightInputData input = new SaveFlightInputData(makeDummyDTO());
        interactor.execute(input);

        assertTrue(presenter.failCalled);
        assertEquals("No logged-in user found.", presenter.message);
    }

    // ---------------------------------------------------------
    // TEST: Flight already saved
    // ---------------------------------------------------------
    @Test
    void testFlightAlreadyExists() {
        userGateway.currentUser = "bob";
        flightGateway.existingFlightId = "123";

        SaveFlightInputData input = new SaveFlightInputData(makeDummyDTO());

        interactor.execute(input);

        assertTrue(presenter.failCalled);
        assertEquals("This flight is already saved.", presenter.message);
    }

    // ---------------------------------------------------------
    // TEST: Successful save
    // ---------------------------------------------------------
    @Test
    void testSuccessfulSave() {
        userGateway.currentUser = "alice";

        SaveFlightInputData input = new SaveFlightInputData(makeDummyDTO());

        interactor.execute(input);

        assertTrue(presenter.successCalled);
        assertEquals("Flight saved successfully!", presenter.message);

        // Also ensure the gateway was called correctly
        assertNotNull(flightGateway.savedFlight);
    }

    // ---------------------------------------------------------
    // TEST: Exception during save
    // ---------------------------------------------------------
    @Test
    void testExceptionOnSave() {
        userGateway.currentUser = "alice";
        flightGateway.throwExceptionOnSave = true;

        SaveFlightInputData input = new SaveFlightInputData(makeDummyDTO());

        interactor.execute(input);

        assertTrue(presenter.failCalled);
        assertTrue(presenter.message.startsWith("Failed to save flight:"));
    }

    // ---------------------------------------------------------
    // Helper: Dummy FlightDetail DTO
    // ---------------------------------------------------------
    private FlightDetailDataTransferObject makeDummyDTO() {
        FlightDetailDataTransferObject.PriceDTO price =
                new FlightDetailDataTransferObject.PriceDTO(100.0, "USD");

        FlightDetailDataTransferObject.BaggageDTO baggage =
                new FlightDetailDataTransferObject.BaggageDTO(1, 1);

        FlightDetailDataTransferObject.AmenityDTO amenity =
                new FlightDetailDataTransferObject.AmenityDTO("Bag", "BAG", false);

        FlightDetailDataTransferObject.SegmentDTO segment =
                new FlightDetailDataTransferObject.SegmentDTO(
                        "YYZ", "10:00", "T1",
                        "JFK", "12:00", "T4",
                        "AC", "123", "A320", "2h",
                        "ECONOMY", baggage, Collections.singletonList(amenity)
                );

        return new FlightDetailDataTransferObject(
                "123",
                5,
                price,
                "STANDARD",
                Collections.singletonList(segment)
        );
    }

    // ---------------------------------------------------------
    // MOCK CLASSES
    // ---------------------------------------------------------

    class MockFlightGateway implements SaveFlightDataAccessInterface {
        String existingFlightId = null;
        boolean throwExceptionOnSave = false;

        public FlightDetail savedFlight = null;

        @Override
        public boolean flightExistsForUser(String username, String flightId) {
            return flightId.equals(existingFlightId);
        }

        @Override
        public void saveFlightForUser(String username, FlightDetail detail) {
            if (throwExceptionOnSave) {
                throw new RuntimeException("DB error");
            }
            this.savedFlight = detail;
        }

        @Override
        public java.util.List<FlightDetail> getSavedFlights(String username) {
            // For testing, just return the one saved flight (if any)
            if (savedFlight == null) {
                return java.util.Collections.emptyList();
            }
            return java.util.Collections.singletonList(savedFlight);
        }
    }

    class MockUserGateway implements
            SignupUserDataAccessInterface,
            LoginUserDataAccessInterface,
            ChangePasswordUserDataAccessInterface,
            LogoutUserDataAccessInterface {

        private String currentUser = "defaultUser";

        @Override
        public String getCurrentUsername() {
            return currentUser;
        }

        @Override
        public void setCurrentUsername(String name) {
            this.currentUser = name;
        }

        // Dummy implementations for required methods:

        @Override
        public boolean existsByName(String name) {
            return false;
        }

        @Override
        public User get(String username) {
            return null;
        }

        @Override
        public void save(User user) { }

        @Override
        public void changePassword(User user) { }
    }


    class MockPresenter implements SaveFlightOutputBoundary {
        boolean successCalled = false;
        boolean failCalled = false;
        String message = "";

        @Override
        public void prepareSuccessView(SaveFlightOutputData outputData) {
            successCalled = true;
            this.message = outputData.getMessage();
        }

        @Override
        public void prepareFailView(SaveFlightOutputData outputData) {
            failCalled = true;
            this.message = outputData.getMessage();
        }
    }
}


