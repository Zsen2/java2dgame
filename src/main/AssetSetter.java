package main;

import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_Key;
import object.SuperObject;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setObject() {
        addObj(new OBJ_Key(), 23, 7);
        addObj(new OBJ_Key(), 23, 40);
        addObj(new OBJ_Key(), 38, 8);
        addObj(new OBJ_Door(), 10, 11);
        addObj(new OBJ_Door(), 8, 28);
        addObj(new OBJ_Door(), 12, 22);
        addObj(new OBJ_Chest(), 10, 7);
    }

    public void addObj(SuperObject obj, int col, int row) {
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == null) {
                gp.obj[i] = obj;
                gp.obj[i].worldX = col * gp.tileSize;
                gp.obj[i].worldY = row * gp.tileSize;
                break;
            }
        }
    }

}
