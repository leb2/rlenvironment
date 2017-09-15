import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Connection implements Runnable{
    private Socket s;

    private InputStream in;
    private OutputStream out;

    private Reciever r;

    private Thread reader;

    public Connection (Reciever r) {
        this.r = r;
        try {
            s = new Socket("localhost", 2030);
            in = s.getInputStream();
            out = s.getOutputStream();

            reader = new Thread(this);
            reader.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            byte[] read = new byte[120 * 8 * 3];
            try {
                in.read(read);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteBuffer b = ByteBuffer.wrap(read);

            double[][] vision = new double[120][3];
            for (int i = 0; i < vision.length; i++) {
                for (int j = 0; j < vision[0].length; j++) {
                    vision[i][j] = b.getDouble();
                }
            }

            r.update(vision);
        }
    }
}
