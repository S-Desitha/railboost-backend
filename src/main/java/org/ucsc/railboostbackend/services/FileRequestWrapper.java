package org.ucsc.railboostbackend.services;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileRequestWrapper extends HttpServletRequestWrapper {
    private final HttpServletRequest request;

    public FileRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }


    public String getJsonObj() throws ServletException, IOException {
        int bufferSize = 1024;
        char[] buffer = new char[bufferSize];
        StringBuilder out = new StringBuilder();
        InputStream inputStream = request.getPart("jsonObj").getInputStream();

        Reader in = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
            out.append(buffer, 0, numRead);
        }
        return out.toString();
    }


    public String saveFile(String folder, String filename_prefix) throws ServletException, IOException {
        String uploadPath =
                request.getServletContext().getRealPath("").split("target")[0] +
                File.separator +
                folder;
        String filename = filename_prefix + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File fileSaveDir = new File(uploadPath);
        if(!fileSaveDir.exists())
            fileSaveDir.mkdirs();

        Part part = request.getPart("file");
        if (part!=null) {
            filename += getFileType(part.getHeader("content-disposition"));
            part.write(uploadPath + File.separator + filename);
        }
        else {
            throw new RuntimeException(
                    "FileRequestWrapper.java : saveFile : 31\n" +
                    "part is empty\n" +
                    "Cannot find a file in the request\n");
        }

        return filename;
    }


    private String getFileType(String fileData) {
        for (String token : fileData.split(";")) {
            if (token.trim().startsWith("filename")) {
                return "." + token.split("\\.")[1].replace("\"", "");
            }
        }
        return null;
    }


}
