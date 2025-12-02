package interface_adapter.flight_detail;

import data_transfer_objects.FlightDetailDataTransferObject;

public class FlightDetailState {

    private FlightDetailDataTransferObject flightDetail;
    private String errorMessage = null;

    private String previousViewName;

    public FlightDetailState(FlightDetailState state) {
        this.flightDetail = state.flightDetail;
        this.errorMessage = state.errorMessage;
        this.previousViewName = state.previousViewName;
    }

    public FlightDetailState(){}

    public FlightDetailDataTransferObject getFlightDetail() { return flightDetail;}

    public void setFlightDetail(FlightDetailDataTransferObject flightDetail) { this.flightDetail = flightDetail; }

    public String getErrorMessage() { return errorMessage; }

    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public String getPreviousViewName() { return previousViewName; }

    public void setPreviousViewName(String previousViewName) {
        this.previousViewName = previousViewName;
    }
}
