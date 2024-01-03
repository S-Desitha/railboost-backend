package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.Station;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
