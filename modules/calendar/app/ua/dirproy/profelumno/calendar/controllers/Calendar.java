package ua.dirproy.profelumno.calendar.controllers;

import authenticate.Authenticate;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;

import ua.dirproy.profelumno.calendar.views.html.calendar;
import ua.dirproy.profelumno.user.models.User;

@Authenticate({Teacher.class, Student.class})
public class Calendar extends Controller {

    public static Result calendarView() { return ok(calendar.render()); }

    public static Result getUserName() {
        User user = User.finder.byId(Long.parseLong(session("id")));
        return user != null ? ok(user.getName() + " " + user.getSurname()) : badRequest();
    }

}
