package ua.dirproy.profelumno.controllers;

import authenticate.Authenticate;
import play.*;
import play.mvc.*;

import ua.dirproy.profelumno.user.models.User;
import ua.dirproy.profelumno.views.html.*;

@Authenticate()
public class Application extends Controller {

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

}
