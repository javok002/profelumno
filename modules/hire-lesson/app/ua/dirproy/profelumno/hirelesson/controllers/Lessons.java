package ua.dirproy.profelumno.hirelesson.controllers;

import play.Application;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.hirelesson.models.Lesson;
import ua.dirproy.profelumno.hirelesson.views.html.hire;


/**
 * Created by Paca on 9/13/15.
 */
public class Lessons extends Controller {

    public static Result newLesson() {
        final Lesson lesson = Form.form(Lesson.class).bindFromRequest().get();
        lesson.save();
        return ok(); //todo redireccionar al index
    }

    public static Result redirect() {
        return ok(hire.render());
    }
}
