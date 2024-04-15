package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Claims;
import org.ucsc.railboostbackend.models.User;
import org.ucsc.railboostbackend.repositories.ProfileRepo;
import org.ucsc.railboostbackend.services.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

@MultipartConfig(
        fileSizeThreshold = 1024*1024,
        maxFileSize = 1024*1024*5,
        maxRequestSize = 1024*1024*10
)
public class ProfileController extends HttpServlet {
    private static final String UPLOAD_DIR = "uploads";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String viewParam = req.getParameter("view");
        if (viewParam != null && viewParam.equals("1")) {
            PrintWriter writer = resp.getWriter();
            int userId = Integer.parseInt(req.getParameter("userId"));
            ProfileRepo profileRepo = new ProfileRepo();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                    .create();

            User user = profileRepo.getUserById(userId);

            writer.write(gson.toJson(user));
            writer.flush();
            writer.close();
        }
        else {
            FileResponseWrapper responseWrapper = new FileResponseWrapper(req, resp);

            String filename = req.getParameter("fileName");
            responseWrapper.sendFile(filename, UPLOAD_DIR);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String viewParam = req.getParameter("view");
        if(viewParam != null && viewParam.equals("1")){
            HttpServletRequest wrappedReq = new CustomRequest(req);
            PrintWriter writer = resp.getWriter();
            ProfileRepo profileRepo = new ProfileRepo();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                    .create();


            User user;
            user = gson.fromJson(req.getReader(), User.class);
            profileRepo.ChangePassword(user);

            writer.flush();
            writer.close();

        }else{
            FileRequestWrapper requestWrapper = new FileRequestWrapper(req);
            PrintWriter writer = resp.getWriter();

            try {
//            String jsonObj = requestWrapper.getJsonObj();
                Claims jwt = (Claims) req.getAttribute("jwt");
                Object id = jwt.get("userId");
                String sid = (id != null) ? id.toString() : "";
                ProfileRepo profileRepo = new ProfileRepo();
//            Gson gson = new GsonBuilder()
//                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
//                    .create();


                String filename = requestWrapper.saveFile(UPLOAD_DIR, sid);
                writer.write(filename);
                profileRepo.UpdateDp(id,filename);
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
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        try {
            ProfileRepo profileRepo = new ProfileRepo();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                    .create();

            User user;
            user = gson.fromJson(req.getReader(), User.class);
            profileRepo.updateProfile(user);
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .create();
        ProfileRepo profileRepo = new ProfileRepo();
        User user;

        user = gson.fromJson(req.getReader(), User.class);
        profileRepo.deleteAcc(user);
    }
}
