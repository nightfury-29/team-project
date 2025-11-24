package data_access;

import entity.FlightDetail;
import entity.FlightDetail.Price;
import entity.FlightDetail.SegmentDetail;
import entity.FlightDetail.Baggage;
import entity.FlightDetail.Amenity;

import org.json.JSONArray;
import org.json.JSONObject;
import use_case.save_flight.SaveFlightDataAccessInterface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SaveFlightDataAccessObject implements SaveFlightDataAccessInterface {

    private final String basePath = "saved_flights/";

    public SaveFlightDataAccessObject() {
        File folder = new File(basePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private File getUserFile(String username) {
        return new File(basePath + username + ".json");
    }

    private JSONObject loadUserJson(String username) {
        File file = getUserFile(username);

        if (!file.exists()) {
            return new JSONObject().put("flights", new JSONArray());
        }

        try {
            String content = Files.readString(file.toPath());
            return new JSONObject(content);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().put("flights", new JSONArray());
        }
    }

    private void saveUserJson(String username, JSONObject json) {
        try (FileWriter writer = new FileWriter(getUserFile(username))) {
            writer.write(json.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean flightExistsForUser(String username, String flightId) {
        JSONObject json = loadUserJson(username);
        JSONArray arr = json.getJSONArray("flights");

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj.getString("id").equals(flightId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void saveFlightForUser(String username, FlightDetail detail) {
        JSONObject json = loadUserJson(username);
        JSONArray arr = json.getJSONArray("flights");

        JSONObject converted = convertFlightDetailToJson(detail);
        arr.put(converted);

        saveUserJson(username, json);
    }

    @Override
    public List<FlightDetail> getSavedFlights(String username) {
        JSONObject json = loadUserJson(username);
        JSONArray arr = json.getJSONArray("flights");

        List<FlightDetail> list = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            list.add(convertJsonToFlightDetail(obj));
        }
        return list;
    }


    /* ======================================================================
       JSON CONVERSION HELPERS
       ====================================================================== */

    private JSONObject convertFlightDetailToJson(FlightDetail d) {
        JSONObject obj = new JSONObject();

        obj.put("id", d.id);
        obj.put("numberOfBookableSeats", d.numberOfBookableSeats);

        // Price
        JSONObject priceJson = new JSONObject();
        priceJson.put("total", d.price.total);
        priceJson.put("currency", d.price.currency);
        obj.put("price", priceJson);

        obj.put("fareOption", d.fareOption);

        // Segments array
        JSONArray segmentsArray = new JSONArray();
        for (SegmentDetail seg : d.segments) {
            JSONObject segJson = new JSONObject();

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
            JSONObject baggageJson = new JSONObject();
            baggageJson.put("checkedBags", seg.baggage.checkedBags);
            baggageJson.put("cabinBags", seg.baggage.cabinBags);
            segJson.put("baggage", baggageJson);

            // Amenities
            JSONArray amenitiesArray = new JSONArray();
            for (Amenity a : seg.amenities) {
                JSONObject aJson = new JSONObject();
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
        JSONObject priceJson = obj.getJSONObject("price");
        Price price = new Price(
                priceJson.getDouble("total"),
                priceJson.getString("currency")
        );

        // Extract segments
        JSONArray segArr = obj.getJSONArray("segments");
        List<SegmentDetail> segments = new ArrayList<>();

        for (int i = 0; i < segArr.length(); i++) {
            JSONObject seg = segArr.getJSONObject(i);

            // Baggage
            JSONObject b = seg.getJSONObject("baggage");
            Baggage baggage = new Baggage(
                    b.getInt("checkedBags"),
                    b.getInt("cabinBags")
            );

            // Amenities
            JSONArray aArr = seg.getJSONArray("amenities");
            List<Amenity> amenities = new ArrayList<>();
            for (int j = 0; j < aArr.length(); j++) {
                JSONObject a = aArr.getJSONObject(j);
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
                obj.getString("id"),
                obj.getInt("numberOfBookableSeats"),
                price,
                obj.getString("fareOption"),
                segments
        );
    }
}

