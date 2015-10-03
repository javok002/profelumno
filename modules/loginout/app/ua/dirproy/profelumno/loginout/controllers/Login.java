package ua.dirproy.profelumno.loginout.controllers;

import play.data.Form;
import play.db.ebean.Model;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.loginout.models.UserLogger;
import ua.dirproy.profelumno.loginout.views.html.login;
import ua.dirproy.profelumno.loginout.views.html.main;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.user.models.User;

import java.util.Date;

/**
 * Created by facundo on 14/9/15.
 */
public class Login extends Controller {

    public static Result loginView (){
        /*User user = new User();
        user.setName("Pepe");
        user.setSurname("Castro");
        user.setEmail("pepe@gmail.com");
        user.setBirthday(new Date());
        user.setGender("M");
        user.setPassword("pepehola");
        user.setSecureAnswer("Fazzo");
        user.setSecureQuestion("aaaa");
        user.save();*/
        return ok(login.render());}

    public static Result loginUser(){
        UserLogger user = Form.form(UserLogger.class).bindFromRequest().get();
        //System.out.println(user.getEmail()+" + "+user.getPassword());
        User user1 = User.validateEmail(user.getEmail(),user.getPassword());
        if (user1 == null){
            flash("error","Email o contraseña incorrectos." );
            flash("previousEmail", user.getEmail());

            //System.out.println("bad");
            return redirect(routes.Login.loginView());
        }
        else{
            session("id", Long.toString(user1.getId()));
            Teacher teacher = Teacher.finder.where().eq("USER_ID", user1.getId()).findUnique();
            if (teacher != null && !teacher.hasCard()){
                return redirect("/subscription");
            }

            //System.out.println("ok");
            return redirect("/");//todo redireccionar al dashboard del alumno o teacher depende que sea
        }
    }

}
