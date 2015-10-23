package ua.dirproy.profelumno.studentprofile.controllers;

import authenticate.Authenticate;
import org.joda.time.DateTime;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Lesson;
import ua.dirproy.profelumno.common.models.LessonComparator;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.user.models.Subject;
import ua.dirproy.profelumno.user.models.User;

import java.util.*;

@Authenticate({Student.class})
public class StudentProfile extends Controller {

    public static Result dashboard(){
        return ok(ua.dirproy.profelumno.studentprofile.views.html.dashboard.render());
    }

    public static Result getAllInfo(){
        final long userId = Long.parseLong(session("id"));
        final User student = User.getUser(userId);

        //todas las materias del alumno
        final List<Subject> subjects = student.getSubjects();

        //materias ordenadas segun fecha para el calendario
        final List<Lesson> list = Lesson.list();
        final List<Lesson> lastLessonsStudent = new ArrayList<>();
        for (Lesson lesson : list) {
            if (Objects.equals(lesson.getStudent().getUser().getId(), student.getId())){

                lastLessonsStudent.add(lesson);
            }
        }
        lastLessonsStudent.sort(new LessonComparator());

        //ultimos profesores con los que tuve contacto
        final HashSet<Teacher> lastTeachers = new HashSet<>();
        final ArrayList<Subject> teacherSubject = new ArrayList<>();
        for (Lesson lesson : lastLessonsStudent) {
            lastTeachers.add(lesson.getTeacher());
            teacherSubject.add(lesson.getSubject());
        }

        //lessons que no estan con rating
        final ArrayList<Lesson> lessonsWithNoReview = new ArrayList<>();
        for (Lesson lesson : lastLessonsStudent) {
            if (lesson.getStudentReview() == null && lesson.getDateTime().before(DateTime.now().toDate())){
                lessonsWithNoReview.add(lesson);
            }
        }

        //rating del alumno
        final ArrayList<Long> rating = new ArrayList<>();
        rating.add(student.getReviews());

        //map que voy a devolver
        Map<String, Collection> answer = new HashMap<>();
        answer.put("rating", rating);
        answer.put("Subjects", subjects);
        answer.put("Lessons", lastLessonsStudent);
        answer.put("Teachers", lastTeachers);
        answer.put("Teacher's subject", teacherSubject);
        answer.put("LessonsNoRating", lessonsWithNoReview);
        return ok(Json.toJson(answer));
    }
}
