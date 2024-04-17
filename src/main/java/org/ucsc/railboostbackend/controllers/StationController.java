package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.ucsc.railboostbackend.models.Station;
import org.ucsc.railboostbackend.repositories.StationRepo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class StationController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        StationRepo stationRepo = new StationRepo();
        List<Station> stationNames;
        Gson gson = new Gson();

        stationNames = stationRepo.getStationNames();

        writer.write(gson.toJson(stationNames, List.class));
        writer.flush();
        writer.close();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        StationRepo stationRepo = new StationRepo();
        String stationCode = req.getParameter("stationCode");
        String stationName;

        stationName = stationRepo.getStationName(stationCode);

        writer.write(stationName);
        writer.flush();
        writer.close();
    }


//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        PrintWriter writer = resp.getWriter();
//        Gson gson = new Gson();
//        StationRepo stationRepo = new StationRepo();
//        Station station;
//
//        try {
//            station = gson.fromJson(req.getReader(), Station.class);
//            stationRepo.addStation(station);
//            resp.setStatus(HttpServletResponse.SC_CREATED);
//            writer.write("Station added successfully");
//        } catch (JsonSyntaxException e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            writer.write("Error occurred while adding station: " + e.getMessage());
//        }
//    }

}
