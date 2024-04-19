package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.ParcelBooking;
import org.ucsc.railboostbackend.models.User;
import org.ucsc.railboostbackend.services.EmailService;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParcelBookingRepo {
    public void addParcel(ParcelBooking parcelBooking) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String parcel_query ="INSERT INTO `parcelbooking`( `trackingId`, `sendingStation`, " +
                "`senderAddress`, `SenderNIC`, `recoveringStation`, `receiverName`, `receiverAddress`, `receiverTelNo`," +
                " `receiverEmail`, `item`, `receiverNIC`, `userId` ,`category`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
        String user_query = "SELECT `username` FROM `users` WHERE userId=?;";

        try (PreparedStatement statement = connection.prepareStatement(parcel_query)){
            statement.setString(1,parcelBooking.getTrackingId());
            statement.setString(2,parcelBooking.getSendingStation());
            statement.setString(3,parcelBooking.getReceiverAddress());
            statement.setString(4,parcelBooking.getSenderNIC());
            statement.setString(5,parcelBooking.getRecoveringStation());
            statement.setString(6,parcelBooking.getReceiverName());
            statement.setString(7,parcelBooking.getSenderAddress());
            statement.setString(8,parcelBooking.getReceiverTelNo());
            statement.setString(9,parcelBooking.getReceiverEmail());
            statement.setString(10,parcelBooking.getItem());
            statement.setString(11,parcelBooking.getReceiverNIC());
            statement.setInt(12,parcelBooking.getUserId());
            statement.setInt(13,parcelBooking.getCategory());


            statement.executeUpdate();

            EmailService emailService =  new EmailService();
//            String body = emailService.createParcelBookingEmail(parcelBooking);
//            emailService.sendEmail(parcelBooking.getReceiverEmail(),"parcel Booking Email",body);



        }catch (SQLException e){
            System.out.println("Error when inserting new entry in line table: "+e.getMessage());
        }
    }

    public List<ParcelBooking> getParcelsByID(Integer userID){
        Connection connection = DBConnection.getConnection();
        //ParcelBooking parcelBooking = new ParcelBooking();
        List<ParcelBooking> parcelList = new ArrayList<>();

        String parcelQuery = "SELECT bookingId, item, receiverName, receiverAddress, receiverEmail,status   FROM parcelbooking  WHERE userId=?";

        try(PreparedStatement statement = connection.prepareStatement(parcelQuery)) {
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                ParcelBooking parcelBooking = new ParcelBooking();


                parcelBooking.setBookingId(resultSet.getString("bookingId"));
                parcelBooking.setItem(resultSet.getString("item"));
                parcelBooking.setReceiverName(resultSet.getString("receiverName"));
                parcelBooking.setReceiverAddress(resultSet.getString("receiverAddress"));
                parcelBooking.setReceiverEmail((resultSet.getString("receiverEmail")));
                parcelBooking.setStatus((resultSet.getString("status")));
                parcelList.add(parcelBooking);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return parcelList;
    }
}
