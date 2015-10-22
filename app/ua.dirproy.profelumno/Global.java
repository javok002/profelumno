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
            String[] subjectName = generateSubjects();

            if (Subject.finder.all().isEmpty()) {

                for (int i = 0; i < subjectName.length; i++) {
                    final Subject subject = new Subject();
                    subjects.add(subject);
                    subjects.get(i).setName(subjectName[i]);
                    subject.save();
                }
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
                    Teacher.updateLessonsDictated(teacher);
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
            String[] last = {"Garcia", "Gonzalez", "Rodriguez", "Fernandez", "Lopez", "Martinez", "Sanchez", "Perez", "Gomez",
                    "Martin", "Jimenez", "Ruiz", "Hernandez", "Diaz", "Moreno", "Alvarez", "Muñoz", "Romero", "Alonso",
                    "Gutierrez", "Navarro", "Torres", "Dominguez", "Vazquez", "Ramos", "Gil", "Ramirez", "Serrano", "Blanco",
                    "Suarez", "Molina", "Morales", "Ortega", "Delgado", "Castro", "Ortiz", "Rubio", "Marin", "Sanz",
                    "Iglesias", "Nuñez", "Medina", "Garrido", "Santos", "Castillo", "Cortes", "Lozano", "Guerrero", "Cano",
                    "Prieto", "Mendez", "Calvo", "Gallego", "Vidal", "Cruz", "Leon", "Herrera", "Marquez", "Peña", "Cabrera"
            };
            return last;
        }

        private static String[] generateNameList() {
            String[] names = {"Jose", "Manuel", "Francisco", "Juan", "David", "Jose Antonio", "Jose Luis", "Javier",
                    "Jesus", "Carlos", "Daniel", "Miguel", "Rafael", "Jose Manuel", "Pedro", "Alejandro", "Angel",
                    "Miguel Angel", "Jose Maria", "Fernando", "Luis", "Pablo", "Sergio", "Jorge", "Alberto", "Juan Carlos",
                    "Juan Jose", "Ramon", "Enrique", "Diego", "Juan Antonio", "Vicente", "Alvaro", "Raul", "Adrian",
                    "Joaquin", "Ivan", "Andres", "Oscar", "Ruben", "Juan Manuel", "Santiago", "Eduardo", "Victor", "Roberto",
                    "Jaime", "Francisco Jose", "Ignacio", "Alfonso", "Salvador", "Ricardo", "Mario", "Benito"
            };
            return names;
        }


        private static String[] generateSubjects() {
            String[] subjectList = {"Análisis Matemático I", "Análisis Matemático II", "Análisis Matemático III",
                    "Programación I", "Programación II", "Álgebra I", "Álgebra II", "Física I", "Física II", "Física III",
                    "Química", "Laboratorio I", "Laboratorio II", "Estadística", "Organización de Computadoras",
                    "Sistemas Operativos", "Base de Datos", "LCD", "Teologia I", "Teologia II"
            };
            return subjectList;
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
    }
}
