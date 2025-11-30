package use_case.sort_flights;

/**
 * The Output Boundary for the Sort Flights Use Case.
 * This interface defines the methods to prepare the view (success or failure) after the interactor finishes.
 */
public interface SortFlightsOutputBoundary {

    /**
     * Prepares the success view with the sorted list of flights.
     *
     * @param sortFlightsOutputData the output data containing the sorted flights.
     */
    void prepareSuccessView(SortFlightsOutputData sortFlightsOutputData);
}
