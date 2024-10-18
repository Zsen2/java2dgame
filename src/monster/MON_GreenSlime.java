package monster;

import entity.Entity;
import java.util.Random;
import main.GamePanel;

public class MON_GreenSlime extends Entity{
    Random random = new Random();
    public MON_GreenSlime(GamePanel gp){
        super(gp);

        name = "Green Slime";
        speed = 1;
        maxLife = 3;
        life = maxLife;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();
    }

    public void getImage(){
        up1 = setup("/monsterImg/greenslime_down_1");
        up2 = setup("/monsterImg/greenslime_down_2");
        down1 = setup("/monsterImg/greenslime_down_1");
        down2 = setup("/monsterImg/greenslime_down_2");
        left1 = setup("/monsterImg/greenslime_down_1");
        left2 = setup("/monsterImg/greenslime_down_2");
        right1 = setup("/monsterImg/greenslime_down_1");
        right2 = setup("/monsterImg/greenslime_down_2");
    }

    public void move(){
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
    
    public void update(){
        super.update();
    }

    
}
