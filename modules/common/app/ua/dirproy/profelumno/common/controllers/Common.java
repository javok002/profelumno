package ua.dirproy.profelumno.common.controllers;

import com.avaje.ebean.Ebean;
import play.mvc.Controller;
import play.mvc.Result;

import play.twirl.api.Html;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.common.views.html.sidebar;
import ua.dirproy.profelumno.common.views.html.topbar;
import ua.dirproy.profelumno.user.models.User;

/**
 * Created by Nicolás Burroni
 * Date: 28/09/15
 */
public class Common extends Controller {

    public static Result sidebar(){
        return ok(sidebar.render());
    }

    public static Result topbar(){
        return ok(topbar.render());
    }

    /*final long userId = Long.parseLong(session("id"));
    User user = Ebean.find(User.class, userId);*/

    private static boolean isTeacher(User user) {
        return Teacher.finder.where().eq("user", user).findUnique() != null;
    }

    private static boolean isStudent(User user) {
        return Student.finder.where().eq("user", user).findUnique() != null;
    }

}
