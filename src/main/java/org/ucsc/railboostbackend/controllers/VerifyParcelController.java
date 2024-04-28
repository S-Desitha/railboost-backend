package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.ucsc.railboostbackend.models.ApproveParcel;
import org.ucsc.railboostbackend.models.Staff;
import org.ucsc.railboostbackend.models.VerifyParcel;
import org.ucsc.railboostbackend.repositories.ApproveParcelRepo;
import org.ucsc.railboostbackend.repositories.StaffRepo;
import org.ucsc.railboostbackend.repositories.VerifyParcelRepo;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpRetryException;
import java.util.List;

public class VerifyParcelController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, HttpRetryException {
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        VerifyParcelRepo verifyParcelRepo = new VerifyParcelRepo();
        StaffRepo staffRepo = new StaffRepo();
        List<VerifyParcel> verifyParcelList;


        Claims jwt = (Claims) req.getAttribute("jwt");
        try {
            int userId = jwt.get("userId", Integer.class);
            Staff staff = staffRepo.getStaffByUserId(userId);
            String station = staff.getStation();
            verifyParcelList = verifyParcelRepo.getParcelDetailsByStation(station);
            writer.write(gson.toJson(verifyParcelList));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
         writer.flush();
        writer.close();
    }
}
