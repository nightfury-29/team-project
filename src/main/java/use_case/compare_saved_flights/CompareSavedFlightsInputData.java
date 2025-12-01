package use_case.compare_saved_flights;

import entity.FlightDetail;

public class CompareSavedFlightsInputData {

    private final FlightDetail first;
    private final FlightDetail second;

    public CompareSavedFlightsInputData(FlightDetail first, FlightDetail second) {
        this.first = first;
        this.second = second;
    }

    public FlightDetail getFirst() { return first; }
    public FlightDetail getSecond() { return second; }
}
