package ru.coolz.weatherservice;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.Locale;

import ru.coolz.weatherservice.cache.InMemoryCache;

public class WeatherService {
    private final InMemoryCache<String, String> cache = new InMemoryCache<>(15 * 60); // ttl 15 минут
    private final HttpClient client = HttpClient.newHttpClient();

    public JsonObject getWeather(String city) throws IOException, InterruptedException {
        var cached = cache.get(city);
        if (cached != null) {
            return JsonParser.parseString(cached).getAsJsonObject();
        }
        double[] coords = GeoService.getCoordinates(city);
        String json = fetchWeather(coords[0], coords[1]);
        cache.put(city, json);
        return JsonParser.parseString(json).getAsJsonObject();
    }

    private String fetchWeather(double lat, double lon) throws IOException, InterruptedException {
        String url = String.format(Locale.US,
            "https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f"
                + "&hourly=temperature_2m"
                + "&forecast_days=1",
            lat, lon
        );
        var request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Failed to fetch weather: " + response.statusCode());
        }
        return response.body();
    }

    public JsonArray getHourlyTemperatures(JsonObject weatherJson) {
        return weatherJson.getAsJsonObject("hourly").getAsJsonArray("temperature_2m");
    }
}

