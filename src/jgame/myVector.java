/*
 * mail@andrebetz.de
 */

package jgame;

/**
 * @author mail@andrebetz.de
 */
public class myVector {
    private double x,y,z,w;
    myVector() {
        set(0.0,0.0,0.0,0.0);
    }
    myVector( myVector o) {
        set(o);
    }
    myVector(double x, double y, double z, double w) {
        set(x,y,z,w);
    }
    myVector(double same) {
        set(same);
    }
    void set(myVector o) {
        set(o.getX(),o.getY(),o.getZ(),o.getW());
    }
    void set(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    void set(double same) {
        this.x = same;
        this.y = same;
        this.z = same;
        this.w = same;
    }
    double getX() {
        return this.x;
    }
    void setX(double x) {
        this.x = x;
    }
    double getY() {
        return this.y;
    }
    void setY(double y) {
        this.y = y;
    }
    double getZ() {
        return this.z;
    }
    void setZ(double z) {
        this.z = z;
    }
    double getW() {
        return this.w;
    }
    void setW(double w) {
        this.w = w;
    }
    myVector scale(double scalar) {
        return new myVector(
            this.getX() * scalar,
            this.getY() * scalar,
            this.getZ() * scalar,
            this.getW() * scalar);
    }
    myVector mul(myVector o) {
        return new myVector(
            this.getX() * o.getX(),
            this.getY() * o.getY(),
            this.getZ() * o.getZ(),
            this.getW() * o.getW());
    }
    myVector add(myVector o) {
        return new myVector(
            this.getX() + o.getX(),
            this.getY() + o.getY(),
            this.getZ() + o.getZ(),
            this.getW() + o.getW() );
    }
    myVector sub(myVector o) {
        return new myVector(
            this.getX() - o.getX(),
            this.getY() - o.getY(),
            this.getZ() - o.getZ(),
            this.getW() - o.getW() );
    }
    myVector rotateX(double rotation) {
        myVector resVector = new myVector(1);
        resVector.setY( this.getY() * Math.cos(rotation) - 
                        this.getZ() * Math.sin(rotation) );
        resVector.setZ( this.getY() * Math.sin(rotation) + 
                        this.getZ() * Math.cos(rotation) );        
        return resVector;
    }
    myVector rotateY(double rotation) {
        myVector resVector = new myVector(1);
        resVector.setX( this.getX() * Math.cos(rotation) + 
                        this.getZ() * Math.sin(rotation) );
        resVector.setZ(-this.getX() * Math.sin(rotation) + 
                        this.getZ() * Math.cos(rotation) );        
        return resVector;
    }
    myVector rotateZ(double rotation) {
        myVector resVector = new myVector(1);
        resVector.setX( this.getX() * Math.cos(rotation) - 
                        this.getY() * Math.sin(rotation) );
        resVector.setY( this.getX() * Math.sin(rotation) + 
                        this.getY() * Math.cos(rotation) );        
        return resVector;
    }
}
