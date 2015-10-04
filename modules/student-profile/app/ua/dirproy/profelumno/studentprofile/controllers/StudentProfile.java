package ua.dirproy.profelumno.studentprofile.controllers;

import authenticate.Authenticate;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Lesson;
import ua.dirproy.profelumno.common.models.LessonComparator;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.user.models.Subject;

import java.util.*;

@Authenticate({Student.class})
public class StudentProfile extends Controller {

    public static Result dashboard(){
        return ok(ua.dirproy.profelumno.studentprofile.views.html.dashboard.render());
    }

    public static Result getAllInfo(){
        final long userId = Long.parseLong(session("id"));
        final Student student = Student.getStudent(userId);

        //todas las materias del alumno
        final List<Subject> subjects = student.getUser().getSubjects();

        //materias ordenadas segun fecha para el calendario
        final List<Lesson> list = Lesson.list();
        final List<Lesson> lastLessonsStudent = new ArrayList<>();
        for (Lesson lesson : list) {
            if (Objects.equals(lesson.getStudent().getId(), student.getId())){
                lastLessonsStudent.add(lesson);
            }
        }
        lastLessonsStudent.sort(new LessonComparator());

        //ultimos profesores con los que tuve contacto
        final HashSet<Teacher> lastTeachers = new HashSet<>();
        for (Lesson lesson : lastLessonsStudent) {
            lastTeachers.add(lesson.getTeacher());
        }

        //map que voy a devolver
        Map<String, Collection> answer = new HashMap<>();
        answer.put("Subjects", subjects);
        answer.put("Lessons", lastLessonsStudent);
        answer.put("Teachers", lastTeachers);
        return ok(Json.toJson(answer));
    }
}
