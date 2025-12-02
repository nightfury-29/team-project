package interface_adapter.flight_detail;

import use_case.flight_detail.FlightDetailInputBoundary;
import use_case.flight_detail.FlightDetailInputData;

public class FlightDetailController {

    private final FlightDetailInputBoundary interactor;

    public FlightDetailController(FlightDetailInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String flightId) {
        final FlightDetailInputData inputData = new FlightDetailInputData(flightId);
        interactor.execute(inputData);
    }
}
