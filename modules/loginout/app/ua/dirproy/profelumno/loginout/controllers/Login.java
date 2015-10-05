package ua.dirproy.profelumno.loginout.controllers;

import play.data.Form;
import play.db.ebean.Model;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Lesson;
import ua.dirproy.profelumno.common.models.Review;
import ua.dirproy.profelumno.loginout.models.UserLogger;
import ua.dirproy.profelumno.loginout.views.html.login;
import ua.dirproy.profelumno.loginout.views.html.main;
import ua.dirproy.profelumno.common.models.Teacher;
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
        createMockTeacherProfile();
        return ok(login.render());}

    private static void createMockTeacherProfile(){

        Date date = new Date();

        final User user = new User();
        user.setName("Premium");
        user.setBirthday(date);
        user.setEmail("pepe@lab.com");
        user.setPassword("123456");

        final Teacher teacher = new Teacher();
        teacher.setRanking(8);
        teacher.setIsInTrial(false);
        teacher.setHasCard(false);
        teacher.setSubscription("Premium");

        teacher.setRenewalDate(date);
        teacher.setUser(user);

        final Subject subject =new Subject();
        subject.setName("M");

        final Subject subject1 =new Subject();
        subject1.setName("L");

        Review review = new Review();
        review.setStars((long) 7);
        review.setComment("M");
        review.setDate(date);

        Review review1 = new Review();
        review1.setStars((long) 5);
        review1.setComment("M");
        review1.setDate(date);

        final Lesson lesson = new Lesson();
        lesson.setTeacher(teacher);
        Calendar c = new GregorianCalendar(1995,1, 1);
        lesson.setDateTime(c.getTime());
        lesson.setSubject(subject);
        lesson.setStudentReview(review);
        lesson.setAddress("M");
        lesson.setComment("M");
        lesson.setLessonState(1);

        final Lesson lesson1 = new Lesson();
        lesson1.setTeacher(teacher);
        Calendar c1 = new GregorianCalendar(2016,1, 1);
        lesson1.setDateTime(c1.getTime());
        lesson1.setSubject(subject1);
        lesson1.setStudentReview(review1);
        lesson1.setAddress("M");
        lesson1.setComment("M");
        lesson1.setLessonState(1);

        user.save();
        teacher.save();
        subject.save();
        subject1.save();
        review.save();
        review1.save();
        lesson.save();
        lesson1.save();
        teacher.save();
    }

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
