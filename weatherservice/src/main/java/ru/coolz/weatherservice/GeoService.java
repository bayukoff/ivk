package ru.coolz.weatherservice;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GeoService {
    private static final HttpClient client = HttpClient.newHttpClient();

    public static double[] getCoordinates(String city) throws IOException, InterruptedException {
        var url = "https://geocoding-api.open-meteo.com/v1/search?name=" + city;

        var request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Failed to get coordinates, status code: " + response.statusCode());
        }

        JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonArray results = root.getAsJsonArray("results");
        if (results == null || results.size() == 0) {
            throw new IOException("City not found");
        }

        JsonObject first = results.get(0).getAsJsonObject();
        var lat = first.get("latitude").getAsDouble();
        var lon = first.get("longitude").getAsDouble();
        return new double[]{lat, lon};
    }
}

