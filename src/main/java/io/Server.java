package io;

import environment.ClientPlayer;
import environment.Player;
import environment.World;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
    ServerSocket ss;
    World w;

    Thread connector;
    public Server(World w) {
        this.w = w;

        try {
            ss = new ServerSocket(2030);
        } catch (IOException e) {
            e.printStackTrace();
        }

        connector = new Thread(this);
        connector.start();
    }


    @Override
    public void run() {
        while (true) {
            try {
                Socket s = ss.accept();
                System.out.println("New Client Connection");
                Player p = new ClientPlayer(s, Math.random() * 20 - 10, Math.random() * 20 - 10);
                synchronized (w.entities) {
                    w.entities.add(p);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
