package environment;

import com.google.gson.JsonObject;

public class Food extends Entity{
    public Food(double x, double y) {
        super(x, y, 0.2, 1);
    }

    @Override
    public boolean update(World w, int dt) {
        return false;
    }
}
