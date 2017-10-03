package game;

import game.scenes.*;

public enum Scenes {
    MENU(0, new Menu()),
    PLAYER_CREATION(1, new PlayerCreation()),
    PAUSE_MENU(2, new PauseMenu()),
    OUTER_SPACE(3, new OuterSpace());

    public final int ID;
    private Scene scene;

    Scenes(int id, Scene scene){
        this.ID = id;
        this.scene = scene;
    }

    public Scene getScene(){
        return scene;
    }

}
