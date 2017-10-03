package game.enemies;

public enum EnemyType {
    SPACESHIP(100f, 17000f, 50f, .6f, .002f, 400f, 600f);

    public final float BASE_MAX_HEALTH, BASE_MAX_SPEED, BASE_ACCELERATION,
            BASE_ANGLE_SPEED, BASE_HEALTH_REGEN, BASE_DETECTION_RADIUS, BASE_FOLLOW_RADIUS;

    EnemyType(float baseMaxHealth, float baseMaxSpeed, float baseAcceleration,
              float baseAngleSpeed, float baseHealthRegen, float baseDetectionRadius, float baseFollowRadius){
        BASE_MAX_HEALTH = baseMaxHealth;
        BASE_MAX_SPEED = baseMaxSpeed;
        BASE_ACCELERATION = baseAcceleration;
        BASE_ANGLE_SPEED = baseAngleSpeed;
        BASE_HEALTH_REGEN = baseHealthRegen;
        BASE_DETECTION_RADIUS = baseDetectionRadius;
        BASE_FOLLOW_RADIUS = baseFollowRadius;
    }
}
