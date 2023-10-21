package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ucsc.railboostbackend.models.Schedule;
import org.ucsc.railboostbackend.repositories.ScheduleRepo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class ScheduleController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // return a list of all the train schedules based on the filtering provided by the user.
        BufferedWriter respWriter = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream()));

        System.out.println(req.getQueryString());
        System.out.println(req.getParameter("id"));
        req.getParameterMap().forEach((key, value) -> {
            System.out.println(key + " : "+ Arrays.toString(value));
        });

        respWriter.write("This is the train schedule home page.");
        respWriter.flush();
        respWriter.close();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        HttpSession httpSession = req.getSession();

        if (verifyAccess(httpSession, "admin")) {
            Gson gson = new GsonBuilder().setDateFormat("HH:mm").create();
            Schedule schedule = gson.fromJson(req.getReader(), Schedule.class);

            ScheduleRepo scheduleRepo = new ScheduleRepo();
            boolean result = scheduleRepo.addSchedule(schedule);

            if (result)
                writer.write("Schedule added successfully!!");
            else
                writer.write("An error occurred while adding the schedule.!!\nPlease try again");
        }
        else {
            writer.write("You dont' have permission for this operation!!");
        }

        writer.flush();
        writer.close();
    }

    private boolean verifyAccess(HttpSession httpSession, String role) {
        if (httpSession.getAttribute("role")!=null)
            return httpSession.getAttribute("role").equals(role);

        return false;
    }
}
