package ua.dirproy.profelumno.common.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.WebSocket;
import ua.dirproy.profelumno.user.models.User;

import java.util.*;
import java.util.concurrent.*;

public class ChatManager {

    private static Map<Long, WebSocket.Out<JsonNode>> map = new ConcurrentHashMap<>();
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static Map<Long, ScheduledFuture<?>> timers = new ConcurrentHashMap<>();

    public static void start(Long userId, WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out){
        addConnection(userId, out);

        in.onMessage(jsonNode -> {
            if(jsonNode.findValue("type") == null) {
                Long idUserFrom = jsonNode.findPath("idUserFrom").asLong();
                String message = jsonNode.findPath("message").asText();
                Long idChat = jsonNode.findPath("idChat").asLong();

                notifyMsg(idUserFrom, message, idChat);
            } else {
                notifyUsersConnections(userId);
            }
        });

        in.onClose(() -> {
            final Runnable close = () -> {
                notifyDisconnection(userId);
                timers.remove(userId);
            };
            timers.put(userId, scheduler.schedule(close, 15, TimeUnit.SECONDS));
        });
    }

    public static void addConnection(Long userId, WebSocket.Out<JsonNode> socketOut){
        ScheduledFuture<?> timer = timers.get(userId);
        if (timer != null){
            timer.cancel(true);
            timers.remove(userId);
        }

        map.put(userId, socketOut);
    }

    public static void notifyUsersConnections(Long userId){
        ObjectNode node = Json.newObject();
        List<User> connected = new ArrayList<>();
        List<User> disconnected = new ArrayList<>();

        for (Long aLong : getUsersRelated(userId)) {
            User temp = User.getUser(aLong);
            WebSocket.Out<JsonNode> out = map.get(temp.getId());
            if (out != null){
                connected.add(temp);
            } else {
                disconnected.add(temp);
            }
        }

        node.put("type", "users");
        node.put("connectedUsers", Json.toJson(connected));
        node.put("disconnectedUsers", Json.toJson(disconnected));

        map.get(userId).write(node);

        notifyIConnected(connected, User.getUser(userId));
    }

    public static void notifyIConnected(List<User> connected, User me){
        for (User user : connected){
            ObjectNode node = Json.newObject();
            node.put("type", "user");
            node.put("user", Json.toJson(me));
            node.put("connected", true);

            map.get(user.getId()).write(node);
        }
    }

    public static void notifyMsg(Long idUserFrom, String message, Long idChat){
        Chat chat = Chat.finder.where().eq("id", idChat).findUnique();
        chat.addMessage(message, User.getUser(idUserFrom)); //aca salta un error no pude identificar cual es el problema

        ObjectNode node = Json.newObject();
        node.put("type", "msg");
        node.put("idChat", chat.getId());

        Message msg = chat.getMessages().get(chat.getMessages().size() - 1);
        ObjectNode temp = Json.newObject();
        temp.put("id", msg.getId());
        temp.put("author", Json.toJson(msg.getAuthor()));
        temp.put("msg", msg.getMsg());

        Date tempDate = msg.getDate();
        String date = (tempDate.getHours() < 10 ? "0" + tempDate.getHours() : tempDate.getHours()) + ":"
                + (tempDate.getMinutes() < 10 ? "0" + tempDate.getMinutes() : tempDate.getMinutes()) + " "
                + tempDate.getDate() + "/" + (tempDate.getMonth() + 1 < 10 ?  "0" + (tempDate.getMonth() + 1) : (tempDate.getMonth() + 1))
                + "/" + (tempDate.getYear() + 1900);

        temp.put("date", date);

        node.put("message", temp);

        WebSocket.Out<JsonNode> outS = map.get(chat.getStudent().getUser().getId());
        WebSocket.Out<JsonNode> outT = map.get(chat.getTeacher().getUser().getId());
        if (outS != null) outS.write(node);
        if (outT != null) outT.write(node);
    }

    public static void notifyDisconnection(Long userId){
        map.remove(userId);

        Iterator<Long> relatedTo = getUsersRelated(userId).iterator();

        User me = User.getUser(userId);

        while (relatedTo.hasNext()){
            ObjectNode node = Json.newObject();
            node.put("type", "user");
            node.put("user", Json.toJson(me));
            node.put("connected", false);

            Long temp = relatedTo.next();

            WebSocket.Out<JsonNode> ws = map.get(temp);

            if (ws != null) ws.write(node);
        }
    }

    private static Set<Long> getUsersRelated(Long userId){
        List<Long> users = new ArrayList<>();
        Iterator<Lesson> lessons;
        Teacher teacher = Teacher.finder.where().eq("user.id", userId).findUnique();
        if (teacher != null){
            lessons = Lesson.finder.where().eq("teacher", teacher).findList().iterator();
            while (lessons.hasNext()){
                users.add(lessons.next().getStudent().getUser().getId());
            }
        } else {
            Student student = Student.finder.where().eq("user.id", userId).findUnique();
            lessons = Lesson.finder.where().eq("student", student).findList().iterator();
            while (lessons.hasNext()){
                users.add(lessons.next().getTeacher().getUser().getId());
            }
        }

        return users.isEmpty() ? new HashSet<>() : new HashSet<>(users);
    }
}
