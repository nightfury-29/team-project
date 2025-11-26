package data_access;

import use_case.sort_flights.SortFlightsDataAccessInterface;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InMemorySortSettingsDAO implements SortFlightsDataAccessInterface {

    private static final Logger LOGGER = Logger.getLogger(InMemorySortSettingsDAO.class.getName());

    private String lastSortType = "DURATION";

    @Override
    public void saveSortPreference(String sortType) {
        this.lastSortType = sortType;
        // Fix 1: Use built-in formatting to avoid string concatenation
        LOGGER.log(Level.INFO, "Sort preference saved: {0}", sortType);
    }

    // Fix 2: Suppress warning because this is a getter for future use/testing
    @SuppressWarnings("unused")
    public String getLastSortType() {
        return lastSortType;
    }
}