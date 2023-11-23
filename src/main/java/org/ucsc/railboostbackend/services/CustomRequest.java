package org.ucsc.railboostbackend.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class CustomRequest extends HttpServletRequestWrapper {
    private String body;

    public CustomRequest(HttpServletRequest request) {
        super(request);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (body == null) {
            // If the body is not cached, read and cache it
            StringBuilder buffer = new StringBuilder();
            try (BufferedReader reader = super.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
            }
            body = buffer.toString();
        }

        // Return a new reader with the cached body
        return new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream(body.getBytes())));
    }
}
