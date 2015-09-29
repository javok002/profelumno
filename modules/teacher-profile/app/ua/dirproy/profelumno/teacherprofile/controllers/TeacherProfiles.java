package ua.dirproy.profelumno.teacherprofile.controllers;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Lesson;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.teacherprofile.views.html.teacherProfile;
import ua.dirproy.profelumno.user.models.Subject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by javier
 * Date: 9/7/15
 * Project profelumno
 */
public class TeacherProfiles extends Controller {

    public static Result teacherProfileView() {
        long id = idTeacher();
        if (id == -1){
            return badRequest("invalid");
        }
        return ok(teacherProfile.render(id));
    }

    private static long idTeacher() {
        String id = session("id");
        Long parseId = Long.parseLong(id);
        List<Teacher> teachers = Teacher.list();
        for (int i = 0; i < teachers.size(); i++) {
            Teacher aux = teachers.get(i);
            if (aux.getUser().getId().equals(parseId)) {
                return aux.getId();
            }
        }
        return -1;
    }

    private static List<Lesson> myLessons(){
        Teacher teacher = Teacher.getTeacher(idTeacher());
        List<Lesson> lessons = Lesson.list();
        List<Lesson> myLessons = new ArrayList<>();
        for (int i = 0; i <lessons.size() ; i++) {
            Lesson aux = lessons.get(i);
            if (aux.getTeacher().getId().equals(teacher.getId())){
                myLessons.add(aux);
            }
        }
        return myLessons;
    }

    public static Result getRanking(){
        Teacher teacher = Teacher.getTeacher(idTeacher());
        float ranking = teacher.getRanking();
        return ok(Json.toJson(ranking));
    }

    public static Result getSubjects(){
        Teacher teacher = Teacher.getTeacher(idTeacher());
        List<Subject> subjects = teacher.getUser().getSubjects();
        return ok(Json.toJson(subjects));
    }

    public static Result getNextsLesson(){
        List<Lesson> lessons = myLessons();
        List<Lesson> nextLessons = new ArrayList<>();
        Date date = new Date();
        for (int i = 0; i <lessons.size() ; i++) {
            if (nextLessons.size() <= 5) {
                Lesson aux = lessons.get(i);
                if (aux.getDateTime().after(date)) {
                    nextLessons.add(aux);
                }
            }
            else {
                break;
            }
        }
        return ok(Json.toJson(nextLessons));
    }

    public static Result getPreviousLesson(){
        List<Lesson> lessons = myLessons();
        List<Lesson> previousLessons = new ArrayList<>();
        Date date = new Date();
        for (int i = 0; i <lessons.size() ; i++) {
            if (previousLessons.size() <= 5) {
                Lesson aux = lessons.get(i);
                if (aux.getDateTime().before(date)){
                previousLessons.add(aux);
                 }
            }
            else {
                break;
            }
        }
        return ok(Json.toJson(previousLessons));
    }


}
