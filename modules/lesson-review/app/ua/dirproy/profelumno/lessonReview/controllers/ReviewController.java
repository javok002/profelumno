package ua.dirproy.profelumno.lessonReview.controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Lesson;
import ua.dirproy.profelumno.common.models.Review;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.user.models.User;

import java.sql.Time;
import java.time.Duration;
import java.util.Date;
import java.util.Iterator;

public class ReviewController extends Controller {

    public static Result show(){
        return ok(ua.dirproy.profelumno.lessonReview.views.html.reviews.render());
    }

    public static Result getReviewedLessons(){
        final Long userId = Long.parseLong(session().get("id"));
        final Teacher teacher = Teacher.finder.where().eq("user.id", userId).findUnique();
        final Iterator<Lesson> lessons;
        final ArrayNode results = Json.newArray();

        if (teacher != null){
            lessons = Lesson.finder.where().eq("teacher", teacher).ne("teacherReview", null)
                    .findList().iterator();
        } else {
            final Student student = Student.finder.where().eq("user.id", userId).findUnique();
            lessons = Lesson.finder.where().eq("student", student).ne("studentReview", null)
                    .findList().iterator();
        }

        while (lessons.hasNext()){
            final Lesson temp = lessons.next();
            final ObjectNode node = Json.newObject();
            final String date;
            final String comment;
            final long stars;

            if (teacher != null){
                node.put("userReviewed", temp.getStudent().getUser().getName() + " "
                        + temp.getStudent().getUser().getSurname());
                date = temp.getTeacherReview().getDate().getDate() + "/"
                        + (temp.getTeacherReview().getDate().getMonth() + 1)
                        + "/" + (temp.getTeacherReview().getDate().getYear() + 1900);

                comment = temp.getTeacherReview().getComment();
                stars = temp.getTeacherReview().getStars();
            } else {
                node.put("userReviewed", temp.getTeacher().getUser().getName() + " "
                        + temp.getTeacher().getUser().getSurname());
                date = temp.getStudentReview().getDate().getDate() + "/"
                        + (temp.getStudentReview().getDate().getMonth() + 1)
                        + "/" + (temp.getStudentReview().getDate().getYear() + 1900);
                comment = temp.getStudentReview().getComment();
                stars = temp.getStudentReview().getStars();
            }

            node.put("reviewDate", date);
            node.put("reviewComment", comment);
            node.put("reviewStars", stars);
            results.add(node);
        }

        return ok(results);
    }

    public static Result getNonReviewedLessons(){
        final Long userId = Long.parseLong(session().get("id"));
        final Teacher teacher = Teacher.finder.where().eq("user.id", userId).findUnique();
        final Iterator<Lesson> lessons;
        final ArrayNode results = Json.newArray();

        if (teacher != null){
            lessons = Lesson.finder.where().eq("teacher", teacher).eq("studentReview", null)
                    .lt("dateTime", new Date()).findList().iterator();
        } else {
            final Student student = Student.finder.where().eq("user.id", userId).findUnique();
            lessons = Lesson.finder.where().eq("student", student).eq("teacherReview", null)
                    .lt("dateTime", new Date()).findList().iterator();
        }

        while (lessons.hasNext()){
            final Lesson temp = lessons.next();
            final ObjectNode node = Json.newObject();
            final String date = temp.getDateTime().getDate() + "/" + (temp.getDateTime().getMonth() + 1)
                    + "/" + (temp.getDateTime().getYear() + 1900);
            node.put("teacherName", temp.getTeacher().getUser().getName() + " "
                    + temp.getTeacher().getUser().getSurname());
            node.put("studentName", temp.getStudent().getUser().getName() + " "
                    + temp.getStudent().getUser().getSurname());
            node.put("lessonDate", date);
            node.put("lessonDuration", temp.getDuration().toHours());
            node.put("lessonPrice", temp.getPrice());
            node.put("lessonId", temp.getId());

            if (teacher != null){
                node.put("userToReview", temp.getStudent().getUser().getEmail());
            } else {
                node.put("userToReview", temp.getTeacher().getUser().getEmail());
            }

            results.add(node);
        }

        return ok(results);
    }

    public static Result getReviewsMade(){
        final Long userId = Long.parseLong(session().get("id"));
        final Teacher teacher = Teacher.finder.where().eq("user.id", userId).findUnique();
        final Iterator<Lesson> lessons;
        final ArrayNode results = Json.newArray();

        if (teacher != null){
            lessons = Lesson.finder.where().eq("teacher", teacher).ne("studentReview", null)
                    .findList().iterator();
        } else {
            final Student student = Student.finder.where().eq("user.id", userId).findUnique();
            lessons = Lesson.finder.where().eq("student", student).ne("teacherReview", null)
                    .findList().iterator();
        }

        while (lessons.hasNext()){
            final Lesson temp = lessons.next();
            final ObjectNode node = Json.newObject();
            final String date;
            final String comment;
            final long stars;

            if (teacher != null){
                node.put("userReviewed", temp.getStudent().getUser().getName() + " "
                        + temp.getStudent().getUser().getSurname());
                date = temp.getStudentReview().getDate().getDate() + "/"
                        + (temp.getStudentReview().getDate().getMonth() + 1)
                        + "/" + (temp.getStudentReview().getDate().getYear() + 1900);
                comment = temp.getStudentReview().getComment();
                stars = temp.getStudentReview().getStars();
            } else {
                node.put("userReviewed", temp.getTeacher().getUser().getName() + " "
                        + temp.getTeacher().getUser().getSurname());
                date = temp.getTeacherReview().getDate().getDate() + "/"
                        + (temp.getTeacherReview().getDate().getMonth() + 1)
                        + "/" + (temp.getTeacherReview().getDate().getYear() + 1900);

                comment = temp.getTeacherReview().getComment();
                stars = temp.getTeacherReview().getStars();
            }

            node.put("reviewDate", date);
            node.put("reviewComment", comment);
            node.put("reviewStars", stars);
            results.add(node);
        }

        return ok(results);
    }

    public static Result review(String comment, long stars, String toEmail, long idLesson){
        final Review review = new Review();
        final Lesson lesson = Lesson.finder.byId(idLesson);
        final Teacher teacher = Teacher.finder.where().eq("user.email", toEmail).findUnique();
        final Student student;

        review.setComment(comment);
        review.setDate(new Date());
        review.setStars(stars);
        review.save();

        if (teacher != null){
            final User temp = teacher.getUser();
            temp.addStars(stars);
            temp.incrementReviews();
            temp.save();

            teacher.setUser(temp);
            teacher.save();

            lesson.setTeacher(teacher);
            lesson.setTeacherReview(review);
            lesson.save();
        } else {
            student =  Student.finder.where().eq("user.email", toEmail).findUnique();
            final User temp = student.getUser();
            temp.addStars(stars);
            temp.incrementReviews();
            temp.save();

            student.setUser(temp);
            student.save();

            lesson.setStudent(student);
            lesson.setStudentReview(review);
            lesson.save();
        }

        return ok();
    }

    public static Result createMockCase(){
        final Teacher teacher = new Teacher();
        final User userTeacher = new User();
        userTeacher.setEmail("user.teacher@gmail.com");
        userTeacher.setName("User");
        userTeacher.setSurname("Teacher");
        userTeacher.save();
        teacher.setUser(userTeacher);
        teacher.save();

        final Student student = new Student();
        final User userStudent = new User();
        userStudent.setEmail("user.student@gmail.com");
        userStudent.setName("User");
        userStudent.setSurname("Student");
        userStudent.save();
        student.setUser(userStudent);
        student.save();

        session("id", userTeacher.getId().toString());

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

        lesson1.setStudentReview(review1);
        lesson1.setTeacherReview(review2);
        lesson1.save();

        Duration oneHour = Duration.ofHours(1);

        lesson2.setTeacherReview(review3);
        lesson2.setDuration(oneHour);
        lesson2.save();

        lesson3.setStudentReview(review4);
        lesson3.save();

        lesson4.setDuration(oneHour);
        lesson4.save();

        lesson5.setDuration(oneHour);
        lesson5.save();


        return redirect(routes.ReviewController.show());
    }
}
