package interface_adapter.flight_detail;

import use_case.flight_detail.FlightDetailInputBoundary;
import use_case.flight_detail.FlightDetailInputData;

/**
 * Controller for the flight-detail use case.
 *
 * <p>Receives a flight ID from the UI, packages it into input data,
 * and forwards it to the interactor.</p>
 */
public class FlightDetailController {

    private final FlightDetailInputBoundary interactor;

    /**
     * Creates a controller with the given interactor.
     *
     * @param interactor the input boundary for this use case
     */
    public FlightDetailController(FlightDetailInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the use case with the provided flight ID.
     *
     * @param flightId the ID of the selected flight
     */
    public void execute(String flightId) {
        final FlightDetailInputData inputData = new FlightDetailInputData(flightId);
        interactor.execute(inputData);
    }
}
