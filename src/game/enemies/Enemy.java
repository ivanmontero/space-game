package game.enemies;

import game.ammo.Ammo;
import game.ammo.AmmoType;
import game.util.Animation;
import game.util.Entity;
import game.util.Vector;
import game.util.Vulnerable;

import java.util.HashSet;


abstract public class Enemy extends Entity implements Vulnerable {
    protected HashSet<Ammo> projectiles;
    protected float maxHealth;
    protected float acceleration;
    protected float maxSpeed;
    protected float angleSpeed;
    protected float healthRegen;
    protected float detectionRadius;
    protected float followRadius;
    protected float fireDelay;
    public final EnemyType enemyType;

    public Enemy(EnemyType e, int x, int y, int width, int height) {
        super(x, y, width, height);
        projectiles = new HashSet<Ammo>();
        enemyType = e;
        maxHealth = e.BASE_MAX_HEALTH;
        health = e.BASE_MAX_HEALTH;
        acceleration = e.BASE_ACCELERATION;
        maxSpeed = e.BASE_MAX_SPEED;
        angleSpeed = e.BASE_ANGLE_SPEED;
        healthRegen = e.BASE_HEALTH_REGEN;
        detectionRadius = e.BASE_DETECTION_RADIUS;
        followRadius = e.BASE_FOLLOW_RADIUS;
        fireDelay = 1000000000;
    }

    abstract public void fire();

    abstract public float getSpeed();

    public HashSet<Ammo> getProjectiles(){
        return projectiles;
    }

}
