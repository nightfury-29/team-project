package use_case.flight_detail;

import entity.Flight;
import entity.FlightDetail;

public interface FlightDetailDataAccessInterface {

    /**
     * Fetch a rich FlightDetail object using the basic Flight.
     * This should call the Amadeus API again.
     */
    FlightDetail fetchDetail(String flightId);
}

