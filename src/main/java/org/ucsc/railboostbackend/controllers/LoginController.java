package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import io.jsonwebtoken.JwtBuilder;
import org.ucsc.railboostbackend.models.Login;
import org.ucsc.railboostbackend.repositories.LoginRepo;
import org.ucsc.railboostbackend.services.AuthorizationService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        AuthorizationService authorizationService = new AuthorizationService();
        JwtBuilder jwtBuilder = authorizationService.getJWTBuilder();
        String jwt;

        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();

        LoginRepo loginRepo = new LoginRepo();
        Login loginReq = gson.fromJson(req.getReader(), Login.class);
        Login loginResp = null;
        try {
            loginResp = loginRepo.verifyLogin(loginReq);
            if (loginResp.isSuccessful()){
                jwtBuilder = jwtBuilder.claim("username", loginResp.getUsername());
                jwtBuilder = jwtBuilder.claim("role", loginResp.getRole().getRoleId());
                jwtBuilder = jwtBuilder.claim("userId", loginRepo.getUserId());

                jwt = jwtBuilder.signWith(authorizationService.getKey()).compact();
                loginResp.setJwt(jwt);

                writer.write(gson.toJson(loginResp));
                writer.flush();
                writer.close();
            }
            else {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }

        } catch (SQLException e) {
            System.out.println("Error executing SQL query!!\n" + e.getMessage());
        }
    }
}
