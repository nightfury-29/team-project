package use_case.sort_flights;

import entity.Flight;
import java.util.List;

/**
 * The Output Data for the Sort Flights Use Case.
 * Encapsulates the resulting sorted list of flights.
 */
public class SortFlightsOutputData {
    private final List<Flight> sortedFlights;

    /**
     * Constructs the output data.
     *
     * @param sortedFlights the list of flights after sorting.
     */
    public SortFlightsOutputData(List<Flight> sortedFlights) {
        this.sortedFlights = sortedFlights;
    }

    public List<Flight> getSortedFlights() {
        return sortedFlights;
    }
}