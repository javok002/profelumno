package ua.dirproy.profelumno.teacherprofile.controllers;

import authenticate.Authenticate;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
                if (subject != null && lesson != null && subject.equals(lesson.getSubject())){
                    if(lesson.getStudentReview()!=null) {
                        listLong.add(lesson.getStudentReview().getStars());
                    }
                }
            }
            subjectList.put(subject,listLong);
        }

        Map<String, Long> result;

        if ((subjectList.size() == 1) && (subjectList.get(subjects.get(0)).isEmpty())){
            result = new HashMap<>();
        }else {
            result = mapProm(subjectList, subjects);
        }

        return ok(Json.toJson(result));
    }

    private static Map<String, Long> mapProm(Map<Subject,List<Long>> subjectListLong,List<Subject> subjects) {
        Map<String, Long> bestSubjects = new HashMap<>();
        Map<String, Long> aux = new HashMap<>();
        List<Long> listProm = new ArrayList<>();
        for (int i = 0; i <subjectListLong.size() ; i++) {
            aux.put( subjects.get(i).getName(),getProm(subjectListLong.get(subjects.get(i))));
            listProm.add(getProm(subjectListLong.get(subjects.get(i))));
        }

        Collections.sort(listProm);

        for (int j = listProm.size() -1; j > listProm.size() -4 && j >= 0 ; j--) {
            float prom = listProm.get(j);
            for (int k = 0; k <subjects.size() ; k++) {
                String subject = subjects.get(k).getName();
                long value = aux.get(subject);
                if ( bestSubjects.size() < 3) {
                    if (value >= prom) {
                        bestSubjects.put(subject, value);
                    }
                }
                else {
                    break;
                }
            }
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
            if (nextLessons.size() <= 6) {
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
        final Long userId = Long.parseLong(session().get("id"));
        Iterator<Lesson> previousLessons = getPreviousLessons(userId);

        final ArrayNode results = Json.newArray();
        while (previousLessons.hasNext()){
            final Lesson temp = previousLessons.next();
            final ObjectNode node = Json.newObject();

            node.put("date", temp.getDateTime().getDate() + "/"
                    + (temp.getDateTime().getMonth() + 1) + "/"
                    + (temp.getDateTime().getYear() + 1900));

            if (temp.getSubject() == null){
                node.put("subject", "Clase");
            } else {
                node.put("subject", temp.getSubject().getName());
            }
            node.put("studentName", temp.getStudent().getUser().getName() + " "
                    + temp.getStudent().getUser().getSurname());
            node.put("studentEmail", temp.getStudent().getUser().getEmail());
            node.put("idLesson", temp.getId());
            node.put("review", temp.getStudentReview() == null);
            results.add(node);
        }
        return ok(results);
    }

    private static Iterator<Lesson> getPreviousLessons(Long userId){
        final Teacher teacher = Teacher.finder.where().eq("user.id", userId).findUnique();
        return Lesson.finder.where().eq("teacher", teacher).lt("dateTime", new Date()).findList().iterator();
    }

}
