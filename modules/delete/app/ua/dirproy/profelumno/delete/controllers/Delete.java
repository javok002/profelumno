package ua.dirproy.profelumno.delete.controllers;

import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.Chat;
import ua.dirproy.profelumno.common.models.Lesson;
import ua.dirproy.profelumno.common.models.Student;
import ua.dirproy.profelumno.common.models.Teacher;
import ua.dirproy.profelumno.user.models.User;

import java.util.List;


/**
 * Created by Nicolas Burroni
 * Date: 9/12/15
 */
public class Delete extends Controller {

    public static Result deleteUser() {
        String id = session("id");
        Long parseId = Long.parseLong(id);
        User user = User.finder.byId(parseId);
        List<Student> students = Student.list();
        boolean deleteUser = false;
        session().clear();
        for (int i = 0; i <students.size() ; i++) {
            Student aux = students.get(i);
            if (aux.getUser().getId().equals(parseId)){
                deleteLessonsStudent(aux.getId());
                deleteChatStudent(aux.getId());
                aux.delete();
                deleteUser = true;
                break;
            }
        }
        if (!deleteUser) {
            List<Teacher> teachers = Teacher.list();
            for (int i = 0; i < teachers.size(); i++) {
                Teacher aux = teachers.get(i);
                if (aux.getUser().getId().equals(parseId)) {
                    deleteLessonsTeacher(aux.getId());
                    deleteChatTeacher(aux.getId());
                    aux.delete();
                    break;
                }
            }
        }
        user.setSubjects(null);
        user.delete();
        return redirect("/");
    }

    private static void deleteLessonsStudent(Long userId){
        List<Lesson> lessonList = Lesson.finder.all();
        for (int j = 0; j <lessonList.size() ; j++) {
            Lesson lesson = lessonList.get(j);
            if (lesson.getStudent().getId().equals(userId)){
                lesson.delete();
            }
        }
    }

    private static void deleteLessonsTeacher(Long userId){
        List<Lesson> lessonList = Lesson.finder.all();
        for (int j = 0; j <lessonList.size() ; j++) {
            Lesson lesson = lessonList.get(j);
            if (lesson.getTeacher().getId().equals(userId)){
                lesson.delete();
            }
        }
    }

    private static void deleteChatStudent(Long userId){
        List<Chat> chatList = Chat.finder.all();
        for (int j = 0; j <chatList.size() ; j++) {
            Chat chat = chatList.get(j);
            if (chat.getStudent().getId().equals(userId)){
                chat.delete();
            }
        }
    }

    private static void deleteChatTeacher(Long userId){
        List<Chat> chatList = Chat.finder.all();
        for (int j = 0; j <chatList.size() ; j++) {
            Chat chat = chatList.get(j);
            if (chat.getTeacher().getId().equals(userId)){
                chat.delete();
            }
        }
    }

}
