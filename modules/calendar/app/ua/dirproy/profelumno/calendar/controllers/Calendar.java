package ua.dirproy.profelumno.calendar.controllers;

import authenticate.Authenticate;

import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;


@Authenticate({Teacher.class, Student.class})
public class Calendar extends Controller {

    

}
