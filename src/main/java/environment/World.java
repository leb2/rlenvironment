package environment;

import io.Websocket;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class World implements Runnable{
    public Set<Entity> entities = new HashSet<>();

    private transient Websocket ws;

    private transient Thread updater;

    public World(Websocket ws) {
        this.ws = ws;
        updater = new Thread(this);
        updater.start();
    }

    public void update(int dt) {
        synchronized (entities) {
            entities.removeIf(e -> e.update(this, dt));
            if (entities.size() < 100) {
                entities.add(new Food(
                        Math.random() * 20 - 10,
                        Math.random() * 20 - 10
                        ));
            }
        }
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            update(50);
        }
    }
}
