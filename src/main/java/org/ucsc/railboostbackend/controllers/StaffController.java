package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ucsc.railboostbackend.models.Staff;
import org.ucsc.railboostbackend.models.TempUID;
import org.ucsc.railboostbackend.models.User;
import org.ucsc.railboostbackend.repositories.StaffRepo;
import org.ucsc.railboostbackend.services.CustomRequest;
import org.ucsc.railboostbackend.services.EmailService;
import org.ucsc.railboostbackend.services.LocalDateDeserializer;
import org.ucsc.railboostbackend.services.MyCache;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class StaffController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        StaffRepo staffRepo = new StaffRepo();
        List<Staff> staffList;
        Staff staff;

        String staffId = req.getParameter("staffId");

        if (staffId!=null){
            staff = staffRepo.getStaffById(staffId);
            writer.write(gson.toJson(staff));
        }
        else {
            staffList = staffRepo.getAllStaff();
            writer.write(gson.toJson(staffList));
        }

        writer.flush();
        writer.close();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpServletRequest wrappedReq = new CustomRequest(req);
        PrintWriter writer = resp.getWriter();
        boolean isSuccessful = false;
        String tempUID = null;
        MyCache myCache = new MyCache();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, LocalDateDeserializer.INSTANCE)
                .setDateFormat("MM/dd/yyyy")
                .create();

        if (wrappedReq.getAttribute("userId")==null){
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/signup");
            dispatcher.forward(wrappedReq, resp);
        }
        else {
            Staff staff = gson.fromJson(wrappedReq.getReader(), Staff.class);
            staff.setUserId((Integer) wrappedReq.getAttribute("userId"));

            isSuccessful = new StaffRepo().addStaffMember(staff);
            
            tempUID = myCache.createTempUID(staff.toString());
            myCache.add(staff.getStaffId(), tempUID);

            if (isSuccessful){
                EmailService emailService = new EmailService();
                String body = emailService.createStaffSignupHTML(tempUID);
                emailService.sendEmail(staff.getUser().getEmail(), "Staff Signup", body);

                TempUID temp = new TempUID();
                temp.setUid(tempUID);
                writer.write(gson.toJson(temp));
            }
            else
                writer.write("There was an error adding the staff member.");
        }

        writer.flush();
        writer.close();
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        StaffRepo staffRepo = new StaffRepo();
        Staff staff;

        staff = gson.fromJson(req.getReader(), Staff.class);
        staffRepo.updateStaff(staff);
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        StaffRepo staffRepo = new StaffRepo();
        Staff staff;

        staff = gson.fromJson(req.getReader(), Staff.class);
        staffRepo.deleteMember(staff);
    }
}
