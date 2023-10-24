package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.enums.Days;
import org.ucsc.railboostbackend.models.Schedule;
import org.ucsc.railboostbackend.models.ScheduleDay;
import org.ucsc.railboostbackend.models.ScheduleStation;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ScheduleRepo {

    public Schedule getScheduleById(Short scheduleId) {
        Schedule schedule = new Schedule();
        List<ScheduleStation> stations = new ArrayList<>();
        Connection connection = DBConnection.getConnection();
        String query = "SELECT ts.scheduleId, ts.startStation, ts.endStation,ts.trainId, train.trainType, ss.station, ss.stIndex, ss.scheduledArrivalTime, ss.scheduledDepartureTime " +
                "FROM schedule ts " +
                "INNER JOIN schedule_stations ss ON ts.scheduleId = ss.scheduleId " +
                "INNER JOIN train ON ts.trainId = train.trainId " +
                "WHERE ts.scheduleId = ? " +
                "ORDER BY ss.stIndex ASC;";

        PreparedStatement pst = null;
        ResultSet resultSet = null;
        try {
            pst = connection.prepareStatement(query);
            pst.setShort(1, scheduleId);

            resultSet = pst.executeQuery();
            for (int i=0; resultSet.next(); i++) {
                if (i==0){
                    schedule.setScheduleId(resultSet.getShort("scheduleId"));
                    schedule.setTrainId(resultSet.getString("trainId"));
                    schedule.setStartStation(resultSet.getString("startStation"));
                    schedule.setEndStation(resultSet.getString("endStation"));
                    schedule.setTrainType(resultSet.getString("trainType"));
                }
                stations.add(new ScheduleStation(
                        resultSet.getShort("scheduleId"),
                        resultSet.getString("station"),
                        resultSet.getShort("stIndex"),
                        resultSet.getTime("scheduledArrivalTime"),
                        resultSet.getTime("scheduledDepartureTime")
                    )
                );
            }
            schedule.setStations(stations);

        } catch (SQLException e) {
            System.out.println("Error executing SQL query!!\n" + e.getMessage());
        }

        try {
            if (resultSet != null)
                resultSet.close();
            if (pst != null)
                pst.close();
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


    public List<Schedule> getSchedules(Days day, String startStation, String endStation){
        List<Schedule> schedules = new ArrayList<>();
        Connection connection = DBConnection.getConnection();
        String query = "SELECT ts.scheduleId FROM schedule ts " +
                "INNER JOIN schedule_stations ss1 ON ts.scheduleId = ss1.scheduleId " +
                "INNER JOIN schedule_stations ss2 ON ts.scheduleId = ss2.scheduleId " +
                "INNER JOIN schedule_days days ON ts.scheduleId = days.scheduleId " +
                "WHERE days.day = ? " +
                "AND ss1.station = ? " +
                "AND ss2.station = ? " +
                "AND ss1.stIndex < ss2.stIndex " +
                "ORDER BY ss1.scheduledArrivalTime ASC";

        PreparedStatement pst = null;
        ResultSet resultSet = null;
        try {
            pst = connection.prepareStatement(query);
            pst.setString(1, day.name());
            pst.setString(2, startStation);
            pst.setString(3, endStation);

            resultSet = pst.executeQuery();
            while (resultSet.next()) {
                schedules.add(getScheduleById(resultSet.getShort("scheduleId")));
            }

        } catch (SQLException e) {
            System.out.println("Error when executing the sql query for retrieving schedules!!\n"+e.getMessage());
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

        return schedules;
    }


//    public boolean updateSchedule(String scheduleId){}

//    public boolean removeSchedule(String scheduleId) {}
}
