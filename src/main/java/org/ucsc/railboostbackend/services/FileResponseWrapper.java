package org.ucsc.railboostbackend.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.nio.file.Files;

public class FileResponseWrapper extends HttpServletResponseWrapper {
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    private static final int BUFFER_SIZE = 10 * 1024;

    public FileResponseWrapper(HttpServletRequest request, HttpServletResponse response) {
        super(response);
        this.response = response;
        this.request = request;
    }


    public void sendFile(String filename, String folder) throws IOException {
        String filePath =
                request.getServletContext().getRealPath("").split("target")[0] + File.separator +
                folder + File.separator +
                filename;

        File file = new File(filePath);
        if (file.exists()) {
            String mimeType = "";
            String fileExtension = getFileExtension(file);
            switch (fileExtension) {
                case "pdf":
                    mimeType = "application/pdf";
                    break;
                case "png":
                    mimeType = "image/png";
                    break;
                case "jpg":
                    mimeType = "image/jpeg";
                    break;
                default:
                    System.out.println("Unsupported file format.");
                    break;
            }

            response.setContentType(mimeType);

            String headerKey = "content-disposition";
            String headerVal = String.format("attachment; filename=\"%s\"", file.getName());
            response.setHeader(headerKey, headerVal);

            try (
                    OutputStream outputStream = response.getOutputStream();
                    InputStream inputStream = Files.newInputStream(file.toPath());
                )
            {
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }
            catch (IllegalStateException e) {
                System.out.println("FileResponseWrapper : sendFile() : 36 \n" +
                        "Cannot get outputstream to send the file.\n" +
                        "Possible error: getWrite() is previously called for this object\n");
                System.out.println(e.getMessage());
            }
            catch (IOException e) {
                System.out.println("FileResponseWrapper : sendFile()\n");
                System.out.println(e.getMessage());
            }
        }
        else {
            response.setContentType("text/html");
            response.getWriter().write("Error: Cannot find the file requested!");
        }
    }


    private String getFileExtension(File file) {
        String filename = file.getName();
        int lastDotIndex = filename.lastIndexOf('.');
        return filename.substring(lastDotIndex + 1);
    }
}
