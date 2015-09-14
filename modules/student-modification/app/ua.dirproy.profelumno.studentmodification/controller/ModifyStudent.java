package ua.dirproy.profelumno.studentmodification.controller;

import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.studentmodification.view.html.*;

/**
 * Created by tombatto on 14/09/15.
 */
public class ModifyStudent extends Controller {

    public static Result modifyStudentView() {
        return ok(modifystudent.render());
    }

}
