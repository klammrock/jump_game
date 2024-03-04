package name.klamm.jump_game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_Q;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_W;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class PaintFrame extends JFrame {
    // TODO: as OpenGL coords, matrix MVP
    
    // View bounds
    private final double VIEW_LEFT = 0.0;
    private final double VIEW_RIGHT = 1.0;
    private final double VIEW_BOTTOM = 0.0;
    private final double VIEW_TOP = 1.0;
    
    private final int SCREEN_WIDTH = 640;
    private final int SCREEN_HEIGHT = 480;
//    private final int SCREEN_WIDTH = 1280;
//    private final int SCREEN_HEIGHT = 900;
    
    private double fit(double fromMin, double fromMax, double toMin, double toMax, double value) {
        if (value < fromMin)
            return toMin;
        
        if (value > fromMax)
            return toMax;
        
        double percent = (value - fromMin) / (fromMax - fromMin);        
        
        return percent * (toMax - toMin) + toMin;
    }
    
    private Point2d viewCoordToPixel(Point2d viewCoord, Point2d transform) {
        double x = fit(VIEW_LEFT, VIEW_RIGHT, 0, SCREEN_WIDTH, viewCoord.getX());
        double y = fit(VIEW_BOTTOM, VIEW_TOP, 0, SCREEN_HEIGHT, viewCoord.getY());
        
        // reverse height
        y = SCREEN_HEIGHT - y;
        
        // camera view
        x -= cameraTransform.getX();
        y -= cameraTransform.getY();
        
        // model
        x += transform.getX();
        y += transform.getY();
        
        var result = new Point2d(x, y);
        
        System.out.println("from " + viewCoord + ", to " + result);
        
        return result;
    }
    
    private Point2d viewCoordToPixel(Point2d viewCoord) {
        return viewCoordToPixel(viewCoord, Point2d.empty);
    }
    
    // camera
    private Point2d cameraTransform = Point2d.empty();
    private final int CAMERA_STEP = 10;
    
    private Point2d playerTransform = Point2d.empty();
    private final int PLAYER_STEP = 2;
    
    private BufferedImage image;
    
    // TODO: anti flikcking
    //private final BufferStrategy bufferStrategy = getBufferStrategy();
    
    private Rect2d rect2d = new Rect2d(new Point2d(100, 100), new Point2d(200, 200));
    private int rectXOffset = 0;
    private int rectYOffset = 0;
    
    private static final double GROUND_CENTER_X = 0.5;
    private static final double GROUND_CENTER_Y = 0.15;
    private static final double GROUND_SIZE_X = 0.5;
    private static final double GROUND_SIZE_Y = 0.1;
    
    private static final double PLATER_CENTER_X = 0.5;
    private static final double PLATER_CENTER_Y = 0.225;
    private static final double PLATER_SIZE = 0.05;

    private Rect2d groundRect2d = new Rect2d(
            new Point2d(GROUND_CENTER_X - GROUND_SIZE_X / 2, GROUND_CENTER_Y - GROUND_SIZE_Y / 2),
            new Point2d(GROUND_CENTER_X + GROUND_SIZE_X / 2, GROUND_CENTER_Y + GROUND_SIZE_Y / 2)
    );
    
    private Rect2d playerRect2d = new Rect2d(
            new Point2d(PLATER_CENTER_X - PLATER_SIZE / 2, PLATER_CENTER_Y - PLATER_SIZE / 2),
            new Point2d(PLATER_CENTER_X + PLATER_SIZE / 2, PLATER_CENTER_Y + PLATER_SIZE / 2)
    );
    
    public PaintFrame() {
        setTitle("Paint");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // to center of screen
        setLocationRelativeTo(null);
        setResizable(false);
        
        //setIgnoreRepaint(true);
        //createBufferStrategy(2);
        
        addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//                super.keyTyped(e);
//                PaintFrame.this.keyTyped(e);
//            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                PaintFrame.this.keyPressed(e);
            }
        });

        // TODO: now is false, do true
        System.out.println("isDoubleBuffered: " + this.isDoubleBuffered());
        
        setupGraphics();
        generateImage();
    }
    
    private void setupGraphics() {
        image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
    }
    
    private void generateImage() {
        var g2 = image.createGraphics();
        // TODO: check
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        drawGround(g2);
        drawPlayer(g2);
        
//        g2.setColor(Color.red);
//        g2.drawRect(rect2d.lowX() + rectXOffset, rect2d.lowY() + rectYOffset,
//                rect2d.width(), rect2d.height());
    }
    
    private void drawPlayer(Graphics2D g2) {
        g2.setColor(Color.RED);
        
        drawFillRect(g2, playerRect2d, playerTransform);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        var g2 = (Graphics2D)g;

        g2.drawImage(image, 0, 0, null);
    }
    
    private void keyTyped(KeyEvent e) {
        System.out.println(e);
        
//        switch (e.getKeyCode()) {
//            case VK_LEFT -> moveX();
////            case 'y' -> moveY();
////            case 'q' -> exit();
//            default -> {
//            }
//        }
        
        generateImage();
        repaint();
        //bufferStrategy.show();
    }
    
    private void keyPressed(KeyEvent e) {
        System.out.println(e);
        
        switch (e.getKeyCode()) {
            case VK_LEFT -> cameraMoveLeft();
            case VK_RIGHT -> cameraMoveRight();
            case VK_UP -> cameraMoveUp();
            case VK_DOWN -> cameraMoveDown();
            case VK_SPACE -> cameraReset();
            
            case VK_A -> playerMoveLeft();
            case VK_D -> playerMoveRight();
            case VK_W -> playerMoveUp();
            
            case VK_Q -> exit();
            default -> {
            }   
        }
        
        generateImage();
        repaint();
    }
    
    private void moveX() {
        rectXOffset += PLAYER_STEP;
    }
    
    private void moveY() {
        rectYOffset += PLAYER_STEP;
    }

    private void drawGround(Graphics2D g2) {
        g2.setColor(Color.GREEN);
        
        drawFillRect(g2, groundRect2d);
    }
    
    private void drawFillRect(Graphics2D g2, Rect2d rect2d) {
        var pixelRect2d = new Rect2d(viewCoordToPixel(rect2d.p1()), viewCoordToPixel(rect2d.p2()));
        
        g2.fillRect(pixelRect2d.lowX(), pixelRect2d.lowY(), pixelRect2d.width(), pixelRect2d.height());
    }
    
    private void drawFillRect(Graphics2D g2, Rect2d rect2d, Point2d transform) {
        var pixelRect2d = new Rect2d(
                viewCoordToPixel(rect2d.p1(), transform), 
                viewCoordToPixel(rect2d.p2(), transform));
        
        g2.fillRect(pixelRect2d.lowX(), pixelRect2d.lowY(), pixelRect2d.width(), pixelRect2d.height());
    }

    private void exit() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void cameraMoveLeft() {
        cameraTransform.setX(cameraTransform.getX() - CAMERA_STEP);
    }
    
    private void cameraMoveRight() {
        cameraTransform.setX(cameraTransform.getX() + CAMERA_STEP);
    }
    
    private void cameraMoveUp() {
        cameraTransform.setY(cameraTransform.getY() - CAMERA_STEP);
    }
    
    private void cameraMoveDown() {
        cameraTransform.setY(cameraTransform.getY() + CAMERA_STEP);
    }

    private void cameraReset() {
        cameraTransform.setX(0);
        cameraTransform.setY(0);
    }
    
    private void playerMoveLeft() {
        playerTransform.setX(playerTransform.getX() - PLAYER_STEP);
    }
    
    private void playerMoveRight() {
        playerTransform.setX(playerTransform.getX() + PLAYER_STEP);
    }

    private void playerMoveUp() {
        // TODO: jump
        playerTransform.setY(playerTransform.getY() - PLAYER_STEP);
    }
}
