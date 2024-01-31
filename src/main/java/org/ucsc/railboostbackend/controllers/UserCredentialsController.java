package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Claims;
import org.ucsc.railboostbackend.models.Role;
import org.ucsc.railboostbackend.models.User;
import org.ucsc.railboostbackend.services.LocalDateSerializer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

public class UserCredentialsController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Claims jwt = (Claims) req.getAttribute("jwt");
        User user = new User();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, LocalDateSerializer.INSTANCE)
                .setDateFormat("MM/dd/yyyy")
                .create();

        user.setUsername(jwt.get("username", String.class));
        user.setRole(new Role(jwt.get("role", Integer.class)));

        writer.write(gson.toJson(user));
        writer.flush();
        writer.close();
    }
}
