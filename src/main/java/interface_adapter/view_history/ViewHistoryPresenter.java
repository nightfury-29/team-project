package interface_adapter.view_history;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.viewing_history.SearchHistoryItem;
import interface_adapter.viewing_history.ViewingHistoryState;
import interface_adapter.viewing_history.ViewingHistoryViewModel;
import use_case.view_history.ViewHistoryOutputBoundary;
import use_case.view_history.ViewHistoryOutputData;
import use_case.view_history.ViewHistoryOutputDataItem;

import java.util.ArrayList;
import java.util.List;

/**
 * The Presenter for the View History Use Case.
 */

public class ViewHistoryPresenter implements ViewHistoryOutputBoundary {

    private final ViewingHistoryViewModel viewingHistoryViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;

    public ViewHistoryPresenter(ViewingHistoryViewModel viewingHistoryViewModel, LoggedInViewModel loggedInViewModel,
                                ViewManagerModel viewManagerModel) {

        this.viewingHistoryViewModel = viewingHistoryViewModel;
        this.loggedInViewModel = loggedInViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(ViewHistoryOutputData viewHistoryOutputData) {

        // Unpack the output data for the view model
        final List<SearchHistoryItem> searchHistory = new ArrayList<>();

        for (ViewHistoryOutputDataItem item : viewHistoryOutputData.getSearchHistory()) {
            final String date = item.getDate();
            final String time = item.getTime();
            final String from = item.getFrom();
            final String to = item.getTo();
            final String day = item.getDay();
            final String month = item.getMonth();
            final String year = item.getYear();

            final SearchHistoryItem searchHistoryItem = new SearchHistoryItem(date, time, from, to, day, month, year);
            searchHistory.add(searchHistoryItem);
        }

        // Retrieve and update the state of the view model
        final ViewingHistoryState viewingHistoryState = viewingHistoryViewModel.getState();
        viewingHistoryState.setSearchHistory(searchHistory);
        viewingHistoryViewModel.firePropertyChange("initialize success view");

        // Switch to the view
        viewManagerModel.setState(viewingHistoryViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setError(errorMessage);
        loggedInViewModel.firePropertyChange("error");
    }
}
