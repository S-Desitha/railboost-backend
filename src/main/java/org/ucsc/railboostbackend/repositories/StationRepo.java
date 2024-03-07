package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.Station;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StationRepo {

    public List<Station> getStationNames() {
        List<Station> stationNames = new ArrayList<>();
        Connection connection = DBConnection.getConnection();
        String query = "SELECT stationCode, name FROM station";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                stationNames.add(new Station(resultSet.getString(1), resultSet.getString(2)));
            }
        } catch (SQLException e) {
            System.out.println("Error when executing select query for stations table: StationRepo.java");
            System.out.println(e.getMessage());
        }

        return stationNames;
    }

    public String getStationName(String stationCode) {
        String stationName = null;
        Connection connection = DBConnection.getConnection();
        String query = "SELECT name from station WHERE stationCode = ? ";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, stationCode);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                stationName = resultSet.getString(1);

        } catch (SQLException e) {
            System.out.println("Error when executing select query for stations table: StationRepo.java : getStationNames()");
            System.out.println(e.getMessage());
        }

        return stationName;
    }
}
