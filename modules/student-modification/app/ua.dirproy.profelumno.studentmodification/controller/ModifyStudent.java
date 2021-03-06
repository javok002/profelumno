package ua.dirproy.profelumno.studentmodification.controller;

import authenticate.Authenticate;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.io.Files;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.studentmodification.view.html.changepassword;
import ua.dirproy.profelumno.studentmodification.view.html.modifystudent;
import ua.dirproy.profelumno.studentprofile.controllers.*;
import ua.dirproy.profelumno.user.models.Subject;
import ua.dirproy.profelumno.user.models.User;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

/**
 * Created by tombatto on 14/09/15.
 */
@Authenticate({Student.class})
public class ModifyStudent extends Controller {

    public static Result modifyStudentView() {

        return ok(modifystudent.render());
    }

    public static Result changePasswordView() {

        return ok(changepassword.render());
    }

    public static Result getStudent() {
        final Long userId = Long.parseLong(session().get("id"));
        User user = User.finder.byId(userId);
        Student student = Student.finder.where().eq("user", user).findUnique();
        JsonNode json = Json.toJson(student);
        return ok(json);
    }

    //si no hay ninguna materia carga 3 materias
    public static Result getSubjects() {
        if (Subject.noSubjects()) {
            Subject matematica = new Subject("Matematica");
            Subject quimica = new Subject("Quimica");
            Subject fisica = new Subject("Fisica");
            matematica.save();
            quimica.save();
            fisica.save();
        }

        List<Subject> subjects = Ebean.find(Subject.class).findList();
        JsonNode json = Json.toJson(subjects);
        return ok(json);
    }


    public static Result saveStudent() {
        Form<Student> form = Form.form(Student.class).bindFromRequest();
        if (form.hasErrors()) {
//            return badRequest(register.render());
            return badRequest("Error in form");
        }
        Student stu = form.get();
        User user = stu.getUser();
        Student student = Student.finder.where().eq("user", user).findUnique();
        System.out.println(stu.getUser().getEmail() + "=============" + student.getUser().getEmail());
        if ((stu.getUser().getEmail()).equalsIgnoreCase(student.getUser().getEmail()) || User.validateEmailUnique(stu.getUser().getEmail())) {
            User studentU = student.getUser();
            User stuU = stu.getUser();
            studentU.setAddress(stuU.getAddress());
            studentU.setBirthday(stuU.getBirthday());
            studentU.setEmail(stuU.getEmail());
            studentU.setGender(stuU.getGender());
            studentU.setName(stuU.getName());
            studentU.setSurname(stuU.getSurname());
            studentU.setLatitude(stuU.getLatitude());
            studentU.setLongitude(stuU.getLongitude());
            studentU.setNeighbourhood(stuU.getNeighbourhood());
            studentU.setCity(stuU.getCity());
            studentU.getSubjects().clear();
            for (Subject subject : stuU.getSubjects()) {
                studentU.getSubjects().add(subject);
            }
            Ebean.save(student);
            Ebean.save(student.getUser());
//                    return ok(Json.toJson(student));
            return ok(ua.dirproy.profelumno.studentprofile.controllers.routes.StudentProfile.dashboard().url());
        } else {
            return badRequest("taken");
        }
    }

    public static Result savePassword() {
        Form<Student> form = Form.form(Student.class).bindFromRequest();
        if (form.hasErrors()) {
//            return badRequest(register.render());
            return badRequest("Error in form");
        }
        Student stu = form.get();
        User user = stu.getUser();
        Student student = Student.finder.where().eq("user", user).findUnique();
        User studentU = student.getUser();
        studentU.setPassword(user.getPassword());
        Ebean.save(student);
        Ebean.save(student.getUser());
//        return ok(Json.toJson(student));
        return ok(ua.dirproy.profelumno.studentprofile.controllers.routes.StudentProfile.dashboard().url());
    }

    public static Result getPicture(){
        final long userId=Long.parseLong(session("id"));
        User user = Ebean.find(User.class, userId);
        Student student =Student.finder.where().eq("user",user).findUnique();
        if(student.getUser().getProfilePicture()==null) return ok("");
        return ok(student.getUser().getProfilePicture());
    }

    public static Result savePicture() {
        final Http.MultipartFormData body = request().body().asMultipartFormData();
        final Http.MultipartFormData.FilePart picture = body.getFile("file");
        if (picture != null) {
            final String fileName = picture.getFilename();
            final String suffix = fileName.substring((fileName.length() - 4));
            if (suffix.equals(".jpg") || suffix.equals("jpeg") || suffix.equals(".png") || suffix.equals(".bmp")) {
                final String contentType = picture.getContentType();
                final File file = picture.getFile();
                if (contentType.contains("image")) {
                    final long userId = Long.parseLong(session("id"));
                    User user = Ebean.find(User.class, userId);
                    Student student = Student.finder.where().eq("user", user).findUnique();
                    byte[] bfile=null;
                    try {
                        bfile= Files.toByteArray(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    student.getUser().setProfilePicture(Base64.getEncoder().encode(bfile));
                    Ebean.save(student.getUser());
                    Ebean.save(student);
                    return ok(student.getUser().getProfilePicture());
                }
            }
        }
        return badRequest();
    }

}
