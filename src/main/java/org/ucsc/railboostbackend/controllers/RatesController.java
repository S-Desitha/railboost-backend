package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ucsc.railboostbackend.models.ResponseType;
import org.ucsc.railboostbackend.models.TicketPrice;
import org.ucsc.railboostbackend.models.Train;
import org.ucsc.railboostbackend.repositories.RatesRepo;
import org.ucsc.railboostbackend.repositories.TicketPriceRepo;
import org.ucsc.railboostbackend.repositories.TrainRepo;
import org.ucsc.railboostbackend.services.CustomRequest;
import org.ucsc.railboostbackend.services.EmailService;
import org.ucsc.railboostbackend.services.LocalDateDeserializer;
import org.ucsc.railboostbackend.services.MyCache;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class RatesController extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        RatesRepo ratesRepo = new RatesRepo();
        List<TicketPrice> ratesList;
        int limit = Integer.parseInt(req.getParameter("limit"));
        int offset = Integer.parseInt(req.getParameter("offset"));

        ratesList=ratesRepo.getAllRates(limit, offset);
        writer.write(gson.toJson(ratesList));
        
        writer.flush();
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseType responseType;
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        RatesRepo rateRepo = new RatesRepo();
        TicketPrice rate;

        rate = gson.fromJson(req.getReader(), TicketPrice.class);
        responseType=rateRepo.addRate(rate);
        writer.write(gson.toJson(responseType));
        writer.flush();
        writer.close();

    }
//
//
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        RatesRepo rateRepo = new RatesRepo();
        TicketPrice rate;

        rate = gson.fromJson(req.getReader(), TicketPrice.class);
        rateRepo.updateRate(rate);
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        RatesRepo rateRepo = new RatesRepo();
        TicketPrice rate;

        rate = gson.fromJson(req.getReader(), TicketPrice.class);
        rateRepo.deleteRate(rate);
    }

}
