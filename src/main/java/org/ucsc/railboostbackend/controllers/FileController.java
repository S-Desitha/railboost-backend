package org.ucsc.railboostbackend.controllers;

import org.ucsc.railboostbackend.services.FileRequestWrapper;
import org.ucsc.railboostbackend.services.FileResponseWrapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/fileUpload")
@MultipartConfig(
        fileSizeThreshold = 1024*1024,
        maxFileSize = 1024*1024*5,
        maxRequestSize = 1024*1024*10
)
public class FileController extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FileResponseWrapper responseWrapper = new FileResponseWrapper(req, resp);

        String filename = req.getParameter("filename");
        responseWrapper.sendFile(filename, UPLOAD_DIR);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FileRequestWrapper requestWrapper = new FileRequestWrapper(req);
        PrintWriter writer = resp.getWriter();

        try {
            String jsonObj = requestWrapper.getJsonObj();

            String filename = requestWrapper.saveFile(UPLOAD_DIR, "32145");
            writer.write(filename);
        }
        catch (ServletException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/html");
            writer.write("Unsupported content-type. Should be \"multipart/form-data\"");
        }
        catch (IllegalStateException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/html");
            writer.write("File size exceeds. Request body is greater than 10 MB or file size is greater than 5 MB");
        }

        writer.flush();
        writer.close();
    }
}
