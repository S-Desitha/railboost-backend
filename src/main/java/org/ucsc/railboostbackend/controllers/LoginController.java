package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import org.ucsc.railboostbackend.models.Login;
import org.ucsc.railboostbackend.repositories.LoginRepo;
import org.ucsc.railboostbackend.utilities.DBConnection;
import org.ucsc.railboostbackend.utilities.HashPassword;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.Map;

public class LoginController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream()));
        writer.write("This is the login page");
        writer.flush();
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        HttpSession httpSession = req.getSession();
        Gson gson = new Gson();

        LoginRepo loginRepo = new LoginRepo();
        Login loginReq = gson.fromJson(req.getReader(), Login.class);
        Login loginResp = new Login(loginReq.getUsername());

        try {
            boolean loginStatus = loginRepo.verifyLogin(loginReq);
            loginResp.setSuccessful(loginStatus);
            if (loginStatus) {
                httpSession.setAttribute("userId", loginRepo.getUserId());
                httpSession.setAttribute("role", loginRepo.getRole());
                loginResp.setRole(loginRepo.getRole());
            }
            else{
                httpSession.invalidate();
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query!!\n" + e.getMessage());
        }

        String respJson = gson.toJson(loginResp);
        writer.write(respJson);

        writer.flush();
        writer.close();

    }

    private void temp(String username, String password) {

        try {
            Map<String, String> hashResult = new HashPassword().hash(password);

            String hash = hashResult.get("hash");
            String salt = hashResult.get("salt");

            System.out.println("Hashed Password: "+ hash);
            System.out.println("Salt: " + salt);

            String sql = "INSERT INTO users VALUES(?, ?, ?)";
            PreparedStatement statement = null;
            ResultSet result = null;
            try {
                Connection connection = DBConnection.getConnection();
                statement = connection.prepareStatement(sql);
                statement.setString(1, username);
                statement.setString(2, hash);
                statement.setString(3, salt);

                statement.executeUpdate();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (InvalidKeySpecException e) {
            System.out.println("Error in hash function.\n"+e.getMessage());
        }

    }
}
