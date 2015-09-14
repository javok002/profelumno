package ua.dirproy.profelumno.delete.controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.delete.views.html.*;

import static play.libs.Json.toJson;

/**
 * Created by Nicolas Burroni
 * Date: 9/12/15
 */
public class Delete extends Controller {

    public static Result deleteView() {
        return ok(delete.render());
    }

    public static Result deleteUser() {
        return ok("To be implemented"); //TODO
    }
}
