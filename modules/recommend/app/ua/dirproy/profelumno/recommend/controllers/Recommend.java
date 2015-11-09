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
                        sendWeMissYou(student.getUser());
                    }
                }
            }
        };
        final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(charger, 0 , 10080, MINUTES);
    }

    private void sendWeMissYou(User user) {
        String subject = "Profelumno te extraña";
        String message = "Hola " + user.getName();
        message += "\n¡Hace tiempo que no nos visitas! Estamos seguros que necesitas ayuda en esa materia que ultimamente no te está yendo tan bien.";
        message += "\n¡Te esperammos!";
        message += "\nlocalhost:9000";
        message += "\nEl equipo de profelumno";
        System.out.println("Sending mail...");
        try {
            String[] to = new String[1];
            to[0] = user.getEmail();
//            to[0] = "jose.illi@ing.austral.edu.ar";
            MailSenderUtil.send(to, subject, message);
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
                    String message = "Hola " + student.getUser().getName() + "\n\n ¿Por qué no pides ayuda para aprobar tus exámenes? Estos profesores enseñan " + subject + " y ¡OH! tu necesitas aprender " + subject + "!\n\n";
                    message += "\nEn el siguiente link puedes ver los profesores que están cerca de tu casa \n";
                    // todo -> falta la tabla con profesores
                    message += "\nlocalhost:9000/teacher-search?subject="+subject+"&sort=true";
                    message += "\n El equipo de profelumno";
                    System.out.println("Sending mail...");
                    try {
                        MailSenderUtil.send(to, subject, message);
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
}
