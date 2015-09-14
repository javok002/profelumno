package ua.dirproy.profelumno.register.controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.register.models.Student;
import ua.dirproy.profelumno.register.models.Teacher;
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
        Form<User> form = Form.form(User.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(register.render());
        }
        String role = form.data().get("role");
        if (role.equals("student")){
            Student student = new Student();
            student.setUser(form.get());
            student.save();
        }else {
            Teacher teacher = new Teacher();
            teacher.setUser(form.get());
            teacher.save();
        }
        return ok();
    }
}
