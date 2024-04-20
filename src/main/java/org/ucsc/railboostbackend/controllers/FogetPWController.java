package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.ucsc.railboostbackend.models.User;
import org.ucsc.railboostbackend.repositories.ForgetPWRepo;
import org.ucsc.railboostbackend.services.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

public class FogetPWController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String viewParam = req.getParameter("view");
        if (viewParam != null && viewParam.equals("1")) {
            CustomRequest wrappedReq = new CustomRequest(req);
            boolean isSuccess = false;
            PrintWriter writer = resp.getWriter();
            MyCache cache = new MyCache();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                    .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                    .create();

            User user = gson.fromJson(wrappedReq.getReader(), User.class);
            String username;
            try {
                String tempUID = user.getTempUID();
                Scheduler scheduler = new StdSchedulerFactory().getScheduler();
                TriggerKey triggerKey = new TriggerKey(tempUID);
                username = cache.get(tempUID);
                scheduler.pauseTrigger(triggerKey);

                ForgetPWRepo forgetPWRepo = new ForgetPWRepo();
                isSuccess = forgetPWRepo.createNewPW(user, username);
                if (isSuccess)
                    cache.clearCache(tempUID, false);
                else
                    scheduler.resumeTrigger(triggerKey);
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write(gson.toJson(e));
                writer.flush();
                writer.close();
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            HttpServletRequest wrappedReq = new CustomRequest(req);
            PrintWriter writer = resp.getWriter();
            boolean isSuccessful = false;
            String tempUID = null;
            MyCache myCache = new MyCache();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                    .create();

            User user = gson.fromJson(wrappedReq.getReader(), User.class);
            ForgetPWRepo forgetPWRepo = new ForgetPWRepo();
            isSuccessful = forgetPWRepo.checkUserName(user);
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("isSuccessful", isSuccessful);
            tempUID = myCache.createTempUID(user.toString());
            myCache.add(user.getUsername(), tempUID);

            if (isSuccessful) {
                EmailService emailService = new EmailService(req.getHeader("Origin"));
                String body = emailService.createFogetPWHTML(tempUID, user);
                String mail = forgetPWRepo.getMail(user);
                emailService.sendEmail(mail, "Forget Password", body);
            }

            writer.write(jsonResponse.toString());
            writer.flush();
            writer.close();
        }
    }
}
