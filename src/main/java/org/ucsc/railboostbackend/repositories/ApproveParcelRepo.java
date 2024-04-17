package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.ApproveParcel;
import org.ucsc.railboostbackend.models.TicketPrice;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApproveParcelRepo {
    public List<ApproveParcel> getParcelDetailsByStation(String station) throws SQLException {
        Connection connection = DBConnection.getConnection();
        List<ApproveParcel> approveParcelsList = new ArrayList<>();

        String ApproveParcelQuery = "SELECT p.bookingId, p.recoveringStation, p.receiverName, p.item, p" +
                ".userId, p.weight, p.status, u.fName FROM parcelbooking p INNER JOIN users u ON p.userId= u.userId " +
                "WHERE p.sendingStation=?";

        try(PreparedStatement statement = connection.prepareStatement(ApproveParcelQuery)) {
            statement.setString(1,station);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                ApproveParcel approveParcel= new ApproveParcel();
                approveParcel.setBookingId(resultSet.getString("bookingId"));
                approveParcel.setSenderName(resultSet.getString("fName"));
                approveParcel.setReceiverName(resultSet.getString("receiverName"));
                approveParcel.setItem(resultSet.getString("item"));
                approveParcel.setRecoveringStation(resultSet.getString("recoveringStation"));
                approveParcel.setWeight(resultSet.getFloat("weight"));
                approveParcel.setStatus(resultSet.getString("status"));

                approveParcelsList.add(approveParcel);

            }
        }


        return approveParcelsList;
    }

    public void updateStatus(ApproveParcel approveParcel) throws IOException {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE parcelbooking SET status = ? WHERE bookingId = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1,(String) approveParcel.getStatus());
            statement.setString(2,(String) approveParcel.getBookingId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addWeightAndTotal(ApproveParcel approveParcel) throws SQLException {
        Connection connection = DBConnection.getConnection();
        TicketPrice ticketPrice = new TicketPrice();
        String query = "UPDATE parcelbooking SET weight= ?,totalprice= ?  WHERE bookingId= ?";

        Double totalPrice = calculateTotalPrice(approveParcel);

        try (PreparedStatement statement = connection.prepareStatement(query)){


            statement.setFloat(1,approveParcel.getWeight());
            statement.setDouble(2,totalPrice);
            statement.setString(3,approveParcel.getBookingId());



            statement.executeUpdate();
        }

    }

    public Double calculateTotalPrice(ApproveParcel approveParcel){

        TicketPriceRepo ticketPriceRepo = new TicketPriceRepo();
        TicketPrice ticketPrice;
        Integer charge = 0;

        Connection connection = DBConnection.getConnection();

        String mQuery = "SELECT  Charges FROM parcelcharges WHERE itemId = ?";
        ResultSet resultSet;

        try (PreparedStatement statement = connection.prepareStatement(mQuery) ){
            statement.setInt(1,approveParcel.getItemId());

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                charge = resultSet.getInt(1);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Float weight = approveParcel.getWeight();
        String startingStation = approveParcel.getSendingStation();
        String endStation = approveParcel.getRecoveringStation();

        ticketPrice = ticketPriceRepo.getTicketPrice(startingStation,endStation);
        Double price = charge*ticketPrice.getThirdClass()*(0.1)*weight;
        return price;
    }

}
