package use_case.compare_saved_flights;

import entity.FlightDetail;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link CompareSavedFlightsInteractor}.
 */
class CompareSavedFlightsInteractorTest {

    /**
     * A simple presenter stub that records the output passed from the interactor.
     */
    private static class RecordingPresenter implements CompareSavedFlightsOutputBoundary {

        boolean presentCalled = false;
        CompareSavedFlightsOutputData outputData = null;

        @Override
        public void present(CompareSavedFlightsOutputData outputData) {
            this.presentCalled = true;
            this.outputData = outputData;
        }
    }

    @Test
    void compareTwoSimpleFlights_success() {
        // Arrange
        RecordingPresenter presenter = new RecordingPresenter();
        CompareSavedFlightsInteractor interactor = new CompareSavedFlightsInteractor(presenter);

        // First flight: AC123 YYZ -> JFK
        FlightDetail firstFlight = makeFlight(
                "ID-1",
                "AC",
                "123",
                "YYZ",
                "10:00",
                "JFK",
                "12:00",
                200.0,
                "CAD",
                1,
                1
        );

        // Second flight: BA456 LHR -> CDG
        FlightDetail secondFlight = makeFlight(
                "ID-2",
                "BA",
                "456",
                "LHR",
                "08:30",
                "CDG",
                "10:50",
                350.0,
                "EUR",
                2,
                0
        );

        CompareSavedFlightsInputData inputData =
                new CompareSavedFlightsInputData(firstFlight, secondFlight);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.presentCalled, "Presenter should be called.");
        assertNotNull(presenter.outputData, "Output data should not be null.");

        CompareSavedFlightsOutputData.FlightSummary firstSummary =
                presenter.outputData.getFirst();
        CompareSavedFlightsOutputData.FlightSummary secondSummary =
                presenter.outputData.getSecond();

        // First flight assertions
        assertEquals("AC", firstSummary.airline);
        assertEquals("123", firstSummary.flightNumber);
        assertEquals("2h", firstSummary.duration);
        assertEquals(200.0, firstSummary.price);
        assertEquals("CAD", firstSummary.currency);
        assertEquals(1, firstSummary.checkedBags);
        assertEquals(1, firstSummary.cabinBags);

        // Second flight assertions
        assertEquals("BA", secondSummary.airline);
        assertEquals("456", secondSummary.flightNumber);
        assertEquals("2h", secondSummary.duration);
        assertEquals(350.0, secondSummary.price);
        assertEquals("EUR", secondSummary.currency);
        assertEquals(2, secondSummary.checkedBags);
        assertEquals(0, secondSummary.cabinBags);
    }

    @Test
    void compareFlights_handlesNullBaggageAsZero() {
        // Arrange
        RecordingPresenter presenter = new RecordingPresenter();
        CompareSavedFlightsInteractor interactor = new CompareSavedFlightsInteractor(presenter);

        // First flight with baggage info
        FlightDetail firstFlight = makeFlight(
                "ID-1",
                "AC",
                "123",
                "YYZ",
                "10:00",
                "JFK",
                "12:00",
                200.0,
                "CAD",
                1,
                1
        );

        // Second flight: baggage is null
        FlightDetail.Price price = new FlightDetail.Price(150.0, "USD");
        FlightDetail.Baggage baggage = null;  // <-- this is what we want to test

        FlightDetail.SegmentDetail segment = new FlightDetail.SegmentDetail(
                "SFO", "09:00", "T2",
                "LAX", "10:30", "T4",
                "UA", "789", "B737", "1h30m",
                "ECONOMY",
                baggage,
                Collections.emptyList()
        );

        FlightDetail secondFlight = new FlightDetail(
                "ID-2",
                3,
                price,
                "STANDARD",
                Collections.singletonList(segment)
        );

        CompareSavedFlightsInputData inputData =
                new CompareSavedFlightsInputData(firstFlight, secondFlight);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.presentCalled);
        assertNotNull(presenter.outputData);

        CompareSavedFlightsOutputData.FlightSummary secondSummary =
                presenter.outputData.getSecond();

        // Since baggage was null, both counts should default to 0
        assertEquals(0, secondSummary.checkedBags);
        assertEquals(0, secondSummary.cabinBags);
    }

    // -------------------------------------------------------------------------
    // Helper to build a simple single-segment FlightDetail
    // -------------------------------------------------------------------------

    /**
     * Builds a simple {@link FlightDetail} with a single segment and baggage.
     */
    private FlightDetail makeFlight(String id,
                                    String airline,
                                    String flightNumber,
                                    String depAirport,
                                    String depTime,
                                    String arrAirport,
                                    String arrTime,
                                    double priceTotal,
                                    String currency,
                                    int checkedBags,
                                    int cabinBags) {

        FlightDetail.Price price = new FlightDetail.Price(priceTotal, currency);
        FlightDetail.Baggage baggage = new FlightDetail.Baggage(checkedBags, cabinBags);

        FlightDetail.SegmentDetail segment = new FlightDetail.SegmentDetail(
                depAirport, depTime, "T1",
                arrAirport, arrTime, "T2",
                airline, flightNumber, "A320", "2h",
                "ECONOMY",
                baggage,
                Collections.emptyList()
        );

        return new FlightDetail(
                id,
                5,
                price,
                "STANDARD",
                Collections.singletonList(segment)
        );
    }
}
