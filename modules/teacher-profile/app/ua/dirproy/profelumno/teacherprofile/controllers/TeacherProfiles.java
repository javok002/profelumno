package ua.dirproy.profelumno.teacherprofile.controllers;

import authenticate.Authenticate;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Lesson;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.teacherprofile.views.html.teacherProfile;
import ua.dirproy.profelumno.user.models.Subject;

import java.util.*;

@Authenticate({Teacher.class})
public class TeacherProfiles extends Controller {

    public static Result teacherProfileView() {
        return ok(teacherProfile.render());
    }

    private static long idTeacher() {
        String id = session("id");
        Long parseId = Long.parseLong(id);
        List<Teacher> teachers = Teacher.list();
        for (int i = 0; i < teachers.size(); i++) {
            Teacher aux = teachers.get(i);
            if (aux.getUser().getId().equals(parseId)) {
                return aux.getId();
            }
        }
        return -1;
    }

    private static List<Lesson> myLessons(){
        Teacher teacher = Teacher.getTeacher(idTeacher());
        List<Lesson> lessons = Lesson.list();
        List<Lesson> myLessons = new ArrayList<>();
        for (int i = 0; i <lessons.size() ; i++) {
            Lesson aux = lessons.get(i);
            if (aux.getTeacher().getId().equals(teacher.getId())){
                myLessons.add(aux);
            }
        }
        return myLessons;
    }

    public static Result getTeacher() {
        return ok(Json.toJson(Teacher.getTeacher(idTeacher())));
    }

    public static Result getRanking(){
        Teacher teacher = Teacher.getTeacher(idTeacher());
        float ranking = teacher.getRanking();
        return ok(Json.toJson(ranking));
    }

    public static Result getBestSubjects(){

        Map<Subject,List<Long>> subjectList = new HashMap<>();
        List<Lesson> lessons = myLessons();
        List<Subject> subjects = new ArrayList<>();

        for (int j = 0; j <lessons.size() ; j++) {
            Subject aux = lessons.get(j).getSubject();
            if (!subjects.contains(aux)){
                subjects.add(aux);
            }
        }

        for (int k = 0; k <subjects.size() ; k++) {
            List<Long> listLong = new ArrayList<>();
            Subject subject = subjects.get(k);
            for (int p = 0; p <lessons.size() ; p++) {
                Lesson lesson = lessons.get(p);
                if (subject.equals(lesson.getSubject())){
                    listLong.add(lesson.getStudentReview().getStars());
                }
            }
            subjectList.put(subject,listLong);
        }

        return ok(Json.toJson(mapProm(subjectList,subjects)));
    }

    private static Map<String, Long> mapProm(Map<Subject,List<Long>> subjectListLong,List<Subject> subjects) {
        Map<String, Long> bestSubjects = new HashMap<>();
        Map<Long, String> aux = new HashMap<>();
        List<Long> listProm = new ArrayList<>();
        for (int i = 0; i <subjectListLong.size() ; i++) {
            aux.put(getProm(subjectListLong.get(subjects.get(i))), subjects.get(i).getName());
            listProm.add(getProm(subjectListLong.get(subjects.get(i))));
        }

        Collections.sort(listProm);

        for (long j = listProm.size() -1; j > listProm.size() -4 ; j--) {
            bestSubjects.put(aux.get(j),j);
        }

        return bestSubjects;

    }

    private static Long getProm(List<Long> list){
        long prom = 0;
        for (Long aux : list) {
            prom += aux;
        }
        prom /= list.size();
        return prom;
    }

    public static Result getNextLessons(){
        List<Lesson> lessons = myLessons();
        List<Lesson> nextLessons = new ArrayList<>();
        Date date = new Date();
        for (int i = 0; i <lessons.size() ; i++) {
            if (nextLessons.size() <= 5) {
                Lesson aux = lessons.get(i);
                if (aux.getDateTime().after(date)) {
                    nextLessons.add(aux);
                }
            }
            else {
                break;
            }
        }
        return ok(Json.toJson(nextLessons));
    }

    public static Result getPreviousLessons(){
        List<Lesson> lessons = myLessons();
        List<Lesson> previousLessons = new ArrayList<>();
        Date date = new Date();
        for (int i = 0; i <lessons.size() ; i++) {
            if (previousLessons.size() <= 5) {
                Lesson aux = lessons.get(i);
                if (aux.getDateTime().before(date)){
                previousLessons.add(aux);
                 }
            }
            else {
                break;
            }
        }
        return ok(Json.toJson(previousLessons));
    }


}
