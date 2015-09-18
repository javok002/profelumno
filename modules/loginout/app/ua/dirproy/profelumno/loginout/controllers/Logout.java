package ua.dirproy.profelumno.loginout.controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by facundo on 18/9/15.
 */
public class Logout extends Controller {
    public static Result logoutUser(){
        session().clear();
        System.out.println(session().containsKey("email"));
        return redirect(routes.Login.loginView());
    }
}
