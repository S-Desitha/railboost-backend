package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.enums.Days;
import org.ucsc.railboostbackend.models.ScheduleDay;
import org.ucsc.railboostbackend.models.ScheduleStation;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDaysRepo {

    public void compAndUpdate(List<ScheduleDay> original, List<ScheduleDay> updated) {
        List<ScheduleDay> deleted = new ArrayList<>(original);
        updated.forEach(x -> {
            deleted.removeIf(y -> y.getDay().equals(x.getDay()) && y.getScheduleId().equals(x.getScheduleId()));
        });
        deleteSchDays(deleted);

        List<ScheduleDay> added = new ArrayList<>(updated);
        original.forEach(x -> {
            added.removeIf(y -> y.getDay().equals(x.getDay()) && y.getScheduleId().equals(x.getScheduleId()));
        });
        addSchDays(added);

    }


    public void deleteSchDays(List<ScheduleDay> scheduleDays) {
        Connection connection = DBConnection.getConnection();
        String query = "DELETE FROM schedule_days WHERE scheduleId=? AND day=?";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(query);
            for (ScheduleDay scheduleDay : scheduleDays) {
                statement.setShort(1, scheduleDay.getScheduleId());
                statement.setString(2, scheduleDay.getDay().toString());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query for schedule_days delete!!\n" + e.getMessage());
        }
        if (statement!=null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.out.println("Error when closing DB connection!! \n" + e.getMessage());
            }
        }
    }


    public void addSchDays(List<ScheduleDay> scheduleDays) {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO schedule_days (scheduleId, day) VALUES(?, ?) ";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(query);
            for (ScheduleDay scheduleDay : scheduleDays) {
                statement.setShort(1, scheduleDay.getScheduleId());
                statement.setString(2, scheduleDay.getDay().toString());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query for schedule_days insert!!\n" + e.getMessage());
        }
        if (statement!=null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.out.println("Error when closing DB connection!! \n" + e.getMessage());
            }
        }
    }
}
