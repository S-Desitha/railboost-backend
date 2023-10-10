package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.Login;
import org.ucsc.railboostbackend.utilities.DBConnection;
import org.ucsc.railboostbackend.utilities.HashPassword;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginRepo {
    private int userId;
    private String role;

    public boolean verifyLogin(Login login) throws SQLException {
        boolean isSuccess = false;
        HashPassword hashPassword = new HashPassword();
        String username = login.getUsername();
        String inputPassword = login.getPassword();


        String query = "SELECT userId, password, salt, role FROM users where username=?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, username);

        ResultSet resultSet = pst.executeQuery();
        if (resultSet.next()) {
            String storedPassword = resultSet.getString("password");
            String salt = resultSet.getString("salt");

            if (hashPassword.hash(inputPassword, salt).equals(storedPassword)) {
                this.userId = resultSet.getInt("userId");
                this.role = resultSet.getString("role");
                isSuccess = true;
            }
        }

        try {
            resultSet.close();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error when closing DB connection!! \n" + e.getMessage());
        }

        return isSuccess;
    }

    public int getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }
}
