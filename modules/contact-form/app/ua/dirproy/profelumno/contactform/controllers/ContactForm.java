package ua.dirproy.profelumno.contactform.controllers;

import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.contactform.view.html.contactform;
import ua.dirproy.profelumno.mailsender.models.MailSenderUtil;

/**
 * Created by: yankee
 * Created on: 28/09/15
 */
public class ContactForm extends Controller {

    public static Result contactFormView() {
        return ok(contactform.render());
    }

    public static Result sendForm() {
        DynamicForm requestData = DynamicForm.form().bindFromRequest();
        String name, email, subject, message;
        name = requestData.get("name");
        email = requestData.get("email");
        subject = requestData.get("subject");
        message = requestData.get("message");
        String[] lines = message.split("\\n");
        StringBuilder messageToSend = new StringBuilder();
        messageToSend.append("New message from: ").append(name).append("<br><br>At: ").append(email).append("<br>");
        for (String line : lines) {
            messageToSend.append("<br>").append(line);
        }
        String emailToSendTo = "francisco.di@ing.austral.edu.ar";
        try {
            MailSenderUtil.send(new String[]{emailToSendTo}, subject, messageToSend.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return badRequest();
        }
        return redirect("/institutional");
    }
}
