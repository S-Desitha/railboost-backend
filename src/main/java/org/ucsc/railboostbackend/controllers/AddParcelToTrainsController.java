package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.ucsc.railboostbackend.models.AddParcelsTOTrain;
import org.ucsc.railboostbackend.models.ApproveParcel;
import org.ucsc.railboostbackend.models.SendParcels;
import org.ucsc.railboostbackend.models.Staff;
import org.ucsc.railboostbackend.repositories.AddParcelsToTrainRepo;
import org.ucsc.railboostbackend.repositories.StaffRepo;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class AddParcelToTrainsController extends HttpServlet {
    protected void doPut(HttpServletRequest req,HttpServletResponse resp)throws IOException{
        String sid = req.getParameter("sid");
        AddParcelsTOTrain addParcelsTOTrain;
        AddParcelsToTrainRepo addParcelsToTrainRepo = new AddParcelsToTrainRepo();
        Gson gson = new Gson();
        SendParcels sendParcels = gson.fromJson(req.getReader(), SendParcels.class);
        int scheduleId = sendParcels.getScheduleId();


        try {
            addParcelsToTrainRepo.updateDeliveryStatus(sendParcels);
            addParcelsToTrainRepo.updateDeliveryStatusToPending(scheduleId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)throws IOException {

        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        String sid = req.getParameter("scheduleId");

        if (sid != null && sid.equals("hello")){

            try {
                Claims jwt = (Claims) req.getAttribute("jwt");
                AddParcelsToTrainRepo addParcelsToTrainRepo = new AddParcelsToTrainRepo();
                List<AddParcelsTOTrain> addParcelsTOTrainList;

                StaffRepo staffRepo = new StaffRepo();
                int userId = jwt.get("userId", Integer.class);
                Staff staff = staffRepo.getStaffByUserId(userId);
                String station = staff.getStation();

                addParcelsTOTrainList = addParcelsToTrainRepo.getScheduleByStation(station);
                writer.write(gson.toJson(addParcelsTOTrainList));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }else {
            int scheduleId = Integer.parseInt(sid);

            AddParcelsToTrainRepo addParcelsToTrainRepo = new AddParcelsToTrainRepo();
            List<AddParcelsTOTrain> addParcelsTOTrainList2;;
            addParcelsTOTrainList2= addParcelsToTrainRepo.getParcelsByScheduleId(scheduleId);
            writer.write(gson.toJson(addParcelsTOTrainList2));
            writer.flush();
            writer.close();


        }


    }
}
