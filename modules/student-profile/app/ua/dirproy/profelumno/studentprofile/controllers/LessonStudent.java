package ua.dirproy.profelumno.studentprofile.controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Lesson;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.studentprofile.views.html.lessonStudent;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 * Created by facundo on 18/10/15.
 */
public class LessonStudent extends Controller {
    public static Result lessonView(){return ok(lessonStudent.render());}
    public static Result currentLessons() throws ParseException {
        Student student = Student.finder.where().eq("USER_ID", Long.parseLong(session("id"))).findUnique();
        List<Lesson> lessons = Lesson.finder.where().eq("STUDENT_ID", student.getId()).findList();
        ArrayList<Lesson> currentLessons = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if (lesson.getLessonState() == 0 && !lesson.getDateTime().before(new Date())){
                currentLessons.add(lesson);
            }
        }
        return ok(Json.toJson(currentLessons));
    }
    public static Result getNextLessons(){
        Student student = Student.finder.where().eq("USER_ID", Long.parseLong(session("id"))).findUnique();
        List<Lesson> lessons = Lesson.finder.where().eq("STUDENT_ID", student.getId()).findList();
        List<Lesson> nextLessons = new ArrayList<>();
        Date date = new Date();
        for (int i = 0; i <lessons.size() ; i++) {

                Lesson aux = lessons.get(i);
                if (aux.getDateTime().after(date) && aux.getLessonState() == 1) {
                    nextLessons.add(aux);
                }
        }
        return ok(Json.toJson(nextLessons));
    }

    public static Result getPreviousLessons(){
        Student student = Student.finder.where().eq("USER_ID", Long.parseLong(session("id"))).findUnique();
        List<Lesson> lessons = Lesson.finder.where().eq("STUDENT_ID", student.getId()).findList();
        ArrayList<Lesson> previousLessons = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if (lesson.getLessonState() == 1 && lesson.getDateTime().before(new Date())){
                previousLessons.add(lesson);
            }
        }
        return ok(Json.toJson(previousLessons));
    }
}
