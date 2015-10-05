package ua.dirproy.profelumno.contactform.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.data.DynamicForm;
import play.libs.Json;
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

    // TODO: send email to correct email address
    // TODO: give a window confirmation after sending email then reload to another page
    public static Result sendForm() {
        DynamicForm requestData = DynamicForm.form().bindFromRequest();
        String name, email, subject, message;
        name = requestData.get("name");
        email = requestData.get("email");
        subject = requestData.get("subject");
        message = requestData.get("message");
        final ObjectNode result = Json.newObject();
        result.put("name", name);
        result.put("email", email);
        result.put("subject",subject);
        result.put("message",message);
        try {
            MailSenderUtil.send(new String[]{"francisco.di@ing.austral.edu.ar"}, subject, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok(result);
    }
}
