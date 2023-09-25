package org.ucsc.railboostbackend.Controllers;

import org.ucsc.railboostbackend.Utilities.DBConnection;
import org.ucsc.railboostbackend.Utilities.HashPassword;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.Map;

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

        if (validateUser(username, password))
            writer.write("Login Successful");
        else
            writer.write("Invalid username or password.\nPlease try again!!");

        writer.flush();
        writer.close();

//        temp(username, password);
    }


    private boolean validateUser(String username, String inputPassword) {
        HashPassword hashPassword = new HashPassword();

        String query = String.format("SELECT password, salt FROM users WHERE username=\"%s\"", username);
        Statement statement = null;
        ResultSet result = null;
        try {
            Connection connection = DBConnection.getInstance().getConnection();

            statement = connection.createStatement();
            result = statement.executeQuery(query);

            while (result.next()) {
                String hash = result.getString("password");
                String salt = result.getString("salt");

                // entered password matches with the password stored with the given username.
                if (hashPassword.hash(inputPassword, salt).equals(hash)) {
                    return true;
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                if (result!=null)
                    result.close();
                if (statement!=null)
                    statement.close();
            } catch (SQLException e) {
                System.out.println("Error when closing ResultSet");
                System.out.println(e.getMessage());
            }
        }


        return false;
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
                Connection connection = DBConnection.getInstance().getConnection();
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
