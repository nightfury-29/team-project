package interface_adapter.flight_detail;

import interface_adapter.ViewManagerModel;
import interface_adapter.flight_results.FlightResultsViewModel;
import use_case.flight_detail.FlightDetailOutputBoundary;
import use_case.flight_detail.FlightDetailOutputData;

/**
 * Presenter for the flight-detail use case.
 *
 * <p>Transforms output data into view-model state and updates the active view.</p>
 */
public class FlightDetailPresenter implements FlightDetailOutputBoundary {

    private final FlightDetailViewModel fdViewModel;
    private final FlightResultsViewModel flightResultsViewModel;
    private final ViewManagerModel viewManagerModel;

    /**
     * Constructs the presenter.
     *
     * @param fdViewModel view model for flight detail
     * @param flightResultsViewModel view model for flight results
     * @param viewManagerModel manager for switching views
     */
    public FlightDetailPresenter(FlightDetailViewModel fdViewModel,
                                 FlightResultsViewModel flightResultsViewModel,
                                 ViewManagerModel viewManagerModel) {
        this.fdViewModel = fdViewModel;
        this.flightResultsViewModel = flightResultsViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Prepares the success state: sets flight detail data and switches the view.
     *
     * @param outputData the use-case output
     */
    @Override
    public void prepareSuccessView(FlightDetailOutputData outputData) {

        final FlightDetailState fdState = fdViewModel.getState();
        fdState.setFlightDetail(outputData.getFlightDetail());
        fdState.setErrorMessage(null);
        fdState.setPreviousViewName(flightResultsViewModel.getViewName());
        fdViewModel.firePropertyChange();

        viewManagerModel.setState(fdViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    /**
     * Prepares the failure state: sets an error message and keeps the user on the detail view.
     *
     * @param errorMessage message to display
     */
    @Override
    public void prepareFailView(String errorMessage) {

        final FlightDetailState fdState = fdViewModel.getState();
        fdState.setErrorMessage(errorMessage);

        viewManagerModel.setState(fdViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
