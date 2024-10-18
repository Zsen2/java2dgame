package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.awt.Rectangle;
import java.awt.AlphaComposite;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public int damageCooldown = 0;

    public Player(GamePanel gp, KeyHandler keyH){

        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle(9, 21, 30, 23); //PLAYER COLLISION AREA
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;


        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";

        // PLAYER STATUS
        maxLife = 6;
        life = maxLife;
    }

    public void getPlayerImage(){
       
        up1 = setup("/player/boy_up_1");
        up2 = setup("/player/boy_up_2");
        down1 = setup("/player/boy_down_1");
        down2 = setup("/player/boy_down_2");
        left1 = setup("/player/boy_left_1");
        left2 = setup("/player/boy_left_2");
        right1 = setup("/player/boy_right_1");
        right2 = setup("/player/boy_right_2");

       
    }

    @Override
    public void move() {
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }

            collisionOn = false;
            gp.colChecker.checkTile(this);

            // CHECK NPC COLLISION
            int npcIndex = gp.colChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);
           
            int objIndex = gp.colChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // CHECK MONSTER COLLISION
            int monsterIndex = gp.colChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            gp.eHandler.checkEvent();

            gp.keyH.spacePressed = false;

            if (!collisionOn) {
                switch (direction) {
                    case "up" -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "left" -> worldX -= speed;
                    case "right"->  worldX += speed;
                }
            }
        }
        else{
            spriteCounter = 0;
        }
    }

    public void contactMonster(int i) {
        if(i != 999){
            if(!invincible){
                life -= 1;
                invincible = true;
            }
        }
    }

    @Override
    public void update() {
        move(); 

        if(invincible){
            invincibleCounter++;
            if(invincibleCounter > 60){
                invincible = false;
                invincibleCounter =0 ;
            }
        }

        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    } 

    public void pickUpObject(int i){
        if(i != 999){
            
        }
    }

    public void interactNPC(int i){
        if(i != 999){
            if(gp.keyH.spacePressed){
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
            
        }
        
    }

    public void draw(Graphics2D g2){
        // g2.setColor(Color.white);
        // g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        BufferedImage image = null;

        switch(direction){
            case "up":
                image = (spriteNum == 1) ? up1 : up2;
                break;
            case "down":
                image = (spriteNum == 1) ? down1 : down2;
                break;
            case "left":
                image = (spriteNum == 1) ? left1 : left2;
                break;
            case "right":
                image = (spriteNum == 1) ? right1 : right2;
                break;
        }

        if(invincible){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        g2.drawImage(image, screenX, screenY, null);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

    }
}
