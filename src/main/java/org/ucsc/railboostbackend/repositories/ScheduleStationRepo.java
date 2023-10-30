package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.ScheduleStation;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleStationRepo {
    public void compAndUpdate(List<ScheduleStation> original, List<ScheduleStation> updated) {
        boolean isSuccess = false;

        List<ScheduleStation> deleted = new ArrayList<>(original);
        updated.forEach(x -> {
            deleted.removeIf(y -> y.getStation().equals(x.getStation()));
        });
        deleteSchStation(deleted);

        List<ScheduleStation> added = new ArrayList<>(updated);
        original.forEach(x -> {
            added.removeIf(y -> y.getStation().equals(x.getStation()));
        });
        addSchStation(added);

        List<ScheduleStation> changed = new ArrayList<>(updated);
        added.forEach(x -> {
            changed.removeIf(y -> y.getStation().equals(x.getStation()));
        });
        original.forEach(x -> {
            changed.removeIf(y -> y.equals(x));
        });
        updateSchStation(changed);
    }


    private void addSchStation(List<ScheduleStation> scheduleStations) {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO schedule_stations (scheduleId, station, stIndex, scheduledArrivalTime, scheduledDepartureTime) VALUES(?, ?, ?, ?, ?)";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(query);
            for (ScheduleStation scheduleStation : scheduleStations){
                statement.setShort(1, scheduleStation.getScheduleId());
                statement.setString(2, scheduleStation.getStation());
                statement.setShort(3, scheduleStation.getStIndex());
                statement.setTime(4, new Time(scheduleStation.getScheduledArrivalTime().getTime()));
                if (scheduleStation.getScheduledDepartureTime() != null) {
                    statement.setTime(5, new Time(scheduleStation.getScheduledDepartureTime().getTime()));
                }
                else
                    statement.setNull(5, Types.TIME);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query for schedule_station insert!!\n" + e.getMessage());
        }
        if (statement!=null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.out.println("Error when closing DB connection!! \n" + e.getMessage());
            }
        }
    }


    public void deleteSchStation(List<ScheduleStation> scheduleStations) {
        Connection connection = DBConnection.getConnection();
        String query = "DELETE FROM schedule_stations WHERE scheduleId=? AND station=?";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(query);
            for (ScheduleStation scheduleStation : scheduleStations){
                statement.setShort(1, scheduleStation.getScheduleId());
                statement.setString(2, scheduleStation.getStation());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query for schedule_station delete!!\n" + e.getMessage());
        }
        if (statement!=null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.out.println("Error when closing DB connection!! \n" + e.getMessage());
            }
        }
    }


    private void updateSchStation(List<ScheduleStation> scheduleStations) {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE schedule_stations SET stIndex=?, scheduledArrivalTime=?, scheduledDepartureTime=? WHERE station=? AND scheduleId=?";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(query);
            for (ScheduleStation scheduleStation : scheduleStations){
                statement.setShort(1, scheduleStation.getStIndex());
                statement.setTime(2, new Time(scheduleStation.getScheduledArrivalTime().getTime()));
                statement.setTime(3, new Time(scheduleStation.getScheduledDepartureTime().getTime()));
                statement.setString(4, scheduleStation.getStation());
                statement.setShort(5, scheduleStation.getScheduleId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query for schedule_station update!!\n" + e.getMessage());
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
