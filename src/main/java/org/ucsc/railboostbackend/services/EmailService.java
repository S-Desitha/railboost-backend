package org.ucsc.railboostbackend.services;

import org.ucsc.railboostbackend.models.Booking;

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



}
