package game.misc;

import game.Constants;
import game.ammo.Ammo;
import game.ammo.AmmoType;
import game.ammo.Bullet;
import game.ammo.Missile;
import game.singletons.PlayerData;
import game.util.*;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;

import static game.Render.*;

public class Player extends Entity {
    private PlayerData pd;
    private Vector target, deltaPos, mouseDistance;
    private HashSet<Ammo> projectiles;
    private AmmoType currentAmmo;
    private float speed;
    private long fireDelay;

    private final int ACCELERATION_BOUND = 800;

    public Player(){
        super(0, 0, 50, 100);
        pd = PlayerData.getInstance();
        direction = new Vector(0, 1);
        deltaPos = new Vector();
        mouseDistance = new Vector();
        target = new Vector();
        width = 50;
        height = 100;
        speed = 0.0f;
        health = pd.getMaxHealth() - 20;
        projectiles = new HashSet<>();
        size = new Rectangle(pd.getX() - width/2, pd.getY() - height/2);
        currentAmmo = AmmoType.MISSILE;
        fireDelay = 1000000000;
    }

    /*
    public Player(int x, int y){
        pd = PlayerData.getInstance();
        direction = new Vector();
        deltaPos = new Vector();
        width = getTexture("ship_outline").getTextureWidth()/10;
        height = getTexture("ship_outline").getTextureHeight()/10;
        speed = 0.0f;
        setPosition(x, y);
        projectiles = new HashSet<>();
        hitbox = new Rectangle(x - width/2, y - height/2);
    }
    */

    @Override
    public void update(int delta){
        /*
        mouseDistance.set(getMouseX() - pd.getX(), getMouseY() - pd.getY());
        pd.update(delta);
        direction.set(getMouseX() - pd.getX(), getMouseY() - pd.getY());
        if(Mouse.isButtonDown(0) && direction.length() > 100){
            float accel = pd.getAcceleration() * ((float) mouseDistance.length()/ACCELERATION_BOUND);
            if(accel > pd.getAcceleration())
                accel = pd.getAcceleration();
            setSpeed(speed + delta * accel);
        } else {
            setSpeed(speed + -delta/2 * pd.getAcceleration());
        }
        if(direction.length() != 0)
            deltaPos = direction.normalize().mul(speed * PlayerData.SPEED_MULTIPLIER * delta);
        pd.setPosition(Vector.toPoint(deltaPos.add(pd.getPosition())));
        */
        // turn speed


        pd.update(delta);
        target.set(getMouseX() - pd.getX(), getMouseY() - pd.getY());
        float thisAngle = direction.getAngle();
        float tarAngle = target.getAngle();
        if(!(thisAngle <= tarAngle + pd.getAngleSpeed() && thisAngle >= tarAngle - pd.getAngleSpeed())){
            float deltaAngle;
            if(thisAngle < tarAngle){
                deltaAngle = tarAngle - thisAngle;
                if(deltaAngle > 180)
                    thisAngle -= pd.getAngleSpeed();
                else
                    thisAngle += pd.getAngleSpeed();
            } else if (thisAngle > tarAngle){
                deltaAngle = thisAngle - tarAngle;
                if(deltaAngle < 180)
                    thisAngle -= pd.getAngleSpeed();
                else
                    thisAngle += pd.getAngleSpeed();
            }
        } else {
            thisAngle = tarAngle;
        }
        /*
        if(thisAngle < 0)
            thisAngle = 359.9f;
        if(thisAngle == 360)
            thisAngle = 0;
            */
        direction.set((float) Math.cos(Math.toRadians(thisAngle)), (float) Math.sin(Math.toRadians(thisAngle)));

        if(Mouse.isButtonDown(0)){
            float accel = pd.getAcceleration() * ((float) target.length()/ACCELERATION_BOUND);
            if(accel > pd.getAcceleration())
                accel = pd.getAcceleration();
            setSpeed(speed + delta * accel);
        } else {
            setSpeed(speed + -delta/2 * pd.getAcceleration());
        }
        if(target.length() != 0)
            deltaPos = direction.mul(speed * Constants.SPEED_MULTIPLIER * delta);
        pd.setPosition(Vector.toPoint(deltaPos.add(pd.getPosition())));

        //TODO: update projectiles
        for(Ammo m : projectiles){
            m.update(delta);
        }

        //update hitbox
        size.setLocation(pd.getX() - width/2, pd.getY() - height/2);
        fireDelay += delta;

        //update health
        if(health < pd.getMaxHealth()){
            health += pd.getHealthRegen() * delta;
        }
        if(health > pd.getMaxHealth()){
            health = pd.getMaxHealth();
        }

        //cleanup
        Iterator<Ammo> aIter = projectiles.iterator();
        while(aIter.hasNext()){
            Ammo current = aIter.next();
            if(current.isDone())
                aIter.remove();
        }
    }

    @Override
    public void draw(){
        for(Ammo m : projectiles){
            m.draw();
        }

        pd.draw(pd.getPosition(), width, height, direction.getAngle() + 90, speed);
        if(health != pd.getMaxHealth()){
            setColor(Color.RED);
            fillRect(pd.getX() - Constants.HEALTH_BAR_WIDTH/2, pd.getY() - Constants.HEALTH_BAR_Y_DISPLACEMENT - Constants.HEALTH_BAR_HEIGHT,
                    Constants.HEALTH_BAR_WIDTH, Constants.HEALTH_BAR_HEIGHT);
            setColor(Color.GREEN);
            fillRect(pd.getX() - Constants.HEALTH_BAR_WIDTH/2, pd.getY() - Constants.HEALTH_BAR_Y_DISPLACEMENT - Constants.HEALTH_BAR_HEIGHT,
                    Constants.HEALTH_BAR_WIDTH * (health/pd.getMaxHealth()), Constants.HEALTH_BAR_HEIGHT);
        }
    }

    public void fire(){
        if (fireDelay > currentAmmo.BASE_DELAY) {
            switch (currentAmmo){
                case MISSILE:
                    projectiles.add(new Missile(this));
                    break;
                case BULLET:
                    projectiles.add(new Bullet(this));
                    break;
            }
            fireDelay = 0;
        }
    }

    public void modHealth(float delta){
        health += delta;
        if(health <= 0)
            health = 0;
    }

    public void setPosition(Point position) {
        this.pd.setPosition(position);
    }

    public void setPosition(int x, int y) {
        this.pd.getPosition().setLocation(x, y);
    }

    @Override
    public void setX(int x){
        this.pd.getPosition().x = x;
    }

    @Override
    public void setY(int y){
        this.pd.getPosition().y = y;
    }

    public void setDimensions(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void setSpeed(float speed){
        this.speed = speed;
        if(this.speed > pd.getMaxSpeed())
            this.speed = pd.getMaxSpeed();
        else if (speed < 0)
            this.speed = 0;
    }

    public void setMaxSpeed(float speed){
        pd.setMaxSpeed(speed);
    }

    public void setAcceleration(float acceleration){
        pd.setAcceleration(acceleration);
    }

    public void setCurrentAmmo(AmmoType ammo){
        this.currentAmmo = ammo;
    }

    public void modMaxSpeed(float delta){
        pd.modMaxSpeed(delta);
    }

    public void modAcceleration(float delta){
        pd.modAcceleration(delta);
    }

    @Override
    public Point getPosition() {
        return pd.getPosition();
    }

    public Vector getDirection(){
        return direction;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public int getX(){
        return pd.getPosition().x;
    }

    @Override
    public int getY(){
        return pd.getPosition().y;
    }

    public float getSpeed() {
        return speed;
    }

    public float getMaxSpeed(){
        return pd.getMaxSpeed();
    }

    public float getAcceleration(){
        return pd.getAcceleration();
    }

    public Vector getDeltaPosition(){
        return deltaPos;
    }

    public PlayerData getPlayerData(){
        return pd;
    }

    public HashSet<Ammo> getProjectiles(){
        return projectiles;
    }

    public AmmoType getCurrentAmmo(){
        return currentAmmo;
    }

    @Override
    public Rectangle getBounds(){
        size.setBounds(0, 0, width, height);
        size.setLocation(pd.getPosition().x - width/2, pd.getPosition().y - height/2);
        trans.setToIdentity();
        trans.rotate(Math.toRadians(direction.getAngle() + 90), pd.getPosition().x, pd.getPosition().y);
        return trans.createTransformedShape(size).getBounds();
    }

}
