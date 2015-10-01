package ua.dirproy.profelumno.teacherprofile.controllers;

import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.teacherprofile.views.html.acceptLesson;

/**
 * Created by facundo on 28/9/15.
 */
public class AceptLesson extends Controller {
    public static Result aceptView(){
        return ok(acceptLesson.render());
    }
}
