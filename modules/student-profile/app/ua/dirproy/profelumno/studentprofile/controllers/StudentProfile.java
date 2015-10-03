package ua.dirproy.profelumno.studentprofile.controllers;

import authenticate.Authenticate;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Student;

@Authenticate({Student.class})
public class StudentProfile extends Controller {

    public static Result dashboard(){
        return ok(ua.dirproy.profelumno.studentprofile.views.html.dashboard.render());
    }
}
