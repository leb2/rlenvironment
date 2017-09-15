package io;

import com.google.gson.Gson;
import environment.World;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@WebSocket
public class Websocket implements Runnable{
  // private Gson GSON = new Gson();
  private Set<Session> sessions;
  private World w;
  private Thread senderThread;

  public Websocket() {
    sessions = new HashSet<>();
  }

  public void setWorld(World w) {
    this.w = w;
    senderThread = new Thread(this);
    senderThread.start();
  }

  public synchronized void sendAll(String message) {
    sessions.forEach(s -> send(s, message));
  }

  public synchronized void send(Session s, String message) {
      try {
        s.getRemote().sendString(message);
      } catch (IOException e) {
        System.out.println("Could not send output to a session");
      }
  }

  @OnWebSocketConnect
  public void onConnect(Session user) {
    // Do nothing
  }

  @OnWebSocketClose
  public void onClose(Session user, int statusCode, String reason) {
    sessions.remove(user);
  }

  @OnWebSocketMessage
  public void onMessage(Session sess, String message) {
    System.out.println("Warning Received Message From Client");
  }

  @Override
  public void run() {
    while (true) {
      // sendAll(GSON.toJson(w));
      try {
        Thread.sleep(100);
      } catch (InterruptedException ignored) { }
    }
  }
}
