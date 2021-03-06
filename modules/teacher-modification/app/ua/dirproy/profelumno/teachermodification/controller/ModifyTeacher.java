package ua.dirproy.profelumno.teachermodification.controller;

import authenticate.Authenticate;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.io.Files;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.teachermodification.view.html.changepassword;
import ua.dirproy.profelumno.teachermodification.view.html.teacherprofile;
import ua.dirproy.profelumno.user.models.Subject;
import ua.dirproy.profelumno.user.models.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * Created by francisco on 13/09/15.
 */
@Authenticate({Teacher.class})
public class ModifyTeacher extends Controller {

    public static Result profileView() {
        return ok(teacherprofile.render());
    }

    public static Result getTeacher(){
        final long userId=Long.parseLong(session("id"));
        User user = Ebean.find(User.class, userId);
        Teacher teacher =Teacher.finder.where().eq("user",user).findUnique();
        JsonNode json= Json.toJson(teacher);
        return ok(json);
    }

    public static Result changePasswordView() {
        return ok(changepassword.render());
    }
    public static Result saveTeacherInfo(String name, String surname, String email, String birthday, String address, String gender, String price, String homeClasses,
                                         String description, String latitude, String longitude, String city, String neighbourhood, String renewalDate, String lastLogin){
        final long userId=Long.parseLong(session("id"));
        User user = Ebean.find(User.class, userId);
        Teacher teacher = Teacher.finder.where().eq("user", user).findUnique();

        if ((email).equalsIgnoreCase(teacher.getUser().getEmail())||
                User.validateEmailUnique(email)) {
            User teacherU=teacher.getUser();
            teacherU.setAddress(address);
            teacherU.setBirthday(new Date(birthday));
            teacherU.setEmail(email);
            teacherU.setGender(gender);
            teacherU.setName(name);
            teacherU.setPassword(teacher.getUser().getPassword());
            teacherU.setSurname(surname);
            teacher.setPrice(Double.parseDouble(price));
            teacher.setHomeClasses(Boolean.parseBoolean(homeClasses));
            teacherU.setLatitude(Double.parseDouble(latitude));
            teacherU.setLongitude(Double.parseDouble(longitude));
            teacherU.setCity(city);
            teacherU.setNeighbourhood(neighbourhood);
            teacher.setDescription(description);
            Ebean.save(teacher.getUser());
            Ebean.save(teacher);

            return ok(routes.ModifyTeacher.profileView().url()) /*ok(Json.toJson(teacher))*/;
        }else {
            return badRequest("Unique");
        }
    }

    public static Result getSubject(){
        List<Subject> subjects=Ebean.find(Subject.class).findList();
        JsonNode json=Json.toJson(subjects);
        return ok(json);
    }

    public static Result saveSubject(){
        Form<Teacher> form = Form.form(Teacher.class).bindFromRequest();
        if (form.hasErrors())
            return badRequest("Error in form");
//        Teacher aux=form.get();
        final long userId = Long.parseLong(session("id"));
        User user = Ebean.find(User.class, userId);
        Teacher teacher = Teacher.finder.where().eq("user", user).findUnique();
        List<Subject> currentSubjects = new ArrayList<>(form.data().size());
        //Creo las materias que no existen
        for (String currentSubject : form.data().values()) {
            currentSubject = currentSubject.replace("-", " ");
            if(!existsSubject(currentSubject, currentSubjects)){
                Subject newSubject = new Subject(currentSubject);
                Ebean.save(newSubject);
                currentSubjects.add(newSubject);
            }
        }

        //Saco del teacher las materias que elimino

        final List<Subject> removedSubjects = new ArrayList<>();
        for (Subject subject : teacher.getUser().getSubjects()) {
            if(!currentSubjects.contains(subject)){
                subject.getUsers().remove(teacher.getUser());
                removedSubjects.add(subject);
            }
        }
        teacher.getUser().getSubjects().removeAll(removedSubjects);
        //Le agrego al teacher las nuevas materias que dicta
        for (Subject currentSubject : currentSubjects) {
            if(!teacher.getUser().getSubjects().contains(currentSubject)){
                teacher.getUser().getSubjects().add(currentSubject);
                currentSubject.getUsers().add(teacher.getUser());
            }
        }

        //teacher.setSubjects(aux.getSubjects());
        Ebean.update(teacher);
        Ebean.update(teacher.getUser());
        return ok();
    }

    private static boolean existsSubject(String currentSubject, List<Subject> listToAddSubject) {
        List<Subject> persistedSubjects = Subject.finder.findList();
        for (Subject persistedSubject : persistedSubjects) {
            if (persistedSubject.getName().equals(currentSubject)) {
                listToAddSubject.add(persistedSubject);
                return true;
            }
        }
        return false;
    }

    public static Result getPicture(){
        final long userId=Long.parseLong(session("id"));
        User user = Ebean.find(User.class, userId);
        Teacher teacher =Teacher.finder.where().eq("user",user).findUnique();
        return ok(teacher.getUser().getProfilePicture());
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
                    Teacher teacher =Teacher.finder.where().eq("user",user).findUnique();
                    byte[] bfile=null;
                    try {
                        bfile=Files.toByteArray(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    teacher.getUser().setProfilePicture(Base64.getEncoder().encode(bfile));
                    Ebean.save(teacher.getUser());
                    Ebean.save(teacher);
                    return ok(teacher.getUser().getProfilePicture());
                }
            }
        }
        return badRequest();
    }

    public static Result savePassword(String password) {
        //Form<Teacher> form = Form.form(Teacher.class).bindFromRequest();
        //if (form.hasErrors()) {
//            return badRequest(register.render());
        //    return badRequest(form.errors().toString());
        //}
        //Teacher tch = form.get();
        final long userId=Long.parseLong(session("id"));
        User user = Ebean.find(User.class, userId);
        Teacher teacher = Teacher.finder.where().eq("user", user).findUnique();


        User studentU = teacher.getUser();
        studentU.setPassword(password);
        Ebean.save(teacher);
        Ebean.save(teacher.getUser());
//        return ok(Json.toJson(student));
        return ok(routes.ModifyTeacher.profileView().url());    }
}
