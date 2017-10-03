package game.misc;

import game.util.ColorData;

import java.io.Serializable;
import java.util.HashMap;

public class PlayerSave implements Serializable {
    private static final long serialVersionUID = -6427687392397625249L;
    private HashMap<String, ColorData> colorDatas;
    private double speed;

    private PlayerSave(PlayerSaveBuilder playerSaveBuilder){
        this.colorDatas = playerSaveBuilder.colorDatas;
    }

    public HashMap<String, ColorData> getColorDatas(){
        return colorDatas;
    }







    public static class PlayerSaveBuilder {
        private HashMap<String, ColorData> colorDatas;
        private double speed;

        public PlayerSaveBuilder setColorDatas(HashMap<String, ColorData> colorDatas){
            this.colorDatas = colorDatas;
            return this;
        }

        public PlayerSave build(){
            return new PlayerSave(this);
        }

    }
}
