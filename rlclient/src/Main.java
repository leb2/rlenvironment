public class Main {
    public static void main(String[] args) {
        RemotePlayer rp = new RemotePlayer();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
