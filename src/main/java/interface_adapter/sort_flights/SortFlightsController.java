package interface_adapter.sort_flights;

import entity.Flight;
import use_case.sort_flights.SortFlightsInputBoundary;
import use_case.sort_flights.SortFlightsInputData;
import java.util.List;

/**
 * The Controller for the Sort Flights Use Case.
 * It takes the list of flights and sort type from the View and passes them to the Interactor.
 */
public class SortFlightsController {

    private final SortFlightsInputBoundary interactor;

    /**
     * Constructs the Controller.
     *
     * @param interactor the input boundary to execute the use case.
     */
    public SortFlightsController(SortFlightsInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the sort request.
     *
     * @param flights  the list of flights currently displayed.
     * @param sortType the type of sort requested (e.g., "PRICE").
     */
    public void execute(List<Flight> flights, String sortType) {
        SortFlightsInputData inputData = new SortFlightsInputData(flights, sortType);
        interactor.execute(inputData);
    }
}
