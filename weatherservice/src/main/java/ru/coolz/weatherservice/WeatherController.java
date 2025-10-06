package ru.coolz.weatherservice;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeatherController {
    public static void startServer(WeatherService weatherService) throws Exception {
        var server = new Server(8080);
        var context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        String webDir = WeatherController.class.getClassLoader()
            .getResource("web").toExternalForm();

        var staticHolder = new ServletHolder("default", DefaultServlet.class);
        staticHolder.setInitParameter("resourceBase", webDir);
        staticHolder.setInitParameter("dirAllowed", "true");
        context.addServlet(staticHolder, "/");

        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                String city = req.getParameter("city");
                if (city == null || city.isEmpty()) {
                    resp.setStatus(400);
                    resp.getWriter().write("Missing city parameter");
                    return;
                }

                try {
                    JsonObject weatherJson = weatherService.getWeather(city);
                    JsonArray hourlyTemps = weatherService.getHourlyTemperatures(weatherJson);

                    List<Double> temps = new ArrayList<>();
                    for (var tempElem : hourlyTemps) temps.add(tempElem.getAsDouble());

                    resp.setStatus(200);
                    resp.setContentType("application/json");
                    resp.getWriter().write(weatherJson.toString());
                } catch (Exception e) {
                    resp.setStatus(500);
                    resp.getWriter().write("Error: " + e.getMessage());
                }
            }
        }), "/weather");
        System.out.println("Server running on http://localhost:8080");
        server.start();
        server.join();
    }
}
