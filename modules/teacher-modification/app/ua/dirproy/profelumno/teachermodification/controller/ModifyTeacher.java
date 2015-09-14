package ua.dirproy.profelumno.teachermodification.controller;

import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.teachermodification.view.html.*;
/**
 * Created by francisco on 13/09/15.
 */
public class ModifyTeacher extends Controller {

    public static Result profileView() {
        return ok(teacherprofile.render());
    }
}
