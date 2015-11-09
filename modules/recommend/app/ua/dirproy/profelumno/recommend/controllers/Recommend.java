package ua.dirproy.profelumno.recommend.controllers;


import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import play.mvc.Controller;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.mailsender.models.MailSenderUtil;
import ua.dirproy.profelumno.teachersearch.controllers.TeacherSearches;
import ua.dirproy.profelumno.user.models.Subject;
import ua.dirproy.profelumno.user.models.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.MINUTES;


/**
 * Created by Nash
 * Date: 10/26/15
 * Project profelumno
 */

public class Recommend extends Controller {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


//    public static void weMissyou(){
//        String[] to = {"nicolas.moreno@ing.austral.edu.ar"};
//        /*try {
//            MailSenderUtil.send(to, "prueba", "Prueba");
//        } catch (javax.mail.MessagingException e) {
//            e.printStackTrace();
//        }*/
//
//
//        /*Akka.system().scheduler().scheduleOnce(
//                Duration.create(5000, TimeUnit.MILLISECONDS),
//                new Runnable() {
//                    public void run() {
//                        MailSender.mockSend(to[0],"Prueba");
//                },
//
//        );*/
//
//    /*    Akka.system().scheduler().schedule(
//                Duration.create(0, TimeUnit.MILLISECONDS), //Initial delay 0 milliseconds
//                Duration.create(60, TimeUnit.SECONDS),     //Frequency 30 minutes
//                new Runnable() {
//                    public void run() {
//                        try {
//                            MailSenderUtil.send(to, "prueba", "Prueba");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                Akka.system().dispatcher());*/
//
//    }

    public void weMissYou() {
        int initialDelay = minutesForNextMondayAtTen();
        final Runnable charger = new Runnable() {
            public void run() {
                List<Student> students = Student.finder.findList();
                for (Student student : students) {
                    if(lastLoginBeforeTenDay(student.getUser().getLastLogin())){
                        sendWeMissYou(student.getUser(), false);
                    }
                }
                List<Teacher> teachers = Teacher.finder.findList();
                for (Teacher teacher : teachers) {
                    if(lastLoginBeforeTenDay(teacher.getUser().getLastLogin())){
                        sendWeMissYou(teacher.getUser(), true);
                    }
                }

            }
        };
        final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(charger, 4 , 10080, MINUTES);
    }

    private void sendWeMissYou(User user, boolean isTeacher) {
        String subject = "Profelumno te extraña";
        String message1 = "<div class=\"container-fluid\">\n" +
                "    <div class=\"row-fluid\">\n" +
                "        <h4>Hola "+user.getName() +"</h4>\n" +
                "        <p>¡Hace tiempo que no nos visitas!";
        if(isTeacher){
            message1 += " Estamos seguros que hay alumnos que están necesitando tu ayuda. Entra al sitio y vuelve a dar clases en Profelumno.</p>\n" +
                    "        <p>¡Te esperamos!</p>\n" +
                    "        <p>localhost:9000</p>\n" +
                    "        <p>El equipo de Profelumno</p>\n" +
                    "    </div>\n" +
                    "</div>";
        } else {
            message1 += " Estamos seguros que necesitas ayuda en esa materia que ultimamente no te está yendo tan bien.</p>\n" +
                    "        <p>¡Te esperamos!</p>\n" +
                    "        <p>localhost:9000</p>\n" +
                    "        <p>El equipo de Profelumno</p>\n" +
                    "    </div>\n" +
                    "</div>";
        }

        System.out.println("Sending mail...");
        try {
            String[] to = new String[1];
            to[0] = user.getEmail();
//            to[0] = "jose.illi@ing.austral.edu.ar";
            MailSenderUtil.send(to, subject, message1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error sending mail");
        }
        System.out.println("Mail sent.");
    }

    private boolean lastLoginBeforeTenDay(Date lastLogin) {
        if(lastLogin == null) return true;
        DateTime dateTime = new DateTime(lastLogin);
        return (dateTime.isBefore(((new DateTime()).minusDays(10))));
    }

    private int minutesForNextMondayAtTen() {
        LocalDate today = LocalDate.now();
        int old = today.getDayOfWeek();
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHourOfDay();
        int min = now.getMinuteOfHour();
        if(old == 1){
            System.out.println("Mins to next monday at 10");
            if(hour < 10){
                System.out.println(600 - hour*60+min);
                return 600 - hour*60-min;
            } else if(hour == 10 && min == 0) {
                System.out.println(0);
                return 0;
            } else {
                System.out.println(6*24*60 + (24 - hour)*60 - min);
                return 6*24*60 + (24 - hour)*60 - min;
            }
        } else {
            int monday = 1;
            if (monday <= old) {
                monday += 7;
            }
            if(hour < 10){
                return (monday - old)*24*60 + 600 - hour * 60 - min;
            } else if(hour == 10 && min == 0){
                return (monday - old)*24*60;
            } else {
                return (monday - old)*24*60 - hour * 60 - min + 600;
            }
        }
    }

    public void doRecommendations() {
        final Runnable charger = new Runnable() {
            public void run() {
                List<Student> students = Student.finder.findList();
//                Student student = students.get(0);
                for (Student student : students) {
                    Subject materia = student.getUser().getSubjects().get((int) (Math.random() * student.getUser().getSubjects().size()));
                    String[] to = new String[1];
                    to[0] = student.getUser().getEmail();
//                    to[0] = "jose.illi@ing.austral.edu.ar";
                    List<Teacher> teachers = Teacher.finder.findList();
                    List<Teacher> teachersToRecommend = new ArrayList<>();
                    for (Teacher teacher : teachers) {
                        if(teacher.getUser().getSubjects().contains(materia)) {
                            teachersToRecommend.add(teacher);
                        }
                    }
                    if(teachersToRecommend.size() == 0) return;
                    TeacherSearches.orderByDistance(teachersToRecommend, student.getUser());
//
                    teachersToRecommend = teachersToRecommend.subList(0, teachersToRecommend.size() > 6 ? 6 : teachersToRecommend.size());
                    teachersToRecommend.sort(new Comparator<Teacher>() {
                        @Override
                        public int compare(Teacher o1, Teacher o2) {
                            if(o1.getUser().getReviews() == 0){
                                return  -1;
                            } else if(o2.getUser().getReviews() == 0){
                                return 1;
                            }
                            float difference = o1.getRanking() - o2.getRanking();
                            return difference < 0 ? -1 : difference > 0 ? 1 : 0;
                        }
                    });
                    String subject = "Profelumno recomienda";
                    String message1 = "<div class=\"container-fluid\">\n" +
                            "    <div class=\"row-fluid\">\n" +
                            "        <h4>Hola " + student.getUser().getName() +"</h4>\n" +
                            "        <p>¿Por qué no pides ayuda para aprobar tus examenes? Estos profesores enseñan " + subject + " y ¡OH! tu necesitas aprender "+ subject +"</p>\n" +
                            "    </div>\n" +
                            "    <div class=\"row-fluid\">\n" +
                            "        <table class=\"table\">\n" +
                            "            <tbody>\n" +
                            "                <tr>\n" +
                            "                    <th>Profesor</th>\n" +
                            "                    <th>Ranking</th>\n" +
                            "                    <th>Clases dictadas</th>\n" +
                            "                    <th>Distancia a tu domicilio</th>\n" +
                            "                </tr>\n" ;
                    for (int i = 0; i < teachersToRecommend.size(); i++) {
                        message1 += "                <tr>\n" +
                                "                    <td>" + teachersToRecommend.get(i).getUser().getName() + " " + teachersToRecommend.get(i).getUser().getSurname() + "</td>\n" +
                                "                    <td>" + teachersToRecommend.get(i).getRanking() + "</td>\n" +
                                "                    <td>" + teachersToRecommend.get(i).getLessonsDictated() + "</td>\n" +
                                "                    <td>" + distFrom(student.getUser().getLatitude(),student.getUser().getLongitude(),teachersToRecommend.get(i).getUser().getLatitude(),teachersToRecommend.get(i).getUser().getLongitude()) +"</td>\n" +
                                "                </tr>\n";

                    }
                    message1 +=        "            </tbody>\n" +
                            "        </table>\n" +
                            "    </div>\n" +
                            "    <div class=\"row-fluid\">\n" +
                            "        <p>En el siguinete link puedes ver los profesores que están cerca de tu casa</p>\n" +
                            "        <p>localhost:9000/teacher-search?subject=" +subject +"&sort=true</p>\n" +
                            "        <p>El equipo de Profelumno</p>\n" +
                            "    </div>\n" +
                            "</div>";

                    System.out.println("Sending mail...");
                    try {
                        MailSenderUtil.send(to, subject, message1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Error sending mail");
                    }
                    System.out.println("Mail sent.");
//
//                LocalDate today = LocalDate.now();
//
//                int old = today.getDayOfWeek();
//                int monday = 1;
//
//                if (monday <= old) {
//                    monday += 7;
//                }
//                LocalDate next = today.plusDays(monday - old);
//                System.out.println("Next monday: " + next);
//
//                LocalDateTime now = LocalDateTime.now();
//                now.getDayOfWeek();

            }
            }
        };
        final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(charger, 0, 7, DAYS);
    }

    public static float distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }
}
