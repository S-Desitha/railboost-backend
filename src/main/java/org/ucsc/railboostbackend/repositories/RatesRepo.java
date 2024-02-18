package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.TicketPrice;
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
        String query = "SELECT * FROM ticketprice";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                TicketPrice rate = new TicketPrice();
                rate.setStartStation(resultSet.getString("startStation"));
                rate.setEndStation(resultSet.getString("endStation"));
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
}
