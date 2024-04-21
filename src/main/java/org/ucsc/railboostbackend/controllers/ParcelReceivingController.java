package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Claims;
import org.ucsc.railboostbackend.models.Notification;
import org.ucsc.railboostbackend.models.ParcelReceiving;
import org.ucsc.railboostbackend.models.Season;
import org.ucsc.railboostbackend.repositories.NotificationRepo;
import org.ucsc.railboostbackend.repositories.ParcelReceivingRepo;
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
import java.time.LocalDateTime;
import java.util.List;

public class ParcelReceivingController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String viewParam = req.getParameter("view");
        if (viewParam != null && viewParam.equals("1")) {
            PrintWriter writer = resp.getWriter();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                    .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                    .setDateFormat("MM/dd/yyyy")
                    .create();
            Claims jwt = (Claims) req.getAttribute("jwt");
            Object id = jwt.get("userId");
            ParcelReceivingRepo parcelReceivingRepo = new ParcelReceivingRepo();
            List<ParcelReceiving> parcelReceivingList;
            parcelReceivingList=parcelReceivingRepo.GetParcelCount(id);
            writer.write(gson.toJson(parcelReceivingList));

            writer.flush();
            writer.close();
        }else if (viewParam != null && viewParam.equals("2")) {
            String ScheduleId = req.getParameter("scheduleId");
            PrintWriter writer = resp.getWriter();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                    .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                    .setDateFormat("MM/dd/yyyy")
                    .create();
            Claims jwt = (Claims) req.getAttribute("jwt");
            Object id = jwt.get("userId");
            ParcelReceivingRepo parcelReceivingRepo = new ParcelReceivingRepo();
            List<ParcelReceiving> parcelReceivingList;
            parcelReceivingList=parcelReceivingRepo.GetParcelDetails(id,ScheduleId);
            writer.write(gson.toJson(parcelReceivingList));

            writer.flush();
            writer.close();
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            ParcelReceivingRepo parcelReceivingRepo = new ParcelReceivingRepo();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                    .create();

            ParcelReceiving parcelReceiving;
            parcelReceiving = gson.fromJson(req.getReader(), ParcelReceiving.class);

            if (parcelReceiving.getDeliverStatus().equals("Received")) {

                // Create a notification
                Notification notification = new Notification();
                notification.setUserId(parcelReceiving.getUserId());

                // Set notification title and message based on the status
                    notification.setTitle("Parcel is reached to the recovering satation.");
                    notification.setMessage("Your Parcel has been recieved to the recovering station and we informed the reciver via provided email.");

                // Set timestamp
                notification.setTimestamp(LocalDateTime.now());

                // Add the notification to the repository
                NotificationRepo.addNotification(notification);
            }
            parcelReceivingRepo.updateDeliveryStatus(parcelReceiving);
        }
        catch (IllegalStateException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/html");
            writer.write("File size exceeds. Request body is greater than 10 MB or file size is greater than 5 MB");
        }


        writer.flush();
        writer.close();
    }
}
