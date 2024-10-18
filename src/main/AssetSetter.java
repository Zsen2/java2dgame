package main;

import entity.Entity;
import entity.NPC_OldMan;
import monster.MON_GreenSlime;
import object.OBJ_Door;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setObject() {
        

    }

    public void setNPC(){
        addNPC(new NPC_OldMan(gp), 21, 21);
    }

    public void setMonster(){
        addMonster(new MON_GreenSlime(gp), 23, 36);
        addMonster(new MON_GreenSlime(gp), 23, 37);
    }
 

    public void addObj(Entity obj, int col, int row) {
        int worldX = col * gp.tileSize;
        int worldY = row * gp.tileSize;
    
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == null) {
                obj.worldX = worldX;
                obj.worldY = worldY;
                gp.obj[i] = obj; 
                return;
            }
        }
    }
    
    public void addNPC(Entity npc, int col, int row){
        int worldX = col * gp.tileSize;
        int worldY = row * gp.tileSize;
    
        for (int i = 0; i < gp.npc.length; i++) {
            if (gp.npc[i] == null) {
                npc.worldX = worldX;
                npc.worldY = worldY;
                gp.npc[i] = npc; 
                return;
            }
        }
    }

    public void addMonster(Entity monster, int col, int row){
        int worldX = col * gp.tileSize;
        int worldY = row * gp.tileSize;
    
        for (int i = 0; i < gp.monster.length; i++) {
            if (gp.monster[i] == null) {
                monster.worldX = worldX;
                monster.worldY = worldY;
                gp.monster[i] = monster; 
                return;
            }
        }
    }
    

}
