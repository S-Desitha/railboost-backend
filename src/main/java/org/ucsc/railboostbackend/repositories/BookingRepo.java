package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.Booking;
import org.ucsc.railboostbackend.services.EmailService;
import org.ucsc.railboostbackend.utilities.DBConnection;
import org.ucsc.railboostbackend.utilities.QRCodeGenerator;

import java.sql.*;

public class BookingRepo {
    public void bookTicketAndSendEmail(Booking booking,Object id){
        Connection connection = DBConnection.getConnection();

        try{
            String query = "INSERT INTO booking (userId, startStation, endStation, date, trainClass, numberOfTickets, totalPrice) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try(PreparedStatement statement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {
                statement.setObject(1, id);
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
                String toEmail = EmailById(id);
                String subject = "RailBoost E-Ticket";
                String body = "<html><body>" +
                        "<h2>RailBoost E-Ticket Confirmation</h2>" +
                        "<p>Dear Passenger,</p>" +
                        "<p>We are delighted to inform you that your ticket booking with RailBoost has been successfully confirmed. Below are the details of your booking:</p>" +
                        "<ul>" +
                        "  <li><strong>Start Station:</strong> " + booking.getStartStation() + "</li>" +
                        "  <li><strong>End Station:</strong> " + booking.getEndStation() + "</li>" +
                        "  <li><strong>Date:</strong> " + booking.getDate() + "</li>" +
                        "  <li><strong>Train Class:</strong> " + booking.getTrainClass() + "</li>" +
                        "  <li><strong>Number of Tickets:</strong> " + booking.getNumberOfTickets() + "</li>" +
                        "  <li><strong>Total Price:</strong> " + booking.getTotalPrice() + "</li>" +
                        "</ul>" +
                        "<p>We look forward to serving you on your journey with RailBoost. If you have any questions or require further assistance, feel free to reach out to our customer support.</p>" +
                        "<p>Thank you for choosing RailBoost for your travel needs. We wish you a pleasant journey!</p>" +
                        "<p>Best regards,<br/>The RailBoost Team</p>" +
                        "</body></html>";


                EmailService emailService = new EmailService();
                emailService.sendEmailWithQRCode(toEmail, subject, body, qrCodePath);
            }

        }catch (SQLException e){
            System.out.println("Error occurred during ticket booking and email sending: " + e.getMessage());
        }
    }
      private byte[] generateAndSaveQRCode(int bookingId){
        String data = "Booking ID: " + bookingId;
        return QRCodeGenerator.generateQRCode(data);
    }
//
//    private void sendEmailWithQRCode(Object userId, byte[] qrCodePath,Booking booking){
//        String host = "smtp.gmail.com";
//        String username = "kalindusankalpa5285@gmail.com";
//        String password = "cmgpxcupqzsmmvpr";
//        String from = "kalindusankalpa5285@gmail.com";
//
//        String to = EmailById(userId);
//        String subject = "RailBoost E-Ticket";
//        String body = "<html><body>" +
//                "<h2>RailBoost E-Ticket</h2>" +
//                "<p><strong>Start Station:</strong> " + booking.getStartStation() + "</p>" +
//                "<p><strong>End Station:</strong> " + booking.getEndStation() + "</p>" +
//                "<p><strong>Date:</strong> " + booking.getDate() + "</p>" +
//                "<p><strong>Train Class:</strong> " + booking.getTrainClass() + "</p>" +
//                "<p><strong>Number of Tickets:</strong> " + booking.getNumberOfTickets() + "</p>" +
//                "<p><strong>Total Price:</strong> " + booking.getTotalPrice() + "</p>" +
//                "</body></html>";
//
//
//
//
//        Properties properties = System.getProperties();
//        properties.setProperty("mail.smtp.host", host);
//        properties.setProperty("mail.smtp.port", "587");
//        properties.setProperty("mail.smtp.starttls.enable", "true");
//        properties.setProperty("mail.smtp.auth", "true");
//
//        Session session = Session.getDefaultInstance(properties, new Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(username, password);
//            }
//        });
//
//        try {
//            MimeMessage message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(from));
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//            message.setSubject(subject);
//
//            MimeBodyPart textPart = new MimeBodyPart();
//            textPart.setContent(body,"text/html");
//
//            MimeBodyPart attachmentPart = new MimeBodyPart();
//            attachmentPart.setDataHandler(new DataHandler(new ByteArrayDataSource(qrCodePath, "image/png")));
//            attachmentPart.setFileName("qrcode.png");
//            attachmentPart.setHeader("Content-Type", "image/png");
//
//            Multipart multipart = new MimeMultipart();
//            multipart.addBodyPart(textPart);
//            multipart.addBodyPart(attachmentPart);
//
//            message.setContent(multipart);
//            session.setDebug(true);
//            Transport.send(message);
//            System.out.println("Email sent successfully!");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            System.out.println("Error sending email: " + e.getMessage());
//        }
//    }

    private static String EmailById(Object userId) {
        String mail = null;
        Connection  connection = DBConnection.getConnection();

        String query = "SELECT email FROM users WHERE userId=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, userId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                mail = resultSet.getString("email");

            }
        } catch (SQLException e){
            System.out.println("Error in select query for users table: \n" + e.getMessage());
        }
        return mail;
    }

}
