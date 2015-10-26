package ua.dirproy.profelumno.chat.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.Controller;
import play.mvc.WebSocket;

/**
 * Created by rudy on 26/10/15.
 */
public class ChatController extends Controller {

    // Websocket interface
    public static WebSocket<JsonNode> wsInterface(){
        return new WebSocket<JsonNode>(){

            // called when websocket handshake is done
            public void onReady(WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out){
            //    SimpleChat.start(in, out);
            }
        };
    }


}
