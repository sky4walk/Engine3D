/*
 mail@andrebetz.de
 */

package jgame;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class Texture {
    int[] pixels;
    String loc;
    int sizeX;
    int sizeY;

    public Texture(String location, int sizeX,int sizeY) {
        loc = location;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        pixels = new int[sizeX * sizeY];
        load();                                
    }

    public int getSizeX() {
        return sizeX;
    }
    public int getSizeY() {
        return sizeY;
    }
    private void load() {
        try {
            InputStream stream = Texture.class.getResourceAsStream(loc);
            BufferedImage image = ImageIO.read(stream);
            int w = image.getWidth();
            int h = image.getHeight();
            image.getRGB(0, 0, w, h, pixels, 0, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Texture wood = new Texture("res/wood.png", 64, 64);
    public static Texture brick = new Texture("res/redbrick.png", 64, 64);
    public static Texture bluestone = new Texture("res/bluestone.png", 64, 64);
    public static Texture stone = new Texture("res/greystone.png", 64, 64);
}

