package use_case.flight_detail;

import entity.Flight;

public class FlightDetailInputData {

    private final String flightId;

    public FlightDetailInputData(String flightId) {
        this.flightId = flightId;
    }

    public String getFlightId() {
        return flightId;
    }
}

