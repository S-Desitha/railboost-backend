package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import org.ucsc.railboostbackend.models.TicketPrice;
import org.ucsc.railboostbackend.repositories.TicketPriceRepo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TicketPriceController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        TicketPriceRepo ticketPriceRepo = new TicketPriceRepo();

        String startStation = req.getParameter("startStation");
        String endStation = req.getParameter("endStation");

        if (startStation != null && endStation !=null){
            TicketPrice ticketPrice = ticketPriceRepo.getTicketPrice(startStation, endStation);
            if (ticketPrice != null){
                writer.write(gson.toJson(ticketPrice));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        writer.flush();
        writer.close();
    }
}
