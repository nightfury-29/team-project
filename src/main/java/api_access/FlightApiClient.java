package api_access;

import entity.Flight;
import use_case.find_flight.FindFlightApiAccessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class FlightApiClient implements FindFlightApiAccessInterface {

    private final Map<String, List<Flight>> flights = new HashMap<>();

    @Override
    public List<Flight> search(String originLocationCode, String destinationLocationCode,
                                      String departureDate, int adults, boolean nonstop) {

        List<Flight> result = AmadeusRawObject.searchFlights(
                originLocationCode,
                destinationLocationCode,
                departureDate,
                adults,
                nonstop
        );

        flights.clear();

        for (Flight f : result) {
            String depDate = departureDate;
            String flightNo = f.airline;
            String key = depDate + "_" + flightNo;

            List<Flight> listForKey = flights.get(key);
            if (listForKey == null) {
                listForKey = new ArrayList<>();
                flights.put(key, listForKey);
            }

            listForKey.add(f);
        }
        return result;

    }
    public Map<String, List<Flight>> getFlightsMap() {
        return new HashMap<>(flights);
    }
    public List<Flight> getFlightsByKey(String key) {
        return flights.get(key);
    }
}
