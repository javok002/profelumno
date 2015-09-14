package ua.dirproy.profelumno.register.controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.register.views.html.*;

import static play.libs.Json.toJson;

/**
 * Created by Nicolas Burroni
 * Date: 9/12/15
 */
public class Register extends Controller {

    public static Result registerView() {
        return ok(register.render());
    }

    public static Result registerUser() {
        return ok("To be implemented"); //TODO
    }
}
