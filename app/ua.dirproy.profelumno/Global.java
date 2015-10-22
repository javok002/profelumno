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
            Random randomizer = new Random();
            String[] address = generateAnAddress();

            for (int i = 0; i < lessons.size(); i++) {
                Date date = new Date((new Date()).getYear(), randomizer.nextInt(11), randomizer.nextInt(30) + 1);
                float price = (new Random()).nextFloat() * 1000;

                while (price < 100 && price > 500) {
                    price = (new Random()).nextFloat() * 1000;
                }
                lessons.get(i).setLessonState((i > (lessons.size() / 2)) ? 0 : 1);
                lessons.get(i).setPrice(price);
                lessons.get(i).setStudent(students.get((new Random()).nextInt(students.size())));
                lessons.get(i).setTeacher(teachers.get((new Random()).nextInt(teachers.size())));
                lessons.get(i).setComment("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum sit amet augue nisl. Sed ultrices rhoncus justo, in hendrerit turpis laoreet a.");
                lessons.get(i).setAddress(address[(new Random()).nextInt(address.length)]);
                lessons.get(i).setDuration(Duration.ofHours((i % 2 == 0) ? 1 : 2));
                lessons.get(i).setDateString("" + (date.getDay() + 1) + "/" + date.getMonth() + "/" + (date.getYear() + 1900));
                lessons.get(i).setDateTime(date);
                lessons.get(i).setSubject(subjects.get(randomizer.nextInt(subjects.size())));
                if (date.after(new Date())) {
                    lessons.get(i).setStudentReview(generateReview(0, date));//0 student
                    lessons.get(i).setTeacherReview(generateReview(1, date));//1 teacher
                }
                lessons.get(i).save();
            }

            Date today = new Date();
            for (int i = 0; i < students.size(); i++) {
                Lesson lessonP = new Lesson();
                lessonP.setAddress(generateAnAddress()[(new Random()).nextInt(generateAnAddress().length)]);
                lessonP.setLessonState(0);
                lessonP.setPrice((float) 100);
                lessonP.setStudent((students.get(i)));
                lessonP.setTeacher(teachers.get(i));
                lessonP.setComment("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum sit amet augue nisl. Sed ultrices rhoncus justo, in hendrerit turpis laoreet a.");
                lessonP.setDuration(Duration.ofHours(2));
                lessonP.setDateString("" + today.getDay() + "/" + (today.getMonth()) + "/" + (today.getYear() + 1900 + 1));
                lessonP.setDateTime(new Date(today.getYear(), today.getMonth(), today.getDay()));
                lessonP.setSubject(subjects.get((new Random()).nextInt(subjects.size())));
                lessonP.save();

                Lesson lessonConfirm = new Lesson();
                lessonConfirm.setAddress(generateAnAddress()[(new Random()).nextInt(generateAnAddress().length)]);
                lessonConfirm.setLessonState(1);
                lessonConfirm.setPrice((float) 100);
                lessonConfirm.setStudent(students.get(i));
                lessonConfirm.setTeacher(teachers.get(i));
                lessonConfirm.setComment("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum sit amet augue nisl. Sed ultrices rhoncus justo, in hendrerit turpis laoreet a.");
                lessonConfirm.setDuration(Duration.ofHours(2));
                lessonConfirm.setDateString("" + today.getDay() + "/" + (today.getMonth()) + "/" + today.getYear() + 1900);
                lessonConfirm.setDateTime(new Date(today.getYear(), today.getMonth(), today.getDay()));
                lessonConfirm.setSubject(subjects.get((new Random()).nextInt(subjects.size())));
                lessonConfirm.save();

                Lesson lessonComplete = new Lesson();
                lessonComplete.setAddress(generateAnAddress()[(new Random()).nextInt(generateAnAddress().length)]);
                lessonComplete.setLessonState(1);
                lessonComplete.setPrice((float) 100);
                lessonComplete.setStudent(students.get(i));
                lessonComplete.setTeacher(teachers.get(i));
                lessonComplete.setComment("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum sit amet augue nisl. Sed ultrices rhoncus justo, in hendrerit turpis laoreet a.");
                lessonComplete.setDuration(Duration.ofHours(1));
                lessonComplete.setDateString("" + today.getDay() + "/" + today.getMonth() + "/" + (today.getYear() + 1900 + 1));
                lessonComplete.setDateTime(new Date(today.getYear() + 1900 + 1, today.getMonth(), today.getDay()));
                lessonComplete.setSubject(subjects.get((new Random()).nextInt(subjects.size())));
                lessonComplete.setTeacherReview(generateReview(1, (new Date(today.getYear() + 1, today.getMonth(), today.getDay()))));
                lessonComplete.setStudentReview(generateReview(0, (new Date(today.getYear() + 1, today.getMonth(), today.getDay()))));
                lessonComplete.save();
            }

        }

        private static Review generateReview(int i, Date date) {
            int stars = (new Random()).nextInt(10);
            Review review = new Review();
            review.setComment((i == 1) ? generateReviewsT()[stars] : generateReviewsS()[stars]);
            review.setDate(date);
            review.setStars((long) (stars / 2));
            review.save();
            return review;
        }

        private static String[] generateReviewsS() {
            String[] comment = {
                    "UN DESASTRE, LLEGO TARDE Y NO EXPLICO NADA"
                    , "No aparecio"
                    , "No me explico nada"
                    , "No sabe lo que explica"
                    , "Llega tarde siempre, pero explica bien"
                    , "Muy buenas sus explicaciones"
                    , "Explica bien"
                    , "Un copado"
                    , "Aprobe gracias a el"
                    , "Muy buen profesor"

            };

            return comment;
        }

        private static String[] generateReviewsT() {
            String[] comment = {
                    "No presto atencion"
                    , "No se esfuerza"
                    , "Salio la noche anterior"
                    , "No se puede quedar sentado mas de 3 minutos"
                    , "Se duerme mientras le hablas"
                    , "Muy callado"
                    , "Poco atento"
                    , "Le cuesta entender pero se esfuersa"
                    , "Hace lo que le pedis"
                    , "Espero que apruebe"

            };

            return comment;
        }

        public static void insert(Application app) {
            setSubjects();
            generateLessonsList();

            String[] names = generateNameList();
            String[] lastNames = generateLastNameList();
            String[] address = generateAnAddress();


            if (User.finder.all().isEmpty()) {
                Random randomizer = new Random(0);

                for (int i = 1; i <= 100; i++) {
                    String name = names[randomizer.nextInt(names.length)];
                    String lastName = lastNames[randomizer.nextInt(lastNames.length)];

                    User userT = new User();
                    userT.setName(name);
                    userT.setSurname(lastName);
                    userT.setEmail("teacher" + i + "@sample.com");
                    userT.setPassword("secret");
                    userT.setAddress(address[randomizer.nextInt(address.length)]);
                    userT.setBirthday(new Date((70 + i/10), (i + 1) / 10, i/4));
                    userT.setGender("male");
                    userT.setSecureAnswer("Hola");
                    userT.setSecureQuestion("Mundo");
                    userT.setSubjects(subjects.subList(0, randomizer.nextInt(subjects.size())));
                    userT.save();
                    Teacher teacher = new Teacher();
                    teacher.setUser(userT);
                    if (i <= 20) {
                        teacher.setHasCard(true);
                        teacher.setRenewalDate(new Date());
                        teacher.setHomeClasses(true);
                        teacher.setPrice((new Random()).nextInt(500));
                    }

                    Teacher.updateLessonsDictated(teacher);
                    teacher.save();
                    teachers.add(teacher);
                }

                for (int k = 1; k <= 100; k++) {
                    String name = names[randomizer.nextInt(names.length)];
                    String lastName = lastNames[randomizer.nextInt(lastNames.length)];

                    User userS = new User();
                    userS.setName(name);
                    userS.setSurname(lastName);
                    userS.setEmail("student" + k + "@sample.com");
                    userS.setPassword("secret");
                    userS.setAddress(address[randomizer.nextInt(address.length)]);
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
