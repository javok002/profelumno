package ua.dirproy.profelumno.mailsender.controllers;

import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.mailsender.models.MailSenderUtil;

import javax.mail.MessagingException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * <p>
 * User: federuiz
 * Date: 11/9/15
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class MailSender extends Controller {

    public static Result mockSend(String to){ //TODO: Test
        try {
            MailSenderUtil.send(to, "Test", "<h1><strong>Hello</strong> World!</h1>", new ArrayList<String>());
            return redirect("/");
        } catch (MessagingException e) {
            return internalServerError(e.toString());
        }
    }
}
