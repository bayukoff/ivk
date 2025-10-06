package ru.coolz.fileshare;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.MultipartConfigElement;
import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import ru.coolz.fileshare.filter.AuthFilter;
import ru.coolz.fileshare.service.AuthService;
import ru.coolz.fileshare.service.FileService;
import ru.coolz.fileshare.servlet.AuthServlet;
import ru.coolz.fileshare.servlet.DownloadServlet;
import ru.coolz.fileshare.servlet.UploadServlet;

import java.util.EnumSet;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FileShareMain {
    public static void main(String[] args) throws Exception {
        var fileService = new FileService();
        var authService = new AuthService();

        scheduleCleanup(fileService);
        var server = new Server(8080);

        var context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        registerServlets(context, fileService, authService);
        registerFilters(context, authService);
        registerStaticContent(context);

        server.start();
        System.out.println("Server running on http://localhost:8080/");
        server.join();
    }

    private static void scheduleCleanup(FileService fileService) {
        Executors.newSingleThreadScheduledExecutor()
            .scheduleAtFixedRate(fileService::cleanup, 0, 24, TimeUnit.HOURS);
    }

    private static void registerServlets(ServletContextHandler context, FileService fileService, AuthService authService) {
        context.addServlet(new ServletHolder(new AuthServlet(authService)), "/login");

        var uploadHolder = new ServletHolder(new UploadServlet(fileService));
        uploadHolder.getRegistration()
            .setMultipartConfig(new MultipartConfigElement(System.getProperty("java.io.tmpdir")));
        context.addServlet(uploadHolder, "/upload");

        context.addServlet(new ServletHolder(new DownloadServlet(fileService)), "/download");
    }

    private static void registerFilters(ServletContextHandler context, AuthService authService) {
        var authFilter = new FilterHolder(new AuthFilter(authService));
        context.addFilter(authFilter, "/upload", EnumSet.of(DispatcherType.REQUEST));
        context.addFilter(authFilter, "/download", EnumSet.of(DispatcherType.REQUEST));
        context.addFilter(authFilter, "/", EnumSet.of(DispatcherType.REQUEST));
    }

    private static void registerStaticContent(ServletContextHandler context) {
        String webDir = FileShareMain.class.getClassLoader()
            .getResource("web").toExternalForm();

        var staticHolder = new ServletHolder("default", DefaultServlet.class);
        staticHolder.setInitParameter("resourceBase", webDir);
        staticHolder.setInitParameter("dirAllowed", "true");
        context.addServlet(staticHolder, "/");
    }
}