package game.scenes;

import game.Main;
import game.SaveManager;
import game.Scenes;
import game.misc.Button;
import game.singletons.PlayerData;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.HashSet;

import static game.Render.*;
import static game.ResourceManager.*;

public class PauseMenu extends Scene {
    private HashSet<Button> buttons;

    private final int BUTTON_WIDTH = 350, BUTTON_HEIGHT = 50;

    @Override
    public void initResources(){
        addDerivedFont("distantGalaxy", 300, "distantGalaxy300" );
    }

    @Override
    public void init() {
        buttons = new HashSet<>();
        //TODO: add buttons
        buttons.add(new PauseButton(getX() + getWidth()/2 - BUTTON_WIDTH/2,
                getY() + (getHeight()/20)*7 - BUTTON_HEIGHT/2, BUTTON_WIDTH, BUTTON_HEIGHT, "Resume"){
            @Override
            public void updatePosition(){
                x = getX() + getWidth()/2 - width/2;
                y = getY() + (getHeight()/20)*7 - height/2;
            }
        });
        buttons.add(new PauseButton(getX() + getWidth()/2 - BUTTON_WIDTH/2,
                getY() + (getHeight()/20)*9 - BUTTON_HEIGHT/2, BUTTON_WIDTH, BUTTON_HEIGHT, "Save"){
            @Override
            public void updatePosition(){
                x = getX() + getWidth()/2 - width/2;
                y = getY() + (getHeight()/20)*9 - height/2;
            }
        });
        buttons.add(new PauseButton(getX() + getWidth()/2 - BUTTON_WIDTH/2,
                getY() + (getHeight()/20)*11 - BUTTON_HEIGHT/2, BUTTON_WIDTH, BUTTON_HEIGHT, "Load"){
            @Override
            public void updatePosition(){
                x = getX() + getWidth()/2 - width/2;
                y = getY() + (getHeight()/20)*11 - height/2;
            }
        });
        buttons.add(new PauseButton(getX() + getWidth()/2 - BUTTON_WIDTH/2,
                getY() + (getHeight()/20)*13 - BUTTON_HEIGHT/2, BUTTON_WIDTH, BUTTON_HEIGHT, "Options"){
            @Override
            public void updatePosition(){
                x = getX() + getWidth()/2 - width/2;
                y = getY() + (getHeight()/20)*13 - height/2;
            }
        });
        buttons.add(new PauseButton(getX() + getWidth()/2 - BUTTON_WIDTH/2,
                getY() + (getHeight()/20)*15 - BUTTON_HEIGHT/2, BUTTON_WIDTH, BUTTON_HEIGHT, "Main Menu"){
            @Override
            public void updatePosition(){
                x = getX() + getWidth()/2 - width/2;
                y = getY() + (getHeight()/20)*15 - height/2;
            }
        });
        buttons.add(new PauseButton(getX() + getWidth()/2 - BUTTON_WIDTH/2,
                getY() + (getHeight()/20)*17 - BUTTON_HEIGHT/2, BUTTON_WIDTH, BUTTON_HEIGHT, "Quit"){
            @Override
            public void updatePosition(){
                x = getX() + getWidth()/2 - width/2;
                y = getY() + (getHeight()/20)*17 - height/2;
            }
        });

    }

    @Override
    public void update(int delta) {
        for(Button b : buttons){
            b.update(delta);
        }
        while(Mouse.next()){
            if(Mouse.getEventButtonState()){
                if(Mouse.getEventButton() == 0){
                    for(Button b : buttons){
                        if(b.getBounds().contains(getMousePoint())){
                            b.clicked();
                        }
                    }
                }
            }
        }
        while(Keyboard.next()){
            if(Keyboard.getEventKeyState()){
                switch (Keyboard.getEventKey()){
                    case Keyboard.KEY_ESCAPE:
                        Main.enterPreviousScene();
                        break;
                }
            }
        }
    }

    @Override
    public void render() {
        Main.getPreviousScene().render();
        setColor(45, 45, 45, 140);
        fillRect(getX(), getY(), getWidth(), getHeight());
        setFont(getFont("distantGalaxy300"));
        setColor(255, 255, 255);
        drawString("Paused", getX() + getWidth()/2 - getCurrentFont().getWidth("Paused")/2,
                getY() + (getHeight() / 20) * 3 - getCurrentFont().getHeight("Paused")/2);
        for(Button b : buttons){
            b.draw();
        }
    }

    public class PauseButton extends Button {

        public PauseButton(int x, int y, int width, int height, String text) {
            super(x, y, width, height, getFont("distantGalaxy30"), text);
        }

        @Override
        public void clicked(){
            switch(text){
                case "Resume":
                    Main.enterPreviousScene();
                    break;
                case "Save":
                    SaveManager.saveGame(PlayerData.getInstance().getSaveSlot(),
                            PlayerData.getInstance().getPlayerSave());
                    break;
                case "Options":
                    //todo
                    break;
                case "Load":
                    //TODO
                    break;
                case "Main Menu":
                    Main.enterScene(Scenes.MENU);
                    break;
                case "Quit":
                    Main.exit();
                    break;
            }
        }

        @Override
        public void update(int delta){
            super.update(delta);
            updatePosition();
        }

        public void updatePosition(){}
    }
}
