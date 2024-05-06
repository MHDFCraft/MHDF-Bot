package cn.chengzhiya.mhdfbot.server;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@Service
@ServerEndpoint("/MHDFBot")
public final class webSocket {
    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();

    public static void send(String message) {
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                sessions.remove(session);
                throw new RuntimeException(e);
            }
        }
    }

    public static void send(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            sessions.remove(session);
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(String message) {

    }

    @OnError
    public void onError(Session session, Throwable e) {
        sessions.remove(session);
    }
}
