package interface_adapter.viewing_history;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.load_history.LoadHistoryOutputBoundary;
import use_case.load_history.LoadHistoryOutputData;

/**
 * The Presenter for the Load History Use Case.
 */

public class LoadHistoryPresenter implements LoadHistoryOutputBoundary {

    private final ViewingHistoryViewModel viewingHistoryViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;

    public LoadHistoryPresenter(ViewingHistoryViewModel viewingHistoryViewModel,  LoggedInViewModel loggedInViewModel,
                                ViewManagerModel viewManagerModel) {

        this.viewingHistoryViewModel = viewingHistoryViewModel;
        this.loggedInViewModel = loggedInViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(LoadHistoryOutputData loadHistoryOutputData) {
        final String from  = loadHistoryOutputData.getFrom();
        final String to = loadHistoryOutputData.getTo();
        final String day  = loadHistoryOutputData.getDay();
        final String month = loadHistoryOutputData.getMonth();
        final String year = loadHistoryOutputData.getYear();

        // On success, update state and switch to logged in view
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setFrom(from);
        loggedInState.setTo(to);
        loggedInState.setDay(day);
        loggedInState.setMonth(month);
        loggedInState.setYear(year);
        loggedInViewModel.firePropertyChange("load selection");

        viewManagerModel.setState(loggedInViewModel.getViewName());
        viewManagerModel.firePropertyChange();

        // Also reset the state of the Viewing History view model
        viewingHistoryViewModel.setState(new ViewingHistoryState());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final ViewingHistoryState viewingHistoryState = viewingHistoryViewModel.getState();
        viewingHistoryState.setErrorMessage(errorMessage);
        viewingHistoryViewModel.firePropertyChange("error");
    }

    @Override
    public void switchToLoggedInView() {
        // Reset the state of the Viewing History view model
        viewingHistoryViewModel.setState(new ViewingHistoryState());

        // Switch to Logged In View
        viewManagerModel.setState(loggedInViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
