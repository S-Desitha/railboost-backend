package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.enums.Roles;
import org.ucsc.railboostbackend.models.Role;
import org.ucsc.railboostbackend.models.User;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepo {

    public User getUserById(int userId) {
        Connection connection = DBConnection.getConnection();
        User user = new User();
        String query = "SELECT * FROM users WHERE userId=?";

        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user.setUserId(resultSet.getInt("userId"));
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
}
