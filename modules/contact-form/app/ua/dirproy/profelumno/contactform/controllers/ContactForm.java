package ua.dirproy.profelumno.contactform.controllers;

import play.mvc.Controller;
import play.mvc.Result;

import ua.dirproy.profelumno.contactform.view.html.*;
import ua.dirproy.profelumno.mailsender.models.MailSenderUtil;

/**
 * Created by yankee on 28/09/15.
 */
public class ContactForm extends Controller {

    public static Result contactFormView() {
        return ok(contactform.render());
    }

    public static void sendForm(String name, String email, String subject, String message) {
        try {
            MailSenderUtil.send(new String[]{"emailToSendTo@email.com"}, subject, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
