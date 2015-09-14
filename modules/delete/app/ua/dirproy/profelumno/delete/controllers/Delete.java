package ua.dirproy.profelumno.delete.controllers;

import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.delete.views.html.delete;

/**
 * Created by Nicolas Burroni
 * Date: 9/12/15
 */
public class Delete extends Controller {

    public static Result deleteView() {
        return ok(delete.render());
    }

    public static Result deleteUser() {
       /* User user = User.finder.byId(id);
        user.delete(); */
        return ok();
    }
}
