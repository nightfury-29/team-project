package interface_adapter.flight_detail;

import interface_adapter.ViewModel;

public class FlightDetailViewModel extends ViewModel<FlightDetailState> {

    public FlightDetailViewModel() {
        super("flight detail");
        setState(new FlightDetailState());
    }
}
