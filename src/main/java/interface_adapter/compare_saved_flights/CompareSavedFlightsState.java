package interface_adapter.compare_saved_flights;

import use_case.compare_saved_flights.CompareSavedFlightsOutputData;

/**
 * State class representing the data required by the Compare Saved Flights view.
 * <p>
 * This state holds two {@link CompareSavedFlightsOutputData.FlightSummary}
 * objects corresponding to the two flights selected by the user for comparison.
 * The presenter updates this state, and the view observes changes to it.
 * </p>
 */
public class CompareSavedFlightsState {

    /**
     * The summary of the first selected flight.
     */
    private CompareSavedFlightsOutputData.FlightSummary first;

    /**
     * The summary of the second selected flight.
     */
    private CompareSavedFlightsOutputData.FlightSummary second;

    /**
     * Constructs an empty CompareSavedFlightsState.
     */
    public CompareSavedFlightsState() {}

    /**
     * Copy constructor. Creates a new state object by copying the values
     * from another {@code CompareSavedFlightsState}.
     *
     * @param other the state to copy
     */
    public CompareSavedFlightsState(CompareSavedFlightsState other) {
        this.first = other.first;
        this.second = other.second;
    }

    /**
     * Returns the summary of the first selected flight.
     *
     * @return the first flight summary
     */
    public CompareSavedFlightsOutputData.FlightSummary getFirst() {
        return first;
    }

    /**
     * Sets the summary of the first selected flight.
     *
     * @param first the first flight summary
     */
    public void setFirst(CompareSavedFlightsOutputData.FlightSummary first) {
        this.first = first;
    }

    /**
     * Returns the summary of the second selected flight.
     *
     * @return the second flight summary
     */
    public CompareSavedFlightsOutputData.FlightSummary getSecond() {
        return second;
    }

    /**
     * Sets the summary of the second selected flight.
     *
     * @param second the second flight summary
     */
    public void setSecond(CompareSavedFlightsOutputData.FlightSummary second) {
        this.second = second;
    }
}
