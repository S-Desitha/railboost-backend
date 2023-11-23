package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ucsc.railboostbackend.models.User;
import org.ucsc.railboostbackend.repositories.SignUpRepo;
import org.ucsc.railboostbackend.services.CustomRequest;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class SignUpController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpServletRequest wrappedReq = new CustomRequest(req);
        PrintWriter writer = resp.getWriter();
        boolean isSuccess = false;
        int userId;
        SignUpRepo signupRepo = new SignUpRepo();
        Gson gson = new GsonBuilder().setDateFormat("dd:MM:yyyy").create();

        User user = gson.fromJson(wrappedReq.getReader(), User.class);
        if (user.getUsername()!=null)
            isSuccess = signupRepo.addGeneralUser(user);
        else{
            userId = signupRepo.addStaffUser(user);
            if (userId>=0){
                isSuccess = true;
                wrappedReq.setAttribute("userId", userId);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/staff");
                dispatcher.forward(wrappedReq, resp);
            }
        }

        if (isSuccess){
            RequestDispatcher dispatcher = getServletContext()
                    .getRequestDispatcher("/login");
            dispatcher.forward(wrappedReq, resp);
        }
        else
            writer.write("Something went wrong when inserting new user!!");
        writer.flush();
        writer.close();
    }

}
