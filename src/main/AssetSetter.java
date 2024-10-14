package main;

import entity.Entity;
import entity.NPC_OldMan;
import object.OBJ_Boots;
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
        

    }

    public void addObj(SuperObject obj, int col, int row) {
       
    }

    public void setNPC(){
        addNPC(new NPC_OldMan(gp), 21, 21);
    }
 
    public void addNPC(Entity npc, int col, int row){
        for (int i = 0; i < gp.npc.length; i++) {
            if (gp.npc[i] == null) {
                gp.npc[i] = npc;
                gp.npc[i].worldX = col * gp.tileSize;
                gp.npc[i].worldY = row * gp.tileSize;
                break;
            }
        }
    }

}
