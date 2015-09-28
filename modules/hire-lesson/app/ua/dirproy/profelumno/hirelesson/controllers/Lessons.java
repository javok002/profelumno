package ua.dirproy.profelumno.hirelesson.controllers;

import play.Application;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.hirelesson.models.Lesson;
import ua.dirproy.profelumno.hirelesson.views.html.hire;

/**
 * Created by Paca on 9/13/15. Oh yeah
 */
public class Lessons extends Controller {

    public static Result newLesson() {
        Form<Lesson> lessonsForm = Form.form(Lesson.class).bindFromRequest();

        Lesson lesson = new Lesson();

        final Teacher teacher = Teacher.getTeacher(Long.getLong(lessonsForm.data().get("teacherId")));
        lesson.setTeacher(teacher);

        final Student student = Student.getStudent(Long.parseLong(session("id")));
        lesson.setStudent(student);


        String address;
        switch (lessonsForm.data().get("address")) {
            case "student" :
                address = student.getUser().getAddress();
                break;
            case "teacher" :
                address = teacher.getUser().getAddress();
                break;
            default:
                address = "unknow";
                break;
        }
        lesson.setAddress(address);

        lesson.setComment(lessonsForm.data().get("comment"));

        lesson.save();

        return ok(); //todo redireccionar al index
    }

    public static Result redirect() {
        return ok(hire.render());
    }
}
