package data_access;

import entity.FlightDetail;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts a single "offer" JSON object from Amadeus API
 * into a FlightDetail entity.
 *
 * This parser assumes ONE-WAY itineraries and extracts:
 * - numberOfBookableSeats
 * - price.total + currency
 * - fareOption
 * - segments (flattened)
 * - segment baggage & amenities
 */
public class FlightDetailParser {

    public FlightDetail parseOfferToFlightDetail(JSONObject offer) {
//        System.out.println("[DEBUG] Parser reached. offerId = " + offer.optString("id"));


        if (offer == null) return null;

        try {
            /* ---------------- 1. Basic top-level fields ---------------- */
            String id = offer.optString("id", "N/A");

            int numberOfBookableSeats = offer.optInt("numberOfBookableSeats", 0);

            JSONObject priceObj = offer.getJSONObject("price");
            double total = priceObj.getDouble("total");
            String currency = priceObj.getString("currency");

            FlightDetail.Price price = new FlightDetail.Price(total, currency);


            /* ---------------- 2. Traveler pricing → fareOption ---------------- */
            String fareOption = "UNKNOWN";

            JSONArray travelerPricings = offer.optJSONArray("travelerPricings");
            JSONObject firstTraveler = null;

            if (travelerPricings != null && travelerPricings.length() > 0) {
                firstTraveler = travelerPricings.getJSONObject(0);
                fareOption = firstTraveler.optString("fareOption", "UNKNOWN");
            }


            /* ---------------- 3. Itinerary → Segments (flatten) ---------------- */
            List<FlightDetail.SegmentDetail> allSegments = new ArrayList<>();

            JSONArray itineraries = offer.getJSONArray("itineraries");
            if (itineraries.length() > 0) {
                JSONObject itin = itineraries.getJSONObject(0);

                JSONArray segs = itin.getJSONArray("segments");

                for (int i = 0; i < segs.length(); i++) {
                    JSONObject seg = segs.getJSONObject(i);

                    /* ---- Flight fields ---- */
                    String depAirport = seg.getJSONObject("departure").getString("iataCode");
                    String depTime = seg.getJSONObject("departure").getString("at");
                    String depTerm = seg.getJSONObject("departure").optString("terminal", "");

                    String arrAirport = seg.getJSONObject("arrival").getString("iataCode");
                    String arrTime = seg.getJSONObject("arrival").getString("at");
                    String arrTerm = seg.getJSONObject("arrival").optString("terminal", "");

                    String carrierCode = seg.getString("carrierCode");
                    String flightNumber = seg.getString("number");
                    String aircraft = seg.getJSONObject("aircraft").getString("code");
                    String duration = seg.getString("duration");

                    /* ---- Cabin class from travelerPricings → fareDetailsBySegment ---- */
                    String cabinClass = "UNKNOWN";
                    FlightDetail.Baggage baggage = new FlightDetail.Baggage(0, 0);
                    List<FlightDetail.Amenity> amenities = new ArrayList<>();

                    if (firstTraveler != null) {
                        JSONArray fareSegments = firstTraveler.getJSONArray("fareDetailsBySegment");

                        // match by segmentId
                        for (int f = 0; f < fareSegments.length(); f++) {
                            JSONObject fs = fareSegments.getJSONObject(f);
                            if (fs.getString("segmentId").equals(seg.getString("id"))) {

                                cabinClass = fs.optString("cabin", "UNKNOWN");

                                // Baggage
                                JSONObject checked = fs.optJSONObject("includedCheckedBags");
                                JSONObject cabin = fs.optJSONObject("includedCabinBags");

                                int checkedQty = (checked != null) ? checked.optInt("quantity", 0) : 0;
                                int cabinQty = (cabin != null) ? cabin.optInt("quantity", 0) : 0;

                                baggage = new FlightDetail.Baggage(checkedQty, cabinQty);

                                // Amenities
                                JSONArray amenArray = fs.optJSONArray("amenities");
                                if (amenArray != null) {
                                    for (int a = 0; a < amenArray.length(); a++) {
                                        JSONObject am = amenArray.getJSONObject(a);
                                        amenities.add(new FlightDetail.Amenity(
                                                am.optString("description", ""),
                                                am.optString("amenityType", ""),
                                                am.optBoolean("isChargeable", false)
                                        ));
                                    }
                                }
                            }
                        }
                    }

                    /* ---- Build segment entity ---- */
                    FlightDetail.SegmentDetail sd = new FlightDetail.SegmentDetail(
                            depAirport, depTime, depTerm,
                            arrAirport, arrTime, arrTerm,
                            carrierCode, flightNumber, aircraft,
                            duration, cabinClass,
                            baggage, amenities
                    );

                    allSegments.add(sd);
                }
            }

            /* ---------------- 4. Build final FlightDetail ---------------- */
            return new FlightDetail(
                    id,
                    numberOfBookableSeats,
                    price,
                    fareOption,
                    allSegments
            );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
