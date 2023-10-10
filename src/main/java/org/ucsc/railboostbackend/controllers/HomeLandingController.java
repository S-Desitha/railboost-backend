package org.ucsc.railboostbackend.controllers;

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
        HttpSession session = req.getSession();

        if (session.getAttribute("role")!=null){
            respWriter.write("You are signed in as " + session.getAttribute("role"));
        }
        else
            respWriter.write("You are not signed in");

        respWriter.flush();
        respWriter.close();
    }
}
