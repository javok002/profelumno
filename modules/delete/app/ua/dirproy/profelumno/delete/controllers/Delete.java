package ua.dirproy.profelumno.delete.controllers;

import play.mvc.Controller;
import play.mvc.Result;
import ua.dirproy.profelumno.common.models.*;
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
        session().clear();
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
        for (Chat chat : chatList) {
            for (Message message : chat.getMessages()) {
                if (message.getAuthor().getId().equals(userId)) {
                    message.setAuthor(null);
                    message.save();
                }
            }
        }
        for ( Chat chat : chatList) {
            if (chat.getStudent() != null && chat.getTeacher() != null && chat.getStudent().getId().equals(userId)) {
                chat.setTeacher(null);
                chat.setStudent(null);
                chat.save();
            }
        }

    }

    private static void deleteChatTeacher(Long userId){
        List<Chat> chatList = Chat.finder.all();
        for (Chat chat : chatList) {
            if (chat.getTeacher().getId().equals(userId)){
                chat.setTeacher(null);
                chat.setStudent(null);
                chat.save();
            }
        }
    }

}
