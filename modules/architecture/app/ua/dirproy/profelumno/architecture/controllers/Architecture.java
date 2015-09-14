package ua.dirproy.profelumno.architecture.controllers;

import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.architecture.views.html.sidebar;
import ua.dirproy.profelumno.architecture.views.html.topbar;

/**
 * Created with IntelliJ IDEA.
 * User: Santiago Ambrosetti
 * Date: 9/14/15
 */
public class Architecture extends Controller {

    public static Result sidebar(){
        return ok(sidebar.render());
    }

    public static Result topbar(){
        return ok(topbar.render());
    }
}
