package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import org.ucsc.railboostbackend.models.GetRevenue;
import org.ucsc.railboostbackend.repositories.GetRevenueRepo;
import org.ucsc.railboostbackend.repositories.ParcelBookingRepo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GetRevenueController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer =resp.getWriter();
        Gson gson = new Gson();
        GetRevenueRepo getRevenueRepo = new GetRevenueRepo();
        GetRevenue getRevenue = new GetRevenue();


        getRevenue.setTotalParcelBookingRevenue(GetRevenueRepo.getTotalParcelRevenue());
        getRevenue.setTotalTicketRevenue(getRevenueRepo.getTotalTicketRevenue());
        getRevenue.setMonthlyParcelRevenueList(getRevenueRepo.getMonthleParcelBookingRevenue());
        getRevenue.setGeMonthlyTicketRevenueList(getRevenueRepo.getmonthlyTicketRevenue());
        getRevenue.setMonthList(getRevenueRepo.getMonths());
        writer.write(gson.toJson(getRevenue));


    }
}
