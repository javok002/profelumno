package ua.dirproy.profelumno.loginout.controllers;

import play.data.Form;
import play.db.ebean.Model;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.loginout.models.UserLogger;
import ua.dirproy.profelumno.loginout.views.html.login;
import ua.dirproy.profelumno.loginout.views.html.main;
import ua.dirproy.profelumno.user.models.User;

/**
 * Created by facundo on 14/9/15.
 */
public class Login extends Controller {

    public static Result loginView (){ return ok(login.render());}

    public static Result loginUser(){
        UserLogger user = Form.form(UserLogger.class).bindFromRequest().get();
        System.out.println(user.getEmail()+" + "+user.getPassword());
        User user1 = User.validateEmail(user.getEmail(),user.getPassword());
        if (user1 == null){
            System.out.println("bad");
            return badRequest(login.render());
        }
        else{
            System.out.println("ok");
            return ok(login.render());
        }
    }

}
