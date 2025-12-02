package interface_adapter.compare_saved_flights;

import use_case.compare_saved_flights.CompareSavedFlightsOutputData;

public class CompareSavedFlightsState {

    private CompareSavedFlightsOutputData.FlightSummary first;
    private CompareSavedFlightsOutputData.FlightSummary second;

    public CompareSavedFlightsState() {}

    public CompareSavedFlightsState(CompareSavedFlightsState other) {
        this.first = other.first;
        this.second = other.second;
    }

    public CompareSavedFlightsOutputData.FlightSummary getFirst() {
        return first;
    }

    public void setFirst(CompareSavedFlightsOutputData.FlightSummary first) {
        this.first = first;
    }

    public CompareSavedFlightsOutputData.FlightSummary getSecond() {
        return second;
    }

    public void setSecond(CompareSavedFlightsOutputData.FlightSummary second) {
        this.second = second;
    }
}
