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
        String query = "SELECT stationCode, name, address, line, nextStation ,prevStation, contactNo  FROM station";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Station station= new Station();
                station.setStationCode(resultSet.getString("stationCode"));
                station.setStationName(resultSet.getString("name"));
                station.setAddress(resultSet.getString("address"));
                station.setLine(resultSet.getString("line"));
                station.setNextStation(resultSet.getString("nextStation"));
                station.setPrevStation(resultSet.getString("prevStation"));
                station.setContactNo(resultSet.getString("contactNo"));
                stationNames.add(station);
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

    public void addStation(Station station) {
        Connection connection = null;
        PreparedStatement insertStatement = null;
        PreparedStatement updateStatement1 = null;
        PreparedStatement updateStatement2 = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false); // Start a transaction

            // Insert query
            String insertQuery = "INSERT INTO station (stationCode, name, address, line, prevStation, nextStation, contactNo) VALUES (?, ?, ?, ?, ?, ?, ?)";
            insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, station.getStationCode());
            insertStatement.setString(2, station.getStationName());
            insertStatement.setString(3, station.getAddress());
            insertStatement.setString(4, station.getLine());
            insertStatement.setString(5, station.getPrevStation());
            insertStatement.setString(6, station.getNextStation());
            insertStatement.setString(7, station.getContactNo());
            insertStatement.executeUpdate();

            // Update query 1
            String updateQuery1 = "UPDATE station SET nextStation = ? WHERE stationCode = ?";
            updateStatement1 = connection.prepareStatement(updateQuery1);
            updateStatement1.setString(1, station.getStationCode());
            updateStatement1.setString(2, station.getPrevStation());
            updateStatement1.executeUpdate();

            // Update query 2
            String updateQuery2 = "UPDATE station SET prevStation = ? WHERE stationCode = ?";
            updateStatement2 = connection.prepareStatement(updateQuery2);
            updateStatement2.setString(1, station.getStationCode());
            updateStatement2.setString(2, station.getNextStation());
            updateStatement2.executeUpdate();

            connection.commit(); // Commit the transaction
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback(); // Rollback the transaction in case of an exception
                }
            } catch (SQLException ex) {
                System.out.println("Error rolling back transaction: " + ex.getMessage());
            }
            System.out.println("Error occurred when adding station: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (insertStatement != null) {
                    insertStatement.close();
                }
                if (updateStatement1 != null) {
                    updateStatement1.close();
                }
                if (updateStatement2 != null) {
                    updateStatement2.close();
                }
                if (connection != null) {
                    connection.setAutoCommit(true); // Reset auto-commit mode
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    public void updateStation(Station station) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBConnection.getConnection();
            String query = "UPDATE station SET contactNo=? WHERE stationCode=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, station.getContactNo());
            statement.setString(2, station.getStationCode());

            statement.executeUpdate();
        } finally {
            // Close resources
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void deleteStation(Station station) {
        Connection connection = null;
        PreparedStatement updatestatement1 = null;
        PreparedStatement updatestatement2 = null;
        PreparedStatement deletestatement = null;


        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false); // Start transaction



            // Update the previous station's next station
            String updatePrevNextQuery1 = "UPDATE station SET prevStation=? WHERE stationCode=?";
            updatestatement1 = connection.prepareStatement(updatePrevNextQuery1);
            updatestatement1.setString(1, station.getPrevStation());
            updatestatement1.setString(2, station.getNextStation());
            updatestatement1.executeUpdate();


            // Update the next station's previous station
            String updateNextPrevQuery2 = "UPDATE station SET nextStation=? WHERE stationCode=?";
            updatestatement2 = connection.prepareStatement(updateNextPrevQuery2);
            updatestatement2.setString(1, station.getNextStation());
            updatestatement2.setString(2, station.getPrevStation());
            updatestatement2.executeUpdate();


            String deleteQuery = "DELETE FROM station WHERE stationCode=?";
            deletestatement = connection.prepareStatement(deleteQuery);
            deletestatement.setString(1, station.getStationCode());
            deletestatement.executeUpdate();


            connection.commit(); // Commit transaction
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback(); // Rollback transaction on error
                }
            } catch (SQLException ex) {
                System.out.println("Error rolling back transaction: " + ex.getMessage());
            }
            System.out.println("Error occurred while executing delete query for station table: StationRepo.java");
            System.out.println(e.getMessage());
        } finally {
            // Close resources
            try {
                if (deletestatement != null) {
                    deletestatement.close();
                }
                if (updatestatement1 != null) {
                    updatestatement1.close();
                }
                if (updatestatement2 != null) {
                    updatestatement2.close();
                }
                if (connection != null) {
                    connection.setAutoCommit(true); // Reset auto-commit mode
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

}
