package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.CheckOTP;
import org.ucsc.railboostbackend.models.ResponseType;
import org.ucsc.railboostbackend.services.EmailService;
import org.ucsc.railboostbackend.utilities.DBConnection;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckOTPRepo extends HttpServlet {
    public void updateDeliverStatus(CheckOTP checkOTP){
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE parcelbooking SET deliver_status=\"Collected\" WHERE bookingId=?";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1,checkOTP.getBookingId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try{
            String userEmailQuery = "SELECT u.email FROM users u INNER JOIN parcelbooking p " +
                    "ON u.userId=p.userId WHERE p.bookingId=?";
            try (PreparedStatement statement = connection.prepareStatement(userEmailQuery)){
                statement.setInt(1,checkOTP.getBookingId());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()){
                    String subject = "RailBoost Parcel Delivery";
                    String toEmail = resultSet.getString("email");
                    EmailService emailService = new EmailService();
                    String body = emailService.createParcelCollectHTML();
                    emailService.sendEmail(toEmail, subject, body);
                }

            } catch (SQLException e) {
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public ResponseType verifyOTP(CheckOTP checkOTP){
        Connection connection = DBConnection.getConnection();
        ResponseType responseType = new ResponseType();
        String query = "SELECT OTP FROM parcelbooking WHERE bookingId=?";
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1,checkOTP.getBookingId());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                int otp = resultSet.getInt("OTP");
                if (otp==checkOTP.getOTP()){
                    responseType.setISSuccessful(true);
                    responseType.setError("Your OTP has been successfully verified");
                }else{
                    responseType.setISSuccessful(false);
                    responseType.setError("Invalid OTP. Please verify and enter the correct code.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return responseType;
    }
}
