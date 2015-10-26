package ua.dirproy.profelumno.chat.models;

import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.WebSocket;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by rudy on 26/10/15.
 */
public class ChatManager {

    private static ConcurrentHashMap<Long, WebSocket.Out<JsonNode>> map;
    private static ChatManager ourInstance = new ChatManager();

    public static ChatManager getInstance() {
        return ourInstance;
    }

    private ChatManager() {
    }
}
