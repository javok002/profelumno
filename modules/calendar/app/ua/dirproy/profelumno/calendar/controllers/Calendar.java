package ua.dirproy.profelumno.calendar.controllers;

import authenticate.Authenticate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.calendar.views.html.calendar;
import ua.dirproy.profelumno.common.models.*;
import ua.dirproy.profelumno.user.models.User;


import java.util.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import play.libs.Json;

@Authenticate({Teacher.class, Student.class})
public class Calendar extends Controller {

    public static Result calendarView() { return ok(calendar.render()); }

    public static Result getUserName() {
        User user = getUser();
        return user != null ? ok(user.getName() + " " + user.getSurname()) : badRequest();
    }

    private static User getUser() {
        return User.getUser(Long.parseLong(session("id")));
    }

    private static Teacher getTeacher() {
        List<Teacher> teachers = Teacher.list();
        for (int i = 0; i < teachers.size(); i++) {
            Teacher aux = teachers.get(i);
            if (aux.getUser().getId().equals(getUser().getId())) {
                return Teacher.getTeacher(aux.getId());
            }
        }
        return null;
    }

    private static Student getStudent(){
        List<Student> student = Student.list();
        for (int i = 0; i < student.size(); i++) {
            Student aux = student.get(i);
            if (aux.getUser().getId().equals(getUser().getId())) {
                return Student.getStudent(aux.getId());
            }
        }
        return null;
    }

    private static List<Lesson> myLessons(){
        User user = getUser();
        List<Lesson> lessons = Lesson.list();
        List<Lesson> myLessons = new ArrayList<>();
        for (Lesson aux : lessons) {
            if (aux.getTeacher().getUser().getId().equals(user.getId())){
                myLessons.add(aux);
            }
            else if (aux.getStudent().getUser().getId().equals(user.getId())){
                myLessons.add(aux);
            }
        }
        return myLessons;
    }

    public static Result getLessonAccepted(){
        List<Lesson> lessons = myLessons();
        List<Lesson> acceptLessons = new ArrayList<>();
        for (Lesson aux : lessons) {
            if (aux.getLessonState() == 1) {
                acceptLessons.add(aux);
            }
        }
        return ok(Json.toJson(acceptLessons));
    }

    public static Result getCalendar(){

        List<DayRange> calendar;
        ArrayNode result = Json.newArray();
        if (getTeacher() != null){
            calendar = getTeacher().getCalendar();
            for (DayRange day : calendar){

                ObjectNode obj = Json.newObject();
                obj.put("day", day.getDayEnum().getDayName());
                obj.put("fromHour", day.getFromHour().toString());
                obj.put("toHour", day.getToHour().toString());
                result.add(obj);

            }
        }
        return ok(Json.toJson(result));
    }


    public static Result updateCalendar() throws IOException {
        JsonNode updateJson = request().body().asJson();
        if(updateJson == null) {
            return badRequest("Expecting Json data");
        }
        else {
            Date date = deserialize(updateJson.get("date"));
            Date fromHour = deserialize(updateJson.get("fromHour"));
            Date toHour = deserialize(updateJson.get("toHour"));
            Teacher teacher;
            if (getTeacher() != null) {
                teacher = getTeacher();
                teacher.updateCalendar(date, fromHour, toHour);
                return ok();
            }
            else {
                return badRequest("Teacher == null");
            }
        }

    }

    private static Date deserialize(JsonNode jsonparser) throws IOException{

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = jsonparser.asText();
        try {
            return format.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    public static Result teacherAvailableTimeWithNoClass(){

        //get del teacher del server (paca checkea esto por lo del form.form)
        Form<Teacher> form = Form.form(Teacher.class).bindFromRequest();
        Long teacherId = Long.parseLong(form.data().get("teacherId"));
        final Teacher teacher = Teacher.getTeacher(teacherId);

        List<DayRange> calendar = teacher.getCalendar();

        //get acceptedLesson from teacher
        List<Lesson> lessons = myLessons();
        List<Lesson> acceptLessons = new ArrayList<>();
        for (Lesson aux : lessons) {
            if (aux.getLessonState() == 1) {
                acceptLessons.add(aux);
            }
        }

        //voy sacando del calendario del teacher los horarios que ya tienen clases
        for (Lesson aux : acceptLessons){
            final DayEnum dayEnum = auxiliaryMethod(aux.getDateTime());
            Duration durationOfClass = aux.getDuration();
            for (DayRange dr : calendar) {
                if (dr.getDayEnum() == dayEnum && dr.getDuration().equals(durationOfClass)){
                    calendar.remove(dr);
                }
            }
        }

        return ok(Json.toJson(calendar));
    }

    private static DayEnum auxiliaryMethod(Date date){
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        int day = cal.get(java.util.Calendar.DAY_OF_WEEK);
        DayEnum dayEnum = null;
        if (java.util.Calendar.MONDAY == day){
            dayEnum = DayEnum.MONDAY;
        }
        if (java.util.Calendar.TUESDAY == day){
            dayEnum = DayEnum.TUESDAY;
        }
        if (java.util.Calendar.WEDNESDAY == day){
            dayEnum = DayEnum.WEDNESDAY;
        }
        if (java.util.Calendar.THURSDAY == day){
            dayEnum = DayEnum.THURSDAY;
        }
        if (java.util.Calendar.FRIDAY == day){
            dayEnum = DayEnum.FRIDAY;
        }
        if (java.util.Calendar.SUNDAY == day){
            dayEnum = DayEnum.SUNDAY;
        }
        if (java.util.Calendar.SATURDAY == day){
            dayEnum = DayEnum.SATURDAY;
        }
        return dayEnum;
    }
}
