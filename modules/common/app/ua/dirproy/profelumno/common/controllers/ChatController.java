package ua.dirproy.profelumno.common.controllers;

import authenticate.Authenticate;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import ua.dirproy.profelumno.common.models.*;
import ua.dirproy.profelumno.user.models.User;

import java.util.Date;
import java.util.Iterator;

@Authenticate({Teacher.class, Student.class})
public class ChatController extends Controller {

    public static WebSocket<JsonNode> wsInterface() {
        final Long id = Long.parseLong(session().get("id"));
        return new WebSocket<JsonNode>() {
            public void onReady(WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out) {
                ChatManager.start(id, in, out);
            }
        };
    }

    public static Result getUserInSession(){
        ObjectNode node = Json.newObject();
        final long userId=Long.parseLong(session("id"));
        User user = Ebean.find(User.class, userId);
        JsonNode json= Json.toJson(user);
        //node.put("user", Long.parseLong(session().get("id")));
        return ok(json);
    }

    public static Result getChat(Long userId) {
        User user = Ebean.find(User.class, userId);
        Teacher teacher = Teacher.finder.where().eq("user", user).findUnique();
        Student student =  Student.finder.where().eq("user", user).findUnique();
        ObjectNode node = Json.newObject();
        Chat chat;

        if (teacher != null) {
            teacher.setUser(user);
            final long userId2=Long.parseLong(session("id"));
            User user2 = Ebean.find(User.class, userId2);
            student =  Student.finder.where().eq("user", user2).findUnique();
            student.setUser(user2);

            chat = Chat.finder.where().and(Expr.eq("teacher", teacher),
                    Expr.eq("student", student)).findUnique();

            if (chat == null) {
                chat = new Chat();
                chat.setStudent(student);
                chat.setTeacher(teacher);
                chat.startMessages();
                chat.save();
            }
            chat.setStudent(student);
            chat.setTeacher(teacher);
        } else {
            student.setUser(user);
            final long userId2=Long.parseLong(session("id"));
            User user2 = Ebean.find(User.class, userId2);
            teacher =  Teacher.finder.where().eq("user", user2).findUnique();
            teacher.setUser(user2);

            chat = Chat.finder.where().and(Expr.eq("student", student),
                    Expr.eq("teacher", teacher)).findUnique();

            if (chat == null) {
                chat = new Chat();
                chat.setTeacher(teacher);
                chat.setStudent(student);
                chat.startMessages();
                chat.save();
            }
            chat.setTeacher(teacher);
            chat.setStudent(student);
        }
        //Modifique un poco el metodo, hasta aca funciona bien igual que como estaba antes

        final ArrayNode results = Json.newArray();

        for (Message message1 : chat.getMessages()) {
            ObjectNode temp = Json.newObject();
            Message message = message1;
            temp.put("id", message.getId());
            temp.put("author", Json.toJson(message.getAuthor()));
            temp.put("msg", message.getMsg());

            Date tempDate = message.getDate();
            String date = (tempDate.getHours() < 10 ? "0" + tempDate.getHours() : tempDate.getHours()) + ":"
                    + (tempDate.getMinutes() < 10 ? "0" + tempDate.getMinutes() : tempDate.getMinutes()) + " "
                    + tempDate.getDate() + "/" + (tempDate.getMonth() + 1 < 10 ?  "0" + (tempDate.getMonth() + 1) : (tempDate.getMonth() + 1))
                    + "/" + (tempDate.getYear() + 1900);

            temp.put("date", date);
            results.add(temp);
        }

        node.put("id", chat.getId());
        node.put("teacher", Json.toJson(chat.getTeacher()));
        node.put("student", Json.toJson(chat.getStudent()));
        node.put("messages", results);

        ObjectNode chatNode = Json.newObject();
        chatNode.put("chat", node);

        return ok(chatNode);
    }
}
