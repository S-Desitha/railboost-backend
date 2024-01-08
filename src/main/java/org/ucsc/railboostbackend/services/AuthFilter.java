package org.ucsc.railboostbackend.services;


import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        AuthorizationService authService = new AuthorizationService();

        String authHeader = req.getHeader("Authorization");
        if (authHeader != null && authHeader.matches("Bearer .+")) {
            String token = authHeader.replaceAll("(Bearer)", "").trim();
            DecodedJWT decodedJWT = authService.verifyToken(token);
            if (authService.isRevoked(decodedJWT))
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            else
                req.setAttribute("jwt", decodedJWT);

        }

        filterChain.doFilter(req, resp);

        // post-processing goes here
    }






}
