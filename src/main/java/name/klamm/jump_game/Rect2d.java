package name.klamm.jump_game;

public record Rect2d(Point2d p1, Point2d p2) {
    public int width() {
        return Math.abs(p1.x() - p2.x());
    }
    
    public int height() {
        return  Math.abs(p1.y() - p2.y());
    }
    
    public int lowX() {
        return Math.min(p1.x(), p2.x());
    }
    
    public int lowY() {
        return Math.min(p1.y(), p2.y());
    }
}
