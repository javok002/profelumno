package ua.dirproy.profelumno.teacherprofile.controllers;

import authenticate.Authenticate;
import play.libs.*;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Lesson;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.mailsender.models.MailSenderUtil;
import ua.dirproy.profelumno.teacherprofile.views.html.acceptLesson;

import javax.mail.*;
import java.util.List;

/**
 * Created by facundo on 28/9/15.
 */
@Authenticate({Teacher.class})
public class AcceptLesson extends Controller {
    public static Result acceptView() {
        return ok(acceptLesson.render());
    }

    public static Result getLessons() {
        Teacher teacher = Teacher.finder.where().eq("USER_ID", Long.parseLong(session("id"))).findUnique();
        List<Lesson> lessons = Lesson.finder.where().eq("TEACHER_ID", teacher.getId()).findList();
        for (Lesson lesson : lessons) {
            System.out.println(lesson.toString());
        }
        return ok(Json.toJson(lessons));
    }

    public static Result decision(String answer, String stringLessonId) throws MessagingException {
        boolean answerBool;
        System.out.println(answer);
        switch (answer) {
            case "true":
                answerBool = true;
                break;
            default:
                answerBool = false;
        }
        boolean action = updateLesson(answerBool, Long.parseLong(stringLessonId));
        notifyStudent(Long.parseLong(stringLessonId));
        return action? ok(): badRequest();
    }

    private static boolean updateLesson(boolean answerBool, long lessonId) {
        try {
            Lesson lesson = Lesson.finder.byId(lessonId);
            lesson.setLessonState(answerBool ? 1 : 2);
            lesson.update();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void notifyStudent(long lessonId) throws MessagingException {
        Lesson lesson = Lesson.finder.where().eq("id", lessonId).findUnique();
        Student student = Student.finder.byId(lesson.getStudent().getId());
        Teacher teacher = Teacher.finder.byId(lesson.getTeacher().getId());
        MailSenderUtil.send(new String[]{student.getUser().getEmail()}, "ProfeLumno: Han respondido tu solicitud de clase.", "<body style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;margin: 0;font-family: &quot;Helvetica Neue&quot;,Helvetica,Arial,sans-serif;font-size: 14px;line-height: 1.428571429;color: #333;background-color: #fff;\">\n" +
                "<div style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;\">\n" +
                "    <h1 align=\"center\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;margin: .67em 0;font-size: 36px;font-family: &quot;Helvetica Neue&quot;,Helvetica,Arial,sans-serif;font-weight: 500;line-height: 1.1;color: inherit;margin-top: 20px;margin-bottom: 10px;\"><b style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;font-weight: bold;\">Profe</b><span class=\"thin\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;\">Lumno</span></h1>\n" +
                "    <div class=\"row\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;margin-right: -15px;margin-left: -15px;\">\n" +
                "        <div class=\"col-md-4 col-sm-3\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;position: relative;min-height: 1px;padding-right: 15px;padding-left: 15px;float: left;width: 33.33333333333333%;\"></div>\n" +
                "        <div class=\"col-md-4 col-sm-6\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;position: relative;min-height: 1px;padding-right: 15px;padding-left: 15px;float: left;width: 33.33333333333333%;\">\n" +
                "            <div class=\"panel panel-primary\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;margin-bottom: 20px;background-color: #fff;border: 1px solid transparent;border-radius: 4px;-webkit-box-shadow: 0 1px 1px rgba(0,0,0,0.05);box-shadow: 0 1px 1px rgba(0,0,0,0.05);border-color: #428bca;\">\n" +
                "                <div class=\"panel-heading\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 10px 15px;border-bottom: 1px solid transparent;border-top-right-radius: 3px;border-top-left-radius: 3px;color: #fff;background-color: #428bca;border-color: #428bca;\">\n" +
                "                    <h3 class=\"panel-title\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;orphans: 3;widows: 3;page-break-after: avoid;font-family: &quot;Helvetica Neue&quot;,Helvetica,Arial,sans-serif;font-weight: 500;line-height: 1.1;color: inherit;margin-top: 0;margin-bottom: 0;font-size: 16px;\">Han respondido a tu solicitud</h3>\n" +
                "                </div>\n" +
                "                <div class=\"panel-body\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 15px;\">\n" +
                "                    "+ teacher.getUser().getName()+" "+teacher.getUser().getSurname()+" ha "+(lesson.getLessonState() == 1?"aceptado":"rechazado")+" tu solicitud de clase. Entra para ver su respuesta!\n" +
                "                </div>\n" +
                "                <div class=\"panel-footer\" align=\"right\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;padding: 10px 15px;background-color: #f5f5f5;border-top: 1px solid #ddd;border-bottom-right-radius: 3px;border-bottom-left-radius: 3px;\"><b style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;font-weight: bold;\">Profe</b><span class=\"thin\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;\">Lumno </span></div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"col-md-4 col-sm-3\" style=\"-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;position: relative;min-height: 1px;padding-right: 15px;padding-left: 15px;float: left;width: 33.33333333333333%;\"></div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>");
    }
}
