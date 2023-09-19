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
        PrintWriter writer = resp.getWriter();

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        String query = String.format("SELECT username, password FROM users WHERE username=\"%s\" AND password=\"%s\"", username, password);
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try {
            connection = new DBConnection().getInstance();

            statement = connection.createStatement();
            result = statement.executeQuery(query);

            resp.setContentType("text/plain");
            if (result.next())
                writer.write("Login Successful");
            else
                writer.write("Invalid username or password. Try again!");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                if (result!=null)
                    result.close();
                if (statement!=null)
                    statement.close();
                if (connection!=null)
                    connection.close();
            } catch (SQLException e) {
                System.out.println("Error when closing ResultSet");
                System.out.println(e.getMessage());
            }
        }
    }
}
