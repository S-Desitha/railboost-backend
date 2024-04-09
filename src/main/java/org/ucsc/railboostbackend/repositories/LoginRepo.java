package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.Login;
import org.ucsc.railboostbackend.models.Role;
import org.ucsc.railboostbackend.utilities.DBConnection;
import org.ucsc.railboostbackend.utilities.Security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginRepo {
    private int userId;
    private int role;
    private String name;

    public Login verifyLogin(Login login) throws SQLException {
        Login response = new Login();
        Security security = new Security();
        String username = login.getUsername();
        String inputPassword = login.getPassword();


        String query = "SELECT username, userId, password, salt, roleId, fName, lName FROM users where username=?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, username);

        ResultSet resultSet = pst.executeQuery();
        if (resultSet.next()) {
            String storedPassword = resultSet.getString("password");
            String salt = resultSet.getString("salt");

            if (security.hash(inputPassword, salt).equals(storedPassword)) {
                this.userId = resultSet.getInt("userId");
                this.role = resultSet.getInt("roleId");
                response.setSuccessful(true);
                response.setUsername(resultSet.getString("username"));
                response.setRole(new Role(resultSet.getInt("roleId")));
                response.setUserID(resultSet.getInt("userId"));
                String fullName = resultSet.getString("fName") + " " + resultSet.getString("lName");
                response.setName(fullName);
            }
            else
                response.setSuccessful(false);
        }

        try {
            resultSet.close();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error when closing DB connection!! \n" + e.getMessage());
        }

        return response;
    }

    public int getUserId() {
        return userId;
    }

    public int getRole() {
        return role;
    }

    public String getName() { return name;
    }
}
