import java.util.Arrays;

public class RemotePlayer implements Reciever{
    Connection c;

    public RemotePlayer() {
        c = new Connection(this);
    }

    boolean print  = true;

    @Override
    public void update(double[][] visionData) {
        if (print) {
            for (int i = 0;i < visionData.length; i++) {
                System.out.println(visionData[i][0]);
            }
            print = false;
        }
        //System.out.println(Arrays.deepToString(visionData));
    }
}
