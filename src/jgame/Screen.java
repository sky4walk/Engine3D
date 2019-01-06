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

<<<<<<< HEAD
    private void BackGround(int[] pixels) {
=======
    private myVector getRayDirPos(double cameraX) {
        myVector dir = world.getCam().getDir();
        myVector plane = world.getCam().getPlane();
        myVector scalePlane = plane.scale(cameraX);
        myVector rayDir = dir.add(scalePlane);
        return rayDir;
    }
    public int[] update(int[] pixels) {
>>>>>>> c67e7c96e8b0e2fc3a7449f95dd086eec06bab33
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
    public int[] update(int[] pixels) {
        BackGround(pixels);
        // raycast
        // only in x direction because y will be generated from the same wall position
        for(int x = 0; x < width; x++) {                    
            double cameraX = 2 * x / (double)(width) -1;
            // ray position 
            myVector rayDir = getRayDirPos(cameraX);
            
            double rayDirX = world.getCam().getDir().getX() + world.getCam().getPlane().getX() * cameraX;
            double rayDirY = world.getCam().getDir().getY() + world.getCam().getPlane().getY() * cameraX;
            
            //position in Map
            myVector mapPos = new myVector(
                (int)world.getCam().getPos().getX(),
                (int)world.getCam().getPos().getY(),
                0,0
            );
            int mapX = (int)world.getCam().getPos().getX();
            int mapY = (int)world.getCam().getPos().getY();
            
            //length of ray from current position to next x or y-side
            double sideDistX;
            double sideDistY;
            
            //Length of ray from one side to next in map
            double deltaDistX = Math.sqrt(1 + (rayDirY*rayDirY) / (rayDirX*rayDirX));
            double deltaDistY = Math.sqrt(1 + (rayDirX*rayDirX) / (rayDirY*rayDirY));
            double perpWallDist;
            
            //Direction to go in x and y
            int stepX, stepY;
            boolean hit = false;//was a wall hit
            int side=0;//was the wall vertical or horizontal
            
            //Figure out the step direction and initial distance to a side
            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (world.getCam().getPos().getX() - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - world.getCam().getPos().getX()) * deltaDistX;
            }
            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (world.getCam().getPos().getY() - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - world.getCam().getPos().getY()) * deltaDistY;
            }
            //Loop to find where the ray hits a wall
            while(!hit) {
                //Jump to next square
                if (sideDistX < sideDistY)
                {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                }
                else
                {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
                //Check if ray has hit a wall
                //System.out.println(mapX + ", " + mapY + ", " + map[mapX][mapY]);
                if(world.getVal(mapX,mapY) > 0) hit = true;
            }
            //Calculate distance to the point of impact
            if(side==0)
                perpWallDist = Math.abs((mapX - world.getCam().getPos().getX() + (1 - stepX) / 2) / rayDirX);
            else
                perpWallDist = Math.abs((mapY - world.getCam().getPos().getY() + (1 - stepY) / 2) / rayDirY);	
            //Now calculate the height of the wall based on the distance from the camera
            int lineHeight;
            if(perpWallDist > 0) lineHeight = Math.abs((int)(height / perpWallDist));
            else lineHeight = height;
            //calculate lowest and highest pixel to fill in current stripe
            int drawStart = -lineHeight/2+ height/2;
            if(drawStart < 0)
                drawStart = 0;
            int drawEnd = lineHeight/2 + height/2;
            if(drawEnd >= height) 
                drawEnd = height - 1;
            //add a texture
            int texNum = world.getVal(mapX, mapY) - 1;
            double wallX;//Exact position of where wall was hit
            if(side==1) {//If its a y-axis wall
                wallX = (world.getCam().getPos().getX() + ((mapY - world.getCam().getPos().getY() + (1 - stepY) / 2) / rayDirY) * rayDirX);
            } else {//X-axis wall
                wallX = (world.getCam().getPos().getY() + ((mapX - world.getCam().getPos().getX() + (1 - stepX) / 2) / rayDirX) * rayDirY);
            }
            wallX-=Math.floor(wallX);
            //x coordinate on the texture
            int texX = (int)(wallX * (world.getTexture(texNum).getSizeX()));
            if(side == 0 && rayDirX > 0) texX = world.getTexture(texNum).getSizeX() - texX - 1;
            if(side == 1 && rayDirY < 0) texX = world.getTexture(texNum).getSizeX() - texX - 1;
            //calculate y coordinate on texture
            for(int y=drawStart; y<drawEnd; y++) {
                int texY = (((y*2 - height + lineHeight) << 6) / lineHeight) / 2;
                int color;
                if(side==0) color = world.getTexture(texNum).pixels[texX + (texY * world.getTexture(texNum).getSizeY())];
                else color = (world.getTexture(texNum).pixels[texX + (texY * world.getTexture(texNum).getSizeY())]>>1) & 8355711;//Make y sides darker
                pixels[x + y*(width)] = color;
            }
        }
        return pixels;
    }
}

