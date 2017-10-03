package game.ammo;

public enum AmmoType {
    MISSILE(1000, 3200, 100000, 128, 40f), //1000 //30000
    BULLET(16, 1600, 150000, 5, .75f);

    public final int BASE_DELAY;
    public final int BASE_LIFETIME;
    public final int BASE_SPEED;
    public final int BASE_DAMAGE_RADIUS;
    public final float BASE_DAMAGE;

    AmmoType(int delay, int lifetime, int baseSpeed, int baseDamageRadius, float baseDamage){
        this.BASE_DELAY = delay;
        this.BASE_LIFETIME = lifetime;
        this.BASE_SPEED = baseSpeed;
        this.BASE_DAMAGE_RADIUS = baseDamageRadius;
        this.BASE_DAMAGE = baseDamage;
    }

}
