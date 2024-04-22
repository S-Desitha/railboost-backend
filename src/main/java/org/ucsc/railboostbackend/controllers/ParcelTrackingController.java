package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.ucsc.railboostbackend.models.ParcelTracking;
import org.ucsc.railboostbackend.models.SendParcels;
import org.ucsc.railboostbackend.models.Staff;
import org.ucsc.railboostbackend.repositories.ParcelTrackingRepo;
import org.ucsc.railboostbackend.repositories.StaffRepo;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class ParcelTrackingController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        ParcelTrackingRepo parcelTrackingRepo = new ParcelTrackingRepo();
        StaffRepo staffRepo = new StaffRepo();
        List<ParcelTracking> parcelTrackingList;


        Claims jwt = (Claims) req.getAttribute("jwt");
        try {
            int userId = jwt.get("userId", Integer.class);
            Staff staff = staffRepo.getStaffByUserId(userId);
            String station = staff.getStation();
            parcelTrackingList = parcelTrackingRepo.getTrackingParcelsByID(station);
            writer.write(gson.toJson(parcelTrackingList));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        ParcelTrackingRepo parcelTrackingRepo = new ParcelTrackingRepo();
        Gson gson = new Gson();
        SendParcels sendParcels = gson.fromJson(req.getReader(), SendParcels.class);
        int scheduleId = sendParcels.getScheduleId();
//        ParcelTracking parcelTracking = gson.fromJson(req.getReader(), ParcelTracking.class);

        try {
            parcelTrackingRepo.addScheduleToParcel(sendParcels,scheduleId);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
