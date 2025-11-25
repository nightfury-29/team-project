package interface_adapter.saved_flights;

import interface_adapter.ViewManagerModel;
import use_case.saved_flights.SavedFlightsOutputBoundary;
import use_case.saved_flights.SavedFlightsOutputData;

public class SavedFlightsPresenter implements SavedFlightsOutputBoundary {

    private final SavedFlightsViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public SavedFlightsPresenter(SavedFlightsViewModel viewModel,
                                 ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void present(SavedFlightsOutputData data) {
        System.out.println("[Presenter] present called");

        viewModel.setFlights(data.getFlights());
        viewModel.firePropertyChanged();

        System.out.println("[Presenter] switching to: " + viewModel.getViewName());

        // Update the ViewManagerModel state
        viewManagerModel.setState(viewModel.getViewName());

        viewManagerModel.firePropertyChange("state");
    }






}

