package interface_adapter.compare_saved_flights;

import interface_adapter.ViewManagerModel;
import use_case.compare_saved_flights.CompareSavedFlightsOutputBoundary;
import use_case.compare_saved_flights.CompareSavedFlightsOutputData;

public class CompareSavedFlightsPresenter implements CompareSavedFlightsOutputBoundary {

    private final CompareSavedFlightsViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public CompareSavedFlightsPresenter(CompareSavedFlightsViewModel viewModel,
                                        ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void present(CompareSavedFlightsOutputData outputData) {
        CompareSavedFlightsState state = viewModel.getState();
        state.setFirst(outputData.getFirst());
        state.setSecond(outputData.getSecond());

        viewModel.setState(state);
        viewModel.firePropertyChange(); // propertyName = "state"

        viewManagerModel.setState(viewModel.getViewName()); // "compare flights"
        viewManagerModel.firePropertyChange();
    }
}
