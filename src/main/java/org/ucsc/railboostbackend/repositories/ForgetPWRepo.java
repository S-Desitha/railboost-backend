package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.Staff;
import org.ucsc.railboostbackend.models.User;
import org.ucsc.railboostbackend.utilities.DBConnection;
import org.ucsc.railboostbackend.utilities.Security;

import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.Map;

public class ForgetPWRepo {

    public boolean checkUserName(User user) {
        boolean isSuccess = false;
        Connection connection = DBConnection.getConnection();

        try{
            String query = "SELECT COUNT(*) AS count FROM users WHERE username = ?";
            try(PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, user.getUsername());

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    if (count > 0) {
                        isSuccess = true;
                    }
                }
            }
        }catch (SQLException e){
            System.out.println("Error occurred during checkiing username : " + e.getMessage());
        }

        return isSuccess;
    }

    public String getMail(User user) {
        String mail = null;
        Connection  connection = DBConnection.getConnection();

        String query = "SELECT email FROM users WHERE username=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, user.getUsername());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                mail = resultSet.getString("email");

            }
        } catch (SQLException e){
            System.out.println("Error in select query for users table: \n" + e.getMessage());
        }
        return mail;
    }

    public boolean createNewPW(User user, String username) {
        Connection connection = DBConnection.getConnection();
        boolean isSuccess = false;

        try {
            String password = user.getPassword();
            Map<String, String> hashResult = new Security().hash(password);
            String hash = hashResult.get("hash");
            String salt = hashResult.get("salt");

            String query = "UPDATE users SET password = ?, salt = ? WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, hash);
                statement.setString(2, salt);
                statement.setString(3, username);
                int rowsAffected = statement.executeUpdate();
                isSuccess = (rowsAffected > 0);
            }
        } catch (SQLException e) {
            System.out.println("Error occurred during changing password: " + e.getMessage());
        } catch (InvalidKeySpecException e) {
            System.out.println("Error in hash function.\n" + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing database connection: " + e.getMessage());
            }
        }

        return isSuccess;
    }

}
