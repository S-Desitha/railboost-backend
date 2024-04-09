package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import org.ucsc.railboostbackend.models.Notification;
import org.ucsc.railboostbackend.repositories.NotificationRepo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class NotificationController extends HttpServlet {

    private final NotificationRepo notificationRepo = new NotificationRepo();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        int userId = Integer.parseInt(req.getParameter("userId"));
        List<Notification> notifications = notificationRepo.getAllNotifications(userId);
        writer.write(gson.toJson(notifications));
        writer.flush();
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Notification notification = gson.fromJson(req.getReader(), Notification.class);
        notificationRepo.addNotification(notification);
    }
}
