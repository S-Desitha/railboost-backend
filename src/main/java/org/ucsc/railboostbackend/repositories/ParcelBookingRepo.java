package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.ParcelBooking;
import org.ucsc.railboostbackend.services.EmailService;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParcelBookingRepo {
    public void addParcel(ParcelBooking parcelBooking) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String parcel_query ="INSERT INTO `parcelbooking`( `trackingId`, `sendingStation`, " +
                "`senderAddress`, `SenderNIC`, `recoveringStation`, `receiverName`, `receiverAddress`, `receiverTelNo`," +
                " `receiverEmail`, `item`, `receiverNIC`, `userId`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";

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

            statement.executeUpdate();

            EmailService emailService =  new EmailService();
            String body = emailService.createParcelBookingEmail(parcelBooking);
            emailService.sendEmail(parcelBooking.getReceiverEmail(),"parcel Booking Email",body);



        }catch (SQLException e){
            System.out.println("Error when inserting new entry in line table: "+e.getMessage());
        }
    }
}
