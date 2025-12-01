package interface_adapter.compare_saved_flights;

import entity.FlightDetail;
import use_case.compare_saved_flights.CompareSavedFlightsInputBoundary;
import use_case.compare_saved_flights.CompareSavedFlightsInputData;

/**
 * Controller for initiating the comparison of two saved flights.
 * <p>
 * This controller receives two {@link FlightDetail} objects selected by the user
 * and forwards them to the compare saved flights use case. It constructs the
 * corresponding {@link CompareSavedFlightsInputData} and delegates the execution
 * to the interactor, following the Clean Architecture structure.
 * </p>
 */
public class CompareSavedFlightsController {

    /**
     * The input boundary (interactor) that handles the compare saved flights use case.
     */
    private final CompareSavedFlightsInputBoundary interactor;

    /**
     * Constructs the controller with the given interactor.
     *
     * @param interactor the use case input boundary responsible for comparing flights
     */
    public CompareSavedFlightsController(CompareSavedFlightsInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the compare saved flights use case by forwarding the two selected flights
     * to the interactor.
     *
     * @param first  the first selected {@link FlightDetail}
     * @param second the second selected {@link FlightDetail}
     */
    public void execute(FlightDetail first, FlightDetail second) {
        CompareSavedFlightsInputData input =
                new CompareSavedFlightsInputData(first, second);
        interactor.execute(input);
    }
}
