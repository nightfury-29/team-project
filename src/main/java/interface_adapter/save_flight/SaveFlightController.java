package interface_adapter.save_flight;

import data_transfer_objects.FlightDetailDataTransferObject;
import use_case.save_flight.SaveFlightInputBoundary;
import use_case.save_flight.SaveFlightInputData;

/**
 * Controller responsible for handling requests to save a flight.
 * <p>
 * This controller receives user actions from the UI (such as clicking
 * the "Save Flight" button), converts the displayed
 * {@link FlightDetailDataTransferObject} into the appropriate input data object,
 * and delegates the operation to the save flight use case interactor.
 * </p>
 */
public class SaveFlightController {

    /**
     * The interactor that executes the save flight use case.
     */
    private final SaveFlightInputBoundary saveFlightinteractor;

    /**
     * Constructs a {@code SaveFlightController} with the specified interactor.
     *
     * @param interactor the use case input boundary responsible for saving flights
     */
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
