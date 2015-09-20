package ua.dirproy.profelumno.teachermodification.controller;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import org.jboss.netty.handler.codec.http.HttpRequest;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import sun.text.resources.FormatData;
import ua.dirproy.profelumno.register.models.Teacher;
import ua.dirproy.profelumno.teachermodification.view.html.*;
import ua.dirproy.profelumno.user.models.User;

import java.util.Date;

/**
 * Created by francisco on 13/09/15.
 */
public class ModifyTeacher extends Controller {

    public static Result profileView() {
        return ok(teacherprofile.render());
    }

    public static Result getTeacher(){
        /*final long userId=Long.parseLong(session("id"));
        Teacher teacher = Ebean.find(Teacher.class, userId);
        JsonNode teacherJson= Json.toJson(teacher);*/
        User a=new User();
        a.setName("Nicolas");
        a.setSurname("Rudolph");
        a.setEmail("nrudolph@gmail.com");
        a.setBirthday(new Date(500));
        a.setGender("male");
        Long b = new Long(13);
        a.setId(b);
        a.setPassword("12345678");
        Teacher teacher=new Teacher(a.getId(),"c=Como minimo 50 caracteres","yes", a);
        JsonNode json= Json.toJson(teacher);
        System.out.println(json);
        return ok(json);
//        return ok(teacherJson);
    }

    public static Result saveTeacherInfo(){
        final long userId=Long.parseLong(session("id"));
        Teacher teacher = Ebean.find(Teacher.class, userId);
        return ok();
    }

    public static Result saveSubject(){
        final long userId=Long.parseLong(session("id"));
        Teacher teacher = Ebean.find(Teacher.class, userId);

        return ok();
    }

    public static Result emailExist(String email){
        User user=(User) User.finder.where().eq("email", email);
        if (user == null)
            return ok();
        else
            return notFound("Email already in use");
    }

    public static Result getPicture(){
        final long userId=Long.parseLong(session("id"));
        Teacher teacher = Ebean.find(Teacher.class, userId);
        return status(200, teacher.getProfilePicture());
    }
    public static Result savePicture(byte[] image){
        final long userId=Long.parseLong(session("id"));
        Teacher teacher = Ebean.find(Teacher.class, userId);
        teacher.setProfilePicture(image);
        Ebean.update(teacher);
        return status(200, image);
    }
}
