package game.ammo;

import game.misc.Player;
import game.util.Collidable;
import game.util.Drawable;
import game.util.Updatable;
import game.util.Vector;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

public abstract class Ammo implements Updatable, Drawable, Collidable {
    protected Point position;
    protected Vector direction;
    protected Line2D deltaPosition;
    protected Rectangle size;
    protected int width, height, delay, damageRadius;
    protected float speed, damage;
    protected boolean isDone;

    public final AmmoType TYPE;

    public Ammo (AmmoType type, int x, int y, int width, int height, float angle, float speed){
        TYPE = type;
        position = new Point(x, y);
        direction = new Vector(Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle)));
        deltaPosition = new Line2D.Double();
        this.size = new Rectangle(x - width/2, y - height/2, width, height);
        this.speed = speed + TYPE.BASE_SPEED;
        this.damage = TYPE.BASE_DAMAGE;
        this.width = width;
        this.height = height;
        this.delay = TYPE.BASE_DELAY;
        this.damageRadius = TYPE.BASE_DAMAGE_RADIUS;
    }

    public void setDone(){
        isDone = true;
    }

    public Point getPosition(){
        return position;
    }

    public Rectangle getBounds(){
        size.setLocation(position.x - width/2, position.y - height/2);
        trans.setToIdentity();
        trans.rotate(Math.toRadians(direction.getAngle() + 90), position.x, position.y);
        return trans.createTransformedShape(size).getBounds();
    }

    public boolean intersects(Rectangle r){
        return (getBounds().intersects(r) || deltaPosition.intersects(r)) && !isDone;
    }

    public float getDamage(){
        return damage;
    }

    public int getDamageRadius(){
        return damageRadius;
    }

    public boolean isDone(){
        return isDone;
    }

    private static AffineTransform trans = new AffineTransform();
}
