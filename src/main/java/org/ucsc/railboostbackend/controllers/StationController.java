package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import org.ucsc.railboostbackend.models.Station;
import org.ucsc.railboostbackend.repositories.StationRepo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
}
