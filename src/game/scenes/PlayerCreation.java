package game.scenes;

import game.Main;
import game.SaveManager;
import game.Scenes;
import game.misc.Button;
import game.misc.Star;
import game.singletons.PlayerData;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import static game.ResourceManager.*;
import static game.Render.*;
import static org.lwjgl.opengl.GL11.*;

// color triangle?

public class PlayerCreation extends Scene {
    private HashSet<Star> stars, newStars;
    private HashSet<Button> buttons;
    private ColorSelection redColor, greenColor, blueColor;
    private Random rand;
    private int starTrans;
    private PlayerData playerData;
    private boolean transparency;
    private String selection;

    private final int SELECTION_BUTTON_WIDTH = 350, SELECTION_BUTTON_HEIGHT = 75;

    @Override
    public void initResources(){
        addDerivedFont("distantGalaxy", 50, "distantGalaxy50");
    }

    @Override
    public void init() {
        stars = new HashSet<>();
        newStars = new HashSet<>();
        buttons = new HashSet<>();
        rand = new Random();
        playerData = PlayerData.getInstance();

        buttons.add(new Button(getX(), getY(), 250, 50, getFont("distantGalaxy30"), "<- Back"){
            @Override
            public void clicked(){
                Main.enterScene(Scenes.MENU);
            }
        });
        buttons.add(new Button(getX() + getWidth()/6 - 175, getY() + getHeight() - 50, 350, 50,
                getFont("distantGalaxy30"), "Transparency"){
            @Override
            public void update(int delta){
                isHighlighted = bounds.contains(getMousePoint());
                if(transparency)
                    text = "Transparency off";
                else
                    text = "Transparency on";
            }

            @Override
            public void clicked(){
                transparency = !transparency;
            }
        });
        buttons.add(new Button(getX() + (getWidth()/40)*17 - SELECTION_BUTTON_WIDTH/2,
                getY(), SELECTION_BUTTON_WIDTH, SELECTION_BUTTON_HEIGHT,
                getFont("distantGalaxy30"), "Randomize"){
            @Override
            public void draw(){
                if(isHighlighted){
                    setColor(0, 0, 128);
                    fillRect(x, y, width, height);
                    setColor(255, 255, 255);
                    drawRect(x, y, width, height);
                    setFont(font);
                    drawString(text, x + width/2 - getCurrentFont().getWidth(text)/2,
                            y + height/2 - (getCurrentFont().getAscent() + getCurrentFont().getDescent())/2);
                } else {
                    setColor(0, 0, 255);
                    fillRect(x, y, width, height);
                    setColor(255, 255, 255);
                    drawRect(x, y, width, height);
                    setFont(font);
                    drawString(text, x + width/2 - getCurrentFont().getWidth(text)/2,
                            y + height/2 - (getCurrentFont().getAscent() + getCurrentFont().getDescent())/2);
                }
            }

            @Override
            public void clicked(){
                for(String key : playerData.getColorDatas().keySet()){
                    playerData.getColorData(key).set(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
                }
            }
        });
        buttons.add(new Button(getX() + (getWidth()/40)*17 - SELECTION_BUTTON_WIDTH/2,
                getY() + getHeight() - SELECTION_BUTTON_HEIGHT, SELECTION_BUTTON_WIDTH, SELECTION_BUTTON_HEIGHT,
                getFont("distantGalaxy30"), "Reset"){
            @Override
            public void draw(){
                if(isHighlighted){
                    setColor(128, 0, 0);
                    fillRect(x, y, width, height);
                    setColor(255, 255, 255);
                    drawRect(x, y, width, height);
                    setFont(font);
                    drawString(text, x + width/2 - getCurrentFont().getWidth(text)/2,
                            y + height/2 - (getCurrentFont().getAscent() + getCurrentFont().getDescent())/2);
                } else {
                    setColor(255, 0, 0);
                    fillRect(x, y, width, height);
                    setColor(255, 255, 255);
                    drawRect(x, y, width, height);
                    setFont(font);
                    drawString(text, x + width/2 - getCurrentFont().getWidth(text)/2,
                            y + height/2 - (getCurrentFont().getAscent() + getCurrentFont().getDescent())/2);
                }
            }

            @Override
            public void clicked(){
                playerData.loadDefaults();
            }
        });
        buttons.add(new Button(getX() + getWidth() - SELECTION_BUTTON_WIDTH,
                getY() + getHeight()/2 - SELECTION_BUTTON_HEIGHT/2, SELECTION_BUTTON_WIDTH, SELECTION_BUTTON_HEIGHT,
                getFont("distantGalaxy30"), "Start"){
            @Override
            public void draw(){
                if(isHighlighted){
                    setColor(0, 128, 0);
                    fillRect(x, y, width, height);
                    setColor(255, 255, 255);
                    drawRect(x, y, width, height);
                    setFont(font);
                    drawString(text, x + width/2 - getCurrentFont().getWidth(text)/2,
                            y + height/2 - (getCurrentFont().getAscent() + getCurrentFont().getDescent())/2);
                } else {
                    setColor(0, 255, 0);
                    fillRect(x, y, width, height);
                    setColor(255, 255, 255);
                    drawRect(x, y, width, height);
                    setFont(font);
                    drawString(text, x + width/2 - getCurrentFont().getWidth(text)/2,
                            y + height/2 - (getCurrentFont().getAscent() + getCurrentFont().getDescent())/2);
                }
            }

            @Override
            public void clicked(){
                SaveManager.saveGame(playerData.getSaveSlot(), playerData.getPlayerSave());
                Main.enterScene(Scenes.OUTER_SPACE);
            }
        });
        buttons.add(new SelectionButton(getX() + (getWidth()/40)*17 - SELECTION_BUTTON_WIDTH/2,
                getY() + (getHeight()/12)*2 - SELECTION_BUTTON_HEIGHT/2, SELECTION_BUTTON_WIDTH, SELECTION_BUTTON_HEIGHT,
                "Outline", "ship_outline"));
        buttons.add(new SelectionButton(getX() + (getWidth()/40)*17 - SELECTION_BUTTON_WIDTH/2,
                getY() + (getHeight()/12)*3 - SELECTION_BUTTON_HEIGHT/2, SELECTION_BUTTON_WIDTH, SELECTION_BUTTON_HEIGHT,
                "Hull", "ship_hull"));
        buttons.add(new SelectionButton(getX() + (getWidth()/40)*17 - SELECTION_BUTTON_WIDTH/2,
                getY() + (getHeight()/12)*4 - SELECTION_BUTTON_HEIGHT/2, SELECTION_BUTTON_WIDTH, SELECTION_BUTTON_HEIGHT,
                "Wings", "ship_wings"));
        buttons.add(new SelectionButton(getX() + (getWidth()/40)*17 - SELECTION_BUTTON_WIDTH/2,
                getY() + (getHeight()/12)*5 - SELECTION_BUTTON_HEIGHT/2, SELECTION_BUTTON_WIDTH, SELECTION_BUTTON_HEIGHT,
                "Window 1", "ship_window1"));
        buttons.add(new SelectionButton(getX() + (getWidth()/40)*17 - SELECTION_BUTTON_WIDTH/2,
                getY() + (getHeight()/12)*6 - SELECTION_BUTTON_HEIGHT/2, SELECTION_BUTTON_WIDTH, SELECTION_BUTTON_HEIGHT,
                "Window 2", "ship_window2"));
        buttons.add(new SelectionButton(getX() + (getWidth()/40)*17 - SELECTION_BUTTON_WIDTH/2,
                getY() + (getHeight()/12)*7 - SELECTION_BUTTON_HEIGHT/2, SELECTION_BUTTON_WIDTH, SELECTION_BUTTON_HEIGHT,
                "Window 3", "ship_window3"));
        buttons.add(new SelectionButton(getX() + (getWidth()/40)*17 - SELECTION_BUTTON_WIDTH/2,
                getY() + (getHeight()/12)*8 - SELECTION_BUTTON_HEIGHT/2, SELECTION_BUTTON_WIDTH, SELECTION_BUTTON_HEIGHT,
                "window 1 outline", "ship_window1_outline"));
        buttons.add(new SelectionButton(getX() + (getWidth()/40)*17 - SELECTION_BUTTON_WIDTH/2,
                getY() + (getHeight()/12)*9 - SELECTION_BUTTON_HEIGHT/2, SELECTION_BUTTON_WIDTH, SELECTION_BUTTON_HEIGHT,
                "Window 2 outline", "ship_window2_outline"));
        buttons.add(new SelectionButton(getX() + (getWidth()/40)*17 - SELECTION_BUTTON_WIDTH/2,
                getY() + (getHeight()/12)*10 - SELECTION_BUTTON_HEIGHT/2, SELECTION_BUTTON_WIDTH, SELECTION_BUTTON_HEIGHT,
                "Window 3 outline", "ship_window3_outline"));

        redColor = new ColorSelection((getWidth()/10)*6, getHeight()/2 - (256*4)/2, 50, 256*4, "red");
        greenColor = new ColorSelection((getWidth()/10)*7, getHeight()/2 - (256*4)/2, 50, 256*4, "green");
        blueColor = new ColorSelection((getWidth()/10)*8, getHeight()/2 - (256*4)/2, 50, 256*4, "blue");

        for(int i = 0; i < Menu.MAX_STARS; i++){
            stars.add(new Star());
        }
        transparency = true;
        selection = "ship_hull";
    }

    @Override
    public void update(int delta) {
        playerData.update(delta);
        redColor.setValue(playerData.getColorData(selection).getRed());
        greenColor.setValue(playerData.getColorData(selection).getGreen());
        blueColor.setValue(playerData.getColorData(selection).getBlue());
        Iterator<Star> sItr = stars.iterator();
        while(sItr.hasNext()){
            Star s = sItr.next();

            if(Keyboard.isKeyDown(Keyboard.KEY_A))
                s.move(-Menu.STAR_SPEED * .5f * s.getSize() * s.getSize(), 0);
            if(Keyboard.isKeyDown(Keyboard.KEY_D))
                s.move(Menu.STAR_SPEED * .5f * s.getSize() * s.getSize(), 0);
            if(Keyboard.isKeyDown(Keyboard.KEY_W))
                s.move(0, -Menu.STAR_SPEED * .5f * s.getSize() * s.getSize());
            if(Keyboard.isKeyDown(Keyboard.KEY_S))
                s.move(0, Menu.STAR_SPEED * .5f * s.getSize() * s.getSize());
            s.move(-Menu.STAR_SPEED * s.getSize() * s.getSize(), Menu.STAR_SPEED/3 * s.getSize() * s.getSize());

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
        if(starTrans < 255)
            starTrans++;

        for(Button b : buttons){
            b.update(delta);
        }
        redColor.update(delta);
        greenColor.update(delta);
        blueColor.update(delta);
        playerData.getColorData(selection).set(redColor.getValue(), greenColor.getValue(), blueColor.getValue());
        while(Mouse.next()) {
            if (Mouse.getEventButtonState()) {
                if (Mouse.getEventButton() == 0) {
                    for(Button b : buttons){
                        if (b.getBounds().contains(getMousePoint())) {
                            b.clicked();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void render() {
        for(Star s : stars){
            s.draw(starTrans);
        }
        if(transparency) {
            setColor(90, 90, 90, 120);
            fillRect(getX(), getY(), getWidth() / 3, getHeight());
            playerData.drawTransparent(getX() + getWidth()/6, getY() + getHeight()/2, (playerData.getOriginalWidth()/3)*4,
                    (playerData.getOriginalHeight()/3)*4, selection, 40);
        } else {
            playerData.draw(getX() + getWidth()/6, getY() + getHeight()/2, (playerData.getOriginalWidth()/3)*4,
                    (playerData.getOriginalHeight()/3)*4);
        }
        setColor(90, 90, 90, 90);
        fillRect(getX() + getWidth()/3, getY(), getWidth() - getWidth()/6, getHeight());
        for(Button b : buttons){
            b.draw();
        }
        redColor.draw();
        greenColor.draw();
        blueColor.draw();
        setColor(Color.WHITE);
        setFont(getFont("distantGalaxy50"));
        drawString("red", getX() + redColor.getX() + redColor.getWidth()/2 - getCurrentFont().getWidth("red")/2,
                redColor.getY()/2);
        drawString("green", getX() + greenColor.getX() + greenColor.getWidth()/2 - getCurrentFont().getWidth("green")/2,
                greenColor.getY()/2);
        drawString("blue", getX() + blueColor.getX() + blueColor.getWidth()/2 - getCurrentFont().getWidth("blue")/2,
                blueColor.getY()/2);
        drawString(Integer.toString(redColor.getValue()),
                getX() + redColor.getX() + redColor.getWidth()/2 - getCurrentFont().getWidth(Integer.toString(redColor.getValue()))/2,
                redColor.getY() + redColor.getHeight() + getHeight()/20);
        drawString(Integer.toString(greenColor.getValue()),
                getX() + greenColor.getX() + greenColor.getWidth()/2 - getCurrentFont().getWidth(Integer.toString(greenColor.getValue()))/2,
                greenColor.getY() + greenColor.getHeight() + getHeight()/20);
        drawString(Integer.toString(blueColor.getValue()),
                getX() + blueColor.getX() + blueColor.getWidth()/2 - getCurrentFont().getWidth(Integer.toString(blueColor.getValue()))/2,
                blueColor.getY() + blueColor.getHeight() + getHeight()/20);
    }

    private class SelectionButton extends Button{
        protected String partName;
        public SelectionButton(int x, int y, int width, int height){
            super(x, y, width, height, getFont("distantGalaxy30"), "");
            partName = "";
        }

        public SelectionButton(int x, int y, int width, int height, String text){
            super(x, y, width, height, getFont("distantGalaxy30"), text);
            partName = "";
        }

        public SelectionButton(int x, int y, int width, int height, String text, String partName){
            super(x, y, width, height, getFont("distantGalaxy30"), text);
            this.partName = partName;
        }

        @Override
        public void draw(){
            if(isHighlighted){
                if(partName.equals(selection))
                    setColor(230, 230, 230);
                else
                    setColor(255, 255, 255);
                fillRect(x, y, width, height);
                setColor(0, 0, 0);
                drawRect(x, y, width, height);
                setFont(font);
                drawString(text, x + width/2 - getCurrentFont().getWidth(text)/2,
                        y + height/2 - (getCurrentFont().getAscent() + getCurrentFont().getDescent())/2);
            } else {
                if(partName.equals(selection))
                    setColor(50, 50, 50);
                else
                    setColor(0, 0, 0);
                fillRect(x, y, width, height);
                setColor(255, 255, 255);
                drawRect(x, y, width, height);
                setFont(font);
                drawString(text, x + width/2 - getCurrentFont().getWidth(text)/2,
                        y + height/2 - (getCurrentFont().getAscent() + getCurrentFont().getDescent())/2);
            }
        }

        @Override
        public void clicked(){
            selection = partName;
        }
    }

    private class ColorSelection{
        private int x, y, width, height, value, barPosition; // value should be a multiple of 256
        private String color;
        private Rectangle bounds;

        public ColorSelection(int x, int y, int width, int height, String color){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.color = color;
            value = 255;
            // Adding area around selection (the selection bar) to bounds
            bounds = new Rectangle(x - width/2, y, width*2, height);
            //barBounds = new Rectangle(x - width/2, barPosition - height/256, width*2, (height/256)*4);
            barPosition = y;
        }

        public void update(int delta){
            if(Mouse.isButtonDown(0)){
                /*
                if(bounds.contains(getMousePoint()) ||
                        (barBounds.contains(getMousePoint()) && getMouseY() < y + height &&getMouseY() > y)){
                */
                if(bounds.contains(getMousePoint())){
                    value = (256 - (int) (((double)(getMouseY() - y)/height)*256)) - 1;
                    if(value < 0)
                        value = 0;
                    else if (value > 255)
                        value = 255;
                    //barPosition = getMouseY();
                    //barBounds.setBounds(x - width/2, barPosition - height/256, width*2, (height/256)*4);
                }
            }
            barPosition = y + (255 - value)*(height/256);
        }

        public void draw(){
            setColor(Color.WHITE);
            drawRect(x-1, y-1, width + 2, height+2);
            /*
            setColor(100, 100, 100);
            fillRect(x, y, width, height);
            */
            // openGL override
            glBegin(GL_QUADS);
                if(color.equals("red"))
                    glColor3f(255, 0, 0);
                else if (color.equals("green"))
                    glColor3f(0, 255, 0);
                else if (color.equals("blue"))
                    glColor3f(0, 0, 255);
                glVertex2f(x, y);
                glVertex2f(x + width, y);
                glColor3f(0, 0, 0);
                glVertex2f(x + width, y + height);
                glVertex2f(x, y + height);
            glEnd();
            if(color.equals("red"))
                setColor(value, 0, 0);
            else if (color.equals("green"))
                setColor(0, value, 0);
            else if (color.equals("blue"))
                setColor(0, 0, value);
            cFillRect(x + width/2, barPosition, width/2 + width/2, (height/256)*2);
            setColor(Color.WHITE);
            cDrawRect(x + width/2, barPosition, width/2 + width/2, (height/256)*2);
        }

        public int getValue(){
            return value;
        }

        public int getX(){
            return x;
        }

        public int getY(){
            return y;
        }

        public int getWidth(){
            return width;
        }

        public int getHeight(){
            return height;
        }

        public void setValue(int value){
            this.value = value;
        }
    }

}
