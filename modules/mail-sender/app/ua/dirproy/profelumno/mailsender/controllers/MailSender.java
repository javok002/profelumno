package ua.dirproy.profelumno.mailsender.controllers;

import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.mailsender.models.MailSenderUtil;

import javax.mail.MessagingException;
import java.util.ArrayList;

/**
 * Created by Alvaro Gaita on 13/09/2015.
 * Universidad Austral.
 * Facultad Ingenieria 2015.
 */
public class MailSender extends Controller {

    public static Result mockSend(String to){
        try {
            MailSenderUtil.send(new String[]{to}, "Test", "<h1>Hello</h1> <h2>World!</h2>");
            return ok("Mail sent to: " + to);
        } catch (MessagingException e) {
            return internalServerError(e.toString());
        }
    }
}
