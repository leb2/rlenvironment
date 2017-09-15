package environment;

public abstract class Entity{
    public double x, y, size;
    public int color;

    public Entity(double x, double y, double size, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.size = size;
    }

    public abstract boolean update(World w, int dt);
}
