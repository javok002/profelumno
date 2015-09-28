package ua.dirproy.profelumno.teachermodification.controller;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import org.jboss.netty.handler.codec.http.HttpRequest;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import scala.util.parsing.json.JSONArray$;
import sun.text.resources.FormatData;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.teachermodification.view.html.*;
import ua.dirproy.profelumno.user.models.Subject;
import ua.dirproy.profelumno.user.models.User;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by francisco on 13/09/15.
 */
public class ModifyTeacher extends Controller {

    public static Result profileView() {
        return ok(teacherprofile.render());
    }

    public static Result getTeacher(){
        /*final long userId=Long.parseLong(session("id"));
        Teacher teacher = Ebean.find(Teacher.class, userId);*/
        //usuario de prueba
        Teacher teacher = new Teacher();
        User user= new User();
        user.setName("Nicolas");
        user.setSurname("Rudolph");
        user.setId(new Long(1));
        user.setAddress("Pilar, Buenos Aires Province, Argentina");
        user.setEmail("n@n");
        user.setGender("male");
        user.setPassword("123456");
        List<Subject> subjects=new ArrayList<>();
        subjects.add(new Subject("Matematica"));
        subjects.add(new Subject("Fisica"));
        //user.setSubjects(subjects);   <- Throws null point exception
        try{
        user.setBirthday(new SimpleDateFormat("dd/MM/yy").parse("6/8/93"));
        }catch (ParseException E){
            System.out.println(E.getMessage());
        }
        teacher.setUser(user);
        teacher.setDescription("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
        teacher.setHomeClasses(true);

        JsonNode json= Json.toJson(teacher);
        System.out.println(json);
        return ok(json);
    }

    public static Result saveTeacherInfo(){
        Form<Teacher> form = Form.form(Teacher.class).bindFromRequest();
        if (form.hasErrors()) {
//            return badRequest(register.render());
            return badRequest("Error in form");
        }
        Teacher tch = form.get();
        Teacher teacher = Ebean.find(Teacher.class, tch.getUser().getId());
        if ((tch.getUser().getEmail()).equalsIgnoreCase(teacher.getUser().getEmail())||
                User.validateEmailUnique(tch.getUser().getEmail())) {
            teacher.setProfilePicture(tch.getProfilePicture());
            User teacherU=teacher.getUser();
            User tchU=tch.getUser();
            teacherU.setAddress(tchU.getAddress());
            teacherU.setBirthday(tchU.getBirthday());
            teacherU.setEmail(tchU.getEmail());
            teacherU.setGender(tchU.getGender());
            teacherU.setName(tchU.getName());
            teacherU.setPassword(tchU.getPassword());
            teacherU.setSurname(tchU.getSurname());
            Ebean.save(teacher);
            Ebean.save(teacher.getUser());
            System.out.println(Teacher.list().get(0).getUser().getName());
            System.out.println("WAAAAAAAAATSSSSSSSSAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP");

            return ok(Json.toJson(teacher));
        }else {
            return badRequest("Unique");
        }
    }

    public static Result saveSubject(){
        Form<Teacher> form = Form.form(Teacher.class).bindFromRequest();
        if (form.hasErrors())
            return badRequest("Error in form");
        Teacher aux=form.get();
        final long userId=Long.parseLong(session("id"));
        Teacher teacher = Ebean.find(Teacher.class, userId);
        //teacher.setSubjects(aux.getSubjects());
        Ebean.save(teacher);
        return ok();
    }
    public static Result savePicture() {
       final Http.MultipartFormData body = request().body().asMultipartFormData();
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
                    Teacher teacher = Ebean.find(Teacher.class, userId);
                    teacher.setProfilePicture(file.toString().getBytes());
                    Ebean.update(teacher);
                    return ok(file);
                }
            }
        }
        return ok("yfvygfvyg");
    }
}
