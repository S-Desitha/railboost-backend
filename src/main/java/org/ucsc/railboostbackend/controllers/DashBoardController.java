package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Claims;
import org.ucsc.railboostbackend.models.DashboardModel;
import org.ucsc.railboostbackend.models.Line;
import org.ucsc.railboostbackend.models.Season;
import org.ucsc.railboostbackend.repositories.DashRepo;
import org.ucsc.railboostbackend.repositories.LineRepo;
import org.ucsc.railboostbackend.repositories.SeasonRepo;
import org.ucsc.railboostbackend.services.FileResponseWrapper;
import org.ucsc.railboostbackend.services.LocalDateDeserializer;
import org.ucsc.railboostbackend.services.LocalDateSerializer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class DashBoardController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer =resp.getWriter();
        Gson gson = new Gson();
        DashRepo dashRepo =  new DashRepo();
        DashboardModel dashboardModel = new DashboardModel();



        int noTrain = dashRepo.getTrainCount();
        int noTicket =dashRepo.getTicketCount();
        dashboardModel.setNoTicket(noTicket);
        dashboardModel.setTrainCount(noTrain);
        writer.write(gson.toJson((dashboardModel)));


        writer.flush();
        writer.close();
    }
}
