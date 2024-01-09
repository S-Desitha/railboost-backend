package org.ucsc.railboostbackend.services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

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
            Claims jwt = authService.verifyToken(token);
            if (authService.isRevoked(jwt))
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            else{
                req.setAttribute("jwt", jwt);
                filterChain.doFilter(req, resp);
            }

        }
        else
            filterChain.doFilter(req, resp);



        // post-processing goes here
    }






}
