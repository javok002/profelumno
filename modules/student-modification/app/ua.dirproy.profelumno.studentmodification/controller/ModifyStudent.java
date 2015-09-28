package ua.dirproy.profelumno.studentmodification.controller;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.studentmodification.view.html.*;
import ua.dirproy.profelumno.user.models.Subject;
import ua.dirproy.profelumno.user.models.User;

import java.io.File;
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
        JsonNode json= Json.toJson(student);
        return ok(json);
    }

    //Hay que precargar las materias en la base de datos de alguna manera, por ahora las cargo aca.
    public static Result getSubjects(){
        Subject matematica=new Subject("Matematica");
        Subject quimica=new Subject("Quimica");
        Subject fisica=new Subject("Fisica");
        matematica.save();
        quimica.save();
        fisica.save();

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
        User user=stu.getUser();
        Student student = Student.finder.where().eq("user",user).findUnique();
        if ((stu.getUser().getEmail()).equalsIgnoreCase(student.getUser().getEmail())||
                User.validateEmailUnique(stu.getUser().getEmail())) {
                    User studentU=student.getUser();
                    User stuU=stu.getUser();
                    studentU.setAddress(stuU.getAddress());
                    studentU.setBirthday(stuU.getBirthday());
                    studentU.setEmail(stuU.getEmail());
                    studentU.setGender(stuU.getGender());
                    studentU.setName(stuU.getName());
                    studentU.setSurname(stuU.getSurname());
                    studentU.getSubjects().clear();
            for (Subject subject : stuU.getSubjects()) {
                studentU.getSubjects().add(subject);
            }
                    Ebean.save(student);
                    Ebean.save(student.getUser());
                    return ok(Json.toJson(student));
        }else {
            return badRequest("taken");
        }
    }

    public static Result savePassword(){
        Form<Student> form = Form.form(Student.class).bindFromRequest();
        if (form.hasErrors()) {
//            return badRequest(register.render());
            return badRequest("Error in form");
        }
        Student stu = form.get();
        User user=stu.getUser();
        Student student = Student.finder.where().eq("user",user).findUnique();
        User studentU=student.getUser();
        studentU.setPassword(user.getPassword());
        Ebean.save(student);
        Ebean.save(student.getUser());
        return ok(Json.toJson(student));
    }

    public static Result savePicture() {
        /*final Http.MultipartFormData body = request().body().asMultipartFormData();
        final Http.MultipartFormData.FilePart picture = body.getFile("fileInput");
        if (picture != null) {
            final String fileName = picture.getFilename();
            final String suffix = fileName.substring((fileName.length() - 4));
            System.out.println("suffix = " + suffix);
            if (suffix.equals(".jpg") || suffix.equals("jpeg") || suffix.equals(".png") || suffix.equals(".bmp")) {
                final String contentType = picture.getContentType();
                final File file = picture.getFile();
                if (contentType.contains("image")) {
                    final long userId = Long.parseLong(session("id"));
                    User user=User.finder.byId(userId);
                    Student student = Student.finder.where().eq("user",user).findUnique();
                    User studentU=student.getUser();
                    student.setProfilePicture(file.toString().getBytes());
                    Ebean.update(student);
                    return ok(file);
                }
            }
        }*/
        return ok("yfvygfvyg");
    }

}
