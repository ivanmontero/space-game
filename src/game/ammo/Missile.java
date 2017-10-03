package game.ammo;

import game.Constants;
import game.enemies.Enemy;
import game.misc.Player;
import game.util.*;
import org.newdawn.slick.opengl.Texture;

import java.awt.*;
import java.awt.geom.Line2D;

import static game.Render.*;
import static game.ResourceManager.*;

public class Missile extends Ammo{
    private Texture hull, outline;
    private ColorData hullColor, outlineColor;
    private Animation animation;
    private boolean exploding;
    private int lifetime;

    public static final int HALF_WIDTH = 8, HALF_HEIGHT = 32;

    public Missile (int x, int y, float angle, ColorData hullColor, ColorData outlineColor, float initialSpeed){
        super(AmmoType.MISSILE, x, y, HALF_WIDTH, HALF_HEIGHT, angle, initialSpeed);
        this.hullColor = hullColor;
        this.outlineColor = outlineColor;
        hull = getTexture("missile_hull");
        outline = getTexture("missile_outline");
        animation = new Animation(getTextureSet("explosion4"), 16);
        animation.setOneTime(true);
    }

    public Missile(Enemy e, ColorData hullColor, ColorData outlineColor) {
        this(e.getX(), e.getY(), e.getDirection().getAngle(), hullColor, outlineColor, e.getSpeed());
    }

    public Missile (Player player){
        super(AmmoType.MISSILE, player.getX(), player.getY(), HALF_WIDTH, HALF_HEIGHT, player.getDirection().getAngle(),
                player.getSpeed());
        this.hullColor = player.getPlayerData().getColorData("ship_hull");
        this.outlineColor = player.getPlayerData().getColorData("ship_outline");
        hull = getTexture("missile_hull");
        outline = getTexture("missile_outline");
        animation = new Animation(getTextureSet("explosion4"), 16);
        animation.setOneTime(true);
    }

    @Override
    public void update(int delta){
        lifetime += delta;
        if(lifetime > TYPE.BASE_LIFETIME)
            explode();
        if(!exploding) {
            Point initPos = new Point((int) position.getX(), (int) position.getY());
            position.setLocation(Vector.toPoint(direction.normalize()
                    .mul(speed * Constants.SPEED_MULTIPLIER * delta).add(position)));
            deltaPosition.setLine(initPos, position);
        } else {
            animation.update(delta);
            if(animation.isDone()){
                isDone = true;
            }
        }
        size.setLocation(position.x - HALF_WIDTH /2, position.y - HALF_HEIGHT /2);
    }

    @Override
    public void draw() {
        if (!isDone) {
            if (!exploding) {
                setTextureColorReset(false);
                setColor(outlineColor);
                cDrawTexture(outline, position.x, position.y, HALF_WIDTH, HALF_HEIGHT, direction.getAngle() + 90);
                setColor(hullColor);
                cDrawTexture(hull, position.x, position.y, HALF_WIDTH, HALF_HEIGHT, direction.getAngle() + 90);
                setTextureColorReset(true);
            } else {
                cDrawTexture(animation.getCurrentFrame(), position.x, position.y, 128, 128);
            }
        }
    }

    @Override
    public void setDone(){
        explode();
    }

    @Override
    public boolean intersects(Rectangle r){
        return (getBounds().intersects(r) || deltaPosition.intersects(r)) && !isDone && !exploding;
    }

    public Line2D getDeltaPosition(){
        return deltaPosition;
    }

    public void explode(){
        exploding = true;
    }

    public boolean isDone(){
        return isDone;
    }
}
