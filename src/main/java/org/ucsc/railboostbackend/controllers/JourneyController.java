package org.ucsc.railboostbackend.controllers;

import com.google.gson.*;
import org.ucsc.railboostbackend.models.JourneyStation;
import org.ucsc.railboostbackend.repositories.JourneyRepo;
import org.ucsc.railboostbackend.services.LocalTimeDeserializer;
import org.ucsc.railboostbackend.utilities.Security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class JourneyController extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        HttpSession session = req.getSession();
//        Gson gson = new GsonBuilder().setDateFormat("HH:mm:ss").create();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalTime.class, LocalTimeDeserializer.INSTANCE)
                .setDateFormat("HH:mm:ss")
                .create();

        JourneyStation journeyStation = gson.fromJson(req.getReader(), JourneyStation.class);

        if (Security.verifyAccess(session, "sm", journeyStation.getStation())) {
//        if (Security.verifyAccess(session, "sm", "FOT")) {
            JourneyRepo journeyRepo = new JourneyRepo();
            journeyRepo.updateJourney(journeyStation);
            writer.write("Journey updated successfully");
        }
        else {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            writer.write("You don't have permission for this operation");
        }

        writer.flush();
        writer.close();
    }
}