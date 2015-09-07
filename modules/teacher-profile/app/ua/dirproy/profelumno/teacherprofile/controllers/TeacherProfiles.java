package ua.dirproy.profelumno.teacherprofile.controllers;

import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.user.models.User;

import static play.libs.Json.toJson;

/**
 * Created by javier
 * Date: 9/7/15
 * Project profelumno
 */
public class TeacherProfiles extends Controller {

    public static Result createMockUser() {
        final User user = new User();
        user.setEmail("pepe@lab.com");
        user.setPassword("secret");
        user.save();
        return ok(toJson(user));
    }
}
