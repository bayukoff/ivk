package ru.coolz.weatherservice;

public class Main {
    public static void main(String[] args) throws Exception {
        WeatherService weatherService = new WeatherService();
        WeatherController.startServer(weatherService);
    }
}
