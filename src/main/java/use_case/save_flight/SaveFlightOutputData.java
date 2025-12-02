package use_case.save_flight;

import data_transfer_objects.FlightDetailDataTransferObject;

/**
 * The output data for the Save Flight Use Case.
 */
public class SaveFlightOutputData {
    private final boolean success;
    private final String message;
    private final FlightDetailDataTransferObject savedFlight;

    public SaveFlightOutputData(boolean success, String message) {
        this(success, message, null);
    }

    public SaveFlightOutputData(boolean success, String message, FlightDetailDataTransferObject savedFlight) {
        this.success = success;
        this.message = message;
        this.savedFlight = savedFlight;
    }

    public String getMessage() {
        return message;
    }
}
