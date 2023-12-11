package org.ucsc.railboostbackend.repositories;


import org.ucsc.railboostbackend.models.Journey;
import org.ucsc.railboostbackend.models.JourneyStation;
import org.ucsc.railboostbackend.models.Schedule;
import org.ucsc.railboostbackend.models.ScheduleStation;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class JourneyRepo {

    public void addJourney(Schedule schedule) {
        LocalDate date = LocalDate.now();
        Connection connection = DBConnection.getConnection();
        String journey_query = "INSERT INTO journey (date, scheduleId) VALUES (?, ?) ";
        String stations_query = "INSERT INTO journey_stations (date, scheduleId, station, stIndex) VALUES (?, ?, ?, ?) ";

        try (PreparedStatement statement = connection.prepareStatement(journey_query)){
            statement.setDate(1, Date.valueOf(date));
            statement.setShort(2, schedule.getScheduleId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error when inserting into journey table : ");
            System.out.println(e.getMessage());
        }

        try (PreparedStatement statement = connection.prepareStatement(stations_query)) {
            for (ScheduleStation station : schedule.getStations()) {
                statement.setDate(1, Date.valueOf(date));
                statement.setShort(2, schedule.getScheduleId());
                statement.setString(3, station.getStation());
                statement.setShort(4, station.getStIndex());
                statement.addBatch();
            }
            statement.executeBatch();

        } catch (SQLException e) {
            System.out.println("Error when inserting to journey_stations table : ");
            System.out.println(e.getMessage());
        }

        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error when closing DB connection : " + e.getMessage());
        }

    }


    public void updateJourney(JourneyStation journeyStation) {
        Connection connection = DBConnection.getConnection();

        LocalTime time;
        StringBuilder builder = new StringBuilder("UPDATE journey_stations SET ");
        if ((time=journeyStation.getArrivalTime())!=null)
            builder.append("arrivalTime = ? ");
        else if ((time=journeyStation.getDepartureTime())!=null)
            builder.append("departureTime = ?");

        builder.append("WHERE date = ? " +
                "AND scheduleId = ? " +
                "AND station = ? ");

        String query = builder.toString();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            assert time != null;
            statement.setTime(1, Time.valueOf(time));
            statement.setDate(2, Date.valueOf(journeyStation.getDate()));
            statement.setShort(3, journeyStation.getScheduleId());
            statement.setString(4, journeyStation.getStation());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error when update arrival/departure time for journey: ");
            System.out.println(e.getMessage());
        }
    }


    public Journey getJourney(LocalDate date, short scheduleId) {
        Connection connection = DBConnection.getConnection();
        Journey journey = new Journey();
        List<JourneyStation> stations = new ArrayList<>();
        String query ="SELECT jour.date, jour.scheduleId, js.station, js.stIndex, js.arrivalTime, js.departureTime " +
                "FROM journey jour " +
                "INNER JOIN journey_stations js ON jour.date = js.date and jour.scheduleId = js.scheduleId " +
                "WHERE jour.date = ? " +
                "AND jour.scheduleId = ? " +
                "ORDER BY  js.stIndex ";
        ResultSet resultSet;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, Date.valueOf(date));
            statement.setShort(2, scheduleId);

            resultSet = statement.executeQuery();
            for (int i=0; resultSet.next(); i++) {
                if (i==0){
                    journey.setDate(resultSet.getDate("date").toLocalDate());
                    journey.setScheduleId(resultSet.getShort("scheduleId"));
                }
                stations.add(new JourneyStation(
                        resultSet.getString("station"),
                        resultSet.getShort("stIndex"),
                        resultSet.getTime("arrivalTime") != null ? resultSet.getTime("arrivalTime").toLocalTime() : null,
                        resultSet.getTime("departureTime") != null ? resultSet.getTime("departureTime").toLocalTime() : null
                ));
            }
            journey.setStations(stations);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return journey;
    }


    public List<Journey> getJourneysByStation(LocalDate date, String stationCode) {
        Connection connection = DBConnection.getConnection();
        List<Journey> journeyList = new ArrayList<>();
        short scheduleId;
        String query = "SELECT ts.scheduleId FROM schedule ts " +
                "INNER JOIN schedule_stations ss ON ts.scheduleId = ss.scheduleId " +
                "WHERE ss.station = ?";
        ResultSet resultSet;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, stationCode);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                scheduleId = resultSet.getShort(1);
                journeyList.add(getJourney(date, scheduleId));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return journeyList;
    }


    private boolean checkExists(JourneyStation journeyStation) {
        // checks whether the relevant field (arrivalTime or departureTime) was updated already.
        // If so, it shouldn't be updated again.

        Connection connection = DBConnection.getConnection();
        String query = "SELECT ? FROM journey_stations " +
                "WHERE date = DATE(date) = ? " +
                "AND scheduleId = ? " +
                "AND station = ? ";

        try (PreparedStatement statement = connection.prepareStatement(query)){
            if (journeyStation.getArrivalTime()!=null)
                statement.setString(1, "arrivalTime");
            else if (journeyStation.getDepartureTime()!=null)
                statement.setString(1, "departureTime");
            statement.setDate(2, Date.valueOf(journeyStation.getDate()));
            statement.setShort(3, journeyStation.getScheduleId());
            statement.setString(4, journeyStation.getStation());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.getTime(1)==null)
                return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
