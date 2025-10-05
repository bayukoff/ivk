package ru.coolz.fileshare.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import ru.coolz.fileshare.service.FileService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1MB
    maxFileSize = 10 * 1024 * 1024,  // 10MB
    maxRequestSize = 20 * 1024 * 1024 // 20MB
)
public class UploadServlet extends HttpServlet {

    private final FileService fileService;

    public UploadServlet(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("file");
        if (filePart == null || filePart.getSize() == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("No file uploaded");
            return;
        }
        String originalFileName = Path.of(filePart.getSubmittedFileName()).getFileName().toString();
        File tempFile = File.createTempFile("upload_", null);
        filePart.write(tempFile.getAbsolutePath());
        String link = fileService.saveFile(originalFileName, tempFile.toPath());
        response.setContentType("application/json");
        response.getWriter().write("{\"link\":\"" + link + "\"}");
    }
}

