package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.checkerframework.checker.units.qual.A;
import org.ucsc.railboostbackend.models.ApproveParcel;
import org.ucsc.railboostbackend.models.Notification;
import org.ucsc.railboostbackend.models.Staff;
import org.ucsc.railboostbackend.repositories.ApproveParcelRepo;
import org.ucsc.railboostbackend.repositories.NotificationRepo;
import org.ucsc.railboostbackend.repositories.StaffRepo;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpRetryException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ApproveParcelController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, HttpRetryException {
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        ApproveParcelRepo approveParcelRepo = new ApproveParcelRepo();
        StaffRepo staffRepo = new StaffRepo();
        List<ApproveParcel> approvedParcelList;


        Claims jwt = (Claims) req.getAttribute("jwt");
        try {
            int userId = jwt.get("userId", Integer.class);
            Staff staff = staffRepo.getStaffByUserId(userId);
            String station = staff.getStation();
            approvedParcelList = approveParcelRepo.getParcelDetailsByStation(station);
            writer.write(gson.toJson(approvedParcelList));
        } catch (Exception e) {
        }
        writer.flush();
        writer.close();
    }


    public void doPut(HttpServletRequest req,HttpServletResponse resp) throws  IOException, HttpRetryException{
        String viewParam = req.getParameter("view");
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        ApproveParcelRepo approveParcelRepo = new ApproveParcelRepo();
        ApproveParcel approveParcel;

        approveParcel = gson.fromJson(req.getReader(), ApproveParcel.class);

        Float weight = approveParcel.getWeight();
        if ((viewParam != null && viewParam.equals("1"))){
            try {
                approveParcelRepo.addWeightAndTotal(approveParcel);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }else {

            approveParcelRepo.updateStatus(approveParcel);

            String approveStatus = approveParcel.getStatus();
            if (approveParcel.getStatus().equals("Approved") || approveParcel.getStatus().equals("Rejected")){
                Notification notification = new Notification();
                notification.setUserId(approveParcel.getUserId());

                if (approveParcel.getStatus().equals("Approved")){
                    notification.setTitle("Parcel Booking Application Approved");
                    notification.setMessage("Your parcel has been approved. You can now proceed to pay for it.");
                }else {
                    notification.setTitle("Parcel Booking Application Rejected");
                    notification.setMessage("Your parcel has been rejected. Please submit a valid application again.");
                }

                // Set timestamp
                notification.setTimestamp(LocalDateTime.now());

                // Add the notification to the repository
                NotificationRepo.addNotification(notification);

            }


        }



    }
}


