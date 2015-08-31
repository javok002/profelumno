package ua.dirproy.profelumno.controllers;

import play.*;
import play.mvc.*;

import ua.dirproy.profelumno.views.html.*;

public class Application extends Controller {

    public Result index() {
        return ok(ua.dirproy.profelumno.views.html.index.render("Your new application is ready."));
    }

}
