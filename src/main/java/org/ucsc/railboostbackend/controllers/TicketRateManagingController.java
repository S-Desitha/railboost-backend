//package org.ucsc.railboostbackend.controllers;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import io.jsonwebtoken.Claims;
//import org.ucsc.railboostbackend.models.TicketPrice;
//import org.ucsc.railboostbackend.repositories.JourneyRepo;
//import org.ucsc.railboostbackend.services.LocalDateDeserializer;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.URLDecoder;
//import java.time.LocalDate;
//import java.util.List;
//
//public class TicketRateManagingController extends HttpServlet {
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        PrintWriter writer = resp.getWriter();
//        Claims jwt = (Claims) req.getAttribute("jwt");
////        JourneyRepo journeyRepo = new JourneyRepo();
//        List<TicketPrice> ticketPriceList;
//        TicketPrice ticketPrice;
//
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(LocalDate.class, LocalDateDeserializer.INSTANCE)
//                .setDateFormat("MM/dd/yyyy")
//                .create();
//
//        String jsonQuery = URLDecoder.decode(req.getParameter("json"), "UTF-8");
//    }
//}
