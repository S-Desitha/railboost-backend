package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.Schedule;
import org.ucsc.railboostbackend.models.ScheduleDay;
import org.ucsc.railboostbackend.models.ScheduleStation;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.*;
import java.util.Arrays;
import java.util.stream.IntStream;

public class ScheduleRepo {


    public Schedule getScheduleById(int scheduleId) {
        Schedule schedule = new Schedule();

        String query = "SELECT * FROM schedule WHERE scheduleId=?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement pst = null;
        ResultSet resultSet = null;
        try {
            pst = connection.prepareStatement(query);
            pst.setInt(1, scheduleId);

            resultSet = pst.executeQuery();
            if (resultSet.next()) {
                schedule.setScheduleId(resultSet.getShort("scheduleId"));
                schedule.setTrainId(resultSet.getString("trainId"));
                schedule.setStartStation(resultSet.getString("startStation"));
                schedule.setEndStation(resultSet.getString("endStation"));
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query!!\n" + e.getMessage());
        }

        try {
            if (resultSet != null)
                resultSet.close();
            if (pst != null)
                pst.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error when closing DB connection!! \n" + e.getMessage());
        }

        return schedule;
    }


    public boolean addSchedule(Schedule schedule) {
        boolean isSuccess = false;
        short scheduleId = schedule.getScheduleId();
        Connection connection = DBConnection.getConnection();
        String query_schedule = "INSERT INTO schedule (scheduleId, startStation, endStation, trainId) VALUES (?, ?, ?, ?)";
        String query_stations = "INSERT INTO schedule_stations (scheduleId, station, stIndex, scheduledArrivalTime, scheduledDepartureTime) VALUES (?, ?, ?, ?, ?)";
        String query_days = "INSERT INTO schedule_days (scheduleId, day) VALUES (?, ?)";

        PreparedStatement pst_schedule = null;
        PreparedStatement pst_stations = null;
        PreparedStatement pst_days = null;
        try {
            pst_schedule = connection.prepareStatement(query_schedule);
            pst_stations = connection.prepareStatement(query_stations);
            pst_days = connection.prepareStatement(query_days);

            pst_schedule.setShort(1, schedule.getScheduleId());
            pst_schedule.setString(2, schedule.getStartStation());
            pst_schedule.setString(3, schedule.getEndStation());
            pst_schedule.setString(4, schedule.getTrainId());

            for (ScheduleStation station : schedule.getStations()) {
                pst_stations.setShort(1, scheduleId);
                pst_stations.setString(2, station.getStation());
                pst_stations.setShort(3, station.getStIndex());
                pst_stations.setTime(4, new Time(station.getScheduledArrivalTime().getTime()));

                if (station.getScheduledDepartureTime() != null)
                    pst_stations.setTime(5, new Time(station.getScheduledDepartureTime().getTime()));

                pst_stations.addBatch();
            }

            for (ScheduleDay day : schedule.getDays()) {
                pst_days.setShort(1, scheduleId);
                pst_days.setString(2, day.getDay().toString());

                pst_days.addBatch();
            }

            int res_schedule = pst_schedule.executeUpdate();
            int[] res_stations = pst_stations.executeBatch();
            int[] res_days = pst_days.executeBatch();

            isSuccess = IntStream.concat(Arrays.stream(res_stations), Arrays.stream(res_days)).allMatch(i -> i == 1) && res_schedule == 1;
        }
        catch (SQLException e) {
            System.out.println("Error executing SQL query for schedule insert!!\n" + e.getMessage());
        }

        try {
            if (pst_schedule != null)
                pst_schedule.close();
            if (pst_stations != null)
                pst_stations.close();
            if (pst_days != null)
                pst_days.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error when closing DB connection!! \n" + e.getMessage());
        }

        return isSuccess;
    }


//    public boolean updateSchedule(String scheduleId){}
//
//
//    public List<Schedule> getSchedules(String date, String startStation, String endStation){}
//
//
//    public boolean removeSchedule(String scheduleId) {}
}
