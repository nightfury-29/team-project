package use_case.view_history;

import java.util.List;

/**
 * Output Data for the View History Use Case.
 */

public class ViewHistoryOutputData {

    private final List<ViewHistoryOutputDataItem> searchHistory;

    public ViewHistoryOutputData(List<ViewHistoryOutputDataItem> searchHistory) {
        this.searchHistory = searchHistory;

    }

    public List<ViewHistoryOutputDataItem> getSearchHistory() {
        return searchHistory;
    }
}
