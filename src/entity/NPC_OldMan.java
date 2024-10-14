package entity;

import main.GamePanel;
import java.util.Random;

public class NPC_OldMan extends Entity{

    public NPC_OldMan(GamePanel gp){
        super(gp);

        direction = "down";
        speed = 1;
        getImage();
    }

    public void getImage(){
        up1 = setup("/npc/oldman_up_1");
        up2 = setup("/npc/oldman_up_2");
        down1 = setup("/npc/oldman_down_1");
        down2 = setup("/npc/oldman_down_2");
        left1 = setup("/npc/oldman_left_1");
        left2 = setup("/npc/oldman_left_2");
        right1 = setup("/npc/oldman_right_1");
        right2 = setup("/npc/oldman_right_2");
    }

    public void move() {
        actionLockCounter++;
    
        if (actionLockCounter >= 120) {
            Random random = new Random();
    
            int i = random.nextInt(100) + 1;
    
            if (i <= 25) {
                direction = "up";
                System.out.println("up");
            } else if (i <= 50) {
                direction = "down";
                System.out.println("down");
            } else if (i <= 75) {
                direction = "left";
                System.out.println("left");
            } else {
                direction = "right";
                System.out.println("right");
            }
    
            actionLockCounter = 0;
        }
    }
    

    public void update(){
        super.update();
    }

    
}
