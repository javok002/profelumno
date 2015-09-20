package ua.dirproy.profelumno.studentmodification.controller;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.register.models.Student;
import ua.dirproy.profelumno.studentmodification.view.html.*;
import ua.dirproy.profelumno.user.models.User;

import java.util.Date;

/**
 * Created by tombatto on 14/09/15.
 */
public class ModifyStudent extends Controller {

    public static Result modifyStudentView() {

        return ok(modifystudent.render());
    }

    public static Result getStudent(){
        //final Long userId = Long.parseLong(session().get("id"));
        //Student student = Ebean.find(Student.class,userId);
        User a=new User();
        a.setName("Tom");
        a.setSurname("Batto");
        a.setEmail("tombatto@gmail.com");
        a.setBirthday(new Date(500));
        a.setGender("male");
        Long b = new Long(10);
        a.setId(b);
        a.setPassword("alabama");
        Student student=new Student(a.getId(),a);
        JsonNode json= Json.toJson(student);
        System.out.println(json);
        return ok(json);
    }

}
