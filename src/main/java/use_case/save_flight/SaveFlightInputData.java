package use_case.save_flight;

import data_transfer_objects.FlightDetailDataTransferObject;

/**
 * The input data for the Save Flight Use Case.
 */
public class SaveFlightInputData {

    private final FlightDetailDataTransferObject flight;

    public SaveFlightInputData(FlightDetailDataTransferObject flight) {
        this.flight = flight;
    }

    public FlightDetailDataTransferObject getFlightDetail() {
        return flight;
    }

    /**
     * Returns the ID of the flight, or null if no flight exists.
     *
     * @return the flight ID, or null if flight is not set
     */
    public String getFlightId() {
        String id = null;
        if (flight != null) {
            id = flight.id;
        }
        return id;
    }
}
