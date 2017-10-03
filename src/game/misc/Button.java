package game.misc;

import game.util.ColorData;
import org.newdawn.slick.UnicodeFont;

import java.awt.*;

import static game.Render.*;
import static game.Render.drawString;
import static game.Render.getCurrentFont;
import static game.ResourceManager.getFont;

public class Button{
    protected float x, y, width, height;
    protected String text;
    protected Rectangle bounds;
    protected boolean isHighlighted;
    protected UnicodeFont font;

    public Button(int x, int y, int width, int height, UnicodeFont font, String text){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.font = font;
        bounds = new Rectangle(x, y, width, height);
    }

    public Button(int x, int y, int width, int height, UnicodeFont font){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = "";
        this.font = font;
        bounds = new Rectangle(x, y, width, height);
    }

    public void update(int delta){
        isHighlighted = getBounds().contains(getMousePoint());
    }

    public void draw(int alpha){
        if(isHighlighted){
            setColor(255, 255, 255, alpha);
            fillRect(x, y, width, height);
            setColor(0, 0, 0, alpha);
            drawRect(x, y, width, height);
            setFont(font);
            drawString(text, x + width/2 - getCurrentFont().getWidth(text)/2,
                    y + height/2 - (getCurrentFont().getAscent() + getCurrentFont().getDescent())/2);
        } else {
            setColor(0, 0, 0, alpha);
            fillRect(x, y, width, height);
            setColor(255, 255, 255, alpha);
            drawRect(x, y, width, height);
            setFont(font);
            drawString(text, x + width/2 - getCurrentFont().getWidth(text)/2,
                    y + height/2 - (getCurrentFont().getAscent() + getCurrentFont().getDescent())/2);
        }
    }

    public void draw(){
        if(isHighlighted){
            setColor(255, 255, 255);
            fillRect(x, y, width, height);
            setColor(0, 0, 0);
            drawRect(x, y, width, height);
            setFont(font);
            drawString(text, x + width/2 - getCurrentFont().getWidth(text)/2,
                    y + height/2 - (getCurrentFont().getAscent() + getCurrentFont().getDescent())/2);
        } else {
            setColor(0, 0, 0);
            fillRect(x, y, width, height);
            setColor(255, 255, 255);
            drawRect(x, y, width, height);
            setFont(font);
            drawString(text, x + width/2 - getCurrentFont().getWidth(text)/2,
                    y + height/2 - (getCurrentFont().getAscent() + getCurrentFont().getDescent())/2);
        }
    }

    public void clicked(){}

    public Rectangle getBounds(){
        bounds.setBounds((int) x, (int) y, (int) width, (int) height);
        return bounds;
    }
}