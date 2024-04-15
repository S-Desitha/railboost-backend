package org.ucsc.railboostbackend.controllers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import io.jsonwebtoken.Claims;
import org.ucsc.railboostbackend.enums.Roles;
import org.ucsc.railboostbackend.models.Schedule;
import org.ucsc.railboostbackend.repositories.ScheduleRepo;
import org.ucsc.railboostbackend.services.LocalDateDeserializer;
import org.ucsc.railboostbackend.services.LocalDateSerializer;
import org.ucsc.railboostbackend.services.LocalTimeDeserializer;
import org.ucsc.railboostbackend.services.LocalTimeSerializer;
import org.ucsc.railboostbackend.utilities.Security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.ucsc.railboostbackend.utilities.Security.verifyAccess;

public class ScheduleController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        ScheduleRepo scheduleRepo = new ScheduleRepo();
        List<Schedule> scheduleList;
        Schedule schedule;

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(LocalTime.class, new LocalTimeSerializer())
                .create();


        String jsonQuery = URLDecoder.decode(req.getParameter("json"), "UTF-8");
        Schedule reqSchedule = gson.fromJson(jsonQuery, Schedule.class);

        if(reqSchedule.getScheduleId()>0) {
            schedule = scheduleRepo.getScheduleById(reqSchedule.getScheduleId());
            writer.write(gson.toJson(schedule));
            writer.flush();
        }
        else {
            scheduleList = scheduleRepo.getSchedules(reqSchedule);
            writer.write(gson.toJson(scheduleList));
            writer.flush();
        }
//        scheduleList.forEach(System.out::println);

        writer.close();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Claims jwt = (Claims) req.getAttribute("jwt");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalTime.class, new LocalTimeDeserializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .create();


        if (verifyAccess(jwt, Roles.ADMINISTRATOR)) {
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
        Claims jwt = (Claims) req.getAttribute("jwt");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalTime.class, new LocalTimeDeserializer())
                .create();

        if (Security.verifyAccess(jwt, Roles.ADMINISTRATOR)){
            ScheduleRepo repo = new ScheduleRepo();
//            Gson gson = new GsonBuilder().setDateFormat("HH:mm").create();
            List<Schedule> schedules = gson.fromJson(req.getReader(), new TypeToken<ArrayList<Schedule>>(){});
            try {
                repo.updateSchedule(schedules.get(0), schedules.get(1));
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            writer.write("Schedule updated successfully");
        }
        else
            writer.write("You don't have permission for this operation");

        writer.flush();
        writer.close();
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Claims jwt = (Claims) req.getAttribute("jwt");

        if (Security.verifyAccess(jwt, Roles.ADMINISTRATOR)){
            ScheduleRepo scheduleRepo = new ScheduleRepo();
            short scheduleId = Short.parseShort(req.getParameter("scheduleId"));
            Schedule schedule = scheduleRepo.getScheduleById(scheduleId);
            scheduleRepo.deleteSchedule(schedule);

            writer.write("Schedule deleted successfully");
        }
        else
            writer.write("You don't have permission for this operation");
        writer.flush();
        writer.close();
    }

}
