package interface_adapter.compare_saved_flights;

import entity.FlightDetail;
import use_case.compare_saved_flights.CompareSavedFlightsInputBoundary;
import use_case.compare_saved_flights.CompareSavedFlightsInputData;

public class CompareSavedFlightsController {

    private final CompareSavedFlightsInputBoundary interactor;

    public CompareSavedFlightsController(CompareSavedFlightsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(FlightDetail first, FlightDetail second) {
        CompareSavedFlightsInputData input =
                new CompareSavedFlightsInputData(first, second);
        interactor.execute(input);
    }
}
