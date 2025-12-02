package use_case.flight_detail;

public interface FlightDetailOutputBoundary {

    void prepareSuccessView(FlightDetailOutputData output);

    void prepareFailView(String errorMessage);
}
