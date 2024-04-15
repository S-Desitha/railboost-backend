package org.ucsc.railboostbackend.repositories;


import org.ucsc.railboostbackend.enums.Roles;
import org.ucsc.railboostbackend.models.Role;
import org.ucsc.railboostbackend.models.User;
import org.ucsc.railboostbackend.utilities.DBConnection;
import org.ucsc.railboostbackend.utilities.Security;

import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.Map;


public class ProfileRepo {

    public void deleteAcc(User user) {
        Connection connection = DBConnection.getConnection();
        String query = "DELETE FROM users WHERE userId=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error occurred while executing delete query for users table");
        }

    }

    public  void ChangePassword(User user) {
        Connection connection = DBConnection.getConnection();

        try{
            int userId = user.getUserId();
            String password = user.getPassword();

            Map<String, String> hashResult = new Security().hash(password);

            String hash = hashResult.get("hash");
            String salt = hashResult.get("salt");

            String query = "UPDATE users SET password = ?, salt = ? WHERE userId = ?";
            try(PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, hash);
                statement.setString(2, salt);
                statement.setInt(3, userId);

                statement.executeUpdate();
            }

        }catch (SQLException e){
            System.out.println("Error occurred during changing password: " + e.getMessage());
        } catch (InvalidKeySpecException e) {
            System.out.println("Error in hash function.\n"+e.getMessage());
        }

    }

    public User getUserById(int userId) {
        Connection connection = DBConnection.getConnection();
        User user = new User();
        String query = "SELECT " +
                "u.username, " +
                "u.userId,"+
                "s1.name AS homeStationName, " +
                "u.homeStation, " +
                "u.roleId, " +
                "u.fName, " +
                "u.lName, " +
                "u.telNo, " +
                "u.email, " +
                "u.dob, " +
                "u.gender, " +
                "u.dp " +
                "FROM " +
                "users u " +
                "JOIN " +
                "station s1 ON u.homeStation COLLATE utf8mb4_unicode_ci = s1.stationCode COLLATE utf8mb4_unicode_ci " +
                "WHERE userId=?";

        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user.setUserId(resultSet.getInt("userId"));
                user.setUsername(resultSet.getString("username"));
                user.setHomeStation(resultSet.getString("homeStationName"));
                user.setHomeStCode(resultSet.getString("homeStation"));
                user.setDp(resultSet.getString("dp"));
                Roles role = Roles.valueOfRoleId(resultSet.getShort("roleId"));
                user.setRole(new Role(role.getRoleId(), role.toString()));
                user.setfName(resultSet.getString("fName"));
                user.setlName(resultSet.getString("lName"));
                user.setTelNo(resultSet.getString("telNo"));
                user.setEmail(resultSet.getString("email"));
                if (resultSet.getDate("dob")!=null)
                    user.setDob(resultSet.getDate("dob").toLocalDate());
                user.setGender(resultSet.getString("gender"));
            }

        } catch (SQLException e) {
            System.out.println("UserRepo : getUserById() : Error when getting user from DB.");
            System.out.println(e.getMessage());
        }

        return user;
    }

    public void UpdateDp(Object id, String filename){
        Connection connection = DBConnection.getConnection();

        try{
            String query = "UPDATE users SET dp = ? WHERE userId = ?";
            try(PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1,filename);
                statement.setObject(2, id);

                statement.executeUpdate();

            }

        }catch (SQLException e){
            System.out.println("Error occurred during updating profile picture : " + e.getMessage());
        }
    }

    public void updateProfile(User user){
        Connection connection = DBConnection.getConnection();

        try{
            String query = "UPDATE users SET fName = ?, lName = ?, homeStation = ? , email = ?, gender = ?, telNo = ? WHERE userId = ?";
            try(PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, user.getfName());
                statement.setString(2, user.getlName());
                statement.setString(3, user.getHomeStCode());
                statement.setString(4, user.getEmail());
                statement.setString(5, user.getGender());
                statement.setString(6, user.getTelNo());
                statement.setInt(7, user.getUserId());

                statement.executeUpdate();
            }

        }catch (SQLException e){
            System.out.println("Error occurred during updating profile: " + e.getMessage());
        }
    }
}
