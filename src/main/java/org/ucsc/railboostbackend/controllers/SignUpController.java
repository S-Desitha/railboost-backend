package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ucsc.railboostbackend.models.Login;
import org.ucsc.railboostbackend.models.Staff;
import org.ucsc.railboostbackend.models.StaffSignup;
import org.ucsc.railboostbackend.models.User;
import org.ucsc.railboostbackend.repositories.SignUpRepo;
import org.ucsc.railboostbackend.repositories.StaffRepo;
import org.ucsc.railboostbackend.services.CustomRequest;
import org.ucsc.railboostbackend.services.LocalDateDeserializer;
import org.ucsc.railboostbackend.services.MyCache;

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
        Gson gson = new Gson();
        StaffRepo staffRepo = new StaffRepo();
        MyCache cache = new MyCache();
        String tempUID = req.getParameter("tempUID");

        staffId = cache.get(tempUID);
        staff = staffRepo.getStaffById(staffId);

        writer.write(gson.toJson(staff));
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
                .registerTypeAdapter(LocalDate.class, LocalDateDeserializer.INSTANCE)
                .setDateFormat("MM/dd/yyyy")
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
            String staffId = cache.get(staff.getTempUID());
            isSuccess = signupRepo.finishStaffSignup(staff.getUser().getPassword(), staffId);
        }

        if (isSuccess){
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login");
            wrappedReq.setBody(gson.toJson(user));
            dispatcher.forward(wrappedReq, resp);
        }
        else
            writer.write("Something went wrong when inserting new user!!");
        writer.flush();
        writer.close();
    }

}
