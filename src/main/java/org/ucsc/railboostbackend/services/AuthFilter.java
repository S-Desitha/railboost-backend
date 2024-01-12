package org.ucsc.railboostbackend.services;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        PrintWriter writer = null;

        AuthorizationService authService = new AuthorizationService();

        String authHeader = req.getHeader("Authorization");
        if (authHeader != null && authHeader.matches("Bearer .+")) {
            String token = authHeader.replaceAll("(Bearer)", "").trim();

            try {
                writer = resp.getWriter();
                Claims jwt = authService.verifyToken(token);

                if (authService.isRevoked(jwt))
                    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                else{
                    req.setAttribute("jwt", jwt);
                    filterChain.doFilter(req, resp);
                }
            } catch ( IllegalArgumentException e) {
                writer.write("empty");
            } catch (ExpiredJwtException e) {
                writer.write("expired");
            } catch (SecurityException e) {
                writer.write("invalid");
            } catch (JwtException e){
                writer.write("error");
            } finally {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                writer.flush();
                writer.close();
            }



        }
        else
            filterChain.doFilter(req, resp);



        // post-processing goes here
    }






}
