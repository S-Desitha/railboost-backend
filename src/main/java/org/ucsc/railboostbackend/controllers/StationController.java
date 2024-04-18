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
//        System.out.println("Get stations called");
        StationRepo stationRepo = new StationRepo();
        List<Station> stationNames;
        Gson gson = new Gson();

        stationNames = stationRepo.getStationNames();

        writer.write(gson.toJson(stationNames, List.class));
        writer.flush();
        writer.close();

    }




    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        StationRepo stationRepo = new StationRepo();
        Station station;

        try {
            station = gson.fromJson(req.getReader(), Station.class);
            stationRepo.addStation(station);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            writer.write("Station added successfully");
        } catch (JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writer.write("Error occurred while adding station: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        StationRepo stationRepo = new StationRepo();
        PrintWriter writer = resp.getWriter();
//        System.out.println("Put stations called");

        // Convert JSON request body to Station object
        Station station = gson.fromJson(req.getReader(), Station.class);

        try {
            // Update the station
            stationRepo.updateStation(station);
            resp.setStatus(HttpServletResponse.SC_OK);
            writer.write("Station updated successfully");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writer.write("Error occurred while updating station: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        StationRepo stationRepo = new StationRepo();
        PrintWriter writer = resp.getWriter();
//        System.out.println("Delete stations called");

            // Convert JSON request body to Station object
            Station station = gson.fromJson(req.getReader(), Station.class);

            try {
                // Delete the station
                stationRepo.deleteStation(station);
                resp.setStatus(HttpServletResponse.SC_OK);
                writer.write("Station deleted successfully");
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                writer.write("Error occurred while deleting station: " + e.getMessage());
            }
    }

}
