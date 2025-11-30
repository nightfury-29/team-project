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
        // Arrange: Create flights with different prices
        final Flight expensive = createFlight("AC100", 500.00, "5h00m");
        final Flight cheap = createFlight("AC200", 100.00, "5h00m");
        final Flight mid = createFlight("AC300", 300.00, "5h00m");

        final SortFlightsInputData inputData = new SortFlightsInputData(
                Arrays.asList(expensive, cheap, mid),
                "PRICE"
        );

        // Act: Execute the use case
        interactor.execute(inputData);

        // Assert: Capture the output and verify the order
        final ArgumentCaptor<SortFlightsOutputData> captor = ArgumentCaptor.forClass(SortFlightsOutputData.class);
        verify(mockPresenter).prepareSuccessView(captor.capture());

        final List<Flight> sorted = captor.getValue().getSortedFlights();
        assertEquals(3, sorted.size());
        // 100.00
        assertEquals(cheap, sorted.get(0));
        // 300.00
        assertEquals(mid, sorted.get(1));
        // 500.00
        assertEquals(expensive, sorted.get(2));

        // Verify DAO was called
        verify(mockDao).saveSortPreference("PRICE");
    }

    @Test
    void testSortByDuration() {
        // Arrange: Create flights with different durations strings
        // 630 min
        final Flight longFlight = createFlight("AC1", 100, "10h30m");
        // 135 min
        final Flight shortFlight = createFlight("AC2", 100, "2h15m");
        // 300 min
        final Flight mediumFlight = createFlight("AC3", 100, "5h00m");

        final SortFlightsInputData inputData = new SortFlightsInputData(
                Arrays.asList(longFlight, shortFlight, mediumFlight),
                "DURATION"
        );

        // Act
        interactor.execute(inputData);

        // Assert
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
        // Arrange
        // Logic assumes: "AC123" (len 2 letters) is nonstop.
        // "AC123 + UA456" (len 4 letters) has stops.
        // "AC" -> length 2
        final Flight nonStop = createFlight("AC123", 100, "2h");
        // "ACUA" -> length 4
        final Flight withStop = createFlight("AC123 + UA456", 100, "5h");

        final SortFlightsInputData inputData = new SortFlightsInputData(
                Arrays.asList(nonStop, withStop),
                "NONSTOP"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        final ArgumentCaptor<SortFlightsOutputData> captor = ArgumentCaptor.forClass(SortFlightsOutputData.class);
        verify(mockPresenter).prepareSuccessView(captor.capture());

        final List<Flight> sorted = captor.getValue().getSortedFlights();
        assertEquals(1, sorted.size());
        // Only non-stop should remain
        assertEquals(nonStop, sorted.get(0));

        verify(mockDao).saveSortPreference("NONSTOP");
    }

    @Test
    void testSortWithNullDao() {
        // Arrange: Initialize interactor with NULL DAO to ensure it doesn't crash
        interactor = new SortFlightsInteractor(mockPresenter, null);
        final Flight f1 = createFlight("AC1", 100, "1h");
        final SortFlightsInputData inputData = new SortFlightsInputData(List.of(f1), "PRICE");

        // Act
        interactor.execute(inputData);

        // Assert
        verify(mockPresenter).prepareSuccessView(any(SortFlightsOutputData.class));
        // No Exception thrown
    }

    @Test
    void testEmptyList() {
        // Arrange: Empty list input
        final SortFlightsInputData inputData = new SortFlightsInputData(new ArrayList<>(), "PRICE");

        // Act
        interactor.execute(inputData);

        // Assert
        final ArgumentCaptor<SortFlightsOutputData> captor = ArgumentCaptor.forClass(SortFlightsOutputData.class);
        verify(mockPresenter).prepareSuccessView(captor.capture());

        final List<Flight> sorted = captor.getValue().getSortedFlights();
        assertTrue(sorted.isEmpty());
    }

    @Test
    void testSingleFlight() {
        // Arrange: List with one flight
        final Flight f1 = createFlight("AC1", 100, "1h");
        final SortFlightsInputData inputData = new SortFlightsInputData(List.of(f1), "PRICE");

        // Act
        interactor.execute(inputData);

        // Assert
        final ArgumentCaptor<SortFlightsOutputData> captor = ArgumentCaptor.forClass(SortFlightsOutputData.class);
        verify(mockPresenter).prepareSuccessView(captor.capture());

        final List<Flight> sorted = captor.getValue().getSortedFlights();
        assertEquals(1, sorted.size());
        assertEquals(f1, sorted.get(0));
    }

    @Test
    void testInvalidDurationFormat() {
        // Arrange: Flight with bad duration string
        // The parsing logic catches exceptions and defaults to 0 minutes
        final Flight badDuration = createFlight("AC1", 100, "invalid");
        // 60 mins
        final Flight goodDuration = createFlight("AC2", 100, "1h");

        final SortFlightsInputData inputData = new SortFlightsInputData(
                Arrays.asList(goodDuration, badDuration),
                "DURATION"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        final ArgumentCaptor<SortFlightsOutputData> captor = ArgumentCaptor.forClass(SortFlightsOutputData.class);
        verify(mockPresenter).prepareSuccessView(captor.capture());

        final List<Flight> sorted = captor.getValue().getSortedFlights();
        // badDuration (0 mins) should come before goodDuration (60 mins)
        assertEquals(badDuration, sorted.get(0));
        assertEquals(goodDuration, sorted.get(1));
    }
    @Test
    void testEqualPrices() {
        // Arrange: Two flights with same price
        final Flight f1 = createFlight("AC1", 100.00, "1h");
        final Flight f2 = createFlight("AC2", 100.00, "2h");

        final SortFlightsInputData inputData = new SortFlightsInputData(
                Arrays.asList(f2, f1),
                "PRICE"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        final ArgumentCaptor<SortFlightsOutputData> captor = ArgumentCaptor.forClass(SortFlightsOutputData.class);
        verify(mockPresenter).prepareSuccessView(captor.capture());

        final List<Flight> sorted = captor.getValue().getSortedFlights();
        assertEquals(2, sorted.size());
        // Since it's a stable sort or order doesn't matter for equal values,
        // we just verify both are present.
        assertTrue(sorted.contains(f1));
        assertTrue(sorted.contains(f2));
    }

    // Helper method to create dummy Flight objects quickly
    private Flight createFlight(String airline, double price, String duration) {
        return new Flight(
                "ID", "YYZ", "2025-01-01", "YVR", "2025-01-01",
                price, "CAD", duration, airline, "Boeing 747"
        );
    }
}