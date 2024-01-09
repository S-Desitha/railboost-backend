package org.ucsc.railboostbackend.controllers;

import io.jsonwebtoken.Claims;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HomeLandingController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter respWriter = resp.getWriter();
        Claims claims = (Claims) req.getAttribute("jwt");

        if (claims.get("role")!=null){
            respWriter.write("You are signed in as " + claims.get("role"));
        }
        else
            respWriter.write("You are not signed in");

        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        respWriter.flush();
        respWriter.close();
    }
}
