package name.klamm.jump_game;

public record Rect2d(Point2d p1, Point2d p2) {
    public int width() {
        return (int)(Math.abs(p1.getX() - p2.getX()));
    }
    
    public int height() {
        return (int)(Math.abs(p1.getY() - p2.getY()));
    }
    
    public int lowX() {
        return (int)(Math.min(p1.getX(), p2.getX()));
    }
    
    public int lowY() {
        return (int)(Math.min(p1.getY(), p2.getY()));
    }
}
