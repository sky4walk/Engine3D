/*
 * mail@andrebetz.de
 */

package jgame;

import java.util.ArrayList;

/**
 *
 * @author mail@andrebetz.de
 */
public class playGround {
    static int[][] defaultMap = {
	{1,1,1,1,1,1,1,1,2,2,2,2,2,2,2},
	{1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
	{1,0,3,3,3,3,3,0,0,0,0,0,0,0,2},
	{1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
	{1,0,3,0,0,0,3,0,2,2,2,0,2,2,2},
	{1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
	{1,0,3,3,0,3,3,0,2,0,0,0,0,0,2},
	{1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
	{1,1,1,1,1,1,1,1,4,4,4,0,4,4,4},
	{1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
	{1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
	{1,0,0,0,0,0,1,4,0,3,3,3,3,0,4},
	{1,0,0,0,0,0,1,4,0,3,3,3,3,0,4},
	{1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
	{1,1,1,1,1,1,1,4,4,4,4,4,4,4,4}
    };
    private double MOVE_SPEED       = .08;
    private double ROTATION_SPEED   = .045;
    private Camera cam = new Camera(4.5, 4.5, 1, 0, 0, -.66);
    private Screen screen;
    private ArrayList<Texture> textures = new ArrayList<Texture>();

    public playGround(int sizeX,int sizeY ) {
        screen = new Screen(this,sizeX,sizeY);
	textures.add(Texture.wood);
	textures.add(Texture.brick);
	textures.add(Texture.bluestone);
	textures.add(Texture.stone);
    }
    public int getSizeX() {
        return defaultMap[0].length;        
    }
    public int getSizeY() {
        return defaultMap.length;        
    }
    public int getVal(int x, int y) {
        if ( 0 <= x && x < getSizeX() &&
             0 <= y && y < getSizeY() ) {
            return defaultMap[x][y];
        }
        return -1;
    }
    public Camera getCam() {
        return cam;
    }
    public Texture getTexture(int nr) {
        if ( 0 <= nr && nr < textures.size())
            return textures.get(nr);
        return null;
    }
    public void moveForward(boolean forward) {
        cam.setForward(forward);
    }
    public void moveBackward(boolean backward) {
        cam.setBackward(backward);
    }
    public void rotateLeft(boolean left) {
        cam.setLeft(left);
    }
    public void rotateRight(boolean right) {
        cam.setRight(right);
    }
    public void update(int[] pixels) {
        screen.update(pixels);
        cam.update(this);
    }
    
}
