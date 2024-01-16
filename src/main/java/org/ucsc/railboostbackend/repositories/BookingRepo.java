package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.Booking;
import org.ucsc.railboostbackend.utilities.DBConnection;
import org.ucsc.railboostbackend.utilities.QRCodeGenerator;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.sql.*;
import java.util.Properties;

public class BookingRepo {
    public void bookTicketAndSendEmail(Booking booking){
        Connection connection = DBConnection.getConnection();

        try{
            String query = "INSERT INTO booking (userId, startStation, endStation, date, trainClass, numberOfTickets, totalPrice) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try(PreparedStatement statement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, booking.getUserId());
                statement.setString(2, booking.getStartStation());
                statement.setString(3, booking.getEndStation());
                statement.setObject(4,  booking.getDate());
                statement.setString(5, booking.getTrainClass());
                statement.setInt(6, booking.getNumberOfTickets());
                statement.setDouble(7, booking.getTotalPrice());

                statement.executeUpdate();

                int bookingId;
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        bookingId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve booking ID.");
                    }
                }

                byte[] qrCodePath = generateAndSaveQRCode(bookingId);
                sendEmailWithQRCode(booking.getUserId(), qrCodePath, booking);
            }

        }catch (SQLException e){
            System.out.println("Error occurred during ticket booking and email sending: " + e.getMessage());
        }
    }
      private byte[] generateAndSaveQRCode(int bookingId){
        String data = "Booking ID: " + bookingId;
        return QRCodeGenerator.generateQRCode(data);
    }

    private void sendEmailWithQRCode(int userId, byte[] qrCodePath,Booking booking){
        String host = "smtp.gmail.com";
        String username = "kalindusankalpa5285@gmail.com";
        String password = "cmgpxcupqzsmmvpr";
        String from = "kalindusankalpa5285@gmail.com";

        String to = "sankalpa5285@gmail.com";
        String subject = "RailBoost E-Ticket";
        String body = "<html><body>" +
                "<h2>RailBoost E-Ticket</h2>" +
                "<p><strong>Start Station:</strong> " + booking.getStartStation() + "</p>" +
                "<p><strong>End Station:</strong> " + booking.getEndStation() + "</p>" +
                "<p><strong>Date:</strong> " + booking.getDate() + "</p>" +
                "<p><strong>Train Class:</strong> " + booking.getTrainClass() + "</p>" +
                "<p><strong>Number of Tickets:</strong> " + booking.getNumberOfTickets() + "</p>" +
                "<p><strong>Total Price:</strong> " + booking.getTotalPrice() + "</p>" +
                "</body></html>";




        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(body,"text/html");

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.setDataHandler(new DataHandler(new ByteArrayDataSource(qrCodePath, "image/png")));
            attachmentPart.setFileName("qrcode.png");
            attachmentPart.setHeader("Content-Type", "image/png");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);
            session.setDebug(true);
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error sending email: " + e.getMessage());
        }
    }

    private static String EmailById(int userId) {
        return "";
    }

}
