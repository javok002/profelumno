package ua.dirproy.profelumno;

import play.Application;
import play.GlobalSettings;
import ua.dirproy.profelumno.common.models.Lesson;
import ua.dirproy.profelumno.common.models.Review;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.user.models.Subject;
import ua.dirproy.profelumno.user.models.User;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Santiago Ambrosetti
 * Date: 9/28/15
 */
public class Global extends GlobalSettings {
    @Override
    public void beforeStart(Application app) {
        InitialData.insert(app);
    }

    static class InitialData {
        private static List<Subject> subjects = new ArrayList<>();

        public static void setSubjects() {

            if(Subject.finder.all().isEmpty()) {
                Subject subject1 = new Subject();
                subject1.setName("Matematica");
                subject1.save();

                Subject subject2 = new Subject();
                subject2.setName("Algebra");
                subject2.save();
                Subject subject3 = new Subject();
                subject3.setName("LCD");
                subject3.save();
                Subject subject4 = new Subject();
                subject4.setName("Anaydis");
                subject4.save();
                Subject subject5 = new Subject();
                subject5.setName("Lab2");
                subject5.save();

                subjects.add(0, subject1);
                subjects.add(1, subject2);
                subjects.add(2, subject3);
                subjects.add(3, subject4);
                subjects.add(4, subject5);

            }
        }

        public static void insert(Application app) {

            setSubjects();

            if (User.finder.all().isEmpty()) {

                for (long i = 1; i <= 20; i++) {
                    User userT = new User();
                    userT.setName("Teacher");
                    userT.setSurname("" + i);
                    userT.setEmail("teacher" + i + "@sample.com");
                    userT.setPassword("secret");
                    userT.setAddress("Buenos Aires");
                    userT.setBirthday(new Date());
                    userT.setGender("Masculino");
                    userT.setSecureAnswer("Hola");
                    userT.setSecureQuestion("Mundo");
                    userT.setSubjects(subjects.subList(0, (int) (Math.random() * 5)));
                    userT.save();
                    Teacher teacher = new Teacher();
                    teacher.setUser(userT);
                    teacher.save();
                }

                for (long k = 1; k <= 80; k++) {
                    User userS = new User();
                    userS.setName("Student");
                    userS.setSurname("" + k);
                    userS.setEmail("student" + k + "@sample.com");
                    userS.setPassword("secret");
                    userS.setAddress("Buenos Aires");
                    userS.setBirthday(new Date());
                    userS.setGender("Masculino");
                    userS.setSecureAnswer("Hola");
                    userS.setSecureQuestion("Mundo");
                    userS.save();
                    Student student = new Student();
                    student.setUser(userS);
                    student.save();
                }

            }


        }


    }
}
