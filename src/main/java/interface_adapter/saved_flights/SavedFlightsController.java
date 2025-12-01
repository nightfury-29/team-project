package interface_adapter.saved_flights;

import use_case.saved_flights.SavedFlightsInputBoundary;
import use_case.saved_flights.SavedFlightsInputData;

/**
 * Controller for retrieving saved flights for a specific user.
 * <p>
 * This controller receives the username from the UI layer and creates
 * a {@link SavedFlightsInputData} object, which is then passed to the
 * {@link SavedFlightsInputBoundary} interactor to execute the use case.
 * </p>
 */
public class SavedFlightsController {

    /**
     * The interactor responsible for executing the saved flights use case.
     */
    private final SavedFlightsInputBoundary interactor;

    /**
     * Constructs a {@code SavedFlightsController} with the given interactor.
     *
     * @param interactor the use case input boundary handling saved flight retrieval
     */
    public SavedFlightsController(SavedFlightsInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Requests the retrieval of saved flights for the specified user.
     * <p>
     * This method packages the given username into a {@link SavedFlightsInputData}
     * object and delegates the execution to the interactor.
     * </p>
     *
     * @param username the username whose saved flights are being requested
     */
    public void getSavedFlights(String username) {
        SavedFlightsInputData inputData = new SavedFlightsInputData(username);
        interactor.execute(inputData);
    }
}
