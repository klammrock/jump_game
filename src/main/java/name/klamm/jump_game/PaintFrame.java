package name.klamm.jump_game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class PaintFrame extends JFrame {
    // TODO: as OpenGL coords, matrix MVP
    private final double X_LEFT = 0.0;
    private final double X_RIGHT = 1.0;
    private final double Y_BOTTOM = 0.0;
    private final double Y_TOP = 1.0;
    
    private final int SCREEN_WIDTH = 640;
    private final int SCREEN_HEIGHT = 480;
    
    private BufferedImage image;
    
    // TODO: anti flikcking
    //private final BufferStrategy bufferStrategy = getBufferStrategy();
    
    private Rect2d rect2d = new Rect2d(new Point2d(100, 100), new Point2d(200, 200));
    private int rectXOffset = 0;
    private int rectYOffset = 0;
    
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
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                PaintFrame.this.keyTyped(e);
            }
            
        });
        
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
        
        g2.setColor(Color.red);
        g2.drawRect(rect2d.lowX() + rectXOffset, rect2d.lowY() + rectYOffset,
                rect2d.width(), rect2d.height());
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        var g2 = (Graphics2D)g;

        g2.drawImage(image, 0, 0, null);
    }
    
    private void keyTyped(KeyEvent e) {
        System.out.println(e);
        
        switch (e.getKeyChar()) {
            case 'x' -> moveX();
            case 'y' -> moveY();
            default -> {
            }
        }
        
        generateImage();
        repaint();
        //bufferStrategy.show();
    }
    
    private void moveX() {
        rectXOffset++;

    }
    
    private void moveY() {
        rectYOffset++;
    }
}
