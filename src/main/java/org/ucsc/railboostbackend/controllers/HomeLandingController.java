package org.ucsc.railboostbackend.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class HomeLandingController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter respWriter = resp.getWriter();
        DecodedJWT jwt = (DecodedJWT) req.getAttribute("jwt");

        if (jwt.getClaim("role")!=null){
            respWriter.write("You are signed in as " + jwt.getClaim("role"));
        }
        else
            respWriter.write("You are not signed in");

        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        respWriter.flush();
        respWriter.close();
    }
}
