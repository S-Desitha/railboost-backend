package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Claims;
import org.ucsc.railboostbackend.enums.Roles;
import org.ucsc.railboostbackend.models.CancelledSchedule;
import org.ucsc.railboostbackend.models.CancelledSchedulesList;
import org.ucsc.railboostbackend.models.ResponseType;
import org.ucsc.railboostbackend.repositories.ScheduleRepo;
import org.ucsc.railboostbackend.services.LocalDateDeserializer;
import org.ucsc.railboostbackend.utilities.Security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class ScheduleCancelController extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Claims jwt = (Claims) req.getAttribute("jwt");
        ResponseType responseType;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .create();

        if (Security.verifyAccess(jwt, Roles.ADMINISTRATOR)) {
            CancelledSchedulesList cancelledScheduleList = gson.fromJson(req.getReader(), CancelledSchedulesList.class);
            ScheduleRepo scheduleRepo = new ScheduleRepo();

            responseType = scheduleRepo.cancelSchedules(cancelledScheduleList.getCancelledScheduleList());
        }
        else {
            responseType = new ResponseType(false, "You don't have access to perform this operation!");
        }

        writer.write(gson.toJson(responseType));
        writer.flush();
        writer.close();
    }
}
