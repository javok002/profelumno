package ua.dirproy.profelumno.contactform.controllers;

import play.mvc.Controller;
import play.mvc.Result;

import ua.dirproy.profelumno.contactform.view.html.*;

/**
 * Created by yankee on 28/09/15.
 */
public class ContactForm extends Controller {

    public static Result contactFormView(){
        return ok(contactform.render());
    }
}
