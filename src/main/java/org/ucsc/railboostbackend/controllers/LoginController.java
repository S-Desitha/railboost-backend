package org.ucsc.railboostbackend.controllers;

import com.auth0.jwt.JWTCreator;
import com.google.gson.Gson;
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
        JWTCreator.Builder jwtBuilder = authorizationService.getJWTBuilder();
        String jwt;

        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();

        LoginRepo loginRepo = new LoginRepo();
        Login loginReq = gson.fromJson(req.getReader(), Login.class);
        Login loginResp = null;
        try {
            loginResp = loginRepo.verifyLogin(loginReq);
            if (loginResp.isSuccessful()){
                jwtBuilder = jwtBuilder.withClaim("username", loginResp.getUsername());
                jwtBuilder = jwtBuilder.withClaim("role", loginResp.getRole());
                jwtBuilder = jwtBuilder.withClaim("userId", loginRepo.getUserId());

                jwt = jwtBuilder.sign(authorizationService.getAlgorithm());
                writer.write(jwt);
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
