package ua.dirproy.profelumno.teachermodification.controller;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import org.jboss.netty.handler.codec.http.HttpRequest;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import sun.text.resources.FormatData;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
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
        a.setBirthday(new Date());
        a.setGender("male");
        a.setAddress("La Ramona 1254");
        Long b = new Long(13);
        a.setId(b);
        a.setPassword("12345678");
        Teacher teacher=new Teacher(a.getId(),"Subjects to ecstatic children he. Could ye leave up as built match. Dejection agreeable attention set suspected led offending. Admitting an performed supposing by. Garden agreed matter are should formed temper had. Full held gay now roof whom such next was. Ham pretty our people moment put excuse narrow. Spite mirth money six above get going great own. Started now shortly had for assured hearing expense. Led juvenile his laughing speedily put pleasant relation offering.",true, a);
        JsonNode json= Json.toJson(teacher);
        return ok(json);
//        return ok(teacherJson);
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
            return ok(Json.toJson(teacher));
        }else {
            return badRequest("Unique");
        }
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
