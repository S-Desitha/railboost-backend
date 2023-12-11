package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import org.ucsc.railboostbackend.models.Login;
import org.ucsc.railboostbackend.repositories.LoginRepo;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.*;

public class LoginController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream()));
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        writer.write("This is the login page");
        writer.flush();
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        HttpSession httpSession = req.getSession();
        Gson gson = new Gson();

        httpSession.setMaxInactiveInterval(24*60*60);

        LoginRepo loginRepo = new LoginRepo();
        Login loginReq = gson.fromJson(req.getReader(), Login.class);
//        Login loginResp = new Login(loginReq.getUsername());
        Login loginResp = null;
        try {
            loginResp = loginRepo.verifyLogin(loginReq);
//            boolean loginStatus = loginRepo.verifyLogin(loginReq);
//            loginResp.setSuccessful(loginStatus);
//            if (loginStatus) {
//                httpSession.setAttribute("username", loginRepo.getUserId());
//                httpSession.setAttribute("role", loginRepo.getRole());
//                loginResp.setRole(loginRepo.getRole());
//            }
            if (loginResp.isSuccessful()){
                httpSession.setAttribute("username", loginResp.getUsername());
                httpSession.setAttribute("role", loginResp.getRole());
                httpSession.setAttribute("userId", loginRepo.getUserId());
            }
            else{
                httpSession.invalidate();
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query!!\n" + e.getMessage());
        }

        resp.setContentType("application/json; charset=UTF-8");

        String respJson = gson.toJson(loginResp);
        writer.write(respJson);

        writer.flush();
        writer.close();

    }
}
