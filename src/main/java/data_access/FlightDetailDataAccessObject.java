package data_access;

import entity.Flight;
import entity.FlightDetail;
import org.json.JSONObject;
import use_case.flight_detail.FlightDetailDataAccessInterface;

public class FlightDetailDataAccessObject implements FlightDetailDataAccessInterface {

    private final FlightOfferCache cache;
    private final FlightDetailParser parser;

    public FlightDetailDataAccessObject() {
        this.cache = FlightOfferCache.getInstance();
        this.parser = new FlightDetailParser();
    }

    @Override
    public FlightDetail fetchDetail(Flight flight) {
//        System.out.println("[DEBUG] DAO called: fetchDetail(), flight ID = " + flight.Id);


        if (flight == null || flight.Id == null) {
            System.out.println("[ERROR] Flight or Flight.Id is null.");
            return null;
        }

        String id = flight.Id;

        JSONObject offerJson = cache.getOfferById(id);

        if (offerJson == null) {
            System.out.println("[ERROR] No cached offer with id = " + id);
            return null;
        }

        FlightDetail detail = parser.parseOfferToFlightDetail(offerJson);

        if (detail == null) {
            System.out.println("[ERROR] Could not parse offer with id = " + id);
        }

        return detail;
    }
}
