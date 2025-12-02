package use_case.flight_detail;

public class FlightDetailInputData {

    private final String flightId;

    public FlightDetailInputData(String flightId) {
        this.flightId = flightId;
    }

    public String getFlightId() {
        return flightId;
    }
}

