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
import java.net.MalformedURLException;
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
        Form<Teacher> form = Form.form(Teacher.class).bindFromRequest();
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
        List<String> subjects = new ArrayList<>(form.data().size() - 4);
        int index = form.data().size() - 5;
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
        if(Boolean.parseBoolean(form.data().get("sort")) && user.getAddress() != null){
            orderByDistance(teachersToShow, user);
        } else{
            teachersToShow.sort(new Comparator<Teacher>() {
                @Override
                public int compare(Teacher o1, Teacher o2) {
                    float difference = o2.getRanking() - o1.getRanking();
                    return difference < 0 ? -1 : difference > 0 ? 1 : 0;
                }
            });
        }
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

    public static void orderByDistance(List<Teacher> users, User user){
        users.sort(new Comparator<Teacher>() {
            @Override
            public int compare(Teacher o1, Teacher o2) {
                if(o1.getUser().getLatitude() == 0 || o1.getUser().getLongitude() == 0) return -1;
                else if(o2.getUser().getLatitude() == 0 || o2.getUser().getLongitude() == 0) return 1;
                int distance = 0;
                try {
                    double distance1 = getDistance(user, o1.getUser());
                    double distance2 = getDistance(user, o2.getUser());
                    double realDistances = distance1 - distance2;
                    distance = realDistances < 0 ? -1 : realDistances > 0 ? 1 : 0;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    if(distance == 0){
                        float difference = o2.getRanking() - o1.getRanking();
                        distance = difference < 0 ? -1 : difference > 0 ? 1 : 0;
                    }
                    return distance;
                }

        });
        }


    private static double getDistance(User user1, User user2) throws IOException {
        if((user1.getLatitude() == 0 || user1.getLongitude() == 0) && user1.getAddress() != null){
            getCordinates(user1);
        }

        if ((user2.getLatitude() == 0 || user2.getLongitude() == 0) && user2.getAddress()!= null){
            getCordinates(user2);
        }
        if(user1.getAddress() != null && user1.getAddress().equals(user2.getAddress())){
            return 0;
        }
        return Math.hypot((user1.getLatitude() - user2.getLatitude()),(user1.getLongitude() - user2.getLongitude()));
    }
        // TODO las coordenadas de TODOS los usuarios son iguales, la distancia va a ser siempre 0.0 mts.




    // https://www.google.com.ar/maps/search/Ballivian+2329,+Villa+Ortuzar,+Buenos+Aires/

    /**
     * Metodo villero para agarrar las coordenadas segun la direccion del usuario
     * @param user usuario a obtener las coordenadas
     * @throws IOException
     */
    private static void  getCordinates(User user) throws IOException {

        String address2 = user.getAddress().replace(" ", "+");
        URL url = new URL("https://www.google.com.ar/maps/search/" + address2);

        BufferedReader theHTML = new BufferedReader(new InputStreamReader(url.openStream()));
        String line = theHTML.readLine();
        while (!line.contains("cacheResponse([[")){
            line = theHTML.readLine();
        }
        String[] separatedLine = line.split(",",5);
        user.setLatitude(Double.parseDouble(separatedLine[1]));
        user.setLongitude(Double.parseDouble(separatedLine[2].replace("]", "")));

        //URL url1 = new URL("http://maps.google.com/maps/api/geocode/xml?address="+address2+"&sensor=false");
        //BufferedReader theHTML1 = new BufferedReader(new InputStreamReader(url1.openStream()));
        //Json.toJson(url1.openStream());
        /*String line2 = theHTML1.readLine();
        while (!line2.contains("<location>")){              // Este es un metodo que utiliza un JSON y no supe como agarrarlo. "url1" en teoria devuelve un JSON.
            line2 = theHTML1.readLine();
        }
        double lat = Double.parseDouble(theHTML1.readLine().replace("<lat>", "").replace("</lat>", "").replace(" ",""));
        double longit = Double.parseDouble(theHTML1.readLine().replace("<lng>", "").replace("</lng>","").replace(" ", ""));
*/
    }



}
