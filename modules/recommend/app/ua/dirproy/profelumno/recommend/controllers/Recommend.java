package ua.dirproy.profelumno.recommend.controllers;


import com.avaje.ebean.Ebean;
import play.libs.Akka;
import play.mvc.Controller;
import scala.concurrent.duration.Duration;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.mailsender.models.MailSenderUtil;
import ua.dirproy.profelumno.teachersearch.controllers.TeacherSearches;
import ua.dirproy.profelumno.user.models.Subject;
import ua.dirproy.profelumno.user.models.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.DAYS;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by Nash
 * Date: 10/26/15
 * Project profelumno
 */

public class Recommend extends Controller {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    public static void weMissyou(){
        String[] to = {"nicolas.moreno@ing.austral.edu.ar"};
        /*try {
            MailSenderUtil.send(to, "prueba", "Prueba");
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
        }*/


        /*Akka.system().scheduler().scheduleOnce(
                Duration.create(5000, TimeUnit.MILLISECONDS),
                new Runnable() {
                    public void run() {
                        MailSender.mockSend(to[0],"Prueba");
                },

        );*/

    /*    Akka.system().scheduler().schedule(
                Duration.create(0, TimeUnit.MILLISECONDS), //Initial delay 0 milliseconds
                Duration.create(60, TimeUnit.SECONDS),     //Frequency 30 minutes
                new Runnable() {
                    public void run() {
                        try {
                            MailSenderUtil.send(to, "prueba", "Prueba");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                Akka.system().dispatcher());*/

    }

    public void weMissYou() {
        final Runnable charger = new Runnable() {
            public void run() {
                List<Student> students = Student.finder.findList();
                for (Student student : students) {
                    Subject materia = student.getUser().getSubjects().get((int) (Math.random() * student.getUser().getSubjects().size()));
                    String[] to = new String[1];
//                    to[0] = student.getUser().getEmail();
                    to[0] = "nicolas.moreno@ing.austral.edu.ar";
                    List<Teacher> teachers = Teacher.finder.findList();
                    List<User> teachersToRecommend = new ArrayList<>();
                    for (Teacher teacher : teachers) {
                        if(teacher.getUser().getSubjects().contains(materia)) {
                            teachersToRecommend.add(teacher.getUser());
                        }
                    }
                    TeacherSearches.orderByDistance(teachersToRecommend, student.getUser());

                    teachersToRecommend = teachersToRecommend.subList(0, teachersToRecommend.size() > 6 ? 6 : teachersToRecommend.size());
                    teachersToRecommend.sort(new Comparator<User>() {
                        @Override
                        public int compare(User o1, User o2) {
                            if(o1.getReviews() == 0){
                                return  -1;
                            } else if(o2.getReviews() == 0){
                                return 1;
                            }
                            return (int) ((o1.getTotalStars() / o1.getReviews()) - (o2.getTotalStars() / o2.getReviews()));
                        }
                    });
                    String subject = "Profelumno recomienda";
                    String message = "Hola " + student.getUser().getName() + "\n\n ¿Por qué no pides ayuda para aprobar tus exámenes? Estos profesores enseñan " + materia + " y ¡OH! tu necesitas aprender " + materia + "!\n\n";
                    message += "En el siguiente link puedes ver los profesores que están cerca de tu casa \n";
                    message += "www.google.com";
                    System.out.println("Sending mail...");
                    try {
                        MailSenderUtil.send(to, subject, message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Error sending mail");
                    }
                    System.out.println("Mail sent.");
                }
                }
            };
        final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(charger, 0, 180, SECONDS);
    }
}
