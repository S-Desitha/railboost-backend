package org.ucsc.railboostbackend.services;

import org.ucsc.railboostbackend.models.Staff;

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


    public EmailService() {
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


    public String createStaffSignupText(String tempUID, Staff staff, String railBoostLogoURL) {
        String username = staff.getUser().getUsername();
        String firstName = staff.getUser().getfName();

        return "Dear **" + firstName + "**,\n\n" +
                "Welcome to RailBoost!\n" +
                "Your username is: **" + username + "**\n\n" +
                "We're thrilled to have you as part of RailBoost. To get started, please click the link below to complete your profile:\n" +
                "http://localhost:5500/html/signup.html?tempUID=" + tempUID + "\n\n" +
                "RailBoost Team Logo: " + "https://drive.google.com/file/d/159SpP2HSHQ-V5J9wYAcLVnooZSVRH6pv/view?usp=sharing" + "\n\n" +
                "Thank you for choosing RailBoost!\n\n" +
                "Thanks,\n" +
                "RailBoost Team";
    }


}
