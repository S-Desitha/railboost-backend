package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.TicketPrice;
import org.ucsc.railboostbackend.models.Train;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RatesRepo {

    public List<TicketPrice> getAllRates(){
        Connection connection= DBConnection.getConnection();
        List<TicketPrice> ratesList = new ArrayList<>();
        String query = "SELECT " +
                "s1.name AS startStationName, " +
                "tp.startCode, " +
                "s2.name AS endStationName, " +
                "tp.endCode, " +
                "tp.`1st Class`, " +
                "tp.`2nd Class`, " +
                "tp.`3rd Class` " +
                "FROM " +
                "ticketprice tp " +
                "JOIN " +
                "station s1 ON tp.startCode COLLATE utf8mb4_unicode_ci = s1.stationCode COLLATE utf8mb4_unicode_ci " +
                "JOIN " +
                "station s2 ON tp.endCode COLLATE utf8mb4_unicode_ci = s2.stationCode COLLATE utf8mb4_unicode_ci";



        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                TicketPrice rate = new TicketPrice();
                rate.setStartStation(resultSet.getString("startStationName"));
                rate.setEndStation(resultSet.getString("endStationName"));
                rate.setFirstClass(Double.parseDouble(resultSet.getString("1st Class")));
                rate.setSecondClass(Double.parseDouble(resultSet.getString("2nd Class")));
                rate.setThirdClass(Double.parseDouble(resultSet.getString("3rd Class")));

                ratesList.add(rate);
            }
        } catch (SQLException e){
            System.out.println("Error in select query for rates table: \n"+e.getMessage());
        }
        return ratesList;
    }

//    public Train getRateById(String trainId) {
//        Train train = new Train();
//
//        return train;
//    }


    public void addRate(TicketPrice rate) {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO ticketprice (startCode,endCode,`1st Class`,`2nd Class`, `3rd Class`) VALUES (?, ?, ?, ?, ?) ";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, rate.getStartCode());
            statement.setString(2, rate.getEndCode());
            statement.setDouble(3, rate.getFirstClass());
            statement.setDouble(4, rate.getSecondClass());
            statement.setDouble(5, rate.getThirdClass());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error when inserting new entry in train table: "+e.getMessage());
        }
    }


    public void updateRate(Train train) {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE ticketprice SET trainType=?WHERE trainId=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, train.getTrainType());
            statement.setString(2, train.getTrainId());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error occurred when executing the update query for train table");
        }
    }


    public void deleteRate(Train train) {
        Connection connection = DBConnection.getConnection();
        String query = "DELETE FROM train WHERE trainId=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, train.getTrainId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error occurred while executing delete query for train table");
            e.printStackTrace();
        }
    }
}

