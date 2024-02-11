package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ucsc.railboostbackend.models.Role;
import org.ucsc.railboostbackend.repositories.RoleRepo;
import org.ucsc.railboostbackend.services.LocalDateSerializer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class RoleController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        RoleRepo roleRepo = new RoleRepo();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, LocalDateSerializer.INSTANCE)
                .setDateFormat("MM/dd/yyyy")
                .create();
        List<Role> roles;

        roles = roleRepo.getAllRoles();
        writer.write(gson.toJson(roles));
        writer.flush();
        writer.close();

    }
}
