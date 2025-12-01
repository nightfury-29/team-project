package use_case.find_flight;

import java.util.List;

import entity.Flight;

public interface FindFlightApiAccessInterface {

    /**
     * Searches for flights based on the provided criteria.
     *
     * @param originLocationCode      The starting location code.
     * @param destinationLocationCode The destination location code.
     * @param departureDate           The date of departure.
     * @param adults                  Number of adult passengers.
     * @param nonstop                 Whether the flight should be non-stop.
     * @return A list of Flight objects matching the criteria.
     */
    List<Flight> search(String originLocationCode,
                  String destinationLocationCode,
                  String departureDate,
                  int adults,
                  boolean nonstop
                  );
}
