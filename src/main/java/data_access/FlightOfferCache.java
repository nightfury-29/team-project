package data_access;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simple cache to store the full Amadeus flight-offer JSON response.
 * Allows lookup of a single offer by its ID.
 *
 * This avoids calling the API again when the user clicks "View Details".
 */
public class FlightOfferCache {

    private static FlightOfferCache instance;

    private JSONObject fullResponseJson = null;
    private JSONArray offersArray = null;

    /** Private constructor for singleton */
    private FlightOfferCache() {}

    /** Singleton accessor */
    public static synchronized FlightOfferCache getInstance() {
        if (instance == null) {
            instance = new FlightOfferCache();
        }
        return instance;
    }

    /**
     * Stores the full JSON response from Amadeus API.
     * @param json The full root JSON object returned by Amadeus.
     */
    public synchronized void storeFullResponse(JSONObject json) {
        this.fullResponseJson = json;

        if (json.has("data")) {
            this.offersArray = json.getJSONArray("data");
        } else {
            this.offersArray = new JSONArray();
        }
    }

    /** Get the raw "data" array containing all flight offers. */
    public synchronized JSONArray getAllOffers() {
        return this.offersArray;
    }

    /** Check if the cache has data */
    public synchronized boolean hasData() {
        return offersArray != null && offersArray.length() > 0;
    }

    /**
     * Get a single offer by ID.
     * @param id Offer ID string
     * @return The matching offer JSON object, or null if not found
     */
    public synchronized JSONObject getOfferById(String id) {
        if (offersArray == null) return null;

        for (int i = 0; i < offersArray.length(); i++) {
            JSONObject offer = offersArray.getJSONObject(i);
            if (offer.has("id") && offer.getString("id").equals(id)) {
                return offer;
            }
        }
        return null;
    }

    /**
     * Clears the cache
     */
    public synchronized void clear() {
        fullResponseJson = null;
        offersArray = null;
    }
}
