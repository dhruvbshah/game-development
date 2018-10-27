/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lazarusgame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JApplet;

/**
 * This class contains generic methods for both games
 */
abstract public class Game extends JApplet implements Runnable {

    private BufferedImage bimg;
    private Thread thread;
    Image p2m, p1m;
    BufferedImage img = null;

    @Override
    public void init() {
        initSound();
        initImages();
    }

    abstract public void initSound();

    abstract public void initImages();

    abstract public void drawAll(int w, int h, Graphics2D g2);

    // returns image 
    public Image getMyImage(String name) {
        URL url = this.getClass().getResource(name);
        Image img = getToolkit().getImage(url);
        try {
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            tracker.waitForID(0);
        } catch (Exception e) {
        }
        return img;

    }

    public Graphics2D createGraphics2D(int w, int h) {
        Graphics2D g2;
        if (bimg == null || bimg.getWidth() != w || bimg.getHeight() != h) {
            bimg = (BufferedImage) createImage(w, h);
        }
        g2 = bimg.createGraphics();
        g2.setBackground(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, w, h);
        return g2;
    }

    @Override
    public void paint(Graphics g) {
        Dimension d = getSize();
        Graphics2D g2 = createGraphics2D(d.width, d.height);
        drawAll(d.width, d.height, g2);
        g2.dispose();
        g.drawImage(bimg, 0, 0, this);
    }

    @Override
    public void run() {
        Thread me = Thread.currentThread();
        setFocusable(true);
        // this loop will paint it at regular interval.
        while (thread == me) {
            repaint();
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    @Override
    public void start() {
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    public BufferedImage getBimg() {
        return this.bimg;
    }

}
