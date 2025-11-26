package use_case.sort_flights;

import entity.Flight;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

/**
 * The Interactor for the Sort Flights Use Case.
 * Implements the business logic for sorting flights based on criteria.
 */
public class SortFlightsInteractor implements SortFlightsInputBoundary {

    // We call the interface, NOT the concrete Presenter class.
    private final SortFlightsOutputBoundary outputBoundary;
    // We use the interface for data access.
    private final SortFlightsDataAccessInterface dataAccessObject;

    /**
     * Constructs the Interactor.
     *
     * @param outputBoundary   the output boundary to present results.
     * @param dataAccessObject the data access object to save preferences (optional but added for CA compliance).
     */
    public SortFlightsInteractor(SortFlightsOutputBoundary outputBoundary,
                                 SortFlightsDataAccessInterface dataAccessObject) {
        this.outputBoundary = outputBoundary;
        this.dataAccessObject = dataAccessObject;
    }

    @Override
    public void execute(SortFlightsInputData sortFlightsInputData) {
        // Create a copy of the list to avoid mutating the original input directly (good practice)
        List<Flight> flights = new ArrayList<>(sortFlightsInputData.getFlights());
        String sortType = sortFlightsInputData.getSortType();

        // 1. Business Logic: Perform the Sort
        if ("PRICE".equals(sortType)) {
            flights.sort(Comparator.comparingDouble(flight -> flight.priceTotal));
        }
        else if ("NONSTOP".equals(sortType)) {
            // Filter for non-stop flights
            List<Flight> nonstopFlights = new ArrayList<>();
            for (Flight f : flights) {
                if (f.airline != null) {
                    // Logic: Non-stop flights usually have an airline code like "EK5053" (Length ~6)
                    // Flights with stops might look different or have "+" in API data.
                    // Assuming your logic for extracting airline code length == 2 implies non-stop based on your previous code.
                    String letters = f.airline.replaceAll("[^A-Z]", "");
                    if (letters.length() == 2) {
                        nonstopFlights.add(f);
                    }
                }
            }
            flights = nonstopFlights;
        }
        else {
            // Default: DURATION
            flights.sort(Comparator.comparingInt(this::calculateDurationInMinutes));
        }

        // 2. (Optional) Save the preference via DAO
        // This satisfies the "Data Access" requirement of Clean Architecture if persistence is needed.
        if (dataAccessObject != null) {
            dataAccessObject.saveSortPreference(sortType);
        }

        // 3. Prepare Output
        SortFlightsOutputData outputData = new SortFlightsOutputData(flights);
        outputBoundary.prepareSuccessView(outputData);
    }

    /**
     * Helper method to parse duration string (e.g., "9h50m") into minutes.
     */
    private int calculateDurationInMinutes(Flight flight) {
        String duration = flight.duration;
        int hours = 0;
        int minutes = 0;

        if (duration == null) return 0;

        int hIndex = duration.indexOf("h");
        int mIndex = duration.indexOf("m");

        if (hIndex != -1) {
            try {
                hours = Integer.parseInt(duration.substring(0, hIndex));
            } catch (NumberFormatException ignored) {
                // If parsing fails, keep hours as 0
            }
        }
        if (mIndex != -1) {
            try {
                // Parse string between 'h' (or start) and 'm'
                int start = (hIndex != -1) ? hIndex + 1 : 0;
                String minStr = duration.substring(start, mIndex);
                minutes = Integer.parseInt(minStr);
            } catch (NumberFormatException ignored) {
                // If parsing fails, keep minutes as 0
            }
        }
        return (hours * 60) + minutes;
    }
}