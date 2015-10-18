package ua.dirproy.profelumno.institutional.controllers;

import play.mvc.Controller;
import play.mvc.Result;

import ua.dirproy.profelumno.institutional.views.html.institutional;
/**
 * Created by rudy on 17/10/15.
 */
public class Institutional extends Controller {

    public static Result institutionalView() {
        return ok(institutional.render());
    }
}
