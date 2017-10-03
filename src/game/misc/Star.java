package game.misc;

import game.Render;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.util.Random;

import static game.Render.*;

public class Star implements Comparable<Star>{
    private float x, y, size;

    public static final int BORDER = 100;
    public static final float MIN_SIZE = .5f, MAX_SIZE = 2.5f;
    public static final int SMALL_CHANCE = 75;
    public static final int SEGMENTS = 10;

    public Star(){
        this(true);
    }

    public Star(boolean useDefaultBorder){
        if(useDefaultBorder) {
            x = Render.getX() + rand.nextInt(Render.getWidth() + 2 * BORDER) - BORDER;
            y = Render.getY() + rand.nextInt(Render.getHeight() + 2 * BORDER) - BORDER;
        } else {
            x = Render.getX() + rand.nextInt(Render.getWidth() + 2 * Render.getWidth()) - Render.getWidth();
            y = Render.getY() + rand.nextInt(Render.getHeight() + 2 * Render.getHeight()) - Render.getHeight();
        }
        this.size = ((MAX_SIZE - MIN_SIZE) * rand.nextFloat()) + MIN_SIZE;
    }

    public Star(float x, float y){
        this(x, y, false);
    }

    public Star(float x, float y, boolean smallChance){
        this.x = x;
        this.y = y;
        if(smallChance){
            int r = rand.nextInt(100) + 1;
            if(r < SMALL_CHANCE){
                this.size = ((MIDDLE_STAR.getSize() - MIN_SIZE) * rand.nextFloat()) + MIN_SIZE;
            } else {
                this.size = ((MAX_SIZE - MIDDLE_STAR.getSize()) * rand.nextFloat()) + MIDDLE_STAR.getSize();
            }
        } else {
            this.size = ((MAX_SIZE - MIN_SIZE) * rand.nextFloat()) + MIN_SIZE;
        }
    }

    public void move(float x, float y){
        this.x += x;
        this.y += y;
    }

    public void draw(){
        draw(255);
    }

    public void draw(int alpha){
        if(x > Render.getX() && x < Render.getX() + Render.getWidth() && y > Render.getY() && y < Render.getY() + Render.getHeight()) {
            /*
            setCircleSegments(SEGMENTS);
            setColor(255, 255, 255, alpha);
            cFillCircle(x, y, size);
            */
            setColor(255, 255, 255, alpha);
            drawPoint(x, y, size);
        }
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getSize(){
        return size - MIN_SIZE;
    }

    private static Random rand = new Random();

    @Override
    public int compareTo(Star o) {
        if(size < o.getSize())
            return -1;
        else if (size > o.getSize())
            return 1;
        else
            return 0;
    }

    private Star(String middleStar){
        this.x = 0;
        this.y = 0;
        this.size = ((MAX_SIZE-MIN_SIZE)/2f) + MIN_SIZE;
    }

    public static final Star MIDDLE_STAR = new Star("");
}
