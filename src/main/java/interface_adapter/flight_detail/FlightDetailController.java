package interface_adapter.flight_detail;

import entity.Flight;
import use_case.flight_detail.FlightDetailInputBoundary;
import use_case.flight_detail.FlightDetailInputData;

public class FlightDetailController {

    private final FlightDetailInputBoundary interactor;

    public FlightDetailController(FlightDetailInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(Flight flight) {
//        System.out.println("FlightDetailController.execute called with flight: " + flight);

        FlightDetailInputData inputData = new FlightDetailInputData(flight);
        interactor.execute(inputData);
    }
}
