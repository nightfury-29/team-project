package use_case.save_flight;

import entity.FlightDetail;

/**
 * The input data for the Save Flight Use Case.
 */
public class SaveFlightInputData {

    private final FlightDetail flight;

    public SaveFlightInputData(FlightDetail flight) {
        this.flight = flight;
    }

    public FlightDetail getFlightDetail() {
        return flight;
    }

    public String getFlightId() {
        return flight == null ? null : flight.id; // adjust if you use getter
    }
}
