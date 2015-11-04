package ua.dirproy.profelumno.teachersearch.controllers;

import authenticate.Authenticate;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import jdk.nashorn.internal.parser.JSONParser;
import org.xml.sax.SAXException;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.user.models.Subject;
import ua.dirproy.profelumno.user.models.User;


import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
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
                /*if(o1.getLatitude() == 0 || o1.getLongitude() == 0) return -1;
                else if(o2.getLatitude() == 0 || o2.getLongitude() == 0) return 1;*/
                try {
                    getCordinates(o1.getAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
                return (int) (getDistance(user, o1) - getDistance(user, o2));
            }
        });
    }

    private static double getDistance(User user1, User user2){
        return Math.hypot((user1.getLatitude() - user2.getLatitude()),(user1.getLongitude() - user2.getLongitude()));
        // TODO las coordenadas de TODOS los usuarios son iguales, la distancia va a ser siempre 0.0 mts.


    }

    // https://www.google.com.ar/maps/search/Ballivian+2329,+Villa+Ortuzar,+Buenos+Aires/
    private static String getCordinates(String address) throws IOException, ParserConfigurationException, SAXException {

        String address2 = address.replace(" ", "+");
        URL url = new URL("https://www.google.com.ar/maps/search/" + address2);

        BufferedReader theHTML = new BufferedReader(new InputStreamReader(url.openStream()));
        String line = theHTML.readLine();
        while (!line.contains("cacheResponse([[")){
            line = theHTML.readLine();
        }
        String[] separatedLine = line.split(",",5);
        double longitud = Double.parseDouble(separatedLine[1]);
        double altitud = Double.parseDouble(separatedLine[2].replace("]",""));

        //URL url1 = new URL("http://maps.google.com/maps/api/geocode/xml?address="+address2+"&sensor=false");
        //BufferedReader theHTML1 = new BufferedReader(new InputStreamReader(url1.openStream()));
        //Json.toJson(url1.openStream());
        /*String line2 = theHTML1.readLine();
        while (!line2.contains("<location>")){
            line2 = theHTML1.readLine();
        }
        double lat = Double.parseDouble(theHTML1.readLine().replace("<lat>", "").replace("</lat>", "").replace(" ",""));
        double longit = Double.parseDouble(theHTML1.readLine().replace("<lng>", "").replace("</lng>","").replace(" ", ""));
*/
        return line;



    }



}
