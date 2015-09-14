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
 * Facultad Ingenieria 2015.
 */
public class MailSenderUtil {

    private final static String FROM = "no-reply@profelumno.com";
    private final static String HOST = "localhost";

    public static void send(String to, String subject, String htmlMsg,
                            List<String> attachmentsPath) throws MessagingException {
        final Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", HOST);

        final Session session = Session.getDefaultInstance(properties);
        final MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
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
