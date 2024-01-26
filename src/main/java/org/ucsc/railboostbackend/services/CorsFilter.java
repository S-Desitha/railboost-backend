package org.ucsc.railboostbackend.services;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

//        if (httpRequest.getMethod().equals("OPTIONS")) {
//            httpResponse.setHeader("Access-Control-Allow-Origin", "*"); // Replace with your specific origin
//            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
//            httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
//            httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
//
//            System.out.println("CORS filter: OPTIONS");
//
////            filterChain.doFilter(httpRequest, httpResponse);
//        }


        // Allow requests from any origin - you might want to restrict this to specific origins in a production environment
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:5500");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");

        filterChain.doFilter(httpRequest, httpResponse);
    }
}
