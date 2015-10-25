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


            for (Teacher teacher1 : teachers) {
                for (int j = 0; j < 3; j++) {
                    Date oldDateState1 = getOldDate(randomizer);
                    Date futureDateState0 = getFutureDate(randomizer);
                    Date futureDateState1 = getFutureDate(randomizer);

                    Lesson lessonInbox = new Lesson();
                    lessonInbox.setAddress(address[randomizer.nextInt(address.length)]);
                    lessonInbox.setLessonState(0);
                    lessonInbox.setPrice((float) 70);
                    lessonInbox.setStudent(students.get(randomizer.nextInt(students.size())));
                    lessonInbox.setTeacher(teacher1);
                    lessonInbox.setComment("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum sit amet augue nisl. Sed ultrices rhoncus justo, in hendrerit turpis laoreet a.");
                    lessonInbox.setDuration(Duration.ofHours(2));
                    lessonInbox.setDateString("" + futureDateState0.getDate() + "/" + (futureDateState0.getMonth() + 1) + "/" + (futureDateState0.getYear() + 1900));
                    lessonInbox.setDateTime(futureDateState0);
                    lessonInbox.setSubject(subjects.get((new Random()).nextInt(subjects.size())));
                    lessonInbox.save();

                    Lesson nextLesson = new Lesson();
                    nextLesson.setAddress(address[randomizer.nextInt(address.length)]);
                    nextLesson.setLessonState(1);
                    nextLesson.setPrice((float) 100);
                    nextLesson.setStudent(students.get(randomizer.nextInt(students.size())));
                    nextLesson.setTeacher(teacher1);
                    nextLesson.setComment("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum sit amet augue nisl. Sed ultrices rhoncus justo, in hendrerit turpis laoreet a.");
                    nextLesson.setDuration(Duration.ofHours(2));
                    nextLesson.setDateString("" + futureDateState1.getDate() + "/" + (futureDateState1.getMonth() + 1) + "/" + (futureDateState1.getYear() + 1900));
                    nextLesson.setDateTime(futureDateState1);
                    nextLesson.setSubject(subjects.get((new Random()).nextInt(subjects.size())));
                    nextLesson.save();

                    Lesson lessonFinished = new Lesson();
                    lessonFinished.setAddress(address[randomizer.nextInt(address.length)]);
                    lessonFinished.setLessonState(1);
                    lessonFinished.setPrice((float) 60);
                    Student tempS = students.get(randomizer.nextInt(students.size()));
                    lessonFinished.setStudent(tempS);
                    lessonFinished.setTeacher(teacher1);
                    lessonFinished.setComment("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum sit amet augue nisl. Sed ultrices rhoncus justo, in hendrerit turpis laoreet a.");
                    lessonFinished.setDuration(Duration.ofHours(1));
                    lessonFinished.setDateString("" + oldDateState1.getDate() + "/" + (oldDateState1.getMonth() + 1) + "/" + (oldDateState1.getYear() + 1900));
                    lessonFinished.setDateTime(oldDateState1);
                    lessonFinished.setSubject(subjects.get((new Random()).nextInt(subjects.size())));

                    int rand = randomizer.nextInt(4);
                    if (rand == 0) {
                        generateTeacherReview(teacher1, lessonFinished, oldDateState1);
                    } else if (rand == 1) {
                        generateStudentReview(tempS, lessonFinished, oldDateState1);
                    } else if (rand == 2) {
                        generateTeacherReview(teacher1, lessonFinished, oldDateState1);
                        generateStudentReview(tempS, lessonFinished, oldDateState1);
                    }

                    lessonFinished.save();
                }
            }

            for (Student student1 : students){
                for (int j = 0; j < 3; j++) {
                    Date oldDateState1 = getOldDate(randomizer);
                    Date futureDateState0 = getFutureDate(randomizer);
                    Date futureDateState1 = getFutureDate(randomizer);

                    Lesson lessonInbox = new Lesson();
                    lessonInbox.setAddress(address[randomizer.nextInt(address.length)]);
                    lessonInbox.setLessonState(0);
                    lessonInbox.setPrice((float) 70);
                    lessonInbox.setStudent(student1);
                    lessonInbox.setTeacher(teachers.get(randomizer.nextInt(teachers.size())));
                    lessonInbox.setComment("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum sit amet augue nisl. Sed ultrices rhoncus justo, in hendrerit turpis laoreet a.");
                    lessonInbox.setDuration(Duration.ofHours(2));
                    lessonInbox.setDateString("" + futureDateState0.getDate() + "/" + (futureDateState0.getMonth() + 1) + "/" + (futureDateState0.getYear() + 1900));
                    lessonInbox.setDateTime(futureDateState0);
                    lessonInbox.setSubject(subjects.get((new Random()).nextInt(subjects.size())));
                    lessonInbox.save();

                    Lesson nextLesson = new Lesson();
                    nextLesson.setAddress(address[randomizer.nextInt(address.length)]);
                    nextLesson.setLessonState(1);
                    nextLesson.setPrice((float) 100);
                    nextLesson.setStudent(student1);
                    nextLesson.setTeacher(teachers.get(randomizer.nextInt(teachers.size())));
                    nextLesson.setComment("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum sit amet augue nisl. Sed ultrices rhoncus justo, in hendrerit turpis laoreet a.");
                    nextLesson.setDuration(Duration.ofHours(2));
                    nextLesson.setDateString("" + futureDateState1.getDate() + "/" + (futureDateState1.getMonth() + 1) + "/" + (futureDateState1.getYear() + 1900));
                    nextLesson.setDateTime(futureDateState1);
                    nextLesson.setSubject(subjects.get((new Random()).nextInt(subjects.size())));
                    nextLesson.save();

                    Lesson lessonFinished = new Lesson();
                    lessonFinished.setAddress(address[randomizer.nextInt(address.length)]);
                    lessonFinished.setLessonState(1);
                    lessonFinished.setPrice((float) 60);
                    Teacher tempT = teachers.get(randomizer.nextInt(teachers.size()));
                    lessonFinished.setStudent(student1);
                    lessonFinished.setTeacher(tempT);
                    lessonFinished.setComment("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum sit amet augue nisl. Sed ultrices rhoncus justo, in hendrerit turpis laoreet a.");
                    lessonFinished.setDuration(Duration.ofHours(1));
                    lessonFinished.setDateString("" + oldDateState1.getDate() + "/" + (oldDateState1.getMonth() + 1) + "/" + (oldDateState1.getYear() + 1900));
                    lessonFinished.setDateTime(oldDateState1);
                    lessonFinished.setSubject(subjects.get((new Random()).nextInt(subjects.size())));

                    int rand = randomizer.nextInt(4);
                    if (rand == 0) {
                        generateTeacherReview(tempT, lessonFinished, oldDateState1);
                    } else if (rand == 1) {
                        generateStudentReview(student1, lessonFinished, oldDateState1);
                    } else if (rand == 2) {
                        generateTeacherReview(tempT, lessonFinished, oldDateState1);
                        generateStudentReview(student1, lessonFinished, oldDateState1);
                    }

                    lessonFinished.save();
                }
            }

            /*for (int i = 0; i < lessons.size(); i++) {
                Date date = new Date((new Date()).getYear(), randomizer.nextInt(11), randomizer.nextInt(31) + 1);
                int teacherNumber = (new Random()).nextInt(teachers.size());
                int studentNumber = (new Random()).nextInt(students.size());
                int addressNumber = randomizer.nextInt(address.length / 4);


                lessons.get(i).setLessonState((date.before(new Date())) ? 1 : 0);
                lessons.get(i).setPrice((float) (new Random()).nextInt(500));
                lessons.get(i).setStudent(students.get(studentNumber));
                lessons.get(i).setTeacher(teachers.get(teacherNumber));
                lessons.get(i).setComment("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum sit amet augue nisl. Sed ultrices rhoncus justo, in hendrerit turpis laoreet a.");
                lessons.get(i).setAddress(address[addressNumber * 4] + " " + address[(addressNumber * 4) + 1]);
                lessons.get(i).setDuration(Duration.ofHours((i % 2 == 0) ? 1 : 2));
                lessons.get(i).setDateString("" + (date.getDate()) + "/" + (date.getMonth()+1 )+ "/" + (date.getYear() + 1900));
                lessons.get(i).setDateTime(date);
                lessons.get(i).setSubject(subjects.get(randomizer.nextInt(subjects.size())));
                if (!date.after(new Date())) {
                    generateTeacherReview(teachers.get(teacherNumber), lessons.get(i), date);
                    generateStudentReview(students.get(studentNumber), lessons.get(i), date);

                    forRatingT[teacherNumber]++;
                    forRatingS[studentNumber]++;
                }
                lessons.get(i).save();
            }*/

            /*Date today = new Date();
            for (int i = 0; i < students.size(); i++) {
                int addressNumber = randomizer.nextInt(address.length / 4);

                Lesson lessonP = new Lesson();
                lessonP.setAddress(address[addressNumber * 4] + " " + address[(addressNumber * 4) + 1]);
                lessonP.setLessonState(0);
                lessonP.setPrice((float) 70);
                lessonP.setStudent((students.get(i)));
                lessonP.setTeacher(teachers.get(i));
                lessonP.setComment("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum sit amet augue nisl. Sed ultrices rhoncus justo, in hendrerit turpis laoreet a.");
                lessonP.setDuration(Duration.ofHours(2));
                lessonP.setDateString("" + today.getDate() + "/" + (today.getMonth() + 1) + "/" + (today.getYear() +1900  + 2));
                lessonP.setDateTime(new Date((today.getYear() + 2), (today.getMonth() + 1), today.getDate()));
                lessonP.setSubject(subjects.get((new Random()).nextInt(subjects.size())));
                lessonP.save();

                Lesson lessonConfirm = new Lesson();
                lessonConfirm.setAddress(address[addressNumber * 4] + " " + address[(addressNumber * 4) + 1]);
                lessonConfirm.setLessonState(1);
                lessonConfirm.setPrice((float) 100);
                lessonConfirm.setStudent(students.get(i));
                lessonConfirm.setTeacher(teachers.get(i));
                lessonConfirm.setComment("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum sit amet augue nisl. Sed ultrices rhoncus justo, in hendrerit turpis laoreet a.");
                lessonConfirm.setDuration(Duration.ofHours(2));
                lessonConfirm.setDateString("" + ((today.getDate() + 1 < 30) ? (today.getDate() + 1) : (today.getDate())) + "/" + (((today.getDate() + 1 < 30) ? (today.getMonth()) : (((today.getMonth() + 1) < 11) ? (today.getMonth() + 1) : (today.getMonth()))) + 1) + "/" + ((today.getMonth() + 1 < 11) ? (today.getYear() + 1900) : (today.getYear() + 1 + 1900)));
                lessonConfirm.setDateTime(new Date((today.getMonth() + 1 < 11) ? (today.getYear()) : (today.getYear() + 1), (today.getDate() + 1 < 30) ? (today.getMonth()) : ((today.getMonth() + 1 < 11) ? (today.getMonth() + 1) : (today.getMonth())), (today.getDate() + 1 < 30) ? (today.getDate() + 1) : (today.getDate())));
                lessonConfirm.setSubject(subjects.get((new Random()).nextInt(subjects.size())));
                lessonConfirm.save();

                Lesson lessonComplete = new Lesson();
                lessonComplete.setAddress(address[addressNumber * 4] + " " + address[(addressNumber * 4) + 1]);
                lessonComplete.setLessonState(1);
                lessonComplete.setPrice((float) 60);
                lessonComplete.setStudent(students.get(i));
                lessonComplete.setTeacher(teachers.get(i));
                lessonComplete.setComment("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum sit amet augue nisl. Sed ultrices rhoncus justo, in hendrerit turpis laoreet a.");
                lessonComplete.setDuration(Duration.ofHours(1));
                lessonComplete.setDateString("" + today.getDate() + "/" + (today.getMonth() + 1) + "/" + (today.getYear() + 1900 - 1));
                lessonComplete.setDateTime(new Date((today.getYear() - 1), (today.getMonth() + 1), today.getDate()));
                lessonComplete.setSubject(subjects.get((new Random()).nextInt(subjects.size())));
                Review reviewT=new Review();
                reviewT.setComment(generateReviewsS()[9]);
                reviewT.setDate(new Date((today.getYear() - 1), (today.getMonth() + 1), today.getDate()));
                reviewT.setStars((long) 5);
                reviewT.save();
                Review reviewS=new Review();
                reviewS.setComment(generateReviewsT()[9]);
                reviewS.setDate(new Date((today.getYear() - 1), (today.getMonth() + 1), today.getDate()));
                reviewS.setStars((long) 5);
                reviewS.save();
                lessonComplete.setTeacherReview(reviewT);
                lessonComplete.setStudentReview(reviewS);
                forRatingS[i]++;
                forRatingT[i]++;
                teachers.get(i).setRanking((int) (teachers.get(i).getRanking() + lessonComplete.getTeacherReview().getStars()));
                lessonComplete.save();
            }*/

            for (Teacher teacher : teachers) {
                Teacher.updateLessonsDictated(teacher);
            }

        }

        private static int[] arrayRating(List list) {
            int[] array = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = 0;
            }
            return array;
        }

        private static void generateTeacherReview(Teacher teacher, Lesson lesson, Date date){
            int stars = (new Random()).nextInt(10);
            Review review = new Review();
            review.setComment(generateCommentsForTeacher()[stars]);
            review.setDate(date);
            review.setStars((long) ((stars + 1) / 2));
            review.save();

            User temp = teacher.getUser();
            temp.addStars((long) ((stars + 1) / 2));
            temp.incrementReviews();
            temp.save();

            teacher.setUser(temp);
            teacher.save();

            lesson.setTeacherReview(review);
        }

        private static void generateStudentReview(Student student, Lesson lesson, Date date){
            int stars = (new Random()).nextInt(10);
            Review review = new Review();
            review.setComment(generateCommentsForStudent()[stars]);
            review.setDate(date);
            review.setStars((long) ((stars + 1) / 2));
            review.save();

            User temp = student.getUser();
            temp.addStars((long) ((stars + 1) / 2));
            temp.incrementReviews();
            temp.save();

            student.setUser(temp);
            student.save();

            lesson.setStudentReview(review);
        }

        private static String[] generateCommentsForTeacher() {
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

        private static String[] generateCommentsForStudent() {
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
                Random randomizer = new Random();

                for (int i = 1; i <= 100; i++) {
                    String name = names[randomizer.nextInt(names.length)];
                    String lastName = lastNames[randomizer.nextInt(lastNames.length)];
                    int addressNumber = randomizer.nextInt(address.length / 4);

                    User userT = new User();
                    userT.setName(name);
                    userT.setSurname(lastName);
                    userT.setEmail("teacher" + i + "@sample.com");
                    userT.setPassword("secret");
                    userT.setAddress(address[addressNumber * 4]);
                    userT.setCity(address[(addressNumber * 4) + 1]);
                    userT.setLatitude(address[(addressNumber * 4) + 2]);
                    userT.setLongitude(address[(addressNumber * 4) + 3]);
                    userT.setBirthday(new Date((70 + i / 10), (i + 1) / 10, i / 4));
                    userT.setGender("male");
                    userT.setSecureAnswer("Hola");
                    userT.setSecureQuestion("Mundo");
                    userT.setSubjects(subjects.subList(0, subjects.size()));
                    userT.save();
                    Teacher teacher = new Teacher();
                    teacher.setUser(userT);
                    if (i <= 20) {
                        teacher.setHasCard(true);
                        teacher.setSubscription("" + (new Random()).nextInt(9999) + new Random().nextInt(9999) + new Random().nextInt(9999) + new Random().nextInt(9999));
                        teacher.setHomeClasses(true);
                        teacher.setPrice((new Random()).nextInt(500));
                        teacher.setRenewalDate(new Date(new Date().getYear() + 1, new Date().getMonth(), new Date().getDate()));
                    }

                    Teacher.updateLessonsDictated(teacher);
                    teacher.save();
                    teachers.add(teacher);
                }

                for (int k = 1; k <= 100; k++) {
                    String name = names[randomizer.nextInt(names.length)];
                    String lastName = lastNames[randomizer.nextInt(lastNames.length)];
                    int addressNumber = randomizer.nextInt(address.length / 4);

                    User userS = new User();
                    userS.setName(name);
                    userS.setSurname(lastName);
                    userS.setEmail("student" + k + "@sample.com");
                    userS.setPassword("secret");
                    userS.setAddress(address[addressNumber * 4]);
                    userS.setCity(address[(addressNumber * 4) + 1]);
                    userS.setLatitude(address[(addressNumber * 4) + 2]);
                    userS.setLongitude(address[(addressNumber * 4) + 3]);
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
            for (int i = 0; i < 800; i++) {
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

        private static Date getOldDate(Random random) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date()); // Now use today date.
            c.add(Calendar.DATE, (random.nextInt(15) * -1) - 1); // Subtract 1 to 15 days

            return new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        }

        private static Date getFutureDate(Random random) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date()); // Now use today date.
            c.add(Calendar.DATE, random.nextInt(15) + 1); // Adds from 1 to 15 days

            return new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        }
    }
}
