/*
 * mail@andrebetz.de
 */

package jgame;

public class Camera {
    // Player position.
    private myVector pos        = new myVector();
    // direction in which the player looks.
    private myVector dir        = new myVector();
    // perpendicular vector to direction Dir. 
    // Points to farthest edge of camera field.
    // The other fathest side is the negative side.
    private myVector plane      = new myVector();
    private boolean left        = false;
    private boolean right       = false;
    private boolean forward     = false;
    private boolean backward    = false;
    private double MOVE_SPEED   = .08;
    final double ROTATION_SPEED = .045;
    public Camera(  double x, 
                    double y, 
                    double xd, 
                    double yd, 
                    double xp, 
                    double yp) {
        pos.set(x,y,0,0);
        dir.set(xd,yd,0,0);
        plane.set(xp,yp,0,0);                
    }
    public myVector getDir() {
        return dir;
    }
    public myVector getPlane() {
        return plane;
    }
    public myVector getPos() {
        return pos;
    }
    public void setLeft(boolean left) {
        this.left = left;
    }
    public void setRight(boolean right) {
        this.right = right;
    }
    public void setForward(boolean forward) {
        this.forward = forward;
    }
    public void setBackward(boolean backward) {
        this.backward = backward;
    }
    public void update(playGround world) {
        if(forward) {
            myVector nextStep = pos.add(dir.scale(MOVE_SPEED));
            if ( world.getVal(  (int)nextStep.getX(),
                                (int)pos.getY() ) == 0) {
                pos.setX(nextStep.getX());
            }
            if ( world.getVal(  (int)pos.getX(),
                                (int)nextStep.getY()    ) == 0) {
                pos.setY(nextStep.getY());
            }
        }
        if(backward) {
            myVector nextStep = pos.sub(dir.scale(MOVE_SPEED));
            if ( world.getVal(  (int)nextStep.getX(),
                                (int)pos.getY() ) == 0) {
                pos.setX(nextStep.getX());
            }
            if ( world.getVal(  (int)pos.getX(),
                                (int)nextStep.getY()    ) == 0) {
                pos.setY(nextStep.getY());
            }
        }
        if(right) {
            dir.set(dir.rotateZ(-ROTATION_SPEED));
            plane.set(plane.rotateZ(-ROTATION_SPEED));  
        }
        if(left) {
            dir.set(dir.rotateZ(ROTATION_SPEED));
            plane.set(plane.rotateZ(ROTATION_SPEED));
        }
    }	
}
