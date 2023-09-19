package org.ucsc.railboostbackend.Controllers;

import org.ucsc.railboostbackend.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream()));
        writer.write("This is the login page");
        writer.flush();
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean isValid = false;
        PrintWriter writer = resp.getWriter();

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

//        String query = "select * from users";
        try {
            Connection connection = new DBConnection().getInstance();
            isValid = authenticateUser(username, password, connection);
//            Statement statement = connection.createStatement();
//            ResultSet result = statement.executeQuery(query);
//            while (result.next()) {
//                System.out.println(result.getString("username"));
//            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        resp.setContentType("text/plain");
        if (isValid)
            writer.write("Login Successful");
        else 
            writer.write("Invalid username or password. Try again!");
            

    }


    private boolean authenticateUser(String input_username, String input_password, Connection connection) throws SQLException {
        String query = String.format("SELECT username, password FROM users WHERE username=\"%s\" AND password=\"%s\"", input_username, input_password);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query);

         return result.next();
    }
}
