package interface_adapter.save_flight;

import data_transfer_objects.FlightDetailDataTransferObject;
import use_case.save_flight.SaveFlightInputBoundary;
import use_case.save_flight.SaveFlightInputData;

public class SaveFlightController {
    private final SaveFlightInputBoundary saveFlightinteractor;

    public SaveFlightController(SaveFlightInputBoundary interactor) {
        this.saveFlightinteractor = interactor;
    }

    /**
     * Handles the save button action from the UI.
     * Converts the FlightDetailDataTransferObject into input data
     * and passes it to the interactor.
     *
     * @param detail the flight details to save
     */
    public void handleSaveButton(FlightDetailDataTransferObject detail) {
        // This method is called from the Swing action listener, pass the FlightDetail
        saveFlightinteractor.execute(new SaveFlightInputData(detail));
    }
}
