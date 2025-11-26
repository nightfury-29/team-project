package use_case.saved_flights;

import entity.FlightDetail;
import java.util.List;

public class SavedFlightsOutputData {
    private final List<FlightDetail> flights;

    public SavedFlightsOutputData(List<FlightDetail> flights) {
        this.flights = flights;
    }

    public List<FlightDetail> getFlights() {
        return flights;
    }
}
