package game.util;

import java.awt.*;
import java.awt.geom.AffineTransform;

//TODO: Update with Point and Direction

public abstract class Entity implements Updatable, Drawable, Collidable, Vulnerable{

    protected Point position;
    protected Vector direction;
    protected Rectangle size;
    protected int width, height;
    protected float health;
    protected boolean isDead;

    public Entity(){
        this.position = new Point();
        this.size = new Rectangle();
        this.direction = new Vector();
    }

    public Entity(int x, int y, int width, int height){
        this.position = new Point(x, y);
        this.width = width;
        this.height = height;
        direction = new Vector(0, 1);
        size = new Rectangle(x - width/2, y - height/2, width, height);
    }

    abstract public void update(int delta);

    abstract public void draw();

    public int getX(){
        return position.x;
    }

    public int getY(){
        return position.y;
    }

    public Point getPosition(){
        return position;
    }

    public Vector getDirection(){
        return direction;
    }

    public Rectangle getBounds(){
        size.setLocation(position.x - width/2, position.y - height/2);
        trans.setToIdentity();
        trans.rotate(Math.toRadians(direction.getAngle() + 90), position.x, position.y); //90 degrees to make it upright
        return trans.createTransformedShape(size).getBounds();
    }

    public void setY(int y) {
        this.position.y = y;
    }

    public void setX(int x) {
        this.position.x = x;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getHealth(){
        return health;
    }

    @Override
    public void modHealth(float delta){
        if(!isDead) {
            health += delta;
            if(health <= 0) {
                die();
                health = 0;
            }
        }
    }

    public void die(){
        isDead = true;
    }

    public boolean isDead(){
        return isDead;
    }

    protected static AffineTransform trans = new AffineTransform();
}
