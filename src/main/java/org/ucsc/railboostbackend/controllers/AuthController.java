package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import org.ucsc.railboostbackend.models.Auth;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        HttpSession session = req.getSession();
        Gson gson = new Gson();

        Auth auth = new Auth();
        if (!session.isNew()) {
            auth.setUsername((String) session.getAttribute("username"));
            auth.setRole((String) session.getAttribute("role"));
        }

        String json = gson.toJson(auth);
        writer.write(json);
        writer.flush();
        writer.close();
    }
}
