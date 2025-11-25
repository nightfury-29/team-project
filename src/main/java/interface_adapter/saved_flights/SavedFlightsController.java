package interface_adapter.saved_flights;

import use_case.saved_flights.SavedFlightsInputBoundary;
import use_case.saved_flights.SavedFlightsInputData;

public class SavedFlightsController {

    private final SavedFlightsInputBoundary interactor;

    public SavedFlightsController(SavedFlightsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void getSavedFlights(String username) {
        SavedFlightsInputData inputData = new SavedFlightsInputData(username);
        interactor.execute(inputData);
    }
}
