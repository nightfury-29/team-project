package use_case.sort_flights;

/**
 * The Input Boundary for the Sort Flights Use Case.
 * This interface defines the method signature for the interactor to execute the sorting logic.
 */
public interface SortFlightsInputBoundary {

    /**
     * Executes the sort flight use case.
     *
     * @param sortFlightsInputData the input data containing the flights and sort criteria.
     */
    void execute(SortFlightsInputData sortFlightsInputData);
}