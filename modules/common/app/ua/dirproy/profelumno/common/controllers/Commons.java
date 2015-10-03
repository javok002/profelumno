package ua.dirproy.profelumno.common.controllers;

import play.mvc.Controller;
import play.mvc.Result;

import play.twirl.api.Html;
import ua.dirproy.profelumno.common.views.html.sidebar;
import ua.dirproy.profelumno.common.views.html.topbar;

/**
 * Created by Nicolás Burroni
 * Date: 28/09/15
 */
public class Commons extends Controller {

    public static Result sidebar(){
        return ok(sidebar.render());
    }

    public static Result topbar(){
        return ok(topbar.render());
    }

    public static Html testContent() {
        return new Html("<h1>Test Content Works!!!</h1>");
    }



}
