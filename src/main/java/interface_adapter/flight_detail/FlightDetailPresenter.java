package interface_adapter.flight_detail;

import interface_adapter.ViewManagerModel;
import interface_adapter.flight_results.FlightResultsViewModel;
import use_case.flight_detail.FlightDetailOutputBoundary;
import use_case.flight_detail.FlightDetailOutputData;

public class FlightDetailPresenter implements FlightDetailOutputBoundary {

    private final FlightDetailViewModel fdViewModel;
    private final FlightResultsViewModel flightResultsViewModel;
    private final ViewManagerModel viewManagerModel;

    public FlightDetailPresenter(FlightDetailViewModel fdViewModel,
                                 FlightResultsViewModel flightResultsViewModel,
                                 ViewManagerModel viewManagerModel) {
        this.fdViewModel = fdViewModel;
        this.flightResultsViewModel = flightResultsViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(FlightDetailOutputData outputData) {
//        System.out.println("[DEBUG] Presenter success view called.");

        final FlightDetailState fdState = fdViewModel.getState();
        fdState.setFlightDetail(outputData.getFlightDetail());
        fdState.setErrorMessage(null);
        fdViewModel.firePropertyChange();

        // Switch to the detail view
        viewManagerModel.setState(fdViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {

        final FlightDetailState fdState = fdViewModel.getState();
        fdState.setErrorMessage(errorMessage);

        viewManagerModel.setState(fdViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}