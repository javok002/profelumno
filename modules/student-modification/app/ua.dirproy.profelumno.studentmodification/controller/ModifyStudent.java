package ua.dirproy.profelumno.studentmodification.controller;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Student;
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
        Student student = Ebean.find(Student.class,1);
//        User a=new User();
//        a.setName("Tom");
//        a.setSurname("Batto");
//        a.setEmail("tombatto@gmail.com");
//        a.setBirthday(new Date(500));
//        a.setGender("male");
//        a.setPassword("alabama");
//        a.setAddress("Palermo, Autonomous City of Buenos Aires, Argentina");
//        Student student=new Student();
//        student.setUser(a);
//        a.save();
//        student.save();
        JsonNode json= Json.toJson(student);
        System.out.println(json);
        return ok(json);
    }

    public static Result saveStudent(){
        Form<Student> form = Form.form(Student.class).bindFromRequest();
        if (form.hasErrors()) {
//            return badRequest(register.render());
            return badRequest("Error in form");
        }
        Student stu = form.get();
        Student student = Ebean.find(Student.class,stu.getUser().getId());
        if ((stu.getUser().getEmail()).equalsIgnoreCase(student.getUser().getEmail())||
                User.validateEmailUnique(stu.getUser().getEmail())) {
                    student.setProfilePicture(stu.getProfilePicture());
                    User studentU=student.getUser();
                    User stuU=stu.getUser();
                    studentU.setAddress(stuU.getAddress());
                    studentU.setBirthday(stuU.getBirthday());
                    studentU.setEmail(stuU.getEmail());
                    studentU.setGender(stuU.getGender());
                    studentU.setName(stuU.getName());
                    studentU.setPassword(stuU.getPassword());
                    studentU.setSurname(stuU.getSurname());
                    Ebean.save(student);
                    Ebean.save(student.getUser());
                    return ok(Json.toJson(student));
        }else {
            return badRequest("Unique");
        }
    }

}
