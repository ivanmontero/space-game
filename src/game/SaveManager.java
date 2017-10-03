package game;

import game.misc.PlayerSave;

import java.io.*;
import java.util.HashMap;

public class SaveManager {
    private static File folder;
    private static HashMap<Integer, PlayerSave> saveGames;

    private static final String SAVE_FOLDER = "saves";
    private static final String SAVE_NAMES = "playerSave";

    public static void init(){
        saveGames = new HashMap<>();
        folder = new File(SAVE_FOLDER);
        if(!folder.exists()){
            try{
                folder.mkdir();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        loadGameSaves();
    }

    public static void loadGameSaves(){
        for (File f : folder.listFiles()) {
            if (f.getName().indexOf(SAVE_NAMES) == 0 && f.getName().endsWith(".ser")) {
                try(ObjectInputStream is = new ObjectInputStream(new FileInputStream(f))){
                    saveGames.put(Integer.parseInt(f.getName().substring(SAVE_NAMES.length(),
                            f.getName().indexOf(".ser"))), (PlayerSave) is.readObject());
                    is.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveGame(int saveSlot){
        File f = new File(SAVE_FOLDER + "/" + SAVE_NAMES + saveSlot + ".ser");
        /*
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        */
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(f))) {
            os.writeObject(saveGames.get(saveSlot));
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveGame(int saveSlot, PlayerSave playerSave){
        File f = new File(SAVE_FOLDER + "/" + SAVE_NAMES + saveSlot + ".ser");
        /*
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        */
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(f))) {
            os.writeObject(playerSave);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //saveGames.put(saveSlot, playerSave);
    }

    public static boolean saveGameExists(int saveSlot){
        return saveGames.get(saveSlot) != null;
    }

    public static PlayerSave getGameSave(int saveSlot){
        return saveGames.get(saveSlot);
    }

}
