package use_case.view_history;

import entity.FlightSearchInformation;

import java.util.ArrayList;
import java.util.List;

/**
 * The View History Interactor.
 */

public class ViewHistoryInteractor implements ViewHistoryInputBoundary {

    private final ViewHistoryDataAccessInterface viewHistoryDataAccessObject;
    private final ViewHistoryOutputBoundary viewHistoryPresenter;

    public ViewHistoryInteractor(ViewHistoryDataAccessInterface viewHistoryDataAccessInterface,
                                 ViewHistoryOutputBoundary viewHistoryOutputBoundary) {

        this.viewHistoryDataAccessObject = viewHistoryDataAccessInterface;
        this.viewHistoryPresenter = viewHistoryOutputBoundary;
    }

    @Override
    public void execute(ViewHistoryInputData viewHistoryInputData) {

        // Call the Data Access Object to get the user's search history
        final String username = viewHistoryInputData.getUsername();
        final List<FlightSearchInformation> searchHistory = viewHistoryDataAccessObject.getSearchHistory(username);

        // If user has a search history, extract relevant data from entities and package as output data
        if (searchHistory != null) {

            final List<ViewHistoryOutputDataItem> data = new ArrayList<>();

            for (FlightSearchInformation search : searchHistory) {
                final String date = search.getDateOfSearch();
                final String time = search.getTimeOfSearch();
                final String from = search.getFrom();
                final String to = search.getTo();
                final String day = Integer.toString(search.getDay());
                final String month = search.getMonth();
                final String year = Integer.toString(search.getYear());

                final ViewHistoryOutputDataItem outputDataItem = new ViewHistoryOutputDataItem(date, time, from, to,
                                                                                                    day, month, year);
                data.add(outputDataItem);
            }

            final ViewHistoryOutputData outputData = new ViewHistoryOutputData(data);

            viewHistoryPresenter.prepareSuccessView(outputData);
        }

        // If not, prepare fail view
        else {
            viewHistoryPresenter.prepareFailView("Hmm...it appears you do not have any search history yet."
                    + "\nSearch for a flight and try clicking this button again!");
        }

    }
}
