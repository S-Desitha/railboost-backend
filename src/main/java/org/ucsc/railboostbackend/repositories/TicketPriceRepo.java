package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.TicketPrice;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketPriceRepo {
    public TicketPrice getTicketPrice(String startStation,String endStation){
        TicketPrice ticketPrice = new TicketPrice();
        Connection  connection = DBConnection.getConnection();

        String query = "SELECT * FROM ticketprice WHERE startCode=? AND endCode=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, startStation);
            statement.setString(2, endStation);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ticketPrice.setId(resultSet.getInt("id"));
                ticketPrice.setStartStation(resultSet.getString("startCode"));
                ticketPrice.setEndStation(resultSet.getString("endCode"));
                ticketPrice.setFirstClass(resultSet.getDouble("1st Class"));
                ticketPrice.setSecondClass(resultSet.getDouble("2nd Class"));
                ticketPrice.setThirdClass(resultSet.getDouble("3rd Class"));

            }
        } catch (SQLException e){
            System.out.println("Error in select query for ticketprice table: \n" + e.getMessage());
        }
        return ticketPrice;
    }
}

