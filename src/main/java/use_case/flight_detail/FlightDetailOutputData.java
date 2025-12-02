package use_case.flight_detail;

import data_transfer_objects.FlightDetailDataTransferObject;

public class FlightDetailOutputData {
    private final FlightDetailDataTransferObject flightDetail;

    public FlightDetailOutputData(FlightDetailDataTransferObject flightDetail) {
        this.flightDetail = flightDetail;
    }

    public FlightDetailDataTransferObject getFlightDetail() {
        return flightDetail;
    }
}

