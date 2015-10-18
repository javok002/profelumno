package ua.dirproy.profelumno;

import play.Application;
import play.GlobalSettings;
import ua.dirproy.profelumno.common.models.Lesson;
import ua.dirproy.profelumno.common.models.Review;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.user.models.Subject;
import ua.dirproy.profelumno.user.models.User;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        public static void setLessonsAndReviews(){
            final Teacher teacher = Teacher.finder.where().eq("user.email", "teacher1@sample.com").findUnique();

            final Student student = Student.finder.where().eq("user.email", "student1@sample.com").findUnique();

            final Lesson lesson1 = new Lesson();
            final Lesson lesson2 = new Lesson();
            final Lesson lesson3 = new Lesson();
            final Lesson lesson4 = new Lesson();
            final Lesson lesson5 = new Lesson();

            lesson1.setDateTime(new Date(115, 8, 27));
            lesson2.setDateTime(new Date(115, 8, 28));
            lesson3.setDateTime(new Date(115, 8, 29));
            lesson4.setDateTime(new Date(115, 10, 26));
            lesson5.setDateTime(new Date(115, 9, 1));

            lesson1.setPrice(new Float(100.0));
            lesson2.setPrice(new Float(110.0));
            lesson3.setPrice(new Float(120.0));
            lesson4.setPrice(new Float(130.5));
            lesson5.setPrice(new Float(155.5));

            lesson1.setStudent(student);
            lesson2.setStudent(student);
            lesson3.setStudent(student);
            lesson4.setStudent(student);
            lesson5.setStudent(student);

            lesson1.setTeacher(teacher);
            lesson2.setTeacher(teacher);
            lesson3.setTeacher(teacher);
            lesson4.setTeacher(teacher);
            lesson5.setTeacher(teacher);

            final Review review1 = new Review();
            review1.setComment("El alumno estuvo muy atento");
            review1.setDate(new Date(115, 8, 28));
            review1.setStars((long) 5);
            review1.save();

            final Review review2 = new Review();
            review2.setComment("El profesor me ayudo a sacarme un 8 en la prueba");
            review2.setDate(new Date(115, 8, 28));
            review2.setStars((long) 4);
            review2.save();

            final Review review3 = new Review();
            review3.setComment("El profesor es mal educado");
            review3.setDate(new Date(115, 8, 29));
            review3.setStars((long) 2);
            review3.save();

            final Review review4 = new Review();
            review4.setComment("El alumno no presta atención");
            review4.setDate(new Date(115, 8, 30));
            review4.setStars((long) 3);
            review4.save();

            Duration oneHour = Duration.ofHours(1);

            lesson1.setStudentReview(review1);
            lesson1.setTeacherReview(review2);
            lesson1.setDuration(oneHour);
            lesson1.save();

            lesson2.setTeacherReview(review3);
            lesson2.setDuration(oneHour);
            lesson2.save();

            lesson3.setStudentReview(review4);
            lesson3.setDuration(oneHour);
            lesson3.save();

            lesson4.setDuration(oneHour);
            lesson4.save();

            lesson5.setDuration(oneHour);
            lesson5.save();
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

            setLessonsAndReviews();
        }


    }
}
