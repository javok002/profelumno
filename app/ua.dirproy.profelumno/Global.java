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
import java.util.Random;

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
        private static List<Lesson> lessons = new ArrayList<>();
        private static List<Teacher> teachers = new ArrayList<>();
        private static List<Student> students = new ArrayList<>();


        public static void setSubjects() {

            if (Subject.finder.all().isEmpty()) {
                final Subject subject1 = new Subject();
                subject1.setName("MATEMATICA");
                subject1.save();
                final Subject subject2 = new Subject();
                subject2.setName("ALGEBRA");
                subject2.save();
                final Subject subject3 = new Subject();
                subject3.setName("LCD");
                subject3.save();
                final Subject subject4 = new Subject();
                subject4.setName("ANAYDIS");
                subject4.save();
                final Subject subject5 = new Subject();
                subject5.setName("LAB2");
                subject5.save();
                final Subject subject6 = new Subject();
                subject6.setName("FISICA");
                subject6.save();
                final Subject subject7 = new Subject();
                subject7.setName("QUIMICA");
                subject7.save();
                final Subject subject8 = new Subject();
                subject8.setName("GEOGRAFIA");
                subject8.save();
                final Subject subject9 = new Subject();
                subject9.setName("LENGUA");
                subject9.save();
                final Subject subject0 = new Subject();
                subject0.setName("TEOLOGIA");
                subject0.save();

                subjects.add(0, subject1);
                subjects.add(1, subject2);
                subjects.add(2, subject3);
                subjects.add(3, subject4);
                subjects.add(4, subject5);
                subjects.add(5, subject6);
                subjects.add(6, subject7);
                subjects.add(7, subject8);
                subjects.add(8, subject9);
                subjects.add(9, subject0);

            }
        }

        public static void setLessonsAndReviews() {
            final Random randomizer = new Random();
            final Date date = new Date((new Date()).getYear(), randomizer.nextInt(11), randomizer.nextInt(30) + 1);
            String[] address = generateAnAddress();

            for (int i = 0; i < lessons.size(); i++) {
                int index = (int) (Math.random() * address.length);
                float price = (new Random()).nextFloat() * 1000;

                while (price < 100 && price > 500) {
                    price = (new Random()).nextFloat() * 1000;
                }
                lessons.get(i).setLessonState((i > (lessons.size() / 2)) ? 0 : 1);
                lessons.get(i).setPrice(price);
                lessons.get(i).setStudent(students.get((new Random()).nextInt(students.size())));
                lessons.get(i).setTeacher(teachers.get((new Random()).nextInt(teachers.size())));
                lessons.get(i).setComment("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum sit amet augue nisl. Sed ultrices rhoncus justo, in hendrerit turpis laoreet a.");
                lessons.get(i).setAddress(address[index]);
                lessons.get(i).setDuration(Duration.ofHours((i % 2 == 0) ? 1 : 2));
                lessons.get(i).setDateString("" + (date.getDay() + 1) + "/" + date.getMonth() + "/" + date.getYear());
                lessons.get(i).setDateTime(date);
                lessons.get(i).setSubject(subjects.get(randomizer.nextInt(subjects.size())));
                if (date.after(new Date())) {
                    lessons.get(i).setStudentReview(generateReview(0, date));//0 student
                    lessons.get(i).setTeacherReview(generateReview(1, date));//1 teacher
                }
                lessons.get(i).save();
            }


        }

        private static String[] generateAnAddress() {
            String[] address = {
                    "Av. Juan Domingo Perón 1500, Buenos Aires",
                    "El Tordo 55, Pilar Centro, Buenos Aires",
                    "San Martín 744, Pilar Centro, Buenos Aires",
                    "Yrigoyen 2749, Vicente Lopez, Buenos Aires",
                    "Ballivian 2329, Villa Ortuzar, Buenos Aires",
                    "El Salvador 5528, Palermo, Buenos Aires",
                    "Cuba 2039, Belgrano, Buenos Aires",
                    "Defensa 1431, San Telmo, Buenos Aires"
            };
            return address;
        }

        private static Review generateReview(int i, Date date) {
            final Random randomizer = new Random();

            generateReviewsS();
            generateReviewsT();
            Review review = (i == 1) ? generateReviewsT().get(randomizer.nextInt(generateReviewsT().size())) : generateReviewsS().get(randomizer.nextInt(generateReviewsT().size()));
            review.setDate(date);
            review.setStars((long) randomizer.nextInt(5));
            review.save();
            return review;
        }

        private static List<Review> generateReviewsS() {
            String[] comment = {
                    "Muy buen profesor"
                    , "Un copado"
                    , "No me explico nada"
                    , "UN DESASTRE, LLEGO TARDE Y NO EXPLICO NADA"
                    , "Muy buenas sus explicaciones"
                    , "Llega tarde siempre, pero explica bien"
                    , "No sabe lo que explica"
                    , "No aparecio"
                    , "Explica bien"
                    , "Aprobe gracias a el"
            };

            List<Review> list = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                list.add(new Review());
            }
            for (int i = 0; i < 10; i++) {
                (list.get(i)).setComment(comment[i]);
                (list.get(i)).setComment(comment[i]);
            }
            return list;
        }

        private static List<Review> generateReviewsT() {
            String[] comment = {
                    "No presto atencion"
                    , "Hace lo que le pedis"
                    , "Se duerme mientras le hablas"
                    , "No se esfuerza"
                    , "Le cuesta entender pero se esfuersa"
                    , "Espero que apruebe"
                    , "Salio la noche anterior"
                    , "Muy callado"
                    , "Poco atento"
                    , "No se puede quedar sentado mas de 3 minutos"
            };

            List<Review> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                list.add(new Review());
            }
            for (int i = 0; i < 10; i++) {
                (list.get(i)).setComment(comment[i]);
            }
            return list;
        }

        public static void insert(Application app) {
            setSubjects();
            generateLessonsList();

            String[] names = generateNameList();
            String[] lastNames = generateLastNameList();
            String[] address = generateAnAddress();


            if (User.finder.all().isEmpty()) {
                final Random randomizer = new Random(0);

                for (int i = 1; i <= 20; i++) {
                    int index = (int) (Math.random() * names.length);
                    int indexL = (int) (Math.random() * lastNames.length);
                    int indexA = (int) (Math.random() * address.length);

                    String name = names[index];
                    String lastName = lastNames[indexL];


                    User userT = new User();
                    userT.setName(name);
                    userT.setSurname(lastName);
                    userT.setEmail("teacher" + i + "@sample.com");
                    userT.setPassword("secret");
                    userT.setAddress(address[indexA]);
                    userT.setBirthday(new Date((70 + i), (i + 1) / 2, i));
                    userT.setGender("male");
                    userT.setSecureAnswer("Hola");
                    userT.setSecureQuestion("Mundo");
                    userT.setSubjects(subjects.subList(0, randomizer.nextInt(subjects.size())));
                    userT.save();
                    Teacher teacher = new Teacher();
                    teacher.setUser(userT);
                    if (i <= 10) {
                        teacher.setHasCard(true);
                        teacher.setRenewalDate(new Date());
                        teacher.setHomeClasses(true);
                    }
                    teacher.save();
                    teachers.add(teacher);
                }

                for (int k = 1; k <= 80; k++) {
                    int index = (int) (Math.random() * names.length);
                    int indexL = (int) (Math.random() * lastNames.length);
                    int indexA = (int) (Math.random() * address.length);

                    String name = names[index];
                    String lastName = lastNames[indexL];

                    User userS = new User();
                    userS.setName(name);
                    userS.setSurname(lastName);
                    userS.setEmail("student" + k + "@sample.com");
                    userS.setPassword("secret");
                    userS.setAddress(address[indexA]);
                    userS.setBirthday(new Date((93), 5, 10));
                    userS.setGender("male");
                    userS.setSecureAnswer("Hola");
                    userS.setSecureQuestion("Mundo");
                    userS.setSubjects(subjects.subList(0, randomizer.nextInt(subjects.size())));
                    userS.save();
                    Student student = new Student();
                    student.setUser(userS);
                    student.save();
                    students.add(student);
                }
            }

            setLessonsAndReviews();
        }

        private static void generateLessonsList() {
            for (int i = 0; i < 200; i++) {
                lessons.add(new Lesson());
            }

        }

        private static String[] generateLastNameList() {
            String[] last = {"GARCIA", "GONZALEZ", "RODRIGUEZ", "FERNANDEZ", "LOPEZ", "MARTINEZ", "SANCHEZ", "PEREZ", "GOMEZ",
                    "MARTIN", "JIMENEZ", "RUIZ", "HERNANDEZ", "DIAZ", "MORENO", "ALVAREZ", "MUÑOZ", "ROMERO", "ALONSO",
                    "GUTIERREZ", "NAVARRO", "TORRES", "DOMINGUEZ", "VAZQUEZ", "RAMOS", "GIL", "RAMIREZ", "SERRANO", "BLANCO",
                    "SUAREZ", "MOLINA", "MORALES", "ORTEGA", "DELGADO", "CASTRO", "ORTIZ", "RUBIO", "MARIN", "SANZ",
                    "IGLESIAS", "NUÑEZ", "MEDINA", "GARRIDO", "SANTOS", "CASTILLO", "CORTES", "LOZANO", "GUERRERO", "CANO",
                    "PRIETO", "MENDEZ", "CALVO", "GALLEGO", "VIDAL", "CRUZ", "LEON", "HERRERA", "MARQUEZ", "PEÑA", "CABRERA"
            };
            return last;
        }


        private static String[] generateNameList() {
            String[] names = {"JOSE", "MANUEL", "FRANCISCO", "JUAN", "DAVID", "JOSE ANTONIO", "JOSE LUIS", "JAVIER",
                    "JESUS", "CARLOS", "DANIEL", "MIGUEL", "RAFAEL", "JOSE MANUEL", "PEDRO", "ALEJANDRO", "ANGEL",
                    "MIGUEL ANGEL", "JOSE MARIA", "FERNANDO", "LUIS", "PABLO", "SERGIO", "JORGE", "ALBERTO", "JUAN CARLOS",
                    "JUAN JOSE", "RAMON", "ENRIQUE", "DIEGO", "JUAN ANTONIO", "VICENTE", "ALVARO", "RAUL", "ADRIAN",
                    "JOAQUIN", "IVAN", "ANDRES", "OSCAR", "RUBEN", "JUAN MANUEL", "SANTIAGO", "EDUARDO", "VICTOR", "ROBERTO",
                    "JAIME", "FRANCISCO JOSE", "IGNACIO", "ALFONSO", "SALVADOR", "RICARDO", "MARIO", "PAU", "BENITO"
            };
            return names;
        }
    }
}
