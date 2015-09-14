package ua.dirproy.profelumno.register.controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.register.views.html.register;
import ua.dirproy.profelumno.user.models.User;

/**
 * Created by Nicolas Burroni
 * Date: 9/12/15
 */
public class Register extends Controller {

    public static Result registerView() {
        return ok(register.render());
    }

    public static Result registerUser() {
        Form<User> user = Form.form(User.class).bindFromRequest();
        if (user.hasErrors()) {
            return badRequest(register.render());
        }
        user.get().save();
        return ok();
    }
}
