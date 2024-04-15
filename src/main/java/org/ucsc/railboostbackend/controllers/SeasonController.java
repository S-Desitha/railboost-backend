package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Claims;
import org.ucsc.railboostbackend.models.Notification;
import org.ucsc.railboostbackend.models.Season;
import org.ucsc.railboostbackend.repositories.SeasonRepo;
import org.ucsc.railboostbackend.repositories.NotificationRepo;
import org.ucsc.railboostbackend.services.FileRequestWrapper;
import org.ucsc.railboostbackend.services.FileResponseWrapper;
import org.ucsc.railboostbackend.services.LocalDateDeserializer;
import org.ucsc.railboostbackend.services.LocalDateSerializer;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@WebServlet("/season")
@MultipartConfig(
        fileSizeThreshold = 1024*1024,
        maxFileSize = 1024*1024*5,
        maxRequestSize = 1024*1024*10
)
public class SeasonController extends HttpServlet {
    private static final String UPLOAD_DIR = "uploads";
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FileRequestWrapper requestWrapper = new FileRequestWrapper(req);
        PrintWriter writer = resp.getWriter();

        try {
            String jsonObj = requestWrapper.getJsonObj();
            Claims jwt = (Claims) req.getAttribute("jwt");
            Object id = jwt.get("userId");
            String sid = (id != null) ? id.toString() : "";
            SeasonRepo seasonRepo = new SeasonRepo();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                    .create();

            Season season;
            season = gson.fromJson(jsonObj, Season.class);

            String filename = requestWrapper.saveFile(UPLOAD_DIR, sid);
            writer.write(filename);


            ArrayList<Integer> SMIds = seasonRepo.getSMIds(season); // Retrieve all station master IDs

// Iterate over each station master ID
            for (int SMId : SMIds) {
                // Print the SMId
                System.out.println(SMId);

                // Create a notification for the station master
                Notification notification = new Notification();
                notification.setUserId(SMId);
                notification.setTitle("New Season Ticket Application");
                notification.setMessage("A new season ticket application has been received.");
                notification.setTimestamp(LocalDateTime.now());

                // Add the notification to the repository
                NotificationRepo.addNotification(notification);
            }

            seasonRepo.ApplySeason(season,id,filename);
        }
        catch (ServletException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/html");
            writer.write("Unsupported content-type. Should be \"multipart/form-data\"");
        }
        catch (IllegalStateException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/html");
            writer.write("File size exceeds. Request body is greater than 10 MB or file size is greater than 5 MB");
        }


        writer.flush();
        writer.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            String viewParam = req.getParameter("view");
            if (viewParam != null && viewParam.equals("1")) {
                FileResponseWrapper responseWrapper = new FileResponseWrapper(req, resp);

                String filename = req.getParameter("fileName");
                responseWrapper.sendFile(filename, UPLOAD_DIR);
            }else if(viewParam != null && viewParam.equals("2")){
                PrintWriter writer = resp.getWriter();
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                        .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                        .setDateFormat("MM/dd/yyyy")
                        .create();
                Claims jwt = (Claims) req.getAttribute("jwt");
                Object id = jwt.get("userId");
                SeasonRepo seasonRepo = new SeasonRepo();
                List<Season> seasonList;
                seasonList=seasonRepo.getSeasonsOfUser(id);
                writer.write(gson.toJson(seasonList));

                writer.flush();
                writer.close();
            } else if(viewParam != null && viewParam.equals("3")){
                PrintWriter writer = resp.getWriter();
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                        .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                        .setDateFormat("MM/dd/yyyy")
                        .create();
                Claims jwt = (Claims) req.getAttribute("jwt");
                Object id = jwt.get("userId");
                SeasonRepo seasonRepo = new SeasonRepo();
                List<Season> seasonList;
                seasonList=seasonRepo.getPendingSeasons(id);
                writer.write(gson.toJson(seasonList));

                writer.flush();
                writer.close();
            }else{
                PrintWriter writer = resp.getWriter();
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                        .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                        .setDateFormat("MM/dd/yyyy")
                        .create();
                SeasonRepo seasonRepo = new SeasonRepo();

                String sid = req.getParameter("id");

                if (sid != null){
                    Season season = seasonRepo.getSeasonDetails(sid);
                    if (season != null){
                        writer.write(gson.toJson(season));
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

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        System.out.println("PUT method called");
        try {
            SeasonRepo seasonRepo = new SeasonRepo();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                    .create();

            Season season;
            season = gson.fromJson(req.getReader(), Season.class);

            if (season.getStatus().equals("Approved") || season.getStatus().equals("Rejected")) {

                System.out.println("Season status is approved or rejected");
                // Create a notification
                Notification notification = new Notification();
                notification.setUserId(season.getUserId());

                // Set notification title and message based on the status
                if (season.getStatus().equals("Approved")) {
                    System.out.println("Season status is approved");
                    notification.setTitle("Season Ticket Application Approved");
                    notification.setMessage("Your season ticket application has been approved. You can now proceed to pay for it.");
                } else {
                    notification.setTitle("Season Ticket Application Rejected");
                    notification.setMessage("Your season ticket application has been rejected. Please submit a valid application again.");
                }

                // Set timestamp
                notification.setTimestamp(LocalDateTime.now());

                // Add the notification to the repository
                NotificationRepo.addNotification(notification);
            }
            seasonRepo.updateStatus(season);
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
