package data_access;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.FlightDetail;
import entity.FlightDetail.Amenity;
import entity.FlightDetail.Baggage;
import entity.FlightDetail.Price;
import entity.FlightDetail.SegmentDetail;
import use_case.save_flight.SaveFlightDataAccessInterface;

public class SaveFlightDataAccessObject implements SaveFlightDataAccessInterface {

    private final String basePath = "saved_flights/";
    private final String flightString = "flights";
    private final String idString = "id";
    private final Integer indentFactor = 4;

    public SaveFlightDataAccessObject() {
        final File folder = new File(basePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private File getUserFile(String username) {
        return new File(basePath + username + ".json");
    }

    private JSONObject loadUserJson(String username) {
        final File file = getUserFile(username);

        if (!file.exists()) {
            return new JSONObject().put(flightString, new JSONArray());
        }

        try {
            final String content = Files.readString(file.toPath());
            return new JSONObject(content);
        }
        catch (Exception ex) {
            return new JSONObject().put(flightString, new JSONArray());
        }
    }

    private void saveUserJson(String username, JSONObject json) {
        try (FileWriter writer = new FileWriter(getUserFile(username))) {
            writer.write(json.toString(indentFactor));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean flightExistsForUser(String username, String flightId) {
        final JSONObject json = loadUserJson(username);
        final JSONArray arr = json.getJSONArray(flightString);
        boolean torf = false;

        for (int i = 0; i < arr.length(); i++) {
            final JSONObject obj = arr.getJSONObject(i);
            if (obj.getString(idString).equals(flightId)) {
                torf = true;
            }
        }
        return torf;
    }

    @Override
    public void saveFlightForUser(String username, FlightDetail detail) {
        final JSONObject json = loadUserJson(username);
        final JSONArray arr = json.getJSONArray(flightString);

        final JSONObject converted = convertFlightDetailToJson(detail);
        arr.put(converted);

        saveUserJson(username, json);
    }

    @Override
    public List<FlightDetail> getSavedFlights(String username) {
        final JSONObject json = loadUserJson(username);
        final JSONArray arr = json.getJSONArray(flightString);

        final List<FlightDetail> list = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            final JSONObject obj = arr.getJSONObject(i);
            list.add(convertJsonToFlightDetail(obj));
        }
        return list;
    }

    /* ======================================================================
       JSON CONVERSION HELPERS
       ====================================================================== */

    private JSONObject convertFlightDetailToJson(FlightDetail det) {
        final JSONObject obj = new JSONObject();

        obj.put(idString, det.id);
        obj.put("numberOfBookableSeats", det.numberOfBookableSeats);

        // Price
        final JSONObject priceJson = new JSONObject();
        priceJson.put("total", det.price.total);
        priceJson.put("currency", det.price.currency);
        obj.put("price", priceJson);

        obj.put("fareOption", det.fareOption);

        // Segments array
        final JSONArray segmentsArray = new JSONArray();
        for (SegmentDetail seg : det.segments) {
            final JSONObject segJson = new JSONObject();

            segJson.put("departureAirport", seg.departureAirport);
            segJson.put("departureTime", seg.departureTime);
            segJson.put("departureTerminal", seg.departureTerminal);

            segJson.put("arrivalAirport", seg.arrivalAirport);
            segJson.put("arrivalTime", seg.arrivalTime);
            segJson.put("arrivalTerminal", seg.arrivalTerminal);

            segJson.put("carrierCode", seg.carrierCode);
            segJson.put("flightNumber", seg.flightNumber);
            segJson.put("aircraft", seg.aircraft);
            segJson.put("duration", seg.duration);
            segJson.put("cabinClass", seg.cabinClass);

            // Baggage
            final JSONObject baggageJson = new JSONObject();
            baggageJson.put("checkedBags", seg.baggage.checkedBags);
            baggageJson.put("cabinBags", seg.baggage.cabinBags);
            segJson.put("baggage", baggageJson);

            // Amenities
            final JSONArray amenitiesArray = new JSONArray();
            for (Amenity a : seg.amenities) {
                final JSONObject aJson = new JSONObject();
                aJson.put("description", a.description);
                aJson.put("amenityType", a.amenityType);
                aJson.put("isChargeable", a.isChargeable);
                amenitiesArray.put(aJson);
            }
            segJson.put("amenities", amenitiesArray);

            segmentsArray.put(segJson);
        }

        obj.put("segments", segmentsArray);

        return obj;
    }

    private FlightDetail convertJsonToFlightDetail(JSONObject obj) {

        // Extract price
        final JSONObject priceJson = obj.getJSONObject("price");
        final Price price = new Price(
                priceJson.getDouble("total"),
                priceJson.getString("currency")
        );

        // Extract segments
        final JSONArray segArr = obj.getJSONArray("segments");
        final List<SegmentDetail> segments = new ArrayList<>();

        for (int i = 0; i < segArr.length(); i++) {
            final JSONObject seg = segArr.getJSONObject(i);

            // Baggage
            final JSONObject b = seg.getJSONObject("baggage");
            final Baggage baggage = new Baggage(
                    b.getInt("checkedBags"),
                    b.getInt("cabinBags")
            );

            // Amenities
            final JSONArray aArr = seg.getJSONArray("amenities");
            final List<Amenity> amenities = new ArrayList<>();
            for (int j = 0; j < aArr.length(); j++) {
                final JSONObject a = aArr.getJSONObject(j);
                amenities.add(new Amenity(
                        a.getString("description"),
                        a.getString("amenityType"),
                        a.getBoolean("isChargeable")
                ));
            }

            segments.add(new SegmentDetail(
                    seg.getString("departureAirport"),
                    seg.getString("departureTime"),
                    seg.getString("departureTerminal"),
                    seg.getString("arrivalAirport"),
                    seg.getString("arrivalTime"),
                    seg.getString("arrivalTerminal"),
                    seg.getString("carrierCode"),
                    seg.getString("flightNumber"),
                    seg.getString("aircraft"),
                    seg.getString("duration"),
                    seg.getString("cabinClass"),
                    baggage,
                    amenities
            ));
        }

        return new FlightDetail(
                obj.getString(idString),
                obj.getInt("numberOfBookableSeats"),
                price,
                obj.getString("fareOption"),
                segments
        );
    }
}

