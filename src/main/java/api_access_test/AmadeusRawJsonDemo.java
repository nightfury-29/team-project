package api_access_test;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AmadeusRawJsonDemo {
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
                    .addQueryParameter("nonStop", "false")
                    .build();

            Request offersRequest = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();

            try (Response offersResponse = client.newCall(offersRequest).execute()) {
                System.out.println("HTTP " + offersResponse.code());
                System.out.println(offersResponse.body() != null ? offersResponse.body().string() : "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
