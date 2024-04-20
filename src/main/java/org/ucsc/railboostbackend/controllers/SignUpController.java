package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.ucsc.railboostbackend.models.*;
import org.ucsc.railboostbackend.repositories.SignUpRepo;
import org.ucsc.railboostbackend.repositories.StaffRepo;
import org.ucsc.railboostbackend.services.CustomRequest;
import org.ucsc.railboostbackend.services.LocalDateDeserializer;
import org.ucsc.railboostbackend.services.LocalDateSerializer;
import org.ucsc.railboostbackend.services.MyCache;

import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

public class SignUpController extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Staff staff;
        String staffId;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .create();
        StaffRepo staffRepo = new StaffRepo();
        MyCache cache = new MyCache();
        String tempUID = req.getParameter("tempUID");

        try {
            staffId = cache.get(tempUID);
            staff = staffRepo.getStaffById(staffId);
            writer.write(gson.toJson(staff));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.write(gson.toJson(e));
        }
        writer.flush();
        writer.close();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CustomRequest wrappedReq = new CustomRequest(req);
        PrintWriter writer = resp.getWriter();
        boolean isSuccess = false;
        int userId;
        SignUpRepo signupRepo = new SignUpRepo();
        MyCache cache = new MyCache();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .create();

        Staff staff = gson.fromJson(wrappedReq.getReader(), Staff.class);
        User user = staff.getUser();

        if (!user.isStaff())
            isSuccess = signupRepo.addGeneralUser(user);
        else if (req.getAttribute("isForward")!=null){
            userId = signupRepo.addStaffUser(user);
            if (userId>=0){
                isSuccess = true;
                wrappedReq.setAttribute("userId", userId);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/staff");
                dispatcher.forward(wrappedReq, resp);
            }
        }
        else {
            String staffId = null;
            try {
                String tempUID = staff.getTempUID();
                Scheduler scheduler = new StdSchedulerFactory().getScheduler();
                TriggerKey triggerKey = new TriggerKey(tempUID);

                staffId = cache.get(tempUID);
                scheduler.pauseTrigger(triggerKey);

                isSuccess = signupRepo.finishStaffSignup(staff.getUser().getPassword(), staffId);
                if (isSuccess)
                    cache.clearCache(tempUID, false);
                else
                    scheduler.resumeTrigger(triggerKey);
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write(gson.toJson(e));
            }
        }

        if (isSuccess){
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login");
            wrappedReq.setBody(gson.toJson(user));
            dispatcher.forward(wrappedReq, resp);
        }
        else {
            writer.write("Something went wrong when inserting new user!!");
            writer.flush();
            writer.close();
        }
    }

}
