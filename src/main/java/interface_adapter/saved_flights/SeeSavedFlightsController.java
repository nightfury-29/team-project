package interface_adapter.saved_flights;

import use_case.saved_flights.SavedFlightsInputBoundary;
import use_case.saved_flights.SavedFlightsInputData;

public class SeeSavedFlightsController {

    private final SavedFlightsInputBoundary interactor;

    public SeeSavedFlightsController(SavedFlightsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String username) {
        System.out.println("[Controller] execute called with username = " + username);
        if (interactor == null) {
            System.err.println("[Controller] ERROR: interactor is NULL!");
            return;
        }
        interactor.execute(new SavedFlightsInputData(username));
    }
}
