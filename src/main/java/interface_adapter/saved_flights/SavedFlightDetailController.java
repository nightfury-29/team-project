package interface_adapter.saved_flights;

import data_transfer_objects.FlightDetailDataTransferObject;
import data_transfer_objects.FlightDetailToDTOMapper;
import entity.FlightDetail;
import interface_adapter.ViewManagerModel;
import interface_adapter.flight_detail.FlightDetailState;
import interface_adapter.flight_detail.FlightDetailViewModel;

/**
 * Controller responsible for displaying the details of a saved flight.
 * <p>
 * This controller is used when a user selects a saved flight from the
 * SavedFlightsView. It maps the {@link FlightDetail} entity into a
 * {@link FlightDetailDataTransferObject}, updates the
 * {@link FlightDetailViewModel} state, and instructs the
 * {@link ViewManagerModel} to navigate to the flight detail screen.
 * </p>
 */
public class SavedFlightDetailController {

    /**
     * The ViewModel for the flight detail screen.
     */
    private final FlightDetailViewModel fdViewModel;

    /**
     * The global view manager that controls screen transitions.
     */
    private final ViewManagerModel viewManagerModel;

    /**
     * Mapper used to convert {@link FlightDetail} into
     * {@link FlightDetailDataTransferObject}.
     */
    private final FlightDetailToDTOMapper mapper = new FlightDetailToDTOMapper();

    /**
     * Constructs the controller with the required ViewModel and ViewManagerModel.
     *
     * @param fdViewModel       the FlightDetailViewModel to update with flight detail data
     * @param viewManagerModel  the ViewManagerModel responsible for changing screens
     */
    public SavedFlightDetailController(FlightDetailViewModel fdViewModel,
                                       ViewManagerModel viewManagerModel) {
        this.fdViewModel = fdViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Loads the selected saved flight into the FlightDetailViewModel and
     * navigates to the flight detail view.
     * <p>
     * If the provided flight detail is null, an error message is set instead.
     * </p>
     *
     * @param detail the {@link FlightDetail} selected by the user
     */
    public void execute(FlightDetail detail) {
        if (detail == null) {
            FlightDetailState state = fdViewModel.getState();
            state.setErrorMessage("No flight detail available.");
            fdViewModel.firePropertyChange();

            viewManagerModel.setState(fdViewModel.getViewName());
            viewManagerModel.firePropertyChange();
            return;
        }

        FlightDetailDataTransferObject dto = mapper.map(detail);

        FlightDetailState state = fdViewModel.getState();
        state.setFlightDetail(dto);
        state.setErrorMessage(null);

        state.setPreviousViewName("saved flights");

        fdViewModel.firePropertyChange();

        viewManagerModel.setState(fdViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
