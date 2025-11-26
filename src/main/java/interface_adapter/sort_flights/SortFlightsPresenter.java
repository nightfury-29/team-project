package interface_adapter.sort_flights;

import interface_adapter.flight_results.FlightResultsState;
import interface_adapter.flight_results.FlightResultsViewModel;
import use_case.sort_flights.SortFlightsOutputBoundary;
import use_case.sort_flights.SortFlightsOutputData;

/**
 * The Presenter for the Sort Flights Use Case.
 * It receives the sorted data and updates the ViewModel.
 */
public class SortFlightsPresenter implements SortFlightsOutputBoundary {

    private final FlightResultsViewModel flightResultsViewModel;

    /**
     * Constructs the Presenter.
     *
     * @param flightResultsViewModel the view model to update.
     */
    public SortFlightsPresenter(FlightResultsViewModel flightResultsViewModel) {
        this.flightResultsViewModel = flightResultsViewModel;
    }

    @Override
    public void prepareSuccessView(SortFlightsOutputData sortFlightsOutputData) {
        // Update the state in the ViewModel
        FlightResultsState currentState = flightResultsViewModel.getState();
        currentState.setFlights(sortFlightsOutputData.getSortedFlights());

        // Fire property change to notify the View to update the table
        flightResultsViewModel.setState(currentState);
        flightResultsViewModel.firePropertyChange();
    }
}
