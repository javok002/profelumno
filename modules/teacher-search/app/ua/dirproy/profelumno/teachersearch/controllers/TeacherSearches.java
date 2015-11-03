package ua.dirproy.profelumno.teachersearch.controllers;

import authenticate.Authenticate;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.user.models.Subject;
import ua.dirproy.profelumno.user.models.User;


import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static play.libs.Json.toJson;
import static ua.dirproy.profelumno.common.models.Teacher.updateLessonsDictated;

/**
 * Created by Nash
 * Date: 9/13/15
 * Project profelumno
 */
@Authenticate({Student.class})
public class TeacherSearches extends Controller {

    public static Result teacherSearchView() {
        return ok(ua.dirproy.profelumno.teachersearch.views.html.teachersearch.render());
    }

    public static Result getTeachers() {
        List<Teacher> allTeachers = Teacher.list();
        for (Teacher teacher : allTeachers) {
            Teacher.updateLessonsDictated(teacher);
            Teacher.updateRating(teacher);
        }
        allTeachers = Teacher.list();
        final List<Teacher> teachersToShow = Teacher.list();
        Form<Teacher> form = Form.form(Teacher.class).bindFromRequest();
        List<String> subjects = new ArrayList<>(form.data().size() - 3);
        int index = form.data().size() - 4;
        while (index >= 0) {
            subjects.add(form.data().get("subjects[" + index + "]").replace("-"," "));
            index--;
        }
        allTeachers.stream().filter(teacher -> checkLessons(Integer.parseInt(form.data().get("lessons")), teacher) ||
                checkRanking(Integer.parseInt(form.data().get("ranking")), teacher) ||
                checkHome(Boolean.parseBoolean(form.data().get("homeClasses")), teacher) ||
                checkSubjects(subjects, teacher)).forEach(teachersToShow::remove);
        final long userId = Long.parseLong(session("id"));
        User user = Ebean.find(User.class, userId);
        ArrayList<Object> toReturn = new ArrayList<>(2);
        toReturn.add(teachersToShow);
        toReturn.add(user.getAddress());
//        toReturn.add(user.getLongitude());

        return ok(toJson(toReturn));
    }

    private static boolean checkSubjects(List<String> subjects, Teacher teacher) {
        List<String> currentSubjects = new ArrayList<>();
        for (Subject subject : teacher.getUser().getSubjects()) {
            currentSubjects.add(subject.getName());
        }
        return !currentSubjects.containsAll(subjects);
    }

    private static boolean checkRanking(Integer ranking, Teacher teacher) {
        return teacher.getRanking() < ranking;
    }

    private static boolean checkLessons(Integer lessons, Teacher teacher) {
//        updateLessonsDictated(teacher); //comment this line if group0 added it on global.
        return teacher.getLessonsDictated() < lessons;
    }

    public static Result getSubject() {
        List<Subject> subjects = Ebean.find(Subject.class).findList();
        JsonNode json = Json.toJson(subjects);
        return ok(json);
    }

    private static boolean checkHome(Boolean home, Teacher teacher) {
        return home && !teacher.getHomeClasses();
    }

    public static void orderByDistance(List<User> users, User user){
        users.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if(o1.getLatitude() == null || o1.getLongitude() == null) return -1;
                else if(o2.getLatitude() == null || o2.getLongitude() == null) return 1;
                return (int) (getDistance(user, o1) - getDistance(user, o2));
            }
        });
    }

    private static double getDistance(User user1, User user2){
        double distance = 0;
        try{
            double x = Double.parseDouble(user1.getLatitude()) - Double.parseDouble(user2.getLatitude());
            double y = Double.parseDouble(user2.getLongitude()) - Double.parseDouble(user2.getLongitude());
            distance = Math.hypot(x,y);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        return distance;
    }

}
