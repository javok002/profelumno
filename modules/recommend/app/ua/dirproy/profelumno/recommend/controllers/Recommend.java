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
    private final static boolean TEST = true;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

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
        final ScheduledFuture<?> beeperHandle;

        if(TEST){
            beeperHandle = scheduler.scheduleAtFixedRate(charger, 2 , 60, MINUTES);
        }else {
            beeperHandle = scheduler.scheduleAtFixedRate(charger, initialDelay , 10080, MINUTES);
        }
    }

    private void sendWeMissYou(User user, boolean isTeacher) {
        if(TEST){
            if((!user.getEmail().equals("jose.illi@ing.austral.edu.ar")) && (!user.getEmail().equals("gutierrezmartin1992@gmail.com"))) return;
        }
        String subject = "Profelumno te extraña";
        String message = "<h2><span style=\"color: #008080;\">&iexcl;Hola "+user.getName() +"!</span></h2>\n" +
                "<h4>&iexcl;Hace tiempo que no nos visitas!</h4>\n";
//        String message1 = "<div class=\"container-fluid\">\n" +
//                "    <div class=\"row-fluid\">\n" +
//                "        <h4>Hola "+user.getName() +"</h4>\n" +
//                "        <p>¡Hace tiempo que no nos visitas!";
        if(isTeacher){
            message+="<p>Estamos seguros que hay alumnos que est&aacute;n necesitando tu ayuda. Entra al sitio y vuelve a dar clases en Profelumno.</p>";
//            message1 += " Estamos seguros que hay alumnos que están necesitando tu ayuda. Entra al sitio y vuelve a dar clases en Profelumno.</p>\n" +
//                    "        <p>¡Te esperamos!</p>" +
//                    "        <a href=\"localhost:9000\">localhost:9000</a>\n" +
//                    "        <p>El equipo de Profelumno</p>\n" +
//                    "    </div>\n" +<p>
//                    "</div>";
        } else {
            message+=" <p>Estamos seguros que necesitas ayuda en esa materia que ultimamente no te está yendo tan bien. &iexcl;Vuelve a Profelumno y busca ayuda!</p>\n";
//            message1 += " Estamos seguros que necesitas ayuda en esa materia que ultimamente no te está yendo tan bien.</p>\n" +
//                    "        <p>¡Te esperamos!</p>\n" +
//                    "        <a href=\"localhost:9000\">localhost:9000</a>\n" +
//                    "        <p>El equipo de Profelumno</p>\n" +
//                    "    </div>\n" +
//                    "</div>";
        }
        message+= "<h1 style=\"text-align: center;\"><span style=\"color: #ff6600;\">&iexcl;Te esperamos!</span></h1>\n" +
                "<p><a href=\"http://localhost:9000\" target=\"_blank\">http://localhost:9000</a></p>\n" +
                "<h2>El equipo de <span style=\"color: #33cccc;\">Profelumno</span></h2>";

        System.out.println("Sending mail we miss you to " + user.getEmail());
        try {
            String[] to = new String[1];
            to[0] = user.getEmail();
//            to[0] = "jose.illi@ing.austral.edu.ar";
            MailSenderUtil.send(to, subject, message);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error sending we miss you to " + user.getEmail());
        }
        System.out.println("Mail we miss you to " + user.getEmail() + " sent");
    }

    private boolean lastLoginBeforeTenDay(Date lastLogin) {
        if(lastLogin == null) return true;
        DateTime dateTime = new DateTime(lastLogin);
        DateTime today = new DateTime();
        return (dateTime.isBefore((today.minusDays(10))));
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
                Student student;
                for (int j = 0; j < students.size(); j++) {
                    student = students.get(students.size()-1-j);
                    if(TEST){
                        if((!student.getUser().getEmail().equals("illijoseignacio@gmail.com")) && (!student.getUser().getEmail().equals("gutierrezmartin1992@gmail.com"))) continue;
                    }
                    if(student.getUser().getSubjects().isEmpty()) return;
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

                    for (Teacher teacher : teachersToRecommend) {
                        Teacher.updateRating(teacher);
                    }
                    teachersToRecommend.sort(new Comparator<Teacher>() {
                        @Override
                        public int compare(Teacher o1, Teacher o2) {
                            if(o1.getUser().getReviews() == 0){
                                return  1;
                            } else if(o2.getUser().getReviews() == 0){
                                return -1;
                            }
                            float difference = o2.getRanking() - o1.getRanking();
                            return difference < 0 ? -1 : difference > 0 ? 1 : 0;
                        }
                    });
                boolean hasDirection = (student.getUser().getAddress() != null);
                if (hasDirection){
                        TeacherSearches.orderByDistance(teachersToRecommend, student.getUser());
                    }
//                else {
//                        teachersToRecommend.sort(new Comparator<Teacher>() {
//                            @Override
//                            public int compare(Teacher o1, Teacher o2) {
//                                if(o1.getUser().getReviews() == 0){
//                                    return  1;
//                                } else if(o2.getUser().getReviews() == 0){
//                                    return -1;
//                                }
//                                float difference = o2.getUser().getTotalStars()/o2.getUser().getReviews() - o1.getUser().getTotalStars()/o1.getUser().getReviews();
//                                return difference < 0 ? -1 : difference > 0 ? 1 : 0;
//                            }
//                        });
//                    }


                    teachersToRecommend = teachersToRecommend.subList(0, teachersToRecommend.size() > 6 ? 6 : teachersToRecommend.size());

                    String subject = "Profelumno recomienda";
                    String message = "<div>\n" +
                            "    <div>\n" +
                            "        <h2><span style=\"color: #008080;\">&iexcl;Hola " + student.getUser().getName() +"!</span></h2>\n" +
                            "        <p><span style=\"color: #000000;\">&iquest;Por qu&eacute; no pides ayuda para aprobar tus examenes? Estos profesores ense&ntilde;an <em>"+ materia.getName() +"</em> y &iexcl;OH! tu necesitas aprender <em>"+ materia.getName() +"</em></span></p>\n" +
                            "    </div>\n" +
                            "    <div>\n" +
                            "        <div>\n" +
                            "            <table style=\"border-color: #5882fa; margin-left: auto; margin-right: auto;\" border=\"solid\" cellpadding=\"12\">\n" +
                            "                <tbody>\n" +
                            "                <tr><th style=\"text-align: center;\">Profesor</th><th style=\"text-align: center;\">Ranking</th><th style=\"text-align: center;\">Clases dictadas</th><th style=\"text-align: center;\">Distancia a tu domicilio</th></tr>\n";

//                    String message1 = "<div class=\"container-fluid\">\n" +
//                            "    <div class=\"row-fluid\">\n" +
//                            "        <h4>Hola " + student.getUser().getName() +"</h4>\n" +
//                            "        <p>¿Por qué no pides ayuda para aprobar tus examenes? Estos profesores enseñan " + materia.getName() + " y ¡OH! tu necesitas aprender "+ materia.getName() +"</p>\n" +
//                            "    </div>\n" +
//                            "    <div class=\"row-fluid center-block\">\n" + "<div class =\" col-xs-12\">" +
//                            "        <table class=\"table bordered\">\n" +
//                            "            <tbody>\n" +
//                            "                <tr>\n" +
//                            "                    <th class=\"text-center\">Profesor</th>\n" +
//                            "                    <th class=\"text-center\">Ranking</th>\n" +
//                            "                    <th class=\"text-center\">Clases dictadas</th>\n" +
//                            "                    <th class=\"text-center\">Distancia a tu domicilio</th>\n" +
//                            "                </tr>\n" ;
                    for (int i = 0; i < teachersToRecommend.size(); i++) {
                        message += "                <tr>\n" +
                                "                    <td style=\"text-align: center;\">" + teachersToRecommend.get(i).getUser().getName() + " " + teachersToRecommend.get(i).getUser().getSurname() + "</td>\n" +
                                "                    <td style=\"text-align: center;\">" + teachersToRecommend.get(i).getRanking() + "</td>\n" +
                                "                    <td style=\"text-align: center;\">" + teachersToRecommend.get(i).getLessonsDictated() + "</td>\n" +
                                "                    <td style=\"text-align: center;\">" + (hasDirection ? (distFrom(student.getUser().getLatitude(),student.getUser().getLongitude(),teachersToRecommend.get(i).getUser().getLatitude(),teachersToRecommend.get(i).getUser().getLongitude())): "Desconocida") +"</td>\n" +
                                "                </tr>\n";

                    }
                    message+= "                </tbody>\n" +
                            "            </table>\n" +
                            "        </div>\n" +
                            "        <div>\n" +
                            "            <p>En el siguinete link puedes ver los profesores que est&aacute;n cerca de tu casa</p>\n" +
                            "            <p><a href=\"http://localhost:9000/teacher-search?subject="+materia.getName().replace(" ", "-")+"&sort=true\" target=\"_blank\">http://localhost:9000/teacher-search?subject=" +materia.getName().replace(" ", "-") +"&sort=true</a></p>\n" +
                            "            <h2>El equipo de <span style=\"color: #33cccc;\">Profelumno</span></h2>\n" +
                            "        </div>\n" +
                            "    </div>\n" +
                            "</div>";;

//                    message1 +=        "            </tbody>\n" +
//                            "        </table>\n" +
//                            "    </div>\n" + "<div>"+
//                            "    <div class=\"row-fluid\">\n" +
//                            "        <p>En el siguinete link puedes ver los profesores que están cerca de tu casa</p>\n" +
//                            "        <a href=\"localhost:9000/teacher-search?subject"+materia.getName().replace(" ", "-")+"&sort=true\">localhost:9000/teacher-search?subject=" +materia.getName().replace(" ", "-") +"&sort=true</a>\n" +
//                            "        <p>El equipo de Profelumno</p>\n" +
//                            "    </div>\n" +
//                            "</div>";

                    System.out.println("Sending recommendation mail to " + student.getUser().getEmail());
                    try {
                        MailSenderUtil.send(to, subject, message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Error sending recommendation mail to " + student.getUser().getEmail());
                    }
                    System.out.println("Recommendation mail sent to " + student.getUser().getEmail());

            }
            }
        };
        final ScheduledFuture<?> beeperHandle;
        if(TEST){
            beeperHandle = scheduler.scheduleAtFixedRate(charger, 4, 120, MINUTES);
        }else {
            beeperHandle = scheduler.scheduleAtFixedRate(charger, 0, 7, DAYS);
        }
    }

    public static String distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return String.format("%.2f", (dist/1000))+ " km";
    }
}
