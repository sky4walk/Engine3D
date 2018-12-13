/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jgame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

public class JGame extends JFrame implements Runnable, KeyListener{
    private int width = 640;
    private int hight = 480;
    private Thread thread;
    private boolean running;
    private BufferedImage image;
    private int[] pixels;
    private playGround world;
    public JGame() {
        thread = new Thread(this);
        image = new BufferedImage(width, hight, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

        addKeyListener(this);               
        setSize(width, hight);
        world = new playGround(width,hight);
        setResizable(false);
        setTitle("3D Engine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.black);
        setLocationRelativeTo(null);
        setVisible(true);
        start();
    }
    private synchronized void start() {
        running = true;
        thread.start();
    }
    public synchronized void stop() {
        running = false;
        try {
                thread.join();
        } catch(InterruptedException e) {
                e.printStackTrace();
        }
    }
    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
                createBufferStrategy(3);
                return;
        }
        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        bs.show();
    }
    public void keyPressed(KeyEvent key) {
        if((key.getKeyCode() == KeyEvent.VK_LEFT)){
            world.rotateLeft(true);
        }
        if((key.getKeyCode() == KeyEvent.VK_RIGHT)){
            world.rotateRight(true);
        }
        if((key.getKeyCode() == KeyEvent.VK_UP)){
            world.moveForward(true);
        }
        if((key.getKeyCode() == KeyEvent.VK_DOWN)){
            world.moveBackward(true);
        }
    }
    public void keyReleased(KeyEvent key) {
        if((key.getKeyCode() == KeyEvent.VK_LEFT)){
            world.rotateLeft(false);                    
        }
        if((key.getKeyCode() == KeyEvent.VK_RIGHT)){
            world.rotateRight(false);
        }
        if((key.getKeyCode() == KeyEvent.VK_UP)){
            world.moveForward(false);
        }
        if((key.getKeyCode() == KeyEvent.VK_DOWN)){
            world.moveBackward(false);
        }
    }
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub		
    }
    public void run() {
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0;//60 times per second
        double delta = 0;
        requestFocus();
        while(running) {
            long now = System.nanoTime();
            delta = delta + ((now-lastTime) / ns);
            lastTime = now;
            while (delta >= 1)//Make sure update is only happening 60 times a second
            {
                    //handles all of the logic restricted time
                    world.update(pixels);
                    delta--;
            }
            render();//displays to the screen unrestricted time
        }
    }
    public static void main(String [] args) {
        JGame game = new JGame();
    }
}

