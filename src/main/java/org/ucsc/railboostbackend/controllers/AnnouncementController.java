package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ucsc.railboostbackend.models.Announcement;
import org.ucsc.railboostbackend.repositories.AnnouncementRepo;
import org.ucsc.railboostbackend.services.CustomRequest;
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

public class AnnouncementController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpServletRequest wrappedReq = new CustomRequest(req);
        PrintWriter writer = resp.getWriter();
        AnnouncementRepo announcementRepo = new AnnouncementRepo();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .create();


        Announcement announcement;
        announcement = gson.fromJson(req.getReader(), Announcement.class);
        announcementRepo.AddAnnouncement(announcement);

        writer.flush();
        writer.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String viewParam = req.getParameter("view");
        if(viewParam != null && viewParam.equals("1")){
            PrintWriter writer = resp.getWriter();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                    .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                    .setDateFormat("MM/dd/yyyy")
                    .create();
            AnnouncementRepo announcementRepo = new AnnouncementRepo();
            List<Announcement> announcementList;
            announcementList=announcementRepo.getAnnouncements();
            writer.write(gson.toJson(announcementList));

            writer.flush();
            writer.close();
        }else{
            PrintWriter writer = resp.getWriter();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                    .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                    .setDateFormat("MM/dd/yyyy")
                    .create();
            AnnouncementRepo announcementRepo = new AnnouncementRepo();
            List<Announcement> announcementList;
            announcementList=announcementRepo.getAnnouncementsForDash();
            writer.write(gson.toJson(announcementList));
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        try {
            AnnouncementRepo announcementRepo = new AnnouncementRepo();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                    .create();

            Announcement announcement;
            announcement = gson.fromJson(req.getReader(), Announcement.class);
            announcementRepo.updateAnns(announcement);
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .create();
        AnnouncementRepo announcementRepo = new AnnouncementRepo();
        Announcement announcement;

        announcement = gson.fromJson(req.getReader(), Announcement.class);
        announcementRepo.deleteAnns(announcement);
    }

}
