package data_access;

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
    public FlightDetail fetchDetail(String flightId) {

        if (flightId == null) {
            System.out.println("[ERROR] Flight or Flight.Id is null.");
            return null;
        }

        final JSONObject offerJson = cache.getOfferById(flightId);

        if (offerJson == null) {
            System.out.println("[ERROR] No cached offer with id = " + flightId + ".");
            return null;
        }

        final FlightDetail detail = parser.parseOfferToFlightDetail(offerJson);

        if (detail == null) {
            System.out.println("[ERROR] Could not parse offer with id = " + flightId + ".");
        }

        return detail;
    }
}
