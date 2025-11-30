package interface_adapter.viewing_history;

import java.util.List;

/**
 * The state for the Viewing History View Model.
 */

public class ViewingHistoryState {

    private List<SearchHistoryItem> searchHistory;
    private SearchHistoryItem selectedEntry;
    private String errorMessage;

    public List<SearchHistoryItem> getSearchHistory() {
        return searchHistory;
    }

    public SearchHistoryItem getSelectedEntry() {
        return selectedEntry;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setSearchHistory(List<SearchHistoryItem> searchHistory) {
        this.searchHistory = searchHistory;
    }

    public void setSelectedEntry(SearchHistoryItem selectedEntry) {
        this.selectedEntry = selectedEntry;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
