package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.ucsc.railboostbackend.models.Schedule;
import org.ucsc.railboostbackend.repositories.ScheduleRepo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.beans.IntrospectionException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // return a list of all the train schedules based on the filtering provided by the user.
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream()));
        ScheduleRepo scheduleRepo = new ScheduleRepo();
        Gson gson = new Gson();

//        Days day = Days.valueOf(req.getParameter("day"));
//        String startSt = req.getParameter("startSt");
//        String endSt = req.getParameter("endSt");
//        List<Schedule> schedules = scheduleRepo.getSchedules(day, startSt, endSt);

        Short scheduleId = Short.valueOf(req.getParameter("scheduleId"));
        Schedule schedules = scheduleRepo.getScheduleById(scheduleId);

//        schedules.forEach(System.out::println);

        writer.write(gson.toJson(schedules));
        writer.flush();
        writer.close();
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


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        ScheduleRepo repo = new ScheduleRepo();
        Gson gson = new GsonBuilder().setDateFormat("HH:mm").create();
        List<Schedule> schedules = gson.fromJson(req.getReader(), new TypeToken<ArrayList<Schedule>>(){});
        try {
//            Schedule sch = new Schedule();
//            sch.setScheduleId((short) 1);
//            sch.setStartStation("FOT");
//            sch.setEndStation("GPH");
//            sch.setTrainType("Express");

            repo.updateSchedule(schedules.get(0), schedules.get(1));
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        writer.write("Schedule updated successfully");
        writer.flush();
        writer.close();
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        ScheduleRepo scheduleRepo = new ScheduleRepo();

        short scheduleId = Short.parseShort(req.getParameter("scheduleId"));
        Schedule schedule = scheduleRepo.getScheduleById(scheduleId);

        scheduleRepo.deleteSchedule(schedule);

        writer.write("Schedule deleted successfully");
        writer.flush();
        writer.close();
    }


    private boolean verifyAccess(HttpSession httpSession, String role) {
        if (httpSession.getAttribute("role")!=null)
            return httpSession.getAttribute("role").equals(role);

        return false;
    }
}
