package game.singletons;

import game.misc.PlayerSave;
import game.util.Animation;
import game.util.ColorData;
import game.util.Vector;

import java.awt.*;
import java.util.HashMap;

import static game.ResourceManager.*;
import static game.Render.*;
import static org.lwjgl.opengl.GL11.*;

public class PlayerData {
    private Point position;
    private Animation fire;
    private HashMap<String, ColorData> colorDatas;
    private int saveSlot;
    private int stage;
    private float maxSpeed = 30000f;
    private float acceleration = 50f;
    private float maxHealth = 1000f;
    private float angleSpeed = 2.5f; //move speed //2.5f
    private float healthRegen = .002f;
    private float ammoSpeedMult = 1f;
    private float damageMult = 1f;

    private PlayerData() {
        colorDatas = new HashMap<>();
        colorDatas.put("ship_outline",  new ColorData(0, 0, 0));
        colorDatas.put("ship_hull", new ColorData(255, 255, 255));
        colorDatas.put("ship_wings", new ColorData(255, 255, 255));
        colorDatas.put("ship_window1", new ColorData(255, 255, 255));
        colorDatas.put("ship_window2", new ColorData(255, 255, 255));
        colorDatas.put("ship_window3", new ColorData(255, 255, 255));
        colorDatas.put("ship_window1_outline", new ColorData(0, 0, 0));
        colorDatas.put("ship_window2_outline", new ColorData(0, 0, 0));
        colorDatas.put("ship_window3_outline", new ColorData(0, 0, 0));
        position = new Point();
        fire = new Animation(getTextureSet("rocket_fire"), 16);
    }

    public void update(int delta){
        fire.update(delta);
    }

    public void draw(int x, int y, int width, int height){
        cDrawFlipTexture(fire.getCurrentFrame(), x, y + (height/100f)*36.3f + height / 6,
                width/5, height/6, false);
        setTextureColorReset(false);
        setColor(colorDatas.get("ship_outline"));
        cDrawTexture(getTexture("ship_outline"), x, y, width/2, height/2);
        setColor(colorDatas.get("ship_hull"));
        cDrawTexture(getTexture("ship_hull"), x, y, width/2, height/2);
        setColor(colorDatas.get("ship_wings"));
        cDrawTexture(getTexture("ship_wings"), x, y, width/2, height/2);
        setColor(colorDatas.get("ship_window1_outline"));
        cDrawTexture(getTexture("ship_window1_outline"), x, y, width/2, height/2);
        setColor(colorDatas.get("ship_window1"));
        cDrawTexture(getTexture("ship_window1"), x, y, width/2, height/2);
        setColor(colorDatas.get("ship_window2_outline"));
        cDrawTexture(getTexture("ship_window2_outline"), x, y, width/2, height/2);
        setColor(colorDatas.get("ship_window2"));
        cDrawTexture(getTexture("ship_window2"), x, y, width/2, height/2);
        setColor(colorDatas.get("ship_window3_outline"));
        cDrawTexture(getTexture("ship_window3_outline"), x, y, width/2, height/2);
        setColor(colorDatas.get("ship_window3"));
        cDrawTexture(getTexture("ship_window3"), x, y, width/2, height/2);
        setTextureColorReset(true);

    }

    public void draw(Point pos, int width, int height, float angle, float speed){
        float fireDistance = speed;
        if(fireDistance > maxSpeed)
            fireDistance = maxSpeed;
        /*
        cDrawFlipTexture(fire.getCurrentFrame(),
                pos.x - ((height/2) * (float) Math.cos(Math.toRadians(angle - 90))),
                pos.y - ((height/2) * (float) Math.sin(Math.toRadians(angle - 90))),
                width/4, height/2, false, angle + 90);
        drawFlipTexture(fire.getCurrentFrame(),
                x - width/4, y + (height/100f)*36.3f,
                width/2, height/2, false);
        */
        cDrawFlipTexture(fire.getCurrentFrame(),
                pos.x - (((height/100f)*36.3f + (height/3)*((fireDistance/maxSpeed)/2)) * (float) Math.cos(Math.toRadians(angle - 90))),
                pos.y - (((height/100f)*36.3f + (height/3)*((fireDistance/maxSpeed)/2)) * (float) Math.sin(Math.toRadians(angle - 90))),
                width/5, (height/3)*(fireDistance/maxSpeed), false, angle);
        setTextureColorReset(false);
        setColor(colorDatas.get("ship_outline"));
        cDrawTexture(getTexture("ship_outline"), pos.x, pos.y, width/2, height/2, angle);
        setColor(colorDatas.get("ship_hull"));
        cDrawTexture(getTexture("ship_hull"), pos.x, pos.y, width/2, height/2, angle);
        setColor(colorDatas.get("ship_wings"));
        cDrawTexture(getTexture("ship_wings"), pos.x, pos.y, width/2, height/2, angle);
        setColor(colorDatas.get("ship_window1_outline"));
        cDrawTexture(getTexture("ship_window1_outline"), pos.x, pos.y, width/2, height/2, angle);
        setColor(colorDatas.get("ship_window1"));
        cDrawTexture(getTexture("ship_window1"), pos.x, pos.y, width/2, height/2, angle);
        setColor(colorDatas.get("ship_window2_outline"));
        cDrawTexture(getTexture("ship_window2_outline"), pos.x, pos.y, width/2, height/2, angle);
        setColor(colorDatas.get("ship_window2"));
        cDrawTexture(getTexture("ship_window2"), pos.x, pos.y, width/2, height/2, angle);
        setColor(colorDatas.get("ship_window3_outline"));
        cDrawTexture(getTexture("ship_window3_outline"), pos.x, pos.y, width/2, height/2, angle);
        setColor(colorDatas.get("ship_window3"));
        cDrawTexture(getTexture("ship_window3"), pos.x, pos.y, width/2, height/2, angle);
        setTextureColorReset(true);
        /*
        glColor3f(1.0f, 1.0f, 1.0f);
        glEnable(GL_TEXTURE_2D);
        glPushMatrix();
        glTranslatef(pos.x, pos.y, 0);
        glRotatef(direction.getAngle() + 90, pos.x, pos.y, 0);
        fire.getCurrentFrame().bind();
        glBegin(GL_QUADS);
        glTexCoord2f(0,fire.getCurrentFrame().getHeight());
        glVertex2f(-width/2, height/2);
        glTexCoord2f(fire.getCurrentFrame().getWidth(), fire.getCurrentFrame().getHeight());
        glVertex2f(width/2, height/2);
        glTexCoord2f(fire.getCurrentFrame().getWidth(), 0);
        glVertex2f(width/2, height);
        glTexCoord2f(0, 0);
        glVertex2f(-width/2, height);
        glEnd();
        glPopMatrix();
        glDisable(GL_TEXTURE_2D);
        */
    }

    public void drawTransparent(int x, int y, int width, int height, String exclude, int alpha){
        setTextureColorReset(false);
        setColor(255, 255, 255, alpha);
        cDrawFlipTexture(fire.getCurrentFrame(),
                x,
                y + (height/100f)*36.3f + height / 6,
                width/5, height/6, false);
        setColor(colorDatas.get("ship_outline").alpha(alpha));
        cDrawTexture(getTexture("ship_outline"), x, y, width/2, height/2);
        setColor(colorDatas.get("ship_hull").alpha(alpha));
        cDrawTexture(getTexture("ship_hull"), x, y, width/2, height/2);
        setColor(colorDatas.get("ship_wings").alpha(alpha));
        cDrawTexture(getTexture("ship_wings"), x, y, width/2, height/2);
        setColor(colorDatas.get("ship_window1_outline").alpha(alpha));
        cDrawTexture(getTexture("ship_window1_outline"), x, y, width/2, height/2);
        setColor(colorDatas.get("ship_window1").alpha(alpha));
        cDrawTexture(getTexture("ship_window1"), x, y, width/2, height/2);
        setColor(colorDatas.get("ship_window2_outline").alpha(alpha));
        cDrawTexture(getTexture("ship_window2_outline"), x, y, width/2, height/2);
        setColor(colorDatas.get("ship_window2").alpha(alpha));
        cDrawTexture(getTexture("ship_window2"), x, y, width/2, height/2);
        setColor(colorDatas.get("ship_window3_outline").alpha(alpha));
        cDrawTexture(getTexture("ship_window3_outline"), x, y, width/2, height/2);
        setColor(colorDatas.get("ship_window3").alpha(alpha));
        cDrawTexture(getTexture("ship_window3"), x, y, width/2, height/2);
        setColor(colorDatas.get(exclude));
        cDrawTexture(getTexture(exclude), x, y, width/2, height/2);
        setTextureColorReset(true);
    }

    public void setPosition(int x, int y){
        position.setLocation(x, y);
    }

    public void setPosition(Point p){
        position.setLocation(p);
    }

    public void setSaveSlot(int saveSlot){
        this.saveSlot = saveSlot;
    }

    public void setMaxSpeed(float maxSpeed){
        this.maxSpeed = maxSpeed;
    }

    public void setAcceleration(float acceleration){
        this.acceleration = acceleration;
    }

    public float getAmmoSpeedMult() {
        return ammoSpeedMult;
    }

    public float getDamageMult() {
        return damageMult;
    }

    public void modMaxSpeed(float delta){
        this.maxSpeed += delta;
    }

    public void modAcceleration(float delta){
        this.acceleration += delta;
    }

    public Point getPosition(){
        return position;
    }

    public int getX(){
        return position.x;
    }

    public int getY(){
        return position.y;
    }

    public ColorData getColorData(String key){
        return colorDatas.get(key);
    }

    public HashMap<String, ColorData> getColorDatas(){
        return colorDatas;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public int getOriginalWidth(){
        return getTexture("ship_outline").getTextureWidth();
    }

    public int getOriginalHeight(){
        return getTexture("ship_outline").getTextureHeight();
    }

    public float getAngleSpeed(){
        return angleSpeed;
    }

    public float getMaxHealth(){
        return maxHealth;
    }

    public float getHealthRegen(){
        return healthRegen;
    }

    public void setAmmoSpeedMult(float ammoSpeedMult) {
        this.ammoSpeedMult = ammoSpeedMult;
    }

    public void setDamageMult(float damageMult) {
        this.damageMult = damageMult;
    }

    public void loadPlayerData(PlayerSave playerSave){
        this.colorDatas = playerSave.getColorDatas();
    }

    public PlayerSave getPlayerSave(){
        return new PlayerSave.PlayerSaveBuilder()
                .setColorDatas(colorDatas)
                .build();
    }

    public int getSaveSlot(){
        return saveSlot;
    }

    public void loadDefaults(){
        colorDatas.clear();
        colorDatas.put("ship_outline",  new ColorData(0, 0, 0));
        colorDatas.put("ship_hull", new ColorData(255, 255, 255));
        colorDatas.put("ship_wings", new ColorData(255, 255, 255));
        colorDatas.put("ship_window1", new ColorData(255, 255, 255));
        colorDatas.put("ship_window2", new ColorData(255, 255, 255));
        colorDatas.put("ship_window3", new ColorData(255, 255, 255));
        colorDatas.put("ship_window1_outline", new ColorData(0, 0, 0));
        colorDatas.put("ship_window2_outline", new ColorData(0, 0, 0));
        colorDatas.put("ship_window3_outline", new ColorData(0, 0, 0));
        position = new Point();
        maxSpeed = 30000f;
        acceleration = 50f;
        //direction = new Vector();
    }

    public static PlayerData getInstance() {
        if(instance == null){
            instance = new PlayerData();
        }
        return instance;
    }

    private static PlayerData instance;
}
