package use_case.sort_flights;

/**
 * The Data Access Interface for the Sort Flights Use Case.
 * This allows the application to persist sort preferences (e.g., user prefers sorting by Price).
 */
public interface SortFlightsDataAccessInterface {

    /**
     * Saves the user's preferred sorting method (e.g., "PRICE", "DURATION").
     *
     * @param sortType the type of sort selected by the user.
     */
    void saveSortPreference(String sortType);
}