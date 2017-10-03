package game.ammo;

import game.enemies.Enemy;
import game.misc.Player;
import game.util.ColorData;
import game.Constants;
import game.util.Vector;

import java.awt.*;

import static game.Render.*;

public class Bullet extends Ammo {
    private ColorData color;
    private int lifetime;

    public static final int HALF_WIDTH = 2, HALF_HEIGHT = 3;

    public Bullet(int x, int y, float angle, ColorData color, float initialSpeed) {
        super(AmmoType.BULLET, x, y, HALF_WIDTH, HALF_HEIGHT, angle, initialSpeed);
        this.color = color;
    }

    public Bullet(Enemy e, ColorData color){
        super(AmmoType.BULLET, e.getX(), e.getY(), HALF_WIDTH, HALF_HEIGHT, e.getDirection().getAngle(), e.getSpeed());
        this.color = color;
    }

    public Bullet(Player player){
        super(AmmoType.BULLET, player.getX(), player.getY(), HALF_WIDTH, HALF_HEIGHT, player.getDirection().getAngle(),
                player.getSpeed());
        this.color = player.getPlayerData().getColorData("ship_hull");
    }

    @Override
    public void update(int delta) {
        lifetime += delta;
        if(lifetime > TYPE.BASE_LIFETIME)
            isDone = true;
        if(!isDone) {
            Point initPos = new Point((int) position.getX(), (int) position.getY());
            position.setLocation(Vector.toPoint(direction.normalize()
                    .mul(speed * Constants.SPEED_MULTIPLIER * delta).add(position)));
            deltaPosition.setLine(initPos, position);
        }
        size.setLocation(position.x - HALF_WIDTH /2, position.y - HALF_HEIGHT /2);
    }

    @Override
    public void draw() {
        if (!isDone) {
            setColor(color);
            cFillRect(position.x, position.y, HALF_WIDTH, HALF_HEIGHT, direction.getAngle() + 90);
        }
    }

}
