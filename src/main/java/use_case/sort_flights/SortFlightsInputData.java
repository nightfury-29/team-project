package use_case.sort_flights;

import entity.Flight;
import java.util.List;

/**
 * The Input Data for the Sort Flights Use Case.
 * Encapsulates the list of flights to be sorted and the criterion to sort by.
 */
public class SortFlightsInputData {
    private final List<Flight> flights;
    private final String sortType;

    /**
     * Constructs the input data.
     *
     * @param flights  the list of flights to sort.
     * @param sortType the sorting criterion (e.g., "PRICE", "DURATION", "NONSTOP").
     */
    public SortFlightsInputData(List<Flight> flights, String sortType) {
        this.flights = flights;
        this.sortType = sortType;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public String getSortType() {
        return sortType;
    }
}