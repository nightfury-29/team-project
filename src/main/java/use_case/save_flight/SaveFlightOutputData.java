package use_case.save_flight;

import entity.FlightDetail;


/**
 * The output data for the Save Flight Use Case.
 */
public class SaveFlightOutputData {
    private final boolean success;
    private final String message;
    private final FlightDetail savedFlight; // optional, can be null

    public SaveFlightOutputData(boolean success, String message) {
        this(success, message, null);
    }

    public SaveFlightOutputData(boolean success, String message, FlightDetail savedFlight) {
        this.success = success;
        this.message = message;
        this.savedFlight = savedFlight;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public FlightDetail getSavedFlight() { return savedFlight; }
}
