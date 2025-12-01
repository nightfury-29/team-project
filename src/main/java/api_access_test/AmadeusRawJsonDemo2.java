package api_access_test;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AmadeusRawJsonDemo2 {

    static class FlightSummary {
        final String depAirport, depTime;
        final String arrAirport, arrTime;
        final String price, duration, airline, aircraft;

        FlightSummary(String depAirport, String depTime, String arrAirport, String arrTime,
                      String price, String duration, String airline, String aircraft) {
            this.depAirport = depAirport;
            this.depTime = depTime;
            this.arrAirport = arrAirport;
            this.arrTime = arrTime;
            this.price = price;
            this.duration = duration;
            this.airline = airline;
            this.aircraft = aircraft;
        }

        @Override public String toString() {
            return String.format("%s %s -> %s %s | %s | %s | %s | %s",
                    depAirport, depTime, arrAirport, arrTime, price, duration, airline, aircraft);
        }
    }

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();

        String clientId = "xAellLGd477aHyuSLn1cBW0bQr6hKaM9";
        String clientSecret = "LIiEa7QZoQE9yImg";
        String accessToken;

        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            String form = "grant_type=client_credentials"
                    + "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
                    + "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8);

            Request tokenRequest = new Request.Builder()
                    .url("https://test.api.amadeus.com/v1/security/oauth2/token")
                    .post(RequestBody.create(form, mediaType))
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            try (Response tokenResponse = client.newCall(tokenRequest).execute()) {
                if (!tokenResponse.isSuccessful()) {
                    System.err.println("Token HTTP " + tokenResponse.code());
                    System.err.println(tokenResponse.body() != null ? tokenResponse.body().string() : "");
                    return;
                }
                String tokenJson = tokenResponse.body() != null ? tokenResponse.body().string() : "";
                JSONObject tokenObj = new JSONObject(tokenJson);
                accessToken = tokenObj.getString("access_token");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            HttpUrl url = HttpUrl.parse("https://test.api.amadeus.com/v2/shopping/flight-offers")
                    .newBuilder()
                    .addQueryParameter("originLocationCode", "SYD")
                    .addQueryParameter("destinationLocationCode", "BKK")
                    .addQueryParameter("departureDate", "2025-12-02")
                    .addQueryParameter("adults", "1")
                    .addQueryParameter("nonStop", "true")
                    .build();

            Request offersRequest = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();

            try (Response offersResponse = client.newCall(offersRequest).execute()) {
                String body = offersResponse.body() != null ? offersResponse.body().string() : "";
                System.out.println("HTTP " + offersResponse.code());
                if (!offersResponse.isSuccessful()) {
                    System.err.println(body);
                    return;
                }

                JSONObject root = new JSONObject(body);
                JSONObject dictAircraft = root.optJSONObject("dictionaries") != null
                        ? root.getJSONObject("dictionaries").optJSONObject("aircraft") : null;

                List<FlightSummary> list = new ArrayList<>();
                JSONArray data = root.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject offer = data.getJSONObject(i);

                    String price = offer.getJSONObject("price").getString("total")
                            + " " + offer.getJSONObject("price").getString("currency");

                    JSONObject itin = offer.getJSONArray("itineraries").getJSONObject(0);
                    String isoDur = itin.getString("duration");

                    JSONArray segs = itin.getJSONArray("segments");
                    JSONObject first = segs.getJSONObject(0);
                    JSONObject last  = segs.getJSONObject(segs.length() - 1);

                    String depAirport = first.getJSONObject("departure").getString("iataCode");
                    String depTime    = first.getJSONObject("departure").getString("at");
                    String arrAirport = last.getJSONObject("arrival").getString("iataCode");
                    String arrTime    = last.getJSONObject("arrival").getString("at");

                    StringBuilder airline = new StringBuilder();
                    for (int k = 0; k < segs.length(); k++) {
                        if (airline.length() > 0) airline.append(" + ");
                        JSONObject s = segs.getJSONObject(k);
                        airline.append(s.getString("carrierCode")).append(s.getString("number"));
                    }

                    String aircraftCode = first.getJSONObject("aircraft").getString("code");
                    String aircraft = aircraftCode;
                    if (dictAircraft != null && dictAircraft.has(aircraftCode)) {
                        aircraft = dictAircraft.getString(aircraftCode);
                    }

                    list.add(new FlightSummary(
                            depAirport, depTime, arrAirport, arrTime,
                            price, formatIsoDuration(isoDur), airline.toString(), aircraft
                    ));
                }

                for (FlightSummary f : list) {
                    System.out.println(f.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String formatIsoDuration(String iso) {
        int h = 0, m = 0;
        String s = iso.replace("PT", "");
        int hIdx = s.indexOf('H');
        int mIdx = s.indexOf('M');
        if (hIdx >= 0) h = Integer.parseInt(s.substring(0, hIdx));
        if (mIdx >= 0) m = Integer.parseInt(s.substring(hIdx >= 0 ? hIdx + 1 : 0, mIdx));
        return (h > 0 ? h + "h" : "") + (m > 0 ? m + "m" : (h == 0 ? "0m" : ""));
    }
}

