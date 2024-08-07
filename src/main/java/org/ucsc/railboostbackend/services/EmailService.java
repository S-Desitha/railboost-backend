package org.ucsc.railboostbackend.services;

import org.ucsc.railboostbackend.models.*;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.util.Date;
import java.util.Properties;

public class EmailService {

    private final String fromEmail = "railboostSL@gmail.com";
    private final String password = "oaqt gsqt jhpj tudc ";
    Session session;
    private Properties props = new Properties();
    private String origin;


    public EmailService(String origin) {
        this.origin=origin;
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS


        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });
    }

    public EmailService() {
        this.origin=origin;
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS


        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });
    }


    public void sendEmail(String toEmail, String subject, String body) {
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(body, "text/HTML; charset=UTF-8");
            message.setSentDate(new Date());

            Transport.send(message);

        } catch (AddressException e) {
            System.out.println("Address exception in EmailService: ");
            System.out.println(e.getMessage());
        } catch (MessagingException e) {
            System.out.println("Messaging exception in EmailService: ");
            System.out.println(e.getMessage());
        }
    }

    public void sendEmailWithQRCode(String toEmail, String subject, String body, byte[] qrCodePath){
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
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


    public String createStaffSignupHTML(String tempUID, Staff staff) {
        String username = staff.getUser().getUsername();
        String firstName = staff.getUser().getfName();

        String railBoostLogoURL ="https://postimg.cc/QVgT87ys";

        return "<div style=\"text-align: center;\">\n" +
                "<p style=\"font-size: 18px; margin-bottom: 10px;\">Dear " + firstName + ",</p>" +
                "<p style=\"font-size: 16px; margin-bottom: 10px;\">Welcome to RailBoost! Your username is: " + username + "</p>\n" +
                "<p style=\"font-size: 16px; margin-bottom: 10px;\">We're thrilled to have you as part of RailBoost. To get started, please click the link below to complete your profile:</p>\n" +
                "<a href=\""+ origin+ "/html/signup.html?tempUID=" + tempUID + "\" style=\"color: #007bff; text-decoration: none; font-weight: bold; display: inline-block; text-align: left;\">Complete Your Profile</a>\n" +
                "<p style=\"font-size: 14px; margin-top: 10px;\">Thank you for choosing RailBoost!</p>\n" +
                "</div>\n";
                  // Add a clear separation line
    }

    public String createFogetPWHTML(String tempUID, User user) {
        String username = user.getUsername();
//        String firstName = staff.getUser().getfName();

        String railBoostLogoURL ="https://postimg.cc/QVgT87ys";

        return "<div style=\"text-align: center;\">\n" +
                "<p style=\"font-size: 18px; margin-bottom: 10px;\">Dear Sir/Madam" + ",</p>" +
                "<p style=\"font-size: 16px; margin-bottom: 10px;\">Welcome to RailBoost! Your username is: " + username + "</p>\n" +
                "<p style=\"font-size: 16px; margin-bottom: 10px;\">To recover your account, please click the link below:</p>\n" +
                "<a href=\""+ origin+ "/html/forgetPW.html?tempUID=" + tempUID + "\" style=\"color: #007bff; text-decoration: none; font-weight: bold; display: inline-block; text-align: left;\">Recover accoount</a>\n" +
                "<p style=\"font-size: 14px; margin-top: 10px;\">Thank you for choosing RailBoost!</p>\n" +
                "</div>\n";
        // Add a clear separation line
    }
    public String createNormalETicketHTML(Booking booking){
        return "<html><body>" +
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
    }
    public String createSeasonTicketHTML(Season season){
        return "<html><body>" +
                "<h2>RailBoost Season Ticket Confirmation</h2>" +
                "<p>Dear Passenger,</p>" +
                "<p>We are delighted to inform you that your season ticket with RailBoost has been successfully confirmed. Below are the details of your booking:</p>" +
                "<ul>" +
                "  <li><strong>Start Station:</strong> " + season.getStartStation() + "</li>" +
                "  <li><strong>End Station:</strong> " + season.getEndStation() + "</li>" +
//                "  <li><strong>Passenger Type:</strong> " + season.getPassengerType() + "</li>" +
                "  <li><strong>Start Date:</strong> " + season.getStartDate() + "</li>" +
                "  <li><strong>End Date:</strong> " + season.getEndDate() + "</li>" +
//                "  <li><strong>Duration:</strong> " + season.getDuration() + "</li>" +
                "  <li><strong>Train Class:</strong> " + season.getTrainClass() + "</li>" +
                "  <li><strong>Total Price:</strong> " + season.getTotalPrice() + "</li>" +
                "</ul>" +
                "<p>We look forward to serving you on your journey with RailBoost. If you have any questions or require further assistance, feel free to reach out to our customer support.</p>" +
                "<p>Thank you for choosing RailBoost for your travel needs. We wish you a pleasant journey!</p>" +
                "<p>Best regards,<br/>The RailBoost Team</p>" +
                "</body></html>";
    }

    public String createParcelBookingEmail(ParcelBooking parcelBooking){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Parcel Notification</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h2>Parcel Delivery Notification</h2>\n" +
                "    <p>Dear "+parcelBooking.getReceiverName()+",</p>\n" +
                "    <p>We are pleased to inform you that a parcel has been dispatched to you. Below are the details of your delivery:</p>\n" +
                "    <ul>\n" +
                "        <li><strong>Tracking ID:</strong>"+ parcelBooking.getTrackingId()+"</li>\n" +
                "        <li><strong>Booking ID:</strong> "+parcelBooking.getBookingId()+"</li>\n" +
                "        <li><strong>Item:</strong> "+parcelBooking.getItem()+"</li>\n" +
               "         <li><strong>Sender's NIC:</strong> "+parcelBooking.getSenderNIC()+"</li>\n" +
                "    </ul>\n" +
                "    <p>Please note that once your parcel arrives at the station, we will send you another email notification. You can then come to the station to collect your parcel.</p>\n" +
                "    <p>If you have any questions or need further assistance, please feel free to contact our customer support team.</p>\n" +
                "    <p>Thank you for choosing us for your parcel delivery. We look forward to serving you.</p>\n" +
                "    <p>Best regards,<br/>The [Your Company Name] Team</p>\n" +
                "</body>\n" +
                "</html>\n";
    }

    public String createParcelRecievedHTML(ParcelReceiving parcelReceiving,String stationName,Integer OTP){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Parcel Notification</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h2>Parcel Delivery Notification</h2>\n" +
                "    <p>Dear"+parcelReceiving.getReceiverName()+",</p>\n" +
                "    <p>We are pleased to inform you that a parcel has arrived at "+stationName+" Railway station. Below are the details of your delivery:</p>\n" +
                "    <ul>\n" +
                "        <li><strong>Booking ID:</strong> "+parcelReceiving.getBookingId()+"</li>\n" +
                "        <li><strong>Item:</strong> "+parcelReceiving.getItem()+"</li>\n" +
                "        <li><strong>NIC No:</strong> "+parcelReceiving.getReceiverNIC()+"</li>\n" +
                "        <li><strong>Recovering Station:"+stationName+"</li>\n" +
                "        <li><strong>OTP:</strong> "+OTP+"</li>\n" +

                "    </ul>\n" +
                "    <p>Please note that you are required to bring your ID and OTP within 7 days to collect your parcel.</p>\n" +
                "    <p>If you have any questions or need further assistance, please feel free to contact our customer support team.</p>\n" +
                "    <p>Thank you for choosing us for your parcel delivery. We look forward to serving you.</p>\n" +
                "    <p>Best regards,<br/>The RailBoost Team</p>\n" +
                "</body>\n" +
                "</html>\n";
    }

    public String createParcelCollectHTML(){

        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Parcel Received Confirmation</title>\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Parcel Received Confirmation</h1>\n" +
                "        <p>Dear Sender,</p>\n" +
                "        <p>We are pleased to inform you that your parcel has been successfully received by the intended recipient.</p>\n" +
                "        <p>If you have any further inquiries or need assistance, please feel free to contact us.</p>\n" +
                "        <p>Thank you for choosing our service!</p>\n" +
                "        <p>Best regards,</p>\n" +
                "        <p>RailBoost Team</p>\n" +
                "        \n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
    }





}
