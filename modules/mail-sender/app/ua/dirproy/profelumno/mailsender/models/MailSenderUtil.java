package ua.dirproy.profelumno.mailsender.models;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;
import java.util.Properties;

/**
 * Created by Alvaro Gaita on 13/09/2015.
 * Universidad Austral.
 * Facultad Ingenieria 2015
 *
 * You can use this class to send mails with or without attachments to a list of recipients.
 * The body of the message supports html for styling and adding images.
 * The class depends on the JavaMail API.
 */
public class MailSenderUtil {

    private final static String FROM = "noreply.profelumno@gmail.com";
    private final static String FROM_PW = "profelumnolab2";
    private final static String HOST = "smtp.gmail.com";

    public static void send(String[] to, String subject, String htmlMsg) throws MessagingException {
        final Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", "587");

        final Session session = Session.getInstance(properties,
                new javax.mail.Authenticator(){
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(FROM, FROM_PW);
                    }
                });

        final MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM));

        for (String str : to){
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(str));
        }

        message.setSubject(subject);
        message.setContent(htmlMsg, "text/html");
        Transport.send(message);
    }

    public static void sendWithAttachments(String[] to, String subject, String htmlMsg,
                            List<String> attachmentsPath) throws MessagingException {
        final Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", "587");

        final Session session = Session.getInstance(properties,
                new javax.mail.Authenticator(){
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(FROM, FROM_PW);
                    }
                });

        final MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM));

        for (String str : to){
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(str));
        }
        message.setSubject(subject);

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setHeader("Content-Type", "text/html");
        messageBodyPart.setText(htmlMsg);

        final Multipart multipart = new MimeMultipart("mixed");
        multipart.addBodyPart(messageBodyPart);

        for (String str : attachmentsPath) {
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(str);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(source.getName());
            multipart.addBodyPart(messageBodyPart);
        }

        message.setContent(multipart);
        Transport.send(message);
    }
}
