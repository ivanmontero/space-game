package game.scenes;

import game.*;
import game.misc.Star;
import game.singletons.PlayerData;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import game.misc.Button;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import static game.ResourceManager.*;
import static game.Render.*;

public class Menu extends Scene {
    private Random rand = new Random();
    private HashSet<Star> stars, newStars;
    private HashSet<Button> mainButtons, settingButtons, newGameButtons;
    private int titleTrans, mainButtonTrans;
    private MenuState screen;
    private final int  MAIN_BUTTON_WIDTH = 350, SETTING_BUTTON_WIDTH = 400, BUTTON_HEIGHT = 50, SAVE_BUTTON_WIDTH = 900,
            SAVE_BUTTON_HEIGHT = 300;
    public static final float STAR_SPEED = 1.5f; //1.5

    public enum MenuState{
        MAIN_MENU, NEW_GAME, LOAD_SAVES, SETTINGS
    }

    public static final int MAX_STARS = 500; //TODO replace w/ enum

    @Override
    public void initResources(){
        addDerivedFont("distantGalaxy", 150, "distantGalaxy150");
        addDerivedFont("distantGalaxy", 30, "distantGalaxy30");
        addDerivedFont("distantGalaxy", 60, "distantGalaxy60");
    }

    @Override
    public void init() {
        stars = new HashSet<>();
        newStars = new HashSet<>();
        mainButtons = new HashSet<>();
        newGameButtons = new HashSet<>();
        settingButtons = new HashSet<>();
        for(int i = 0; i < MAX_STARS; i++){
            stars.add(new Star());
        }
        // Main menu buttons
        mainButtons.add(new MenuButton(getX() + getWidth()/2 - MAIN_BUTTON_WIDTH /2,
                getY() + (getHeight()/10)*4 - BUTTON_HEIGHT/2, MAIN_BUTTON_WIDTH, BUTTON_HEIGHT, "New Game"));
        mainButtons.add(new MenuButton(getX() + getWidth()/2 - MAIN_BUTTON_WIDTH /2,
                getY() + (getHeight()/10)*5 - BUTTON_HEIGHT/2, MAIN_BUTTON_WIDTH, BUTTON_HEIGHT, "Load Save"));
        mainButtons.add(new MenuButton(getX() + getWidth()/2 - MAIN_BUTTON_WIDTH /2,
                getY() + (getHeight()/10)*6 - BUTTON_HEIGHT/2, MAIN_BUTTON_WIDTH, BUTTON_HEIGHT, "Settings"));
        mainButtons.add(new MenuButton(getX() + getWidth()/2 - MAIN_BUTTON_WIDTH /2,
                getY() + (getHeight()/10)*7 - BUTTON_HEIGHT/2, MAIN_BUTTON_WIDTH, BUTTON_HEIGHT, "Quit"));
        // Settings screen buttons
        settingButtons.add(new MenuButton(getX() + getWidth()/2 - SETTING_BUTTON_WIDTH /2,
                getY() + (getHeight()/10)*4 - BUTTON_HEIGHT/2, SETTING_BUTTON_WIDTH, BUTTON_HEIGHT, "Fullscreen"){
            @Override
            public void update(int delta){
                isHighlighted = bounds.contains(getMousePoint());
                if(Main.isFullscreen()){
                    text = "Windowed";
                } else {
                    text = "Fullscreen";
                }
            }

            @Override
            public void clicked(){
                Main.setFullscreen(!Main.isFullscreen());
            }
        });
        settingButtons.add(new MenuButton(getX() + getWidth()/2 - SETTING_BUTTON_WIDTH /2,
                getY() + (getHeight()/10)*5 - BUTTON_HEIGHT/2, SETTING_BUTTON_WIDTH, BUTTON_HEIGHT, "Antialiasing"){
            @Override
            public void update(int delta){
                isHighlighted = bounds.contains(getMousePoint());
                text = "Antialiasing = " + SettingManager.getBoolean("antialiasing");
            }

            @Override
            public void clicked(){
                SettingManager.set("antialiasing", "" + !SettingManager.getBoolean("antialiasing"));
            }
        });
        settingButtons.add(new MenuButton(getX() + getWidth()/2 - SETTING_BUTTON_WIDTH /2,
                getY() + (getHeight()/10)*6 - BUTTON_HEIGHT/2, SETTING_BUTTON_WIDTH, BUTTON_HEIGHT, "AA Samples"){
            @Override
            public void update(int delta){
                isHighlighted = bounds.contains(getMousePoint());
                text = "AA Samples = " + SettingManager.getInt("antialiasingSamples");
            }

            @Override
            public void clicked(){
                SettingManager.set("antialiasingSamples", "" + (SettingManager.getInt("antialiasingSamples") + 2) % 10);
            }
        });
        settingButtons.add(new MenuButton(getX(), getY(), SETTING_BUTTON_WIDTH/2, BUTTON_HEIGHT, "<- Back"){
            @Override
            public void clicked(){
                screen = MenuState.MAIN_MENU;
            }
        });
        // New game buttons
        newGameButtons.add(new SaveButton(getX() + getWidth()/2 - SAVE_BUTTON_WIDTH /2,
                getY() + (getHeight()/10)*2 - SAVE_BUTTON_HEIGHT/2, SAVE_BUTTON_WIDTH, SAVE_BUTTON_HEIGHT, 1));
        newGameButtons.add(new SaveButton(getX() + getWidth()/2 - SAVE_BUTTON_WIDTH /2,
                getY() + (getHeight()/10)*5 - SAVE_BUTTON_HEIGHT/2, SAVE_BUTTON_WIDTH, SAVE_BUTTON_HEIGHT, 2));
        newGameButtons.add(new SaveButton(getX() + getWidth()/2 - SAVE_BUTTON_WIDTH /2,
                getY() + (getHeight()/10)*8 - SAVE_BUTTON_HEIGHT/2, SAVE_BUTTON_WIDTH, SAVE_BUTTON_HEIGHT, 3));
        // Load game buttons TODO


        newGameButtons.add(new MenuButton(getX(), getY(), SETTING_BUTTON_WIDTH/2, BUTTON_HEIGHT, "<- Back"){
            @Override
            public void clicked(){
                screen = MenuState.MAIN_MENU;
            }
        });

        setCircleSegments(30);
        screen = MenuState.MAIN_MENU;
    }

    @Override
    public void update(int delta) {
        Iterator<Star> sItr = stars.iterator();
        while(sItr.hasNext()){
            Star s = sItr.next();
            //movement
            if(Keyboard.isKeyDown(Keyboard.KEY_A))
                s.move(-STAR_SPEED * .5f * s.getSize() * s.getSize(), 0);
            if(Keyboard.isKeyDown(Keyboard.KEY_D))
                s.move(STAR_SPEED * .5f * s.getSize() * s.getSize(), 0);
            if(Keyboard.isKeyDown(Keyboard.KEY_W))
                s.move(0, -STAR_SPEED * .5f * s.getSize() * s.getSize());
            if(Keyboard.isKeyDown(Keyboard.KEY_S))
                s.move(0, STAR_SPEED * .5f * s.getSize() * s.getSize());
            s.move(-STAR_SPEED * s.getSize() * s.getSize(), STAR_SPEED/3 * s.getSize() * s.getSize());

            //removal and replacement
            if(s.getX() < getX() - 10){
                sItr.remove();
                newStars.add(new Star(getX() + getWidth() + 10, getY() + rand.nextInt(getHeight() + 20) - 10));
            } else if (s.getX() > getX() + getWidth() + 10){
                sItr.remove();
                newStars.add(new Star(getX() - 10, getY() + rand.nextInt(getHeight() + 20) - 10));
            } else if (s.getY() < getY() - 10){
                sItr.remove();
                newStars.add(new Star(getX() + rand.nextInt(getWidth() + 20) - 10, getY() + getHeight() + 10));
            } else if (s.getY() > getY() + getHeight() + 10){
                sItr.remove();
                newStars.add(new Star(getX() + rand.nextInt(getWidth() + 20) - 10, getY() - 10));
            }
        }
        stars.addAll(newStars);
        newStars.clear();

        if(screen == MenuState.MAIN_MENU) {
            if (titleTrans < 255)
                titleTrans++;
            if (titleTrans > 170 && mainButtonTrans < 255)
                mainButtonTrans++;
            for (Button b : mainButtons) {
                b.update(delta);
            }
        } else if (screen == MenuState.NEW_GAME){
            for(Button b : newGameButtons){
                b.update(delta);
            }
        } else if (screen == MenuState.SETTINGS) {
            for (Button b : settingButtons) {
                b.update(delta);
            }
        }

        while(Mouse.next()){
            if(Mouse.getEventButtonState()){
                if(Mouse.getEventButton() == 0){
                    switch (screen) {
                        case MAIN_MENU:
                            for(Button b : mainButtons) {
                                if (b.getBounds().contains(getMousePoint())) {
                                    b.clicked();
                                }
                            }
                            break;
                        case NEW_GAME:
                            for(Button b : newGameButtons){
                                if (b.getBounds().contains(getMousePoint())) {
                                    b.clicked();
                                }
                            }
                            break;
                        case SETTINGS:
                            for(Button b : settingButtons){
                                if (b.getBounds().contains(getMousePoint())) {
                                    b.clicked();
                                }
                            }
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void render() {
        for(Star s : stars){
            s.draw(titleTrans);
        }
        if(screen == MenuState.MAIN_MENU) {
            setColor(255, 255, 255, titleTrans);
            setFont(getFont("distantGalaxy150"));
            drawString("Disturbed silence", getWidth() / 2 - getCurrentFont().getWidth("Disturbed Silence") / 2,
                    (getHeight() / 10) * 2 - getCurrentFont().getHeight("Disturbed Silence") / 2);
            for (Button b : mainButtons) {
                b.draw(mainButtonTrans);
            }
        } else if (screen == MenuState.NEW_GAME){
            for(Button b : newGameButtons) {
                b.draw();
            }
        } else if (screen == MenuState.SETTINGS){
            for (Button b : settingButtons) {
                b.draw();
            }
            setColor(255, 255, 255);
            setFont(getFont("distantGalaxy30"));
            drawString("Some settings require a restart to take effect",
                    getWidth() - getCurrentFont().getWidth("Some settings require a restart to take effect"),
                    getHeight() - getCurrentFont().getHeight("Some settings require a restart to take effect"));
        }
    }

    @Override
    public void entering(){
        titleTrans = 128;
        mainButtonTrans = 0;
        screen = MenuState.MAIN_MENU;
    }

    private class MenuButton extends Button{
        public MenuButton(int x, int y, int width, int height){
            super(x, y, width, height, getFont("distantGalaxy30"), "");
        }

        public MenuButton(int x, int y, int width, int height, String text){
            super(x, y, width, height, getFont("distantGalaxy30"), text);
        }

        @Override
        public void clicked(){
            switch(text){
                case "New Game":
                    SaveManager.loadGameSaves();
                    screen = MenuState.NEW_GAME;
                    break;
                case "Load Game":
                    //TODO
                    break;
                case "Settings":
                    screen = MenuState.SETTINGS;
                    break;
                case "Quit":
                    Main.exit();
                    break;
            }
        }
    }

    private class SaveButton extends Button{
        private boolean saveExists, selected;
        private int saveSlot;

        public SaveButton(int x, int y, int width, int height, int saveSlot){
            super(x, y, width, height, getFont("distantGalaxy60"), "Slot");
            this.saveSlot = saveSlot;
        }

        @Override
        public void update(int delta){
            super.update(delta);
            if(SaveManager.saveGameExists(saveSlot)){
                saveExists = true;
                if(selected)
                    text = "[Click to overwrite]";
                else
                    text = "[Existing save]";
            } else {
                saveExists = false;
                text = "[Empty slot]";
            }
        }

        @Override
        public void draw(){
            if(isHighlighted){
                setColor(255, 255, 255);
                fillRect(x, y, width, height);
                setColor(0, 0, 0);
                drawRect(x, y, width, height);
                if(saveExists)
                    setColor(80, 80, 80);
                if(selected)
                    setColor(60, 0, 0);
                setFont(font);
                drawString(text, x + width/2 - getCurrentFont().getWidth(text)/2,
                        y + height/2 - (getCurrentFont().getAscent() + getCurrentFont().getDescent())/2);
            } else {
                setColor(0, 0, 0);
                fillRect(x, y, width, height);
                setColor(255, 255, 255);
                drawRect(x, y, width, height);
                if(saveExists)
                    setColor(130, 130, 130);
                if(selected)
                    setColor(170, 0, 0);
                setFont(font);
                drawString(text, x + width/2 - getCurrentFont().getWidth(text)/2,
                        y + height/2 - (getCurrentFont().getAscent() + getCurrentFont().getDescent())/2);
            }
        }

        @Override
        public void clicked(){
            if(saveExists){
                if(selected) {
                    PlayerData.getInstance().loadPlayerData(SaveManager.getGameSave(saveSlot));
                    PlayerData.getInstance().setSaveSlot(saveSlot);
                    selected = false;
                    Main.enterScene(Scenes.PLAYER_CREATION);
                } else {
                    selected = true;
                }
            } else {
                PlayerData.getInstance().loadDefaults();
                PlayerData.getInstance().setSaveSlot(saveSlot);
                Main.enterScene(Scenes.PLAYER_CREATION);
            }
        }
    }
}
