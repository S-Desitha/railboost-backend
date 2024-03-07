package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Claims;
import org.ucsc.railboostbackend.models.Season;
import org.ucsc.railboostbackend.repositories.SeasonRepo;
import org.ucsc.railboostbackend.services.FileRequestWrapper;
import org.ucsc.railboostbackend.services.FileResponseWrapper;
import org.ucsc.railboostbackend.services.LocalDateDeserializer;
import org.ucsc.railboostbackend.services.LocalDateSerializer;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

//@WebServlet("/season")
@MultipartConfig(
        fileSizeThreshold = 1024*1024,
        maxFileSize = 1024*1024*5,
        maxRequestSize = 1024*1024*10
)
public class SeasonController extends HttpServlet {
    private static final String UPLOAD_DIR = "uploads";
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FileRequestWrapper requestWrapper = new FileRequestWrapper(req);
        PrintWriter writer = resp.getWriter();

        try {
            String jsonObj = requestWrapper.getJsonObj();
            Claims jwt = (Claims) req.getAttribute("jwt");
            Object id = jwt.get("userId");
            String sid = (id != null) ? id.toString() : "";
            SeasonRepo seasonRepo = new SeasonRepo();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                    .create();

            Season season;
            season = gson.fromJson(jsonObj, Season.class);

            String filename = requestWrapper.saveFile(UPLOAD_DIR, sid);
            writer.write(filename);
            seasonRepo.ApplySeason(season,id,filename);
        }
        catch (ServletException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/html");
            writer.write("Unsupported content-type. Should be \"multipart/form-data\"");
        }
        catch (IllegalStateException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/html");
            writer.write("File size exceeds. Request body is greater than 10 MB or file size is greater than 5 MB");
        }


        writer.flush();
        writer.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            String viewParam = req.getParameter("view");
            if (viewParam != null && viewParam.equals("1")) {
                FileResponseWrapper responseWrapper = new FileResponseWrapper(req, resp);

                String filename = req.getParameter("fileName");
                responseWrapper.sendFile(filename, UPLOAD_DIR);
            }else if(viewParam != null && viewParam.equals("2")){
                PrintWriter writer = resp.getWriter();
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                        .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                        .setDateFormat("MM/dd/yyyy")
                        .create();
                Claims jwt = (Claims) req.getAttribute("jwt");
                Object id = jwt.get("userId");
                SeasonRepo seasonRepo = new SeasonRepo();
                List<Season> seasonList;
                seasonList=seasonRepo.getSeasonsOfUser(id);
                writer.write(gson.toJson(seasonList));

                writer.flush();
                writer.close();
            } else {
                PrintWriter writer = resp.getWriter();
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                        .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                        .setDateFormat("MM/dd/yyyy")
                        .create();
                Claims jwt = (Claims) req.getAttribute("jwt");
                Object id = jwt.get("userId");
                SeasonRepo seasonRepo = new SeasonRepo();
                List<Season> seasonList;
                seasonList=seasonRepo.getPendingSeasons(id);
                writer.write(gson.toJson(seasonList));

                writer.flush();
                writer.close();
            }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        try {
            SeasonRepo seasonRepo = new SeasonRepo();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                    .create();

            Season season;
            season = gson.fromJson(req.getReader(), Season.class);
            seasonRepo.updateStatus(season);
        }
        catch (IllegalStateException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/html");
            writer.write("File size exceeds. Request body is greater than 10 MB or file size is greater than 5 MB");
        }


        writer.flush();
        writer.close();
    }

}
