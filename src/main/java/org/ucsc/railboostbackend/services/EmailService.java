package org.ucsc.railboostbackend.services;


import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;
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


    public String createStaffSignupHTML(String tempUID) {
        return "<table style=\"width: auto; margin-top: 20px; text-align: center;\">\n" +
                "  <tr>\n" +
                "    <td style=\"background-color: #007bff; padding: 15px 30px; border-radius: 5px; color: #fff; font-weight: bold; text-decoration: none; white-space: normal;\">\n" +
                "      <a href=\"http://localhost/html/signup.html?tempUID="+tempUID+"\" style=\"color: #fff; text-decoration: none; display: inline-block; text-align: left;\">\n" +
                "        Complete Your Profile" +
                "      </a>\n" +
                "    </td>\n" +
                "  </tr>\n" +
                "</table>\n";
    }

}