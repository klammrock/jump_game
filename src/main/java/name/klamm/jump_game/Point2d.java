package name.klamm.jump_game;

public class Point2d {
    // TODO: immutable
    public static final Point2d empty = new Point2d(0, 0);
    
    private double x;
    private double y;
    
    public Point2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    static Point2d empty() {
         return new Point2d(0, 0);
    }
}
