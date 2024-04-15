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

        AuthorizationService authService = new AuthorizationService();

        String authHeader = req.getHeader("Authorization");
        if (authHeader != null && authHeader.matches("Bearer .+")) {
            String token = authHeader.replaceAll("(Bearer)", "").trim();

            try {
                Claims jwt = authService.verifyToken(token);

                if (authService.isRevoked(jwt))
                    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                else{
                    req.setAttribute("jwt", jwt);
                    filterChain.doFilter(req, resp);
                }
            }
            catch ( IllegalArgumentException e) {
                PrintWriter writer = resp.getWriter();
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                writer.write("empty");
                writer.flush();
                writer.close();
            }
            catch (ExpiredJwtException e) {
                PrintWriter writer = resp.getWriter();
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                writer.write("expired");
                writer.flush();
                writer.close();
            }
            catch (SecurityException e) {
                PrintWriter writer = resp.getWriter();
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                writer.write("invalid");
                writer.flush();
                writer.close();
            }
            catch (JwtException e){
                PrintWriter writer = resp.getWriter();
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                writer.write("error");
                writer.flush();
                writer.close();
            }

        }
        else
            filterChain.doFilter(req, resp);

    }

}
