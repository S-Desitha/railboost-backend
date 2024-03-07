package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ucsc.railboostbackend.models.Staff;
import org.ucsc.railboostbackend.models.StaffSignup;
import org.ucsc.railboostbackend.repositories.StaffRepo;
import org.ucsc.railboostbackend.services.*;

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
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .create();
        StaffRepo staffRepo = new StaffRepo();
        List<Staff> staffList;
        Staff staff;
//        System.out.println(req.getHeader("origin"));

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
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .create();

        if (wrappedReq.getAttribute("userId")==null){
            req.setAttribute("isForward", true);
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
                EmailService emailService = new EmailService(req.getHeader("Origin"));
                String body = emailService.createStaffSignupHTML(tempUID,staff);
                emailService.sendEmail(staff.getUser().getEmail(), "Staff Signup", body);

                StaffSignup temp = new StaffSignup();
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
