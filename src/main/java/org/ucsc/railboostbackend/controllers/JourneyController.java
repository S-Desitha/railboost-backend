package org.ucsc.railboostbackend.controllers;

import com.google.gson.*;
import io.jsonwebtoken.Claims;
import org.ucsc.railboostbackend.models.Journey;
import org.ucsc.railboostbackend.models.JourneyStation;
import org.ucsc.railboostbackend.models.Staff;
import org.ucsc.railboostbackend.repositories.JourneyRepo;
import org.ucsc.railboostbackend.repositories.StaffRepo;
import org.ucsc.railboostbackend.services.LocalDateDeserializer;
import org.ucsc.railboostbackend.services.LocalTimeDeserializer;
import org.ucsc.railboostbackend.utilities.Security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.time.*;
import java.util.List;

public class JourneyController extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Claims jwt = (Claims) req.getAttribute("jwt");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalTime.class, LocalTimeDeserializer.INSTANCE)
                .setDateFormat("HH:mm:ss")
                .create();

        JourneyStation journeyStation = gson.fromJson(req.getReader(), JourneyStation.class);

        if (Security.verifyAccess(jwt, "sm", journeyStation.getStation())) {
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


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Claims jwt = (Claims) req.getAttribute("jwt");
        JourneyRepo journeyRepo = new JourneyRepo();
        List<Journey> journeyList;
        Journey journey;
        String stationCode;

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, LocalDateDeserializer.INSTANCE)
                .registerTypeAdapter(LocalTime.class, LocalTimeDeserializer.INSTANCE)
                .setDateFormat("MM/dd/yyyy")
                .create();

        String jsonQuery = URLDecoder.decode(req.getParameter("json"), "UTF-8");
        Journey reqJourney = gson.fromJson(jsonQuery, Journey.class);

        if (reqJourney.getScheduleId()>0){
            journey = journeyRepo.getJourney(reqJourney.getDate(), reqJourney.getScheduleId());
            writer.write(gson.toJson(journey));
        }

        else if (jwt.get("role")!=null && jwt.get("role", String.class).equals("sm")) {
            Staff staff = new StaffRepo()
                    .getStaffByUserId((Integer) jwt.get("userId", Integer.class));
            if (staff!=null){
                stationCode = staff.getStation();
                journeyList = journeyRepo.getJourneysByStation(reqJourney.getDate(), stationCode);
                writer.write(gson.toJson(journeyList));
            }
            else{
                writer.write("{}");
            }
        }
//        else {
//            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            writer.write("You are not authorized to do this operation");
//        }

        writer.flush();
        writer.close();
    }
}
