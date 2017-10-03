package game.enemies;

import game.Constants;
import game.ammo.Ammo;
import game.ammo.AmmoType;
import game.ammo.Bullet;
import game.ammo.Missile;
import game.misc.Player;
import game.scenes.OuterSpace;
import game.util.*;
import game.util.Vector;

import java.awt.*;
import java.util.*;

import static game.Render.*;
import static game.ResourceManager.getTexture;
import static game.ResourceManager.getTextureSet;

public class Spaceship extends Enemy{
    private Vector deltaPos;
    private HashMap<String, ColorData> colorDatas;
    private Animation fire, explosion;
    private AmmoType currentAmmo;
    private boolean alerted, accelerating, exploding;
    private float speed, deltaAngle;

    public static final int WIDTH = 50, HEIGHT = 100;

    public Spaceship(int x, int y) {
        super(EnemyType.SPACESHIP, x, y, WIDTH, HEIGHT);
        deltaPos = new Vector();
        currentAmmo = AmmoType.BULLET;
        double rads = Math.random() * Math.PI * 2;
        direction = new Vector(Math.cos(rads), Math.sin(rads));
        colorDatas = new HashMap<>();
        colorDatas.put("ship_outline",  new ColorData(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
        colorDatas.put("ship_hull", new ColorData(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
        colorDatas.put("ship_wings", new ColorData(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
        colorDatas.put("ship_window1", new ColorData(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
        colorDatas.put("ship_window2", new ColorData(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
        colorDatas.put("ship_window3", new ColorData(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
        colorDatas.put("ship_window1_outline", new ColorData(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
        colorDatas.put("ship_window2_outline", new ColorData(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
        colorDatas.put("ship_window3_outline", new ColorData(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
        fire = new Animation(getTextureSet("rocket_fire"), 16);
        explosion = new Animation(getTextureSet("explosion4"), 16);
        explosion.setOneTime(true);
        accelerating = true;
    }

    @Override
    public void update(int delta) {
        //detection
        Player player = OuterSpace.getPlayer();
        if(!exploding && !isDead) {
            Vector distance = new Vector(player.getPosition(), position);
            if (!alerted) {
                if (distance.length() < detectionRadius)
                    alerted = true;
            } else {
                if (distance.length() > followRadius)
                    alerted = false;
            }
            accelerating = !(distance.length() < (detectionRadius / 4) * 3);

            //movement
            if (alerted) {
                Vector target = new Vector(player.getPosition().x - position.x, player.getPosition().y - position.y);
                float thisAngle = direction.getAngle();
                float tarAngle = target.getAngle();
                if (!(thisAngle <= tarAngle + angleSpeed && thisAngle >= tarAngle - angleSpeed)) {
                    if (thisAngle < tarAngle) {
                        deltaAngle = tarAngle - thisAngle;
                        if (deltaAngle > 180)
                            thisAngle -= angleSpeed;
                        else
                            thisAngle += angleSpeed;
                    } else if (thisAngle > tarAngle) {
                        deltaAngle = thisAngle - tarAngle;
                        if (deltaAngle < 180)
                            thisAngle -= angleSpeed;
                        else
                            thisAngle += angleSpeed;
                    }
                } else {
                    thisAngle = tarAngle;
                }
                direction.set((float) Math.cos(Math.toRadians(thisAngle)), (float) Math.sin(Math.toRadians(thisAngle)));
            }

            if (accelerating) {
                speed += delta * acceleration;
                if (speed > maxSpeed)
                    speed = maxSpeed;
            } else {
                speed -= delta / 2 * acceleration;
                if (speed < 0)
                    speed = 0;
            }

            deltaPos = direction.mul(speed * Constants.SPEED_MULTIPLIER * delta); //always max speed
            position = Vector.toPoint(deltaPos.add(position));

            //addUpdates
            fireDelay += delta;
            if (alerted && deltaAngle < 45)
                fire();

            //update health
            if (health < maxHealth) {
                health += healthRegen * delta;
            }
            if (health > maxHealth) {
                health = maxHealth;
            }

        } else {
            explosion.update(delta);
            if(explosion.isDone()){
                isDead = true;
            }
        }

        //update projectiles
        for(Ammo m : projectiles){
            m.update(delta);
        }


        //cleanup
        Iterator<Ammo> aIter = projectiles.iterator();
        while(aIter.hasNext()){
            Ammo current = aIter.next();
            if(current.isDone())
                aIter.remove();
        }

        //fire update
        fire.update(delta);
    }

    @Override
    public void draw() {
        //draw projectiles
        for(Ammo m : projectiles){
            m.draw();
        }
        if(!exploding && !isDead) {
            //draw ship
            float fireDistance = speed; //add speed
            if (fireDistance > maxSpeed)
                fireDistance = maxSpeed;

            float angle = direction.getAngle() + 90;
            cDrawFlipTexture(fire.getCurrentFrame(),
                    position.x - (((height / 100f) * 36.3f + (height / 3) * ((fireDistance / maxSpeed) / 2))
                            * (float) Math.cos(Math.toRadians(angle - 90))),
                    position.y - (((height / 100f) * 36.3f + (height / 3) * ((fireDistance / maxSpeed) / 2))
                            * (float) Math.sin(Math.toRadians(angle - 90))),
                    width / 5, (height / 3) * (fireDistance / maxSpeed), false, angle);
            setTextureColorReset(false);
            setColor(colorDatas.get("ship_outline"));
            cDrawTexture(getTexture("ship_outline"), position.x, position.y, width / 2, height / 2, angle);
            setColor(colorDatas.get("ship_hull"));
            cDrawTexture(getTexture("ship_hull"), position.x, position.y, width / 2, height / 2, angle);
            setColor(colorDatas.get("ship_wings"));
            cDrawTexture(getTexture("ship_wings"), position.x, position.y, width / 2, height / 2, angle);
            setColor(colorDatas.get("ship_window1_outline"));
            cDrawTexture(getTexture("ship_window1_outline"), position.x, position.y, width / 2, height / 2, angle);
            setColor(colorDatas.get("ship_window1"));
            cDrawTexture(getTexture("ship_window1"), position.x, position.y, width / 2, height / 2, angle);
            setColor(colorDatas.get("ship_window2_outline"));
            cDrawTexture(getTexture("ship_window2_outline"), position.x, position.y, width / 2, height / 2, angle);
            setColor(colorDatas.get("ship_window2"));
            cDrawTexture(getTexture("ship_window2"), position.x, position.y, width / 2, height / 2, angle);
            setColor(colorDatas.get("ship_window3_outline"));
            cDrawTexture(getTexture("ship_window3_outline"), position.x, position.y, width / 2, height / 2, angle);
            setColor(colorDatas.get("ship_window3"));
            cDrawTexture(getTexture("ship_window3"), position.x, position.y, width / 2, height / 2, angle);
            setTextureColorReset(true);

            //health bar
            if (health != maxHealth) {
                setColor(Color.RED);
                fillRect(position.x - Constants.HEALTH_BAR_WIDTH / 2, position.y - Constants.HEALTH_BAR_Y_DISPLACEMENT - Constants.HEALTH_BAR_HEIGHT,
                        Constants.HEALTH_BAR_WIDTH, Constants.HEALTH_BAR_HEIGHT);
                setColor(Color.GREEN);
                fillRect(position.x - Constants.HEALTH_BAR_WIDTH / 2, position.y - Constants.HEALTH_BAR_Y_DISPLACEMENT - Constants.HEALTH_BAR_HEIGHT,
                        Constants.HEALTH_BAR_WIDTH * (health / maxHealth), Constants.HEALTH_BAR_HEIGHT);
            }
        } else {
            cDrawTexture(explosion.getCurrentFrame(), position.x, position.y, 128, 128);
        }
    }

    @Override
    public void die(){
        exploding = true;
    }

    @Override
    public void fire() {
        if (fireDelay > currentAmmo.BASE_DELAY) {
            switch (currentAmmo){
                case BULLET:
                    projectiles.add(new Bullet(this, colorDatas.get("ship_hull")));
                    break;
            }
            fireDelay = 0;
        }
    }

    public float getSpeed(){
        return speed;
    }

    private static Random rand = new Random();
}
