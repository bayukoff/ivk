package ru.coolz.fileshare;

import jakarta.servlet.MultipartConfigElement;
import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import ru.coolz.fileshare.service.FileService;
import ru.coolz.fileshare.servlet.DownloadServlet;
import ru.coolz.fileshare.servlet.UploadServlet;


import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FileShareMain {
    public static void main(String[] args) {
        try {
            FileService fileService = new FileService();

            Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(fileService::cleanup, 0, 24, TimeUnit.HOURS);
            Server server = new Server(8080);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);
            ServletHolder uploadHolder = new ServletHolder(new UploadServlet(fileService));
            uploadHolder.getRegistration().setMultipartConfig(new MultipartConfigElement(System.getProperty("java.io.tmpdir")));
            context.addServlet(uploadHolder, "/upload");
            context.addServlet(new ServletHolder(new DownloadServlet(fileService)), "/download");

            String webDir = FileShareMain.class.getClassLoader()
                .getResource("web").toExternalForm();

            ServletHolder staticHolder = new ServletHolder("default", DefaultServlet.class);
            staticHolder.setInitParameter("resourceBase", webDir);
            staticHolder.setInitParameter("dirAllowed", "true");
            context.addServlet(staticHolder, "/");

            server.start();
            System.out.println("Server running on http://localhost:8080/");
            server.join();
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
