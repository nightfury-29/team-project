package interface_adapter.saved_flights;

import interface_adapter.ViewManagerModel;
import use_case.saved_flights.SavedFlightsOutputBoundary;
import use_case.saved_flights.SavedFlightsOutputData;

/**
 * Presenter for the Saved Flights use case.
 * <p>
 * This presenter receives the output data from the interactor,
 * updates the {@link SavedFlightsViewModel} with the retrieved list of flights
 * and triggers a view change through the {@link ViewManagerModel}.
 * </p>
 */
public class SavedFlightsPresenter implements SavedFlightsOutputBoundary {

    /**
     * The ViewModel that holds the list of saved flights and notifies observers.
     */
    private final SavedFlightsViewModel viewModel;

    /**
     * The global view manager responsible for switching between views.
     */
    private final ViewManagerModel viewManagerModel;

    /**
     * Constructs a {@code SavedFlightsPresenter} with the given ViewModel and ViewManagerModel.
     *
     * @param viewModel        the ViewModel to be updated with saved flights
     * @param viewManagerModel the ViewManagerModel to trigger screen transitions
     */
    public SavedFlightsPresenter(SavedFlightsViewModel viewModel,
                                 ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Presents the saved flights returned by the interactor.
     * <p>
     * This method updates the ViewModel with the retrieved flights,
     * fires a property change event so the view is refreshed,
     * and instructs the ViewManagerModel to navigate to the saved flights view.
     * </p>
     *
     * @param data the output data containing the saved flights list
     */
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
