package interface_adapter.saved_flights;

import data_transfer_objects.FlightDetailDataTransferObject;
import data_transfer_objects.FlightDetailToDTOMapper;
import entity.FlightDetail;
import interface_adapter.ViewManagerModel;
import interface_adapter.flight_detail.FlightDetailState;
import interface_adapter.flight_detail.FlightDetailViewModel;


public class SavedFlightDetailController {

    private final FlightDetailViewModel fdViewModel;
    private final ViewManagerModel viewManagerModel;
    private final FlightDetailToDTOMapper mapper = new FlightDetailToDTOMapper();

    public SavedFlightDetailController(FlightDetailViewModel fdViewModel,
                                       ViewManagerModel viewManagerModel) {
        this.fdViewModel = fdViewModel;
        this.viewManagerModel = viewManagerModel;
    }

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
