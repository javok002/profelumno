package ua.dirproy.profelumno.teachersearch.controllers;

import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.register.models.Teacher;


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
        return ok(toJson(teachers));

    }
}
