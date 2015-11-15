package ua.dirproy.profelumno;

import com.avaje.ebean.Expr;
import play.Application;
import play.GlobalSettings;
import ua.dirproy.profelumno.common.models.*;
import ua.dirproy.profelumno.recommend.controllers.Recommend;
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

        Recommend recommend = new Recommend();
        recommend.weMissYou();
        recommend.doRecommendations();
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
            String[] address = generateAnAddressT();


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
                    lessonInbox.setDurationLesson("2");
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
                    nextLesson.setDurationLesson("2");
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
                    lessonFinished.setDurationLesson("1");
                    lessonFinished.setDateString("" + oldDateState1.getDate() + "/" + (oldDateState1.getMonth() + 1) + "/" + (oldDateState1.getYear() + 1900));
                    lessonFinished.setDateTime(oldDateState1);
                    lessonFinished.setSubject(subjects.get((new Random()).nextInt(subjects.size())));

                    String[] conver = genereateConversationList();
                    if (true) {
                        Chat chat = Chat.finder.where().and(Expr.eq("teacher", lessonFinished.getTeacher()),
                                Expr.eq("student", lessonFinished.getStudent())).findUnique();
                        if (chat == null) {
                            chat = new Chat();
                            chat.setTeacher(lessonFinished.getTeacher());
                            chat.setStudent(lessonFinished.getStudent());
                            int typeOfConversation = randomizer.nextInt(conver.length);
                            if (typeOfConversation % 2 != 0) {
                                String[] c = conver[typeOfConversation].split(";");
                                for (int i = 0; i < (c.length); i++) {
                                    chat.addMessage(c[i], lessonFinished.getTeacher().getUser());
                                    i++;
                                    if (i>=c.length)break;
                                    chat.addMessage(c[i], lessonFinished.getStudent().getUser());
                                }
                            } else {
                                String[] c = conver[typeOfConversation].split(";");
                                for (int i = 0; i < (c.length); i++) {
                                    chat.addMessage(c[i], lessonFinished.getStudent().getUser());
                                    i++;
                                    if (i>=c.length)break;
                                    chat.addMessage(c[i], lessonFinished.getTeacher().getUser());

                                }
                            }
                        }
                    }

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
            String[] address = generateAnAddressT();


            if (User.finder.all().isEmpty()) {
                Random randomizer = new Random();

                for (int i = 1; i <= 100; i++) {
                    String name = names[randomizer.nextInt(names.length)];
                    String lastName = lastNames[randomizer.nextInt(lastNames.length)];
                    int addressNumber = (new Random().nextInt(7)); // OJO, addressNumber es siempre 0!!!

                    User userT = new User();
                    userT.setName(name);
                    userT.setSurname(lastName);
                    userT.setEmail("teacher" + i + "@sample.com");
                    userT.setPassword("secret");
                    userT.setAddress(address[addressNumber]);
                    userT.setCity(address[addressNumber]);
                    userT.setLatitude(generateLatitudes()[addressNumber]); // OJO, addressNumber es siempre 0!!!
                    userT.setLongitude(generatLongituds()[addressNumber]); // OJO, addressNumber es siempre 0!!!
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
                    int addressNumber = (new Random().nextInt(6))+7;

                    User userS = new User();
                    userS.setName(name);
                    userS.setSurname(lastName);
                    userS.setEmail("student" + k + "@sample.com");
                    userS.setPassword("secret");
                    userS.setAddress(address[addressNumber]);
                    userS.setCity(address[addressNumber]);
                    userS.setLatitude(generateLatitudes()[addressNumber]);
                    userS.setLongitude(generatLongituds()[addressNumber]);
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

        private static String[] genereateConversationList() {
            String[] conversation = {
                    ";Buenas tardes, quisiera saber por que no vino a la ultima clase; Disculpa, es que estaba enfermo; Cuando la queres recuperar?; Cuando me sienta mejor le confirmo",
                    ";Buenas tardes,  Como le va?; Bien,  Como te fue en el examen?; Mal, fue bastante dificil",
                    ";Como te fue en el examen?; Mas o menos, era medio complicado; Que te tomaron?; Ecuaciones diferenciales y grafos; Pero no me dijiste que te tomaban grafos;Es que lo explico el dia anterior; Vas a querer otra clase?; Despues lo veo",
                    ";Buenos dias, Cuando podriamos tener otra clase?; Podriamos tenerla en dos semanas; Bueno, ya te la reservo"
            };
            return conversation;
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

        private static String[] generateAnAddressT() {
            String[] address = {
                    "Av. Juan Domingo Perón 1500, Buenos Aires",
                    "El Tordo 55, Pilar Centro, Buenos Aires",
                    "San Martín 744, Pilar Centro, Buenos Aires",
                    "Ballivian 2329, Villa Ortuzar, Buenos Aires",
                    "El Salvador 5528, Palermo, Buenos Aires",
                    "Cuba 2039, Belgrano, Buenos Aires",
                    "Defensa 1431, San Telmo, Buenos Aires",


                    "San Martin 1805 - Santa Fe, Buenos Aires",
                    "Rodríguez Peña 1439, Buenos Aires",
                    "Dorrego 2031 | Rosario, Santa Fe, Argentina",
                    "Ayacucho 685 San Miguel de Tucumán Argentina ",
                    "Cdad. de La Paz 1866, Belgrano, Capital Federal",
                    "Clay 3082, CABA, Argentina",
            };
            return address;
        }
        private static String[] generateAnAddressS() {
            String[] address = {
                    "San Martin 1805 - Santa Fe, Buenos Aires",
                    "Rodríguez Peña 1439, Buenos Aires",
                    "Dorrego 2031 | Rosario, Santa Fe, Argentina",
                    "Ayacucho 685 San Miguel de Tucumán Argentina ",
                    "Cdad. de La Paz 1866, Belgrano, Capital Federal",
                    "Clay 3082, CABA, Argentina",
            };
            return address;
        }

        private static double[] generateLatitudes() {
            double[] latitudes = {
                    -34.5,
                    -34.4496,
                    -34.4597,
                    -34.5787,
                    -34.5838,
                    -34.5614,
                    -34.6245,

                    -31.6546,
                    -34.5930,
                    -32.9591,
                    -26.8392,
                    -34.5648,
                    -34.5725
            };
            return latitudes;
        }

        private static double[] generatLongituds() {
            double[] longitudes = {
                    -58.8,
                    -58.9137,
                    -58.9166,
                    -58.4781,
                    -58.4369,
                    -58.4564,
                    -58.3734,

                    -60.7090,
                    -58.3913,
                    -60.6529,
                    -65.2115,
                    -58.4557,
                    -58.4286
            };
            return longitudes;
        }

        private static Date getOldDate(Random random) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date()); // Now use today date.
            c.add(Calendar.DATE, (random.nextInt(15) * -1) - 1); // Subtract 1 to 15 days

            return new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),random.nextInt(23),0);
        }

        private static Date getFutureDate(Random random) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date()); // Now use today date.
            c.add(Calendar.DATE, random.nextInt(15) + 1); // Adds from 1 to 15 days

            return new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),random.nextInt(23),0);
        }
    }
}
