package org.ucsc.railboostbackend.services;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        // Allow requests from any origin - you might want to restrict this to specific origins in a production environment
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:5500");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
