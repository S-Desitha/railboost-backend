package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Claims;
import org.ucsc.railboostbackend.models.Booking;
import org.ucsc.railboostbackend.repositories.BookingRepo;
import org.ucsc.railboostbackend.services.CustomRequest;
import org.ucsc.railboostbackend.services.LocalDateDeserializer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.*;

public class BookingController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpServletRequest wrappedReq = new CustomRequest(req);
        PrintWriter writer = resp.getWriter();
        Claims jwt = (Claims) req.getAttribute("jwt");
        Object id = jwt.get("userId");
        BookingRepo bookingRepo = new BookingRepo();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .create();


        Booking booking;
        booking = gson.fromJson(req.getReader(), Booking.class);
        bookingRepo.bookTicketAndSendEmail(booking,id);

        writer.flush();
        writer.close();
    }
}
