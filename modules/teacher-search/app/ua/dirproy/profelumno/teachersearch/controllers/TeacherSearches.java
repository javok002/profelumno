package ua.dirproy.profelumno.teachersearch.controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Teacher;


import java.util.ArrayList;
import java.util.List;

import static play.libs.Json.toJson;

/**
 * Created by Nash
 * Date: 9/13/15
 * Project profelumno
 */
public class TeacherSearches extends Controller {

    public static Result teacherSearchView(){
        return ok(ua.dirproy.profelumno.teachersearch.views.html.teachersearch.render());
    }

    public static Result getTeachers(){
        final List<Teacher> teachers = Teacher.list();
        Form<Teacher> form = Form.form(Teacher.class).bindFromRequest();
        List<String> subjects = new ArrayList<>(form.data().size() - 3);
        int index =  form.data().size() - 4;
        while (index >= 0 ){
            subjects.add(form.data().get("subjects["+index+"]"));
            index--;
        }
        for (Teacher teacher : teachers) {
            if(checkLessons(Integer.parseInt(form.data().get("lessons")),teacher) ||
                    checkRanking(Integer.parseInt(form.data().get("ranking")),teacher) ||
                    checkHome(Boolean.parseBoolean(form.data().get("homeClasses")),teacher) ||
                    checkSubjects(subjects,teacher)){
                    teachers.remove(teacher);
            }
        }
        return ok(toJson(teachers));
    }

    private static boolean checkSubjects(List<String> subject,Teacher teacher) {
        //TODO: faltaria agregar la lista de subjects en TEACHER.
        return false;
    }

    private static boolean checkRanking(Integer ranking, Teacher teacher) {
        return teacher.getRanking() < ranking;
    }

    private static boolean checkLessons(Integer lessons, Teacher teacher) {
        return teacher.getLessonsDictated() < lessons;
    }

    private static boolean checkHome(Boolean home, Teacher teacher) {
        return !home /*|| CAMBIAR STRING A BOOLEAN -- teacher.getHomeClasses() == home*/;
    }

}
