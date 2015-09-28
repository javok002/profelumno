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
import ua.dirproy.profelumno.user.models.Subject;
import ua.dirproy.profelumno.user.models.User;

import java.util.Date;
import java.util.List;

/**
 * Created by tombatto on 14/09/15.
 */
public class ModifyStudent extends Controller {

    public static Result modifyStudentView() {

        return ok(modifystudent.render());
    }

    public static Result changePasswordView() {

        return ok(changepassword.render());
    }

    public static Result getStudent(){
        final Long userId = Long.parseLong(session().get("id"));
        User user=User.finder.byId(userId);
        Student student = Student.finder.where().eq("user",user).findUnique();
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
        return ok(json);
    }

    public static Result getSubjects(){
        List<Subject>subjects=Ebean.find(Subject.class).findList();
        JsonNode json=Json.toJson(subjects);
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
                    studentU.setSubjects(stuU.getSubjects());
                    Ebean.save(student);
                    Ebean.save(student.getUser());
                    return ok(Json.toJson(student));
        }else {
            return badRequest("Unique");
        }
    }

    public static Result savePassword(){
        Form<Student> form = Form.form(Student.class).bindFromRequest();
        if (form.hasErrors()) {
//            return badRequest(register.render());
            return badRequest("Error in form");
        }
        Student stu = form.get();
        Student student = Ebean.find(Student.class,stu.getUser().getId());
        if (User.validateEmailUnique(stu.getUser().getEmail())) {
            User studentU=student.getUser();
            User stuU=stu.getUser();
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
