package game.util;

import java.awt.*;
import java.io.Serializable;

public class ColorData implements Serializable{
    private static final long serialVersionUID = 987301041034126359L;
    private int r, g, b, a;

    public ColorData(){
        r = 0;
        g = 0;
        b = 0;
        a = 0;
    }

    public ColorData(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
        a = 255;
    }

    public ColorData(int r, int g, int b, int a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public int getRed() {
        return r;
    }

    public int getGreen() {
        return g;
    }

    public int getBlue() {
        return b;
    }

    public int getAlpha() {
        return a;
    }

    public void setRed(int red) {
        this.r = red;
    }

    public void setGreen(int green) {
        this.g = green;
    }

    public void setBlue(int blue) {
        this.b = blue;
    }

    public void setAlpha(int alpha) {
        this.a = alpha;
    }

    public void set(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
        a = 255;
    }

    public void set(int r, int g, int b, int a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color toColor(){
        return new Color(r, g, b, a);
    }

    public ColorData alpha(int a){
        return new ColorData(r, g, b, a);
    }
}
