package environment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ClientPlayer extends Player implements Runnable{

    // client communication streams
    OutputStream out;
    InputStream in;

    // listener thread to record messages from the client
    Thread listen;

    public ClientPlayer(Socket s, double x, double y) {
        super(x, y);
        try {
            out = s.getOutputStream();
            in = s.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        listen = new Thread(this);
        listen.start();
    }

    @Override
    public boolean update(World w, int dt) {
        super.update(w, dt);

        // Send the vision data
        double[][] vis = this.detectVision(w.entities);
        byte[] visionData = new byte[8 * vis.length * vis[0].length];
        ByteBuffer b = ByteBuffer.wrap(visionData);
        for (int i = 0;i < vis.length; i++) {
            for (int j = 0;j < vis[i].length; j++) {
                b.putDouble(vis[i][j]);
            }
        }
        try {
            out.write(visionData);
        } catch (IOException e) {
            System.out.println("Failed to send the vision data over socket");
            return true;
        }
        return false;
    }

    public static double bound(double d, double min, double max) {
        return d < min ? min : d > max ? max : d;
    }

    @Override
    public void run() {
        while (true) {
            byte[] actionData = new byte[16];
            try {
                in.read(actionData);
                ByteBuffer b = ByteBuffer.wrap(actionData);
                turn = bound(b.getDouble(), -1, 1);
                forward = bound(b.getDouble(), 0, 1);
            } catch (IOException e) {
                System.out.println("Stopping reading from client do to inactivity");
                break;
            }
        }
    }
}
