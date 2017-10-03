package game.scenes;

import game.Main;
import game.Scenes;
import game.ammo.Ammo;
import game.ammo.AmmoType;
import game.enemies.Enemy;
import game.enemies.Spaceship;
import game.misc.Player;
import game.misc.Star;
import game.util.Animation;
import game.Constants;
import game.util.Vector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

import static game.Render.*;
import static game.ResourceManager.*;

/**
 * TODO:
 *  - Changing ambient star movement (sine curve)
 *  - Planets
 *      + Planet transition
 *          -> New world/atmosphere
*       + Player sprite (Astronaut)
 *  - Enemies
 *      + Random spawning
 *  - Pause screen
 *  - Upgrades
 *  - Player ammo
 *      + Spaceship collision/explosion
 *      + Damage mechanics
 *      + Health bar updating
 *      + New weapons
 *  - Leveling system
 *
 */

public class OuterSpace extends Scene {
    private Random rand = new Random();
    private TreeSet<Star> stars, newStars;
    private HashSet<Enemy> enemies;
    private Earth earth;

    private final int MAX_ENEMIES = 50;
    private final int SPAWN_RANGE = 2500;
    private final int SPAWN_BUFFER = 500;
    private final int MAX_STARS = 5 * 500;
    private final float STAR_SPEED_DAMPENING = .1f;
    private final float STAR_AMBIENT_MOVEMENT_X = .15f, STAR_AMBIENT_MOVEMENT_Y = .05f;

    @Override
    public void init() {
        player = new Player();
        stars = new TreeSet<>();
        newStars = new TreeSet<>();
        for(int i = 0; i < MAX_STARS; i++){
            stars.add(new Star(false));
        }
        player.setPosition(0, 500);
        earth = new Earth(0, 0);
        enemies = new HashSet<>();
        enemies.add(new Spaceship(300, 300));
    }

    @Override
    public void update(int delta) {
        //System.out.println(enemies.size());
        //test

        //for(int i = 0; i < 12; i++)
        //    enemies.add(new Spaceship(getX() + (int) (Math.random() * getWidth()), getY() + (int) (Math.random() * getHeight())));

        //keyboard input
        while(Keyboard.next()){
            if(Keyboard.getEventKeyState()){
                switch (Keyboard.getEventKey()){
                    case Keyboard.KEY_TAB:
                        earth.blowUp();
                        break;
                    case Keyboard.KEY_ESCAPE:
                        Main.enterScene(Scenes.PAUSE_MENU);
                        break;
                }
            }
        }

        //mouse input
        while(Mouse.next()){
            if(Mouse.getEventButtonState()){
                switch (Mouse.getEventButton()){
                    case 1:
                        player.fire();
                        break;
                }
            }
        }


        int dWheel = Mouse.getDWheel();
        if(dWheel != 0){
            if(player.getCurrentAmmo() == AmmoType.MISSILE)
                player.setCurrentAmmo(AmmoType.BULLET);
            else
                player.setCurrentAmmo(AmmoType.MISSILE);
        }

        if(Mouse.isButtonDown(1))
            player.fire();

        //keyboard input

        //Player updating
        player.update(delta);

        /*
        if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            player.modMaxSpeed(5000f);
            player.modAcceleration(50f);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            player.modMaxSpeed(-5000f);
            player.modAcceleration(-50f);
        }
*/


        //enemy updating
        for(Enemy e : enemies){
            e.update(delta);
        }

        //FOV Moving
        int mouseDistanceX = getMouseX() - (getX() + getWidth()/2);
        int mouseDistanceY = getMouseY() - (getY() + getHeight()/2);

        setFOV((player.getX() - getWidth()/2) + mouseDistanceX/3 +
                        (int)(player.getDirection().normalize().mul(player.getSpeed()*250* Constants.SPEED_MULTIPLIER).x),
                (player.getY() - getHeight()/2) + mouseDistanceY/3 +
                        (int)(player.getDirection().normalize().mul(player.getSpeed()*250*Constants.SPEED_MULTIPLIER).y),
                getWidth(), getHeight());

        if(player.getX() < getX() + getWidth()/20)
            setFOV(player.getX() - getWidth() / 20, getY(), getWidth(), getHeight());
        if(player.getY() < getY() + getHeight()/20)
            setFOV(getX(), player.getY() - getHeight() / 20, getWidth(), getHeight());
        if(player.getX() > getX() + (getWidth()/20)*19)
            setFOV(player.getX() - (getWidth() / 20) * 19, getY(), getWidth(), getHeight());
        if(player.getY() > getY() + (getHeight()/20)*19)
            setFOV(getX(), player.getY() - (getHeight() / 20) * 19, getWidth(), getHeight());

        // Star updating
        float playerDeltaX = (float) player.getDeltaPosition().x;
        float playerDeltaY = (float) player.getDeltaPosition().y;
        Iterator<Star> sItr = stars.iterator();
        while(sItr.hasNext()){
            Star s = sItr.next();
            //movement
            /*
            if(s.getSize() < Star.MIDDLE_STAR.getSize()){
                float deltaSize = (Star.MIDDLE_STAR.getSize() - s.getSize())/(Star.MIDDLE_STAR.getSize() - Star.MIN_SIZE);
                //s.directMove(playerDeltaX * deltaSize * deltaSize * deltaSize * STAR_SPEED_DAMPENING,
                //        playerDeltaY * deltaSize * deltaSize * deltaSize * STAR_SPEED_DAMPENING);
                s.directMove(playerDeltaX * (float) Math.pow(1, deltaSize * deltaSize * deltaSize) * STAR_SPEED_DAMPENING,
                        playerDeltaX * (float) Math.pow(1, deltaSize * deltaSize * deltaSize) * STAR_SPEED_DAMPENING);
            } else if (s.getSize() > Star.MIDDLE_STAR.getSize()){
                float deltaSize = (s.getSize() - Star.MIDDLE_STAR.getSize())/(Star.MAX_SIZE - Star.MIDDLE_STAR.getSize());
                s.directMove(-playerDeltaX * deltaSize * deltaSize * deltaSize * STAR_SPEED_DAMPENING,
                        -playerDeltaY * deltaSize * deltaSize * deltaSize * STAR_SPEED_DAMPENING);
            }
            */

            s.move(STAR_AMBIENT_MOVEMENT_X * s.getSize() * s.getSize(),
                    STAR_AMBIENT_MOVEMENT_Y * s.getSize() * s.getSize());

            s.move(-playerDeltaX * STAR_SPEED_DAMPENING * s.getSize() * s.getSize() * s.getSize(),
                    -playerDeltaY * STAR_SPEED_DAMPENING * s.getSize() * s.getSize() * s.getSize());

            /*
            if(s.getSize() > Star.MIDDLE_STAR.getSize())
                s.move((float) -player.getDeltaPosition().x * STAR_SPEED_DAMPENING,
                        (float) -player.getDeltaPosition().y * STAR_SPEED_DAMPENING);
            else
                s.directMove((float) player.getDeltaPosition().x * STAR_SPEED_DAMPENING * (Star.MIDDLE_STAR.getSize()/(s.getSize()*s.getSize())),
                        (float) player.getDeltaPosition().y * STAR_SPEED_DAMPENING * (Star.MIDDLE_STAR.getSize()/(s.getSize()*s.getSize())));
            */

            //removal and replacement
            if(s.getX() < getX() - getWidth()){
                sItr.remove();
                newStars.add(new Star(getX() + getWidth() + (rand.nextInt(getWidth())+1),
                        getY() + rand.nextInt(getHeight()*3) - getHeight(), true));
            } else if (s.getX() > getX() + 2*getWidth()){
                sItr.remove();
                newStars.add(new Star(getX() - (rand.nextInt(getWidth())+1),
                        getY() + rand.nextInt(getHeight()*3) - getHeight(), true));
            } else if (s.getY() < getY() - getHeight()){
                sItr.remove();
                newStars.add(new Star(getX() + rand.nextInt(getWidth()*3) - getWidth(),
                        getY() + getHeight() + (rand.nextInt(getHeight())+1), true));
            } else if (s.getY() > getY() + 2*getHeight()){
                sItr.remove();
                newStars.add(new Star(getX() + rand.nextInt(getWidth()*3) - getWidth(),
                        getY() - (rand.nextInt(getHeight())+1), true));
            }
        }
        stars.addAll(newStars);
        newStars.clear();

        //planet updates
        earth.update(delta);

        //test collisions
        for(Ammo a : player.getProjectiles()){
            for(Enemy e : enemies)
                if (a.intersects(e.getBounds())) {
                    a.setDone();
                    e.modHealth(-a.getDamage());
                    Vector distance = new Vector();
                    for (Enemy e2 : enemies) {
                        distance.set(e2.getX() - a.getPosition().x, e2.getY() - a.getPosition().y);
                        if (e != e2 && distance.length() < a.getDamageRadius())
                            e2.modHealth(-a.getDamage());
                    }
                    distance.set(player.getX() - a.getPosition().x, player.getY() - a.getPosition().y);
                    if (distance.length() < a.getDamageRadius())
                        player.modHealth(-a.getDamage());
                }
        }

        for(Enemy e : enemies){
            for(Ammo a : e.getProjectiles()){
                if(a.intersects(player.getBounds())){
                    a.setDone();
                    player.modHealth(-a.getDamage());
                    Vector distance = new Vector();
                    for(Enemy e2 : enemies){
                        distance.set(e2.getX() - a.getPosition().x, e2.getY() - a.getPosition().y);
                        if(distance.length() < a.getDamageRadius())
                            e2.modHealth(-a.getDamage());
                    }
                } else{
                    for(Enemy e2 : enemies){
                        if(a.intersects(e2.getBounds()) && e != e2){
                            a.setDone();
                            e2.modHealth(-a.getDamage());
                            Vector distance = new Vector();
                            for(Enemy e3 : enemies){
                                distance.set(e2.getX() - a.getPosition().x, e2.getY() - a.getPosition().y);
                                if (e3 != e2 && distance.length() < a.getDamageRadius())
                                    player.modHealth(-a.getDamage());
                            }
                        }
                    }
                }
            }
        }

        //clean up & spawning
        Iterator<Enemy> eIter = enemies.iterator();
        while(eIter.hasNext()){
            Enemy enemy = eIter.next();
            if(enemy.isDead())
                eIter.remove();

        }

        Iterator<Enemy> eIter2 = enemies.iterator();
        while(eIter2.hasNext()){
            Enemy enemy = eIter2.next();
            if (enemy.getPosition().x > getX() + getWidth() + SPAWN_BUFFER + SPAWN_RANGE ||
                    enemy.getPosition().x < getX() - (SPAWN_BUFFER + SPAWN_RANGE) ||
                    enemy.getPosition().y > getY() + getHeight() + SPAWN_BUFFER + SPAWN_RANGE ||
                    enemy.getPosition().y < getY() - (SPAWN_BUFFER + SPAWN_RANGE))
                eIter2.remove();
        }

        if(enemies.size() < MAX_ENEMIES){
            int xSpot;
            int ySpot;
            if(Math.random() < .5){
                xSpot = getWidth()/2 + SPAWN_BUFFER + (int) (SPAWN_RANGE * Math.random());
                if(Math.random() < .5)
                    xSpot *= -1;
                xSpot += getX() + getWidth()/2;
                ySpot = (int) ((SPAWN_RANGE*2 + SPAWN_BUFFER*2 + getHeight()) * Math.random());
            } else {
                ySpot = getHeight()/2 + SPAWN_BUFFER + (int) (SPAWN_RANGE * Math.random());
                if(Math.random() < .5)
                    ySpot *= -1;
                ySpot += getY() + getHeight()/2;
                xSpot = (int) ((SPAWN_RANGE*2 + SPAWN_BUFFER*2 + getWidth()) * Math.random());
            }
            enemies.add(new Spaceship(xSpot, ySpot));
        }


        /*
        boolean inter = false;
        for(Missile m : player.getProjectiles()){
            if(m.intersects(earth.getBounds()))
                inter = true;
        }
        System.out.println(inter);
        */
    }

    @Override
    public void render() {
        //Game objects
        for(Star s : stars.headSet(Star.MIDDLE_STAR)){
            s.draw();
        }

        //planets
        earth.draw();

        //player
        player.draw();

        //enemies
        for(Enemy e : enemies){
            e.draw();
        }

        //stars
        for(Star s : stars.tailSet(Star.MIDDLE_STAR)){
            s.draw();
        }

        //Cursor
        cDrawTexture(getTexture("crosshair"), getMouseX(), getMouseY(), 25, 25);

        //GUI / HUD
        setFont(getFont("distantGalaxy60"));
        setColor(Color.GRAY);
        drawString("X:" + player.getX(), getX() + getWidth()/3 - getCurrentFont().getWidth("Y:" + player.getY())/2,
                getY() + (getHeight()/10)*9);
        drawString("Y:" + player.getY(), getX() + (getWidth()/3)*2 - getCurrentFont().getWidth("Y:" + player.getY())/2,
                getY() + (getHeight()/10)*9);

    }

    @Override
    public void entering(){
        Mouse.setCursorPosition(Main.getScreenWidth()/2, Main.getScreenHeight()/2);
        Mouse.setGrabbed(true);
    }

    @Override
    public void exiting(){
        Mouse.setGrabbed(false);
    }

    public static Player getPlayer(){
        return player;
    }

    private static Player player;

    private class Earth{
        private Texture earth;
        private Animation explosion;
        private int x, y, width, height;
        private float angle;
        private boolean blownUp;

        public Earth(int x, int y){
            this.x = x;
            this.y = y;
            this.width = 1000;
            this.height = 1000;
            earth = getTexture("earth");
            explosion = new Animation(getTextureSet("explosion4"), 64);
            explosion.setOneTime(true);
        }

        public void update(int delta){
            if(blownUp){
                explosion.update(delta);
            } else {
                angle += .05f;
            }
        }

        public void draw(){
            if(!explosion.isDone()) {
                if (blownUp){
                    cDrawTexture(explosion.getCurrentFrame(), x, y, (width/3f)*2, (height/3f)*2);
                } else {
                    cDrawTexture(earth, x, y, width / 2, height / 2, angle);
                }
            }
        }

        public void blowUp(){
            blownUp = true;
        }

        public Rectangle getBounds(){
            return new Rectangle(x - width/2, y - height/2, width, height);
        }

    }
}
