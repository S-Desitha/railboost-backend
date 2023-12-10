package org.ucsc.railboostbackend.repositories;


import org.ucsc.railboostbackend.models.JourneyStation;
import org.ucsc.railboostbackend.models.Schedule;
import org.ucsc.railboostbackend.models.ScheduleStation;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

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
