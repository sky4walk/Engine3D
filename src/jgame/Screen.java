/*
 * mail@andrebetz.de
 */

package jgame;

import java.awt.Color;

public class Screen {
    private int width, height;
    private playGround world;

    public Screen(playGround world, int w, int h) {
        this.world  = world;
        width       = w;
        height      = h;
    }

    private myVector getRayDirPos(double cameraX) {
        myVector dir = world.getCam().getDir();
        myVector plane = world.getCam().getPlane();
        myVector scalePlane = plane.scale(cameraX);
        myVector rayDir = dir.add(scalePlane);
        return rayDir;
    }
    
    private void BackGround(int[] pixels) {
        // obere bildhaelfte dunkles grau
        for ( int n = 0; n < pixels.length/2; n++ ) {
            if ( pixels[n] != Color.DARK_GRAY.getRGB() ) {
                pixels[n] = Color.DARK_GRAY.getRGB();
            }
        }
        // untere bildhaelfte helles grau
        for ( int i = pixels.length/2; i < pixels.length; i++ ) {
            if ( pixels[i] != Color.gray.getRGB() ) {
                pixels[i] = Color.gray.getRGB();
            }
        }
    }
    private myVector deltaDistance(myVector rayDir) {
        //Length of ray from one side to next in map
        myVector lenghtOfRay  = new myVector(
            Math.sqrt(1 + (rayDir.getY()*rayDir.getY()) / (rayDir.getX()*rayDir.getX())),
            Math.sqrt(1 + (rayDir.getX()*rayDir.getX()) / (rayDir.getY()*rayDir.getY())),
            0,0);
        return lenghtOfRay;
    }
    public int[] update(int[] pixels) {
        BackGround(pixels);
        // raycast
        // only in x direction because y will be generated from the same wall position
        for(int x = 0; x < width; x++) {                    
            double cameraX = 2 * x / (double)(width) -1;
            myVector sideDist  = new myVector();
            myVector step      = new myVector();
            myVector rayDir    = getRayDirPos(cameraX);
            myVector deltaDist = deltaDistance(rayDir);                            
            myVector mapPos    = new myVector(
                (int)world.getCam().getPos().getX(),
                (int)world.getCam().getPos().getY(),
                0,0
            );
            
            //Figure out the step direction and initial distance to a side
            stepDirection(step,sideDist,deltaDist,mapPos,rayDir);
            int side = hitWallSide(step,sideDist,deltaDist,mapPos);
            
            //Calculate distance to the point of impact
            double perpWallDist;
            if( side == 0)
                perpWallDist = Math.abs((mapPos.getX() - world.getCam().getPos().getX() + (1 - step.getX()) / 2) / rayDir.getX());
            else
                perpWallDist = Math.abs((mapPos.getY() - world.getCam().getPos().getY() + (1 - step.getY()) / 2) / rayDir.getY());	
            
            //Now calculate the height of the wall based on the distance from the camera
            int lineHeight;
            if(perpWallDist > 0) 
                lineHeight = Math.abs((int)(height / perpWallDist));
            else 
                lineHeight = height;
            
            //calculate lowest and highest pixel to fill in current stripe
            int drawStart = -lineHeight/2+ height/2;
            if(drawStart < 0)
                drawStart = 0;
            int drawEnd = lineHeight/2 + height/2;
            if(drawEnd >= height) 
                drawEnd = height - 1;                        
            
            addTexture(pixels,x,side,mapPos,step,rayDir,drawStart,drawEnd,lineHeight);
        }
        return pixels;
    }
    
    private void stepDirection(
            myVector step,
            myVector sideDist,
            myVector deltaDist,
            myVector mapPos,
            myVector rayDir ) {

        if (rayDir.getX() < 0) {
            step.setX(-1);
            sideDist.setX(world.getCam().getPos().getX() - mapPos.getX());
        } else {
            step.setX(1);
            sideDist.setX(mapPos.getX() + 1.0 - world.getCam().getPos().getX());
        }
        
        if (rayDir.getY() < 0) {
            step.setY(-1);
            sideDist.setY(world.getCam().getPos().getY() - mapPos.getY());
        } else {
            step.setY(1);
            sideDist.setY(mapPos.getY() + 1.0 - world.getCam().getPos().getY());
        }
        
        sideDist.set(sideDist.mul(deltaDist));
    }
    private int hitWallSide(
            myVector step,
            myVector sideDist,
            myVector deltaDist,
            myVector mapPos) {
        int side = 0;

        boolean hit = false;
        while(!hit) {
            //Jump to next square
            if (sideDist.getX() < sideDist.getY()) {
                sideDist.setX(sideDist.getX()+deltaDist.getX());
                mapPos.setX(mapPos.getX()+step.getX());
                side = 0;
            } else {
                sideDist.setY(sideDist.getY()+deltaDist.getY());
                mapPos.setY(mapPos.getY()+step.getY());
                side = 1;
            }
            if(world.getVal(mapPos) > 0) hit = true;
        }
        return side;
    }
    void addTexture(
            int[] pixels, int posX, int side,
            myVector mapPos, 
            myVector step, 
            myVector rayDir,
            int drawStart, int drawEnd, int lineHeight) {
        int texNum = world.getVal(mapPos) - 1;
        double wallX;//Exact position of where wall was hit

        if( side == 1 ) {
            wallX = (world.getCam().getPos().getX() + 
                    ((mapPos.getY() - world.getCam().getPos().getY() + (1 - step.getY()) / 2) / rayDir.getY()) * rayDir.getX());
        } else {
            wallX = (world.getCam().getPos().getY() + 
                    ((mapPos.getX() - world.getCam().getPos().getX() + (1 - step.getX()) / 2) / rayDir.getX()) * rayDir.getY());
        }
        wallX -= Math.floor(wallX);

        //x coordinate on the texture
        int texX = (int)(wallX * (world.getTexture(texNum).getSizeX()));
        
        if ( side == 0 && rayDir.getX() > 0 ) 
            texX = world.getTexture(texNum).getSizeX() - texX - 1;
        
        if ( side == 1 && rayDir.getY() < 0 ) 
            texX = world.getTexture(texNum).getSizeX() - texX - 1;
            
        //calculate y coordinate on texture
        for(int y = drawStart; y < drawEnd; y++) {
            int texY = (((y*2 - height + lineHeight) << 6) / lineHeight) / 2;
            int color;
            if( side ==0 ) 
                color = world.getTexture(texNum).pixels[texX + (texY * world.getTexture(texNum).getSizeY())];
            else 
                color = (world.getTexture(texNum).pixels[texX + (texY * world.getTexture(texNum).getSizeY())]>>1) & 8355711; //Make y sides darker
            pixels[posX + y*(width)] = color;
        }
    }
    
}

