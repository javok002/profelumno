package ua.dirproy.profelumno.hirelesson.controllers;


import authenticate.Authenticate;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.common.models.Lesson;
import ua.dirproy.profelumno.hirelesson.views.html.hire;
import ua.dirproy.profelumno.mailsender.models.MailSenderUtil;
import ua.dirproy.profelumno.user.models.Subject;
import ua.dirproy.profelumno.user.models.User;

import java.text.ParseException;

import javax.mail.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Paca on 9/13/15. Oh yeah
 */
@Authenticate(Student.class)
public class Lessons extends Controller {

    public static Result newLesson() throws MessagingException, ParseException {
        Form<Lesson> lessonsForm = Form.form(Lesson.class).bindFromRequest();

        Lesson lesson = new Lesson();

        final Teacher teacher = Teacher.getTeacher(Long.parseLong(lessonsForm.data().get("teacherId")));
        lesson.setTeacher(teacher);

        User user = User.finder.byId(Long.parseLong(session("id")));
        Student student = Student.finder.where().eq("user", user).findUnique();
        lesson.setStudent(student);

        final Subject subject = Subject.finder.byId(Long.parseLong(lessonsForm.data().get("subjectId")));
        lesson.setSubject(subject);

        final String dateTime = lessonsForm.data().get("dateTime");
        lesson.setDateTime(new Date(Integer.parseInt(dateTime.substring(0, 4)) - 1900, Integer.parseInt(dateTime.substring(5, 7)) - 1, Integer.parseInt(dateTime.substring(8, 10))));

        String address;
        switch (lessonsForm.data().get("address")) {
            case "student":
                address = student.getUser().getAddress();
                break;
            case "teacher":
                address = teacher.getUser().getAddress();
                break;
            default:
                address = "A confirmar";
                break;
        }
        lesson.setAddress(address);
        lesson.setDateString(dateTime.substring(8, 10) + "/" + dateTime.substring(5, 7) + "/" + dateTime.substring(0, 4));
        lesson.setComment(lessonsForm.data().get("comment"));
        lesson.setTeacherReview(null);
        lesson.setStudentReview(null);
        lesson.setLessonState(0);
        lesson.save();
        notifyTeacher(teacher.getUser().getEmail());
        return ok(); //todo redireccionar al index
    }

    public static Result redirect() {
        return ok(hire.render());
    }

    public static Result getTeacherSubjects(String teacherId) {
        Teacher teacher = Teacher.getTeacher(Long.parseLong(teacherId));
        List<Subject> subjects = teacher.getUser().getSubjects();
        return ok(Json.toJson(subjects));
    }

    private static void notifyTeacher(String emailTeacher) throws MessagingException {
        MailSenderUtil.send(new String[]{emailTeacher}, "ProfeLumno: Usted tiene una nueva clase.", "<body style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;margin: 0;font-family: &quot;Helvetica Neue&quot;,Helvetica,Arial,sans-serif;font-size: 14px;line-height: 1.428571429;color: #333;background-color: #fff;\">\n" +
                "<div style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;\">\n" +
                "    <h1 align=\"center\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;margin: .67em 0;font-size: 36px;font-family: &quot;Helvetica Neue&quot;,Helvetica,Arial,sans-serif;font-weight: 500;line-height: 1.1;color: inherit;margin-top: 20px;margin-bottom: 10px;\"><b style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;font-weight: bold;\">Profe</b><span class=\"thin\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;\">Lumno</span></h1>\n" +
                "    <div class=\"row\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;margin-right: -15px;margin-left: -15px;\">\n" +
                "        <div class=\"col-md-4 col-sm-3\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;position: relative;min-height: 1px;padding-right: 15px;padding-left: 15px;float: left;width: 33.33333333333333%;\"></div>\n" +
                "        <div class=\"col-md-4 col-sm-6\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;position: relative;min-height: 1px;padding-right: 15px;padding-left: 15px;float: left;width: 33.33333333333333%;\">\n" +
                "            <div class=\"panel panel-primary\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;margin-bottom: 20px;background-color: #fff;border: 1px solid transparent;border-radius: 4px;-webkit-box-shadow: 0 1px 1px rgba(0,0,0,0.05);box-shadow: 0 1px 1px rgba(0,0,0,0.05);border-color: #428bca;\">\n" +
                "                <div class=\"panel-heading\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 10px 15px;border-bottom: 1px solid transparent;border-top-right-radius: 3px;border-top-left-radius: 3px;color: #fff;background-color: #428bca;border-color: #428bca;\">\n" +
                "                    <h3 class=\"panel-title\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;orphans: 3;widows: 3;page-break-after: avoid;font-family: &quot;Helvetica Neue&quot;,Helvetica,Arial,sans-serif;font-weight: 500;line-height: 1.1;color: inherit;margin-top: 0;margin-bottom: 0;font-size: 16px;\">Nueva clase</h3>\n" +
                "                </div>\n" +
                "                <div class=\"panel-body\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 15px;\">\n" +
                "                    Usted tiene una nueva clase. Por favor, responda a la solicitud. Gracias!\n" +
                "                </div>\n" +
                "                <div class=\"panel-footer\" align=\"right\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 10px 15px;background-color: #f5f5f5;border-top: 1px solid #ddd;border-bottom-right-radius: 3px;border-bottom-left-radius: 3px;\"><b style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;font-weight: bold;\">Profe</b><span class=\"thin\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;\">Lumno </span></div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"col-md-4 col-sm-3\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;position: relative;min-height: 1px;padding-right: 15px;padding-left: 15px;float: left;width: 33.33333333333333%;\"></div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>");
    }

    private static void createLesson() throws MessagingException {


        Lesson lesson = new Lesson();
        long i = 1;
        final Teacher teacher = Teacher.getTeacher(i);
        lesson.setTeacher(teacher);

        User user = User.finder.byId(Long.parseLong(session("id")));
        Student student = Student.finder.where().eq("user", user).findUnique();
        lesson.setStudent(student);


        lesson.setAddress("pepoito");

        lesson.setComment("prueba");

        lesson.setLessonState(0);

        lesson.save();

        notifyTeacher(teacher.getUser().getEmail());
        System.out.println("se creo");
    }
}
