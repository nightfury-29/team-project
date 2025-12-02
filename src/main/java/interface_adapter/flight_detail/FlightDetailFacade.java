package interface_adapter.flight_detail;

import interface_adapter.ViewManagerModel;

/**
 * Facade for the Flight Detail feature.
 * Simplifies the process of showing a flight's detailed information
 * for the UI layer (e.g., views, buttons, handlers).
 *
 * The UI simply calls:
 *     flightDetailFacade.showFlightDetail(flightId)
 *
 * Facade internally coordinates:
 *  - Controller (sending input to the use case)
 *  - Presenter (building ViewModel)
 *  - ViewManagerModel (switching active screen)
 */
public class FlightDetailFacade {

    private final FlightDetailController controller;
    private final FlightDetailPresenter presenter;
    private final FlightDetailViewModel viewModel;
    private final ViewManagerModel viewManager;

    public FlightDetailFacade(FlightDetailController controller,
                              FlightDetailPresenter presenter,
                              FlightDetailViewModel viewModel,
                              ViewManagerModel viewManager) {
        this.controller = controller;
        this.presenter = presenter;
        this.viewModel = viewModel;
        this.viewManager = viewManager;
    }

    /**
     * Main Facade method used by the UI.
     * Triggers the use case and switches to the detail view.
     */
    public void showFlightDetail(String flightId) {

        // Step 1: call controller -> interactor -> data access
        controller.execute(flightId);

        // Step 2: presenter fills the ViewModel internally
        //         (your presenter already does this)

        // Step 3: ensure the correct view is active
        viewManager.setState(viewModel.getViewName());
        viewManager.firePropertyChange();
    }

    /**
     * UI can direct access to the ViewModel.
     */
    public FlightDetailViewModel getViewModel() {
        return viewModel;
    }
}
