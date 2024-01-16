package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.Staff;
import org.ucsc.railboostbackend.models.StaffSignup;
import org.ucsc.railboostbackend.models.User;
import org.ucsc.railboostbackend.utilities.DBConnection;
import org.ucsc.railboostbackend.utilities.Security;

import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.Map;

public class SignUpRepo {

    public boolean addGeneralUser(User user){
        boolean isSuccess = false;
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO users (username, password, salt, fName, lName, dob, gender, email, telNo, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String username = user.getUsername();
        String password = user.getPassword();

        try {
            Map<String, String> hashResult = new Security().hash(password);

            String hash = hashResult.get("hash");
            String salt = hashResult.get("salt");

            System.out.println("Hashed Password: "+ hash);
            System.out.println("Salt: " + salt);

            try (PreparedStatement statement = connection.prepareStatement(query)){
                statement.setString(1, username);
                statement.setString(2, hash);
                statement.setString(3, salt);
                statement.setString(4, user.getfName());
                statement.setString(5, user.getlName());
                if (user.getDob()!=null)
                    statement.setDate(6, Date.valueOf(user.getDob()));
                else
                    statement.setNull(6, Types.DATE);
                statement.setString(7, user.getGender());
                statement.setString(8, user.getEmail());
                statement.setString(9, user.getTelNo());
                statement.setString(10, user.getRole());

                isSuccess = statement.executeUpdate() > 0;

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (InvalidKeySpecException e) {
            System.out.println("Error in hash function.\n"+e.getMessage());
        }

        return isSuccess;
    }



    public int addStaffUser(User user) {
        int userId = -1;
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO users (fName, lName, dob, gender, email, telNo, role, username) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)){
            statement.setString(1, user.getfName());
            statement.setString(2, user.getlName());
            if (user.getDob()!=null)
                statement.setDate(3, Date.valueOf(user.getDob()));
            else
                statement.setNull(3, Types.DATE);
            statement.setString(4, user.getGender());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getTelNo());
            statement.setString(7, user.getRole());
            statement.setString(8, user.getUsername());

            if (statement.executeUpdate()>0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next())
                    userId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error occurred when inserting user info of staff member: "+e.getMessage());
        }

        return userId;
    }


    public boolean finishStaffSignup(String password, String staffId) {
        boolean isSuccess = false;
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE users " +
                "INNER JOIN staff ON users.userId = staff.userId " +
                "SET password = ? , salt = ? " +
                "WHERE staffId = ? ";


        try {
            Map<String, String> hashResult = new Security().hash(password);

            String hash = hashResult.get("hash");
            String salt = hashResult.get("salt");

            try (PreparedStatement statement = connection.prepareStatement(query)){
                statement.setString(1, hash);
                statement.setString(2, salt);
                statement.setString(3, staffId);

                isSuccess = statement.executeUpdate() > 0;

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (InvalidKeySpecException e) {
            System.out.println("Error in hash function.\n"+e.getMessage());
        }

        return isSuccess;
    }
}
