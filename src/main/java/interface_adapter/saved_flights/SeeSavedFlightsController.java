package interface_adapter.saved_flights;

import use_case.saved_flights.SavedFlightsInputBoundary;
import use_case.saved_flights.SavedFlightsInputData;

/**
 * Controller responsible for initiating the retrieval of a user's saved flights.
 * <p>
 * This controller is invoked when the user requests to view their saved flights.
 * It creates a {@link SavedFlightsInputData} object using the provided username
 * and delegates the execution to the {@link SavedFlightsInputBoundary} interactor.
 * </p>
 */
public class SeeSavedFlightsController {

    /**
     * The interactor that handles the saved flights use case.
     */
    private final SavedFlightsInputBoundary interactor;

    /**
     * Constructs a {@code SeeSavedFlightsController} with the specified interactor.
     *
     * @param interactor the use case input boundary responsible for retrieving saved flights
     */
    public SeeSavedFlightsController(SavedFlightsInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the saved flights retrieval process for the given username.
     * <p>
     * If the interactor is unexpectedly null, an error message is printed and
     * the operation is aborted. Otherwise, a new {@link SavedFlightsInputData}
     * instance is created and passed to the interactor.
     * </p>
     *
     * @param username the username whose saved flights should be retrieved
     */
    public void execute(String username) {
        System.out.println("[Controller] execute called with username = " + username);
        if (interactor == null) {
            System.err.println("[Controller] ERROR: interactor is NULL!");
            return;
        }
        interactor.execute(new SavedFlightsInputData(username));
    }
}
