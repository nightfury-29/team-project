package use_case.sort_flights;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import entity.Flight;

class SortFlightsInteractorTest {

    private SortFlightsOutputBoundary mockPresenter;
    private SortFlightsDataAccessInterface mockDao;
    private SortFlightsInteractor interactor;

    @BeforeEach
    void setUp() {
        // 1. Create the mocks
        mockPresenter = mock(SortFlightsOutputBoundary.class);
        mockDao = mock(SortFlightsDataAccessInterface.class);

        // 2. Initialize the interactor with mocks
        interactor = new SortFlightsInteractor(mockPresenter, mockDao);
    }

    @Test
    void testSortByPrice() {
        final Flight expensive = createFlight("AC100", 500.00, "5h00m");
        final Flight cheap = createFlight("AC200", 100.00, "5h00m");
        final Flight mid = createFlight("AC300", 300.00, "5h00m");

        final SortFlightsInputData inputData = new SortFlightsInputData(
                Arrays.asList(expensive, cheap, mid),
                "PRICE"
        );

        interactor.execute(inputData);

        final ArgumentCaptor<SortFlightsOutputData> captor = ArgumentCaptor.forClass(SortFlightsOutputData.class);
        verify(mockPresenter).prepareSuccessView(captor.capture());

        final List<Flight> sorted = captor.getValue().getSortedFlights();
        assertEquals(3, sorted.size());
        assertEquals(cheap, sorted.get(0));
        assertEquals(mid, sorted.get(1));
        assertEquals(expensive, sorted.get(2));

        verify(mockDao).saveSortPreference("PRICE");
    }

    @Test
    void testSortByDuration() {
        final Flight longFlight = createFlight("AC1", 100, "10h30m");
        final Flight shortFlight = createFlight("AC2", 100, "2h15m");
        final Flight mediumFlight = createFlight("AC3", 100, "5h00m");

        final SortFlightsInputData inputData = new SortFlightsInputData(
                Arrays.asList(longFlight, shortFlight, mediumFlight),
                "DURATION"
        );

        interactor.execute(inputData);

        final ArgumentCaptor<SortFlightsOutputData> captor = ArgumentCaptor.forClass(SortFlightsOutputData.class);
        verify(mockPresenter).prepareSuccessView(captor.capture());

        final List<Flight> sorted = captor.getValue().getSortedFlights();
        assertEquals(shortFlight, sorted.get(0));
        assertEquals(mediumFlight, sorted.get(1));
        assertEquals(longFlight, sorted.get(2));

        verify(mockDao).saveSortPreference("DURATION");
    }

    @Test
    void testSortByNonStop() {
        // "AC" -> length 2 (Nonstop)
        final Flight nonStop = createFlight("AC123", 100, "2h");
        // "ACUA" -> length 4 (With stops)
        final Flight withStop = createFlight("AC123 + UA456", 100, "5h");

        final SortFlightsInputData inputData = new SortFlightsInputData(
                Arrays.asList(nonStop, withStop),
                "NONSTOP"
        );

        interactor.execute(inputData);

        final ArgumentCaptor<SortFlightsOutputData> captor = ArgumentCaptor.forClass(SortFlightsOutputData.class);
        verify(mockPresenter).prepareSuccessView(captor.capture());

        final List<Flight> sorted = captor.getValue().getSortedFlights();
        assertEquals(1, sorted.size());
        assertEquals(nonStop, sorted.get(0));

        verify(mockDao).saveSortPreference("NONSTOP");
    }

    @Test
    void testSortWithNullDao() {
        interactor = new SortFlightsInteractor(mockPresenter, null);
        final Flight f1 = createFlight("AC1", 100, "1h");
        final SortFlightsInputData inputData = new SortFlightsInputData(List.of(f1), "PRICE");

        interactor.execute(inputData);

        verify(mockPresenter).prepareSuccessView(any(SortFlightsOutputData.class));
    }

    @Test
    void testEmptyList() {
        final SortFlightsInputData inputData = new SortFlightsInputData(new ArrayList<>(), "PRICE");

        interactor.execute(inputData);

        final ArgumentCaptor<SortFlightsOutputData> captor = ArgumentCaptor.forClass(SortFlightsOutputData.class);
        verify(mockPresenter).prepareSuccessView(captor.capture());
        assertTrue(captor.getValue().getSortedFlights().isEmpty());
    }

    @Test
    void testSingleFlight() {
        final Flight f1 = createFlight("AC1", 100, "1h");
        final SortFlightsInputData inputData = new SortFlightsInputData(List.of(f1), "PRICE");

        interactor.execute(inputData);

        final ArgumentCaptor<SortFlightsOutputData> captor = ArgumentCaptor.forClass(SortFlightsOutputData.class);
        verify(mockPresenter).prepareSuccessView(captor.capture());
        assertEquals(1, captor.getValue().getSortedFlights().size());
    }

    @Test
    void testInvalidDurationFormat() {
        final Flight badDuration = createFlight("AC1", 100, "invalid");
        final Flight goodDuration = createFlight("AC2", 100, "1h");

        final SortFlightsInputData inputData = new SortFlightsInputData(
                Arrays.asList(goodDuration, badDuration),
                "DURATION"
        );

        interactor.execute(inputData);

        final ArgumentCaptor<SortFlightsOutputData> captor = ArgumentCaptor.forClass(SortFlightsOutputData.class);
        verify(mockPresenter).prepareSuccessView(captor.capture());

        final List<Flight> sorted = captor.getValue().getSortedFlights();
        assertEquals(badDuration, sorted.get(0));
        assertEquals(goodDuration, sorted.get(1));
    }

    @Test
    void testEqualPrices() {
        final Flight f1 = createFlight("AC1", 100.00, "1h");
        final Flight f2 = createFlight("AC2", 100.00, "2h");

        final SortFlightsInputData inputData = new SortFlightsInputData(
                Arrays.asList(f2, f1),
                "PRICE"
        );

        interactor.execute(inputData);

        final ArgumentCaptor<SortFlightsOutputData> captor = ArgumentCaptor.forClass(SortFlightsOutputData.class);
        verify(mockPresenter).prepareSuccessView(captor.capture());
        assertEquals(2, captor.getValue().getSortedFlights().size());
    }

    @Test
    void testDurationWithHoursOnly() {
        final Flight hoursOnly = createFlight("AC1", 100, "3h");
        final Flight goodDuration = createFlight("AC2", 100, "1h");

        final SortFlightsInputData inputData = new SortFlightsInputData(
                Arrays.asList(hoursOnly, goodDuration),
                "DURATION"
        );

        interactor.execute(inputData);

        final ArgumentCaptor<SortFlightsOutputData> captor = ArgumentCaptor.forClass(SortFlightsOutputData.class);
        verify(mockPresenter).prepareSuccessView(captor.capture());

        final List<Flight> sorted = captor.getValue().getSortedFlights();
        assertEquals(goodDuration, sorted.get(0));
        assertEquals(hoursOnly, sorted.get(1));
    }

    @Test
    void testDurationWithMinutesOnly() {
        final Flight minutesOnly = createFlight("AC1", 100, "50m");
        final Flight goodDuration = createFlight("AC2", 100, "1h");

        final SortFlightsInputData inputData = new SortFlightsInputData(
                Arrays.asList(minutesOnly, goodDuration),
                "DURATION"
        );

        interactor.execute(inputData);

        final ArgumentCaptor<SortFlightsOutputData> captor = ArgumentCaptor.forClass(SortFlightsOutputData.class);
        verify(mockPresenter).prepareSuccessView(captor.capture());

        final List<Flight> sorted = captor.getValue().getSortedFlights();
        assertEquals(minutesOnly, sorted.get(0));
        assertEquals(goodDuration, sorted.get(1));
    }

    @Test
    void testSortByUnknownType() {
        final Flight longDuration = createFlight("AC1", 100, "10h30m");
        final Flight shortDuration = createFlight("AC2", 100, "2h15m");

        final SortFlightsInputData inputData = new SortFlightsInputData(
                Arrays.asList(longDuration, shortDuration),
                "UNKNOWN_SORT"
        );

        interactor.execute(inputData);

        final ArgumentCaptor<SortFlightsOutputData> captor = ArgumentCaptor.forClass(SortFlightsOutputData.class);
        verify(mockPresenter).prepareSuccessView(captor.capture());

        final List<Flight> sorted = captor.getValue().getSortedFlights();
        assertEquals(shortDuration, sorted.get(0));
        assertEquals(longDuration, sorted.get(1));
        verify(mockDao).saveSortPreference("UNKNOWN_SORT");
    }

    @Test
    void testInvalidHoursFormat() {
        // "badh30m" -> parseInt("bad") throws exception -> hours=0, min=30 -> 30 mins
        final Flight badHours = createFlight("AC1", 100, "badh30m");
        // 60 mins
        final Flight goodDuration = createFlight("AC2", 100, "1h");

        final SortFlightsInputData inputData = new SortFlightsInputData(
                Arrays.asList(badHours, goodDuration),
                "DURATION"
        );

        interactor.execute(inputData);

        final ArgumentCaptor<SortFlightsOutputData> captor = ArgumentCaptor.forClass(SortFlightsOutputData.class);
        verify(mockPresenter).prepareSuccessView(captor.capture());

        final List<Flight> sorted = captor.getValue().getSortedFlights();
        assertEquals(badHours, sorted.get(0));
        assertEquals(goodDuration, sorted.get(1));
    }

    @Test
    void testInvalidMinutesFormat() {
        // "1hbadm" -> parseInt("bad") throws exception -> hours=1, min=0 -> 60 mins
        final Flight badMinutes = createFlight("AC1", 100, "1hbadm");
        // 30 mins
        final Flight goodDuration = createFlight("AC2", 100, "30m");

        final SortFlightsInputData inputData = new SortFlightsInputData(
                Arrays.asList(badMinutes, goodDuration),
                "DURATION"
        );

        interactor.execute(inputData);

        final ArgumentCaptor<SortFlightsOutputData> captor = ArgumentCaptor.forClass(SortFlightsOutputData.class);
        verify(mockPresenter).prepareSuccessView(captor.capture());

        final List<Flight> sorted = captor.getValue().getSortedFlights();
        assertEquals(goodDuration, sorted.get(0));
        assertEquals(badMinutes, sorted.get(1));
    }

    // --- NEW TESTS FOR 100% BRANCH COVERAGE ---

    @Test
    void testSortByNonStopWithNullAirline() {
        // Covers: if (f.airline != null) -> false branch
        final Flight valid = createFlight("AC12", 100, "2h");
        final Flight nullAirline = createFlight(null, 100, "2h");

        final SortFlightsInputData inputData = new SortFlightsInputData(
                Arrays.asList(valid, nullAirline),
                "NONSTOP"
        );

        interactor.execute(inputData);

        final ArgumentCaptor<SortFlightsOutputData> captor = ArgumentCaptor.forClass(SortFlightsOutputData.class);
        verify(mockPresenter).prepareSuccessView(captor.capture());

        final List<Flight> sorted = captor.getValue().getSortedFlights();
        // Null airline flight should be ignored/removed
        assertEquals(1, sorted.size());
        assertEquals(valid, sorted.get(0));
    }

    @Test
    void testSortByDurationWithNullDuration() {
        // Covers: if (duration != null) -> false branch in calculateDurationInMinutes
        // null duration results in 0 minutes
        final Flight nullDuration = createFlight("AC1", 100, null);
        final Flight normal = createFlight("AC2", 100, "1h");

        final SortFlightsInputData inputData = new SortFlightsInputData(
                Arrays.asList(normal, nullDuration),
                "DURATION"
        );

        interactor.execute(inputData);

        final ArgumentCaptor<SortFlightsOutputData> captor = ArgumentCaptor.forClass(SortFlightsOutputData.class);
        verify(mockPresenter).prepareSuccessView(captor.capture());

        final List<Flight> sorted = captor.getValue().getSortedFlights();
        // null duration (0 mins) should come first
        assertEquals(nullDuration, sorted.get(0));
        assertEquals(normal, sorted.get(1));
    }

    // Helper method to create dummy Flight objects
    private Flight createFlight(String airline, double price, String duration) {
        return new Flight(
                "ID", "YYZ", "2025-01-01", "YVR", "2025-01-01",
                price, "CAD", duration, airline, "Boeing 747"
        );
    }
}
