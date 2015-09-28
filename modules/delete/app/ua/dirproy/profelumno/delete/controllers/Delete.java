package ua.dirproy.profelumno.delete.controllers;

import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.delete.views.html.delete;
import ua.dirproy.profelumno.user.models.User;

import java.util.List;


/**
 * Created by Nicolas Burroni
 * Date: 9/12/15
 */
public class Delete extends Controller {

    public static Result deleteView() {
        return ok(delete.render());
    }

    public static Result deleteUser() {
        String id = session("id");
        Long parseId = Long.parseLong(id);
        User user = User.finder.byId(parseId);
        List<Student> students = Student.list();
        boolean deleteUser = false;
        for (int i = 0; i <students.size() ; i++) {
            Student aux = students.get(i);
            if (aux.getUser().getId().equals(parseId)){
                aux.delete();
                deleteUser = true;
            }
        }
        if (!deleteUser) {
            List<Teacher> teachers = Teacher.list();
            for (int i = 0; i < teachers.size(); i++) {
                Teacher aux = teachers.get(i);
                if (aux.getUser().getId().equals(parseId)) {
                    aux.delete();
                }
            }
        }
        user.delete();
        return ok();
    }
}
