package entity;

import main.GamePanel;
import java.util.Random;

public class NPC_OldMan extends Entity{
    Random random = new Random();

    public NPC_OldMan(GamePanel gp){
        super(gp);

        direction = "down";
        speed = 1;
        getImage();
        setDialogue();
    }

    public void getImage(){
        up1 = setup("/npc/oldman_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/oldman_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/oldman_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/oldman_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/oldman_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/oldman_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/oldman_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/oldman_right_2", gp.tileSize, gp.tileSize);
    }

    public void setDialogue(){
        addDialogue("Hello, lad.");
        addDialogue("So you've come to this island to#find the treasure?");
        addDialogue("I used to be a great wizard but now...#I'm a bit too old for taking an adventure.");
        addDialogue("Well, goodluck nalng nimo.");

    }

    public void addDialogue(String dialogue){
        for(int i = 0; i < dialogues.length; i++){
            if(dialogues[i] == null){
                dialogues[i] = dialogue;
                break;
            }
        }
    }

    public void move() {
        actionLockCounter++;
    
        if (actionLockCounter >= 120) { 
    
            int i = random.nextInt(100) + 1;
    
            if (i <= 25) {
                direction = "up";
            } else if (i <= 50) {
                direction = "down";
            } else if (i <= 75) {
                direction = "left";
            } else {
                direction = "right";
            }
    
            actionLockCounter = 0;
        }
    }

    public void speak(){
        super.speak();
    }
    

    public void update(){
        super.update();
    }

    
}
