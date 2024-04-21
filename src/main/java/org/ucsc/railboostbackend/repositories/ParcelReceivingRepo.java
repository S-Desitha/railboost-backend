package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.ParcelReceiving;
import org.ucsc.railboostbackend.models.Season;
import org.ucsc.railboostbackend.services.EmailService;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParcelReceivingRepo {

    public List<ParcelReceiving> GetParcelDetails(Object userId,String sheduleId){
        Connection connection= DBConnection.getConnection();
        String scode = StationCodeById(userId);
        List<ParcelReceiving> parcelReceivingList = new ArrayList<>();
        String query = "SELECT * FROM parcelbooking WHERE deliver_status = 'Sent' AND recoveringStation = ? AND scheduleId =?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, scode);
            statement.setString(2,sheduleId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                ParcelReceiving parcelReceiving = new ParcelReceiving();
                parcelReceiving.setBookingId(resultSet.getString("bookingId"));
                parcelReceiving.setReceiverEmail(resultSet.getString("receiverEmail"));
                parcelReceiving.setReceiverName(resultSet.getString("receiverName"));
                parcelReceiving.setItem(resultSet.getString("item"));
                parcelReceiving.setWeight(resultSet.getFloat("weight"));
                parcelReceiving.setUserId(resultSet.getInt("userId"));
                parcelReceiving.setSenderNIC(resultSet.getString("SenderNIC"));
                parcelReceiving.setDeliverStatus(resultSet.getString("deliver_status"));

                parcelReceivingList.add(parcelReceiving);
            }
        } catch (SQLException e){
            System.out.println("Error in select query for getting parcel details: \n"+e.getMessage());
        }
        return parcelReceivingList;
    }






    public List<ParcelReceiving> GetParcelCount(Object userId){
        Connection connection= DBConnection.getConnection();
        String scode = StationCodeById(userId);
        List<ParcelReceiving> parcelReceivingList = new ArrayList<>();
        String query = "SELECT scheduleId, COUNT(*) AS Pcount FROM parcelbooking WHERE deliver_status = 'Sent' && recoveringStation = ?" +
                "GROUP BY scheduleId;";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, scode);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                ParcelReceiving parcelReceiving = new ParcelReceiving();
                parcelReceiving.setScheduleId(resultSet.getInt("scheduleId"));
                parcelReceiving.setPCount(resultSet.getInt("Pcount"));

                parcelReceivingList.add(parcelReceiving);
            }
        } catch (SQLException e){
            System.out.println("Error in select query for getting parcel count: \n"+e.getMessage());
        }
        return parcelReceivingList;
    }

    private static String StationCodeById(Object userId) {
        String scode = null;
        Connection  connection = DBConnection.getConnection();

        String query = "SELECT stationCode FROM staff WHERE userId=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, userId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                scode = resultSet.getString("stationCode");

            }
        } catch (SQLException e){
            System.out.println("Error in select query for users table: \n" + e.getMessage());
        }
        return scode;
    }

    public void updateDeliveryStatus(ParcelReceiving parcelReceiving){
        Connection connection = DBConnection.getConnection();

        try{
            String query = "UPDATE parcelbooking SET deliver_status = ? WHERE bookingId = ?";
            try(PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, parcelReceiving.getDeliverStatus());
                statement.setString(2, parcelReceiving.getBookingId());

                statement.executeUpdate();

//                notification function call

                if (parcelReceiving.getDeliverStatus().equals("Received")){
                    String toEmail = parcelReceiving.getReceiverEmail();
                    String subject = "RailBoost Parcel Delivery";


                    EmailService emailService = new EmailService();
                    String body = emailService.createParcelRecievedHTML(parcelReceiving);
                    emailService.sendEmail(toEmail, subject, body);
                }
            }

        }catch (SQLException e){
            System.out.println("Error occurred during updating season ticket status: " + e.getMessage());
        }

    }
}
