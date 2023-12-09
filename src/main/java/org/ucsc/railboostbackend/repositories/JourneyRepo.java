package org.ucsc.railboostbackend.repositories;


import org.ucsc.railboostbackend.models.Schedule;
import org.ucsc.railboostbackend.models.ScheduleStation;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.*;
import java.time.LocalDate;

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
}
