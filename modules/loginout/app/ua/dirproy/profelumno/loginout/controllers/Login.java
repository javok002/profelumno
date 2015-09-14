package ua.dirproy.profelumno.loginout.controllers;

import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.loginout.views.html.login;

/**
 * Created by facundo on 14/9/15.
 */
public class Login extends Controller {
    public static Result loginView (){ return ok(login.render());}

}
