package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.mysql.cj.admin.ServerController;
import org.ucsc.railboostbackend.models.Train;
import org.ucsc.railboostbackend.repositories.TrainRepo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class TrainController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        TrainRepo trainRepo = new TrainRepo();
        List<Train> trains;
        Train train;

        String trainId = req.getParameter("trainId");

        if (trainId!=null){
            train = trainRepo.getTrainById(trainId);
            writer.write(gson.toJson(train));
        }
        else {
            trains = trainRepo.getTrains();
            writer.write(gson.toJson(trains));
        }

        writer.flush();
        writer.close();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Post train");
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        TrainRepo trainRepo = new TrainRepo();
        Train train;

        train = gson.fromJson(req.getReader(), Train.class);
        trainRepo.addTrain(train);

    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        TrainRepo trainRepo = new TrainRepo();
        Train train;

        train = gson.fromJson(req.getReader(), Train.class);
        trainRepo.updateTrain(train);
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        TrainRepo trainRepo = new TrainRepo();
        Train train;

        train = gson.fromJson(req.getReader(), Train.class);
        trainRepo.deleteTrain(train);
    }

}
