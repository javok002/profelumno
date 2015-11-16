package ua.dirproy.profelumno.loginout.controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Lesson;
import ua.dirproy.profelumno.common.models.Review;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.loginout.models.FacebookLogger;
import ua.dirproy.profelumno.loginout.models.UserLogger;
import ua.dirproy.profelumno.loginout.views.html.login;
import ua.dirproy.profelumno.user.models.Subject;
import ua.dirproy.profelumno.user.models.User;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by facundo on 14/9/15.
 */
public class Login extends Controller {

    public static Result loginView (){

        return ok(login.render());
    }



    public static Result loginUser() {
        FacebookLogger facebookLogger = Form.form(FacebookLogger.class).bindFromRequest().get();
        if (facebookLogger.getFb_email()!=null){
            User unique = User.finder.where().eq("email", facebookLogger.getFb_email()).findUnique();
            if (unique != null) {
                session("id", Long.toString(unique.getId()));
                unique.setLastLogin(new Date());
                unique.save();
                Teacher teacher = Teacher.finder.where().eq("USER_ID", unique.getId()).findUnique();
                if (teacher != null && !teacher.hasCard()) {
                    return redirect("/subscription");
                }
                return redirect("/");
            } else {
                flash("email", facebookLogger.getFb_email().trim());
                flash("name", facebookLogger.getFb_name());
                flash("surname", facebookLogger.getFb_surname());
                return redirect("/register");
            }
        } else {
            UserLogger user = Form.form(UserLogger.class).bindFromRequest().get();
            User user1 = User.validateEmail(user.getEmail(), user.getPassword());
            if (user1 == null) {
                flash("error", "Email o contraseña incorrectos.");
                flash("previousEmail", user.getEmail());
                return redirect(routes.Login.loginView());
            } else {
                session("id", Long.toString(user1.getId()));
                user1.setLastLogin(new Date());
                user1.save();
                Teacher teacher = Teacher.finder.where().eq("USER_ID", user1.getId()).findUnique();
                if (teacher != null && !teacher.hasCard()) {
                    return redirect("/subscription");
                }
                return redirect("/");
            }
        }
    }

}
