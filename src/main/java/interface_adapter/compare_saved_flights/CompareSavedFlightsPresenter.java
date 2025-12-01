package interface_adapter.compare_saved_flights;

import interface_adapter.ViewManagerModel;
import use_case.compare_saved_flights.CompareSavedFlightsOutputBoundary;
import use_case.compare_saved_flights.CompareSavedFlightsOutputData;

/**
 * Presenter for the Compare Saved Flights use case.
 * <p>
 * This presenter receives the output data from the interactor,
 * updates the {@link CompareSavedFlightsViewModel} with the
 * comparison results, and triggers a view change to the compare
 * flights screen through the {@link ViewManagerModel}.
 * </p>
 */
public class CompareSavedFlightsPresenter implements CompareSavedFlightsOutputBoundary {

    /**
     * The ViewModel associated with the compare saved flights view.
     */
    private final CompareSavedFlightsViewModel viewModel;

    /**
     * The global view manager used to switch between screens.
     */
    private final ViewManagerModel viewManagerModel;

    /**
     * Constructs the presenter with the required ViewModel and ViewManagerModel.
     *
     * @param viewModel        the ViewModel that will hold the comparison results
     * @param viewManagerModel the view manager responsible for switching screens
     */
    public CompareSavedFlightsPresenter(CompareSavedFlightsViewModel viewModel,
                                        ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Presents the comparison results by:
     * <ol>
     *     <li>Updating the ViewModel state with both flights' summaries</li>
     *     <li>Notifying observers of the state change</li>
     *     <li>Triggering a screen change to the "compare flights" view</li>
     * </ol>
     *
     * @param outputData the use case output containing the two flights being compared
     */
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
