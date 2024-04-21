package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.enums.Day;
import org.ucsc.railboostbackend.models.Schedule;
import org.ucsc.railboostbackend.models.ScheduleDay;
import org.ucsc.railboostbackend.models.ScheduleStation;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

import static org.quartz.JobBuilder.newJob;

public class ScheduleRepo {

    public Schedule getScheduleById(Short scheduleId) {
        Schedule schedule = new Schedule();
        List<ScheduleStation> stations = new ArrayList<>();
        List<ScheduleDay> days = new ArrayList<>();
        Connection connection = DBConnection.getConnection();
        String query = "SELECT ts.scheduleId, ts.startStation, ts.endStation,ts.trainId, ts.startDate, ts.endDate, ts.speed, train.trainType, ss.station, ss.stIndex, ss.scheduledArrivalTime, ss.scheduledDepartureTime " +
                "FROM schedule ts " +
                "INNER JOIN schedule_stations ss ON ts.scheduleId = ss.scheduleId " +
                "INNER JOIN train ON ts.trainId = train.trainId " +
                "WHERE ts.scheduleId = ? " +
                "ORDER BY ss.stIndex ASC;";

        String days_query = "SELECT days.day, days.scheduleId " +
                "FROM schedule_days days " +
                "WHERE days.scheduleId = ? ";

        PreparedStatement pst = null;
        ResultSet resultSet = null;
        try {
            pst = connection.prepareStatement(query);
            pst.setShort(1, scheduleId);

            resultSet = pst.executeQuery();

            StationRepo stationRepo = new StationRepo();
            for (int i=0; resultSet.next(); i++) {
                if (i==0){
                    schedule.setScheduleId(resultSet.getShort("scheduleId"));
                    schedule.setTrainId(resultSet.getString("trainId"));
                    schedule.setStartStation(resultSet.getString("startStation"));
                    schedule.setStartStationName(stationRepo.getStationName(resultSet.getString("startStation")));
                    schedule.setEndStation(resultSet.getString("endStation"));
                    schedule.setEndStationName(stationRepo.getStationName(resultSet.getString("endStation")));

//                 schedule.setTrainType(resultSet.getString("trainType"));


                    if (resultSet.getDate("startDate")!=null)
                        schedule.setStartDate(resultSet.getDate("startDate").toLocalDate());
                    if (resultSet.getDate("endDate")!=null)
                        schedule.setEndDate(resultSet.getDate("endDate").toLocalDate());
                    schedule.setSpeed(resultSet.getString("speed"));
                }
                stations.add(new ScheduleStation(
                        resultSet.getShort("scheduleId"),
                        resultSet.getString("station"),
                        stationRepo.getStationName(resultSet.getString("station")),
                        resultSet.getShort("stIndex"),
                        resultSet.getTime("scheduledArrivalTime").toLocalTime(),
                        resultSet.getTime("scheduledDepartureTime").toLocalTime()
                    )
                );
            }
            schedule.setStations(stations);

        } catch (SQLException e) {
            System.out.println("Error executing SQL query!!\n" + e.getMessage());
        }

        try {
            pst = connection.prepareStatement(days_query);
            pst.setShort(1, scheduleId);

            resultSet = pst.executeQuery();
            while (resultSet.next()){
                days.add(new ScheduleDay(
                        resultSet.getShort("scheduleId"),
                        Day.valueOf(resultSet.getString("day"))
                ));
            }
            schedule.setDays(days);
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


    public List<Schedule> getSchedules(Schedule reqSchedule){
        List<Schedule> schedules = new ArrayList<>();
        Connection connection = DBConnection.getConnection();

        String startStation = reqSchedule.getStartStation();
        String endStation = reqSchedule.getEndStation();
        LocalDate date = reqSchedule.getDate();

        String query = "SELECT ts.scheduleId FROM schedule ts " +
                (startStation!=null? "INNER JOIN schedule_stations ss1 ON ts.scheduleId = ss1.scheduleId " : "") +
                (endStation!=null? "INNER JOIN schedule_stations ss2 ON ts.scheduleId = ss2.scheduleId " : "") +
//                "INNER JOIN schedule_days days ON ts.scheduleId = days.scheduleId " +
//                "WHERE (days.day = ? OR DATE(dates.date) = ?) " +
                (date!=null? "INNER JOIN schedule_days days ON ts.scheduleId = days.scheduleId WHERE (days.day = ? AND (ts.startDate <= ? AND (ts.endDate > ? OR ts.endDate IS NULL ))) " : "") +
                (startStation!=null? "AND ss1.station = ? " : "") +
                (endStation!=null? "AND ss2.station = ? " : "") +
                (startStation!=null && endStation!=null ? "AND ss1.stIndex < ss2.stIndex " : "") +
                (startStation!=null? "ORDER BY ss1.scheduledArrivalTime ASC" : "");



//        if (date!=null)
//            Day day = Day.valueOf(date.getDayOfWeek().toString());

        PreparedStatement pst = null;
        ResultSet resultSet = null;
        try {
            pst = connection.prepareStatement(query);
            if (date!=null) {
                Day day = Day.valueOf(date.getDayOfWeek().toString());
                pst.setString(1, day.name());
                pst.setDate(2, Date.valueOf(date));
                pst.setDate(3, Date.valueOf(date));
            }
            if (startStation!=null)
                pst.setString(4, startStation);
            if (endStation!=null)
                pst.setString(5, endStation);

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


    public List<Schedule> getScheduleByDay(Day day, LocalDate date) {
        List<Schedule> schedules = new ArrayList<>();
        Connection connection = DBConnection.getConnection();
        String query = "SELECT ts.scheduleId FROM schedule ts " +
                "INNER JOIN schedule_days days ON ts.scheduleId = days.scheduleId " +
                "LEFT JOIN schedule_dates dates ON ts.scheduleId = dates.scheduleId " +
                "WHERE (days.day = ? OR DATE(dates.date) = ?) ";

        PreparedStatement pst = null;
        ResultSet resultSet = null;
        try {
            pst = connection.prepareStatement(query);
            pst.setString(1, day.name());
            pst.setDate(2, Date.valueOf(date));

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


    public boolean addSchedule(Schedule schedule) {
        boolean isSuccess = false;
        short scheduleId = schedule.getScheduleId();
        Connection connection = DBConnection.getConnection();
        String query_schedule = "INSERT INTO schedule (scheduleId, startStation, endStation, startDate, endDate, trainId, speed) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
            pst_schedule.setDate(4, Date.valueOf(schedule.getStartDate()));
            if (schedule.getEndDate()!=null)
                pst_schedule.setDate(5, Date.valueOf(schedule.getEndDate()));
            else
                pst_schedule.setNull(5, Types.DATE);
            pst_schedule.setString(6, schedule.getTrainId());
            pst_schedule.setString(7, schedule.getSpeed());

            for (ScheduleStation station : schedule.getStations()) {
                pst_stations.setShort(1, scheduleId);
                pst_stations.setString(2, station.getStation());
                pst_stations.setShort(3, station.getStIndex());
                pst_stations.setTime(4, Time.valueOf(station.getScheduledArrivalTime()));

                if (station.getScheduledDepartureTime() != null)
                    pst_stations.setTime(5, Time.valueOf(station.getScheduledDepartureTime()));
                else
                    pst_stations.setNull(5, Types.TIME);

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


    public void updateSchedule(Schedule original, Schedule updated) throws IntrospectionException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        boolean isSuccess = false;
        Connection connection = DBConnection.getConnection();
        StringBuilder sch_queryBuilder = new StringBuilder("UPDATE schedule SET ");

        for (PropertyDescriptor descriptor : Introspector.getBeanInfo(original.getClass()).getPropertyDescriptors()) {
//            System.out.println(descriptor.getName() +" : " + descriptor.getPropertyType());
            if(descriptor.getName().equals("scheduleId") || descriptor.getName().equals("class"))
                continue;
            if (descriptor.getPropertyType().equals(Class.forName("java.util.List"))) {
//                System.out.println("List found");
                if (descriptor.getName().equals("stations"))
                    new ScheduleStationRepo().compAndUpdate(original.getStations(), updated.getStations());
                else if (descriptor.getName().equals("days"))
                    new ScheduleDaysRepo().compAndUpdate(original.getDays(), updated.getDays());
            }
            else if ((descriptor.getReadMethod().invoke(updated)!=null && descriptor.getReadMethod().invoke(original)==null) || (descriptor.getReadMethod().invoke(original)!=null && !descriptor.getReadMethod().invoke(original).equals(descriptor.getReadMethod().invoke(updated)))) {
                sch_queryBuilder
                        .append(descriptor.getName())
                        .append("=\"")
                        .append(descriptor.getReadMethod().invoke(updated))
                        .append("\"")
                        .append(",");
            }
        }

        sch_queryBuilder.delete(sch_queryBuilder.length()-1, sch_queryBuilder.length());
        sch_queryBuilder.append(" WHERE scheduleId=").append(original.getScheduleId());
        String sch_query = sch_queryBuilder.toString();

        System.out.println(sch_query);

        try (PreparedStatement statement = connection.prepareStatement(sch_query)) {
            int res = statement.executeUpdate();
            System.out.println(res);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void deleteSchedule(Schedule schedule) {
        Connection connection = DBConnection.getConnection();
        short id = schedule.getScheduleId();
        String sch_query = "DELETE FROM schedule WHERE scheduleId=?";

        ScheduleStationRepo scheduleStationRepo = new ScheduleStationRepo();
        scheduleStationRepo.deleteSchStation(schedule.getStations());

        ScheduleDaysRepo scheduleDaysRepo = new ScheduleDaysRepo();
        scheduleDaysRepo.deleteSchDays(schedule.getDays());

        try (PreparedStatement statement = connection.prepareStatement(sch_query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query for schedule delete!!\n" + e.getMessage());
        }
    }
}
