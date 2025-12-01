package data_access;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.FlightDetail;

/**
 * Converts a single "offer" JSON object from Amadeus API
 * into a FlightDetail entity.
 * This parser assumes ONE-WAY itineraries and extracts:
 * - numberOfBookableSeats
 * - price.total + currency
 * - fareOption
 * - segments (flattened)
 * - segment baggage & amenities
 */
public class FlightDetailParser {

    public FlightDetail parseOfferToFlightDetail(JSONObject offer) {
//      System.out.println("[DEBUG] Parser reached. offerId = " + offer.optString("id"));

        if (offer == null) return null;

        try {
            /* ---------------- 1. Basic top-level fields ---------------- */
            final String id = offer.optString("id", "N/A");

            final int numberOfBookableSeats = offer.optInt("numberOfBookableSeats", 0);

            final JSONObject priceObj = offer.getJSONObject("price");
            final double total = priceObj.getDouble("total");
            final String currency = priceObj.getString("currency");

            final FlightDetail.Price price = new FlightDetail.Price(total, currency);

            /* ---------------- 2. Traveler pricing → fareOption ---------------- */
            String fareOption = "UNKNOWN";

            final JSONArray travelerPricings = offer.optJSONArray("travelerPricings");
            JSONObject firstTraveler = null;

            if (travelerPricings != null && travelerPricings.length() > 0) {
                firstTraveler = travelerPricings.getJSONObject(0);
                fareOption = firstTraveler.optString("fareOption", "UNKNOWN");
            }

            /* ---------------- 3. Itinerary → Segments (flatten) ---------------- */
            final List<FlightDetail.SegmentDetail> allSegments = new ArrayList<>();

            final JSONArray itineraries = offer.getJSONArray("itineraries");
            if (itineraries.length() > 0) {
                final JSONObject itin = itineraries.getJSONObject(0);

                final JSONArray segs = itin.getJSONArray("segments");

                for (int i = 0; i < segs.length(); i++) {
                    final JSONObject seg = segs.getJSONObject(i);

                    /* ---- Flight fields ---- */
                    final String depAirport = seg.getJSONObject("departure").getString("iataCode");
                    final String depTime = seg.getJSONObject("departure").getString("at");
                    final String depTerm = seg.getJSONObject("departure").optString("terminal", "");

                    final String arrAirport = seg.getJSONObject("arrival").getString("iataCode");
                    final String arrTime = seg.getJSONObject("arrival").getString("at");
                    final String arrTerm = seg.getJSONObject("arrival").optString("terminal", "");

                    final String carrierCode = seg.getString("carrierCode");
                    final String flightNumber = seg.getString("number");
                    final String aircraft = seg.getJSONObject("aircraft").getString("code");
                    final String duration = seg.getString("duration");

                    /* ---- Cabin class from travelerPricings → fareDetailsBySegment ---- */
                    String cabinClass = "UNKNOWN";
                    FlightDetail.Baggage baggage = new FlightDetail.Baggage(0, 0);
                    final List<FlightDetail.Amenity> amenities = new ArrayList<>();

                    if (firstTraveler != null) {
                        final JSONArray fareSegments = firstTraveler.getJSONArray("fareDetailsBySegment");

                        // match by segmentId
                        for (int f = 0; f < fareSegments.length(); f++) {
                            final JSONObject fs = fareSegments.getJSONObject(f);
                            if (fs.getString("segmentId").equals(seg.getString("id"))) {

                                cabinClass = fs.optString("cabin", "UNKNOWN");

                                // Baggage
                                final JSONObject checked = fs.optJSONObject("includedCheckedBags");
                                final JSONObject cabin = fs.optJSONObject("includedCabinBags");

                                final int checkedQty = (checked != null) ? checked.optInt("quantity", 0) : 0;
                                final int cabinQty = (cabin != null) ? cabin.optInt("quantity", 0) : 0;

                                baggage = new FlightDetail.Baggage(checkedQty, cabinQty);

                                // Amenities
                                final JSONArray amenArray = fs.optJSONArray("amenities");
                                if (amenArray != null) {
                                    for (int a = 0; a < amenArray.length(); a++) {
                                        final JSONObject am = amenArray.getJSONObject(a);
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
                    final FlightDetail.SegmentDetail sd = new FlightDetail.SegmentDetail(
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

        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
