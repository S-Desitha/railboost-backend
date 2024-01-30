package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.Role;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleRepo {

    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM roles";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                roles.add(new Role(
                        resultSet.getShort("roleId"),
                        resultSet.getString("role")
                ));
            }
        } catch (SQLException e) {
            System.out.println("SQL error in getAllRoles() : RoleRepo.java");
            System.out.println(e.getMessage());
        }

        return roles;
    }
}
