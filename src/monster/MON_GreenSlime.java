package monster;

import entity.Entity;
import java.util.Random;
import main.GamePanel;

public class MON_GreenSlime extends Entity{
    Random random = new Random();

    GamePanel gp;
    
    public MON_GreenSlime(GamePanel gp){
        super(gp);

        this.gp = gp;

        name = "Green Slime";
        speed = 1;
        maxLife = 3;
        life = maxLife;
        attack = 5;
        defense = 0;
        exp = 2;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();
    }

    public void getImage(){
        up1 = setup("/monsterImg/greenslime_down_1", gp.tileSize, gp.tileSize);
        up2 = setup("/monsterImg/greenslime_down_2", gp.tileSize, gp.tileSize);
        down1 = setup("/monsterImg/greenslime_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/monsterImg/greenslime_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/monsterImg/greenslime_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("/monsterImg/greenslime_down_2", gp.tileSize, gp.tileSize);
        right1 = setup("/monsterImg/greenslime_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/monsterImg/greenslime_down_2", gp.tileSize, gp.tileSize);
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

    public void damageReaction(){
        actionLockCounter =0;
        direction = gp.player.direction;
    }

    
}
