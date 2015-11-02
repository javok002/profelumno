package ua.dirproy.profelumno.calendar.controllers;

import authenticate.Authenticate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Lesson;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;

import ua.dirproy.profelumno.calendar.views.html.calendar;
import ua.dirproy.profelumno.user.models.User;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private static List<Lesson> myLessons(User user){
        List<Lesson> lessons = Lesson.list();
        List<Lesson> myLessons = new ArrayList<>();
        for (int i = 0; i <lessons.size() ; i++) {
            Lesson aux = lessons.get(i);
            if (aux.getTeacher().getId().equals(user.getId()) || aux.getStudent().getId().equals(user.getId())){
                myLessons.add(aux);
            }
        }
        return myLessons;
    }

    public static Result getLessonAccepted(){
        List<Lesson> lessons = myLessons(getUser());
        List<Lesson> acceptLessons = new ArrayList<>();
        for (int i = 0; i <lessons.size() ; i++) {
            Lesson aux = lessons.get(i);
            if (aux.getLessonState() == 1) {
                acceptLessons.add(aux);
            }
        }
        return ok(Json.toJson(acceptLessons));
    }

    public static void updateCalendar() throws IOException {
        JsonNode updateJson = request().body().asJson();
        Date date = deserialize(updateJson.get("date"));
        Date fromHour = deserialize(updateJson.get("fromHour"));
        Date toHour = deserialize(updateJson.get("toHour"));
        Teacher teacher;
        if (getTeacher() != null){
            teacher = getTeacher();
            teacher.updateCalendar(date,fromHour,toHour);
        }


    }

    private static Date deserialize(JsonNode jsonparser) throws IOException, JsonProcessingException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String date = jsonparser.asText();
        try {
            return format.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }


}
