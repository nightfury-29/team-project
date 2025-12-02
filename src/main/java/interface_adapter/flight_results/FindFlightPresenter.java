package interface_adapter.flight_results;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.find_flight.FindFlightOutputBoundary;
import use_case.find_flight.FindFlightOutputData;

import java.util.Comparator;
import javax.swing.JOptionPane; // <-- ADD THIS IMPORT

public class FindFlightPresenter implements FindFlightOutputBoundary {

    private final FlightResultsViewModel flightResultsViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;



    public FindFlightPresenter(FlightResultsViewModel flightResultsViewModel,
                               LoggedInViewModel loggedInViewModel,
                               ViewManagerModel viewManagerModel) {
        this.flightResultsViewModel = flightResultsViewModel;
        this.loggedInViewModel = loggedInViewModel;
        this.viewManagerModel = viewManagerModel;


    }

    @Override
    public void prepareSuccessView(FindFlightOutputData outputData) {
        // This is the default: sort by duration (shortest)
        outputData.getFlights().sort(Comparator.comparing(flight -> {
            // "PT7H30M" -> 7*60 + 30 = 450
            String duration = flight.duration;
            int hours = 0;
            int minutes = 0;
            int hIndex = duration.indexOf("h");
            int mIndex = duration.indexOf("m");

            if (hIndex != -1) {
                hours = Integer.parseInt(duration.substring(0, hIndex));
            }
            if (mIndex != -1) {
                String minStr = duration.substring(hIndex + 1, mIndex);
                minutes = Integer.parseInt(minStr);
            }
            return (hours * 60) + minutes;
        }));

        FlightResultsState flightResultsState = flightResultsViewModel.getState();
        flightResultsState.setFlights(outputData.getFlights());
        flightResultsState.setError(null);
        flightResultsViewModel.firePropertyChange();

        // Reset the fields in the LoggedIn View
        LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setFrom("");
        loggedInState.setTo("");
        loggedInState.setDay("");
        loggedInState.setMonth("January");
        loggedInState.setYear("2025");
        loggedInViewModel.firePropertyChange("reset fields");

        // Switch the view
        viewManagerModel.setState(flightResultsViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // This will now show a pop-up window with the error
        JOptionPane.showMessageDialog(null, errorMessage, "Search Error", JOptionPane.ERROR_MESSAGE);
    }
}
