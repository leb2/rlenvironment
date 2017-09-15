package environment;

import com.google.gson.JsonObject;

import java.util.Collection;
import java.util.List;

public class Player extends Entity{
    // static variables
    public static final double speed = 0.5;
    public static final double turnSpeed = 5;


    //local action variables
    double turn = 0;
    double forward = 0;

    //local state variables
    double direction = 0;

    public Player(double x, double y) {
        super(x, y, 1.0, 0);
    }

    @Override
    public boolean update(World w, int dt) {
        System.out.println(String.format("X: %.4f\tY: %.4f", x, y));
        direction += turn * turnSpeed * dt / 1000.0;
        double dist = forward * speed * dt / 1000.0;
        x += Math.cos(direction) * dist;
        y += Math.sin(direction) * dist;

        return false;
    }

    public static final int COLORS = 2;

    public double[][] detectVision(Collection<Entity> entities) {
        double[][] vision = new double[120][COLORS + 1];

        for (int i = 0; i < 360; i += 3) {
            double dir = Math.toRadians(i) + direction;

            boolean hit = false;
            double dist = 0;
            int color = 0;

            for (Entity e : entities) {
                if (e != this) {
                    Double d = distance(x, y, dir, e.x, e.y, e.size);
                    if (d != null) {
                        if (!hit) {
                            dist = d;
                            color = e.color;
                            hit = true;
                        } else {
                            if (d < dist) {
                                dist = d;
                                color = e.color;
                            }
                        }
                    }
                }
            }

            if (hit) {
                vision[i / 3][0] = 1 / dist;
                vision[i / 3][color + 1] = 1.0;
            }
        }

        return vision;
    }

    public static Double distance(double x, double y, double theta, double ox, double oy, double r) {
        double cos = Math.cos(theta);
        double sin = Math.sin(theta);

        double dx = ox - x;
        double dy = oy - y;

        double dot = dx * cos + dy * sin;

        dx -= dot * cos;
        dy -= dot * sin;

        double dist2 = dx * dx + dy * dy;
        double r2 = r * r;

        if (dist2 < r2 && dot > 0) {
            return dot - Math.sqrt(r2 - dist2);
        } else {
            return null;
        }
    }


}
