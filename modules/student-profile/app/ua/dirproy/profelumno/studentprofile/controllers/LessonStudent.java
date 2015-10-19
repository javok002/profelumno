package ua.dirproy.profelumno.studentprofile.controllers;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Lesson;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.studentprofile.views.html.lessonStudent;

import java.text.ParseException;
import java.util.List;


/**
 * Created by facundo on 18/10/15.
 */
public class LessonStudent extends Controller {
    public static Result lessonView(){return ok(lessonStudent.render());}
    public static Result getLessons() throws ParseException {
        Student student = Student.finder.where().eq("USER_ID", Long.parseLong(session("id"))).findUnique();
        List<Lesson> lessons = Lesson.finder.where().eq("STUDENT_ID", student.getId()).findList();
        for (Lesson lesson : lessons) {
            System.out.println(lesson.toString());
        }
        return ok(Json.toJson(lessons));
    }
}
