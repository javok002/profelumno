package ua.dirproy.profelumno.teacherprofile.controllers;

import authenticate.Authenticate;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Lesson;
import ua.dirproy.profelumno.common.models.LessonComparator;
import ua.dirproy.profelumno.common.models.Review;
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
        Teacher temp = Teacher.getTeacher(idTeacher());
        Teacher.updateRating(temp);

        return ok(Json.toJson(temp));
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
                    if(lesson.getTeacherReview()!=null) {
                        listLong.add(lesson.getTeacherReview().getStars());
                    }
                }
            }
            subjectList.put(subject,listLong);
        }

        ArrayNode result;
        boolean isEmpty = true;

        for (int i = 0; i <subjects.size() ; i++) {
            if (!subjectList.get(subjects.get(i)).isEmpty()){
                isEmpty = false;
            }
        }

        if (isEmpty){
            result = Json.newArray();
        }else {
            result = mapProm(subjectList, subjects);
        }

        return ok(Json.toJson(result));
    }

    private static ArrayNode mapProm(Map<Subject,List<Long>> subjectListLong,List<Subject> subjects) {
        ArrayNode bestSubjects = Json.newArray();
        Map<String, Long> aux = new HashMap<>();
        List<Long> listProm = new ArrayList<>();
        for (int i = 0; i <subjectListLong.size() ; i++) {
            List<Long> longAux  = subjectListLong.get(subjects.get(i));
            if(!longAux.isEmpty()) {
                aux.put(subjects.get(i).getName(), getProm(longAux));
                listProm.add(getProm(longAux));
            }
        }

        Collections.sort(listProm);

        for (int j = listProm.size() -1; j > listProm.size() -4 && j >= 0 ; j--) {
            float prom = listProm.get(j);
            for (int k = 0; k <subjects.size() ; k++) {
                String subject = subjects.get(k).getName();
                if(aux.containsKey(subject)) {
                    long value = aux.get(subject);
                    if (bestSubjects.size() < 3) {
                        if (value >= prom) {
                            ObjectNode obj = Json.newObject();
                            obj.put("name", subject);
                            obj.put("score", value);
                            bestSubjects.add(obj);
                            aux.remove(subject);
                        }
                    } else {
                        break;
                    }
                }
            }
        }

        return bestSubjects;

    }

    private static Long getProm(List<Long> list){
        if (list.isEmpty()) return 0L;
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
            //if (nextLessons.size() <= 6) {
                Lesson aux = lessons.get(i);
                if (aux.getDateTime().after(date) && aux.getLessonState() == 1) {
                    nextLessons.add(aux);
                }
            //}
            //else {
             //   break;
            //}
        }
        return ok(Json.toJson(nextLessons));
    }

    public static Result getPreviousLessons(){
        final Long userId = Long.parseLong(session().get("id"));
        Iterator<Lesson> previousLessonsAux = getPreviousLessons(userId);

        List<Lesson> previousLessons = new ArrayList<>();
        while (previousLessonsAux.hasNext()){
            final Lesson lesson = previousLessonsAux.next();
            previousLessons.add(lesson);
        }

        Collections.sort(previousLessons,new LessonComparator());

        final ArrayNode results = Json.newArray();

        for (int i = 0; i <previousLessons.size() ; i++) {
            final Lesson temp = previousLessons.get(i);
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
            Review teacherReview = temp.getTeacherReview();
            node.put("score", teacherReview != null ? teacherReview.getStars() : -1);
            results.add(node);
        }


        return ok(results);
    }

    private static Iterator<Lesson> getPreviousLessons(Long userId){
        final Teacher teacher = Teacher.finder.where().eq("user.id", userId).findUnique();
        return Lesson.finder.where().eq("teacher", teacher).lt("dateTime", new Date()).eq("lessonState", 1).findList().iterator();
    }

}
