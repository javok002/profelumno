package ua.dirproy.profelumno.register.controllers;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.register.models.Student;
import ua.dirproy.profelumno.register.models.Teacher;
import ua.dirproy.profelumno.register.views.html.register;
import ua.dirproy.profelumno.user.models.User;

import java.util.List;

/**
 * Created by Nicolas Burroni
 * Date: 9/12/15
 */
public class Register extends Controller {

    public static final String[] secureQuestions = {
            "Cuál es el nombre de mi primer mascota?",
            "En qué ciudad nació mi madre?",
            "Cómo se llama mi cantante/grupo favorito/a?",
            "Quién dejó salir a los perros?",
            "Por qué sibarita es tan rica?"
    };

    public static Result registerView() {
        return ok(register.render());
    }

    public static Result registerUser() {
        Form<User> form = Form.form(User.class).bindFromRequest();
        if (form.hasErrors()) {
//            return badRequest(register.render());
            return badRequest("Error in form");
        }
        String role = form.data().get("role");
        User user = form.get();

        if (User.validateEmailUnique(user.getEmail())) {

            if (role.equals("student")) {
                Student student = new Student();
                student.setUser(user);
                user.save();
                student.save();
                return ok(Json.toJson(student));
            } else {
                Teacher teacher = new Teacher();
                teacher.setUser(user);
                user.save();
                teacher.save();
                return ok(Json.toJson(teacher));
            }

        }
        else {
            return badRequest("Unique");
        }

    }

    public static Result getSecureQuestions() { return ok(Json.toJson(secureQuestions)); }
}
