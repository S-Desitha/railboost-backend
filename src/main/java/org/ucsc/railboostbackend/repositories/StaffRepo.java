package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.Role;
import org.ucsc.railboostbackend.models.Staff;
import org.ucsc.railboostbackend.models.User;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StaffRepo {

    public Staff getStaffById(String staffId) {
        Connection connection = DBConnection.getConnection();
        Staff staff = new Staff();
        String query = "SELECT s.staffId, u.userId, u.fName, u.lName, u.username, u.telNo, u.roleId, s.stationCode, u.email, u.dob, u.gender, r.role " +
                "FROM staff s " +
                "INNER JOIN users u ON s.userId = u.userId " +
                "INNER JOIN roles r ON u.roleId = r.roleId " +
                "WHERE s.staffId = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, staffId);
            ResultSet resultSet = statement.executeQuery();

            User user = staff.getUser();
            if (resultSet.next()) {
                staff.setStaffId(resultSet.getString("staffId"));
                staff.setStation(resultSet.getString("stationCode"));
                staff.setUserId(resultSet.getInt("userId"));
                user.setUsername(resultSet.getString("username"));
                user.setfName(resultSet.getString("fName"));
                user.setlName(resultSet.getString("lName"));
                user.setEmail(resultSet.getString("email"));
                user.setTelNo(resultSet.getString("telNo"));
                user.setRole(new Role(resultSet.getShort("roleId"), resultSet.getString("role")));
                if (resultSet.getDate("dob")!=null)
                    user.setDob(resultSet.getDate("dob").toLocalDate());
                user.setGender(resultSet.getString("gender"));
                staff.setUser(user);
            }

        } catch (SQLException e) {
            System.out.println("Error occurred when executing select query to get staff member details:\n"+e.getMessage());
        }

        return staff;
    }


    public Staff getStaffByUserId(int userId) {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT staffId FROM staff " +
                "INNER JOIN users ON staff.userId = users.userId " +
                "WHERE users.userId = ? ";

        ResultSet resultSet;
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, userId);
            resultSet = statement.executeQuery();
            if (resultSet.next())
                return getStaffById(resultSet.getString(1));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    public boolean addStaffMember(Staff staff) {
        boolean isSuccess = false;
        User user = staff.getUser();
        Connection connection = DBConnection.getConnection();
        String staff_query = "INSERT INTO staff (staffId, userId, stationCode) VALUES (?, ?, ?) ";
//        String user_query = "INSERT INTO users (role, firstName, lastName, dob, gender, email, telNo) VALUES (?, ?, ?, ?, ?, ?, ?) ";

        try (PreparedStatement statement = connection.prepareStatement(staff_query)){
            statement.setString(1, staff.getStaffId());
            statement.setInt(2, staff.getUserId());
            statement.setString(3, staff.getStation());

            isSuccess = statement.executeUpdate()>0;
        } catch (SQLException e) {
            System.out.println("Error in executing sql query for inserting staff: "+e.getMessage());
        }

        return isSuccess;
    }


    public List<Staff> getAllStaff() {
        Connection connection = DBConnection.getConnection();
        List<Staff> staffList = new ArrayList<>();
        String query = "SELECT s.staffId, u.userId, u.roleId, u.fName, u.lName, u.username, u.telNo, u.roleId, r.role ,s.stationCode, u.email, s1.name AS stationName " +
                "FROM staff s " +
                "INNER JOIN users u ON s.userId = u.userId " +
                "INNER JOIN station s1 ON s.stationCode = s1.stationCode " +
                "INNER JOIN roles r ON u.roleId = r.roleId ";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Staff staff = new Staff();
                User user = staff.getUser();
                staff.setStaffId(resultSet.getString("staffId"));
                staff.setStation(resultSet.getString("stationCode"));
                staff.setUserId(resultSet.getInt("userId"));
                staff.setStationName(resultSet.getString("stationName"));
                user.setUserId(resultSet.getInt("userId"));
                user.setUsername(resultSet.getString("username"));
                user.setfName(resultSet.getString("fName"));
                user.setlName(resultSet.getString("lName"));
                user.setEmail(resultSet.getString("email"));
                user.setTelNo(resultSet.getString("telNo"));
                user.setRole(new Role(resultSet.getShort("roleId"), resultSet.getString("role")));

                staff.setUser(user);
                staffList.add(staff);
            }
        } catch (SQLException e) {
            System.out.println("Error occurred when executing select query to get staff member details:\n"+e.getMessage());
        }

        return staffList;
    }


    public void updateStaff(Staff staff) {
        Connection connection = DBConnection.getConnection();
        User user = staff.getUser();
        String query = "UPDATE staff SET stationCode=? WHERE staffId=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, staff.getStation());
            statement.setString(2, staff.getStaffId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteMember(Staff staff) {
        Connection connection = DBConnection.getConnection();
        String query = "DELETE FROM staff WHERE staffId=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, staff.getStaffId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error occurred while executing delete query for staff table");
        }

        query = "DELETE FROM users WHERE userId=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setShort(1, (short) staff.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error occurred while executing delete query for staff table");
        }
    }


    public void deleteMember(String staffId) {
        Staff staff = getStaffById(staffId);
        deleteMember(staff);
    }
}
