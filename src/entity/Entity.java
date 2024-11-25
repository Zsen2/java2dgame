package entity;

import java.awt.image.BufferedImage;

import main.GamePanel;
import main.UtilityTool;
import monster.MON_GreenSlime;

import javax.imageio.ImageIO;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.Rectangle;

public abstract class Entity {

    GamePanel gp;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    public boolean collision = false;
    public Rectangle attackArea = new  Rectangle(0,0,0,0);
    
    String dialogues[] = new String[20];
    int dialogueIndex =0;
    public BufferedImage image, image2, image3;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;

    // COUNTERS
    public int actionLockCounter = 0;
    public int invincibleCounter = 0;
    public int spriteCounter = 0;
    int dyingCounter = 0;
    int hpBarCounter = 0;

    // STATE
    public boolean invincible = false;
    public int spriteNum = 1;
    public String direction = "down";
    public int worldX, worldY;
    public boolean collisionOn =false;
    boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    boolean hpBarOn = false;


    // CHARACTER ATTRIBUTES
    public String name;
    public int speed;
    public int maxLife;
    public int life;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Entity currentWeapon;
    public Entity currentShield;

    // ITEM ATTRIBUTES
    public int attackValue;
    public int defenseValue;
    public String description = "";

    public Entity(GamePanel gp){
        this. gp = gp;
    }

    public void move(){}
    public void damageReaction(){}
    public void speak(){
        if(dialogues[dialogueIndex] == null){
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch(gp.player.direction){
            case "up" :
                direction = "down";
                break;
            case "down" :
                direction = "up";
                break;
            case "left" :
                direction = "right";
                break;
            case "right" :
                direction = "left";
                break;
        }
    }
    public void update(){
        move();

        collisionOn = false;
        gp.colChecker.checkTile(this);
        gp.colChecker.checkObject(this, false);
        boolean contactPlayer = gp.colChecker.checkPlayer(this);
        gp.colChecker.checkEntity(this,  gp.npc);
        gp.colChecker.checkEntity(this,  gp.monster);

        if(this instanceof MON_GreenSlime && contactPlayer){
            if(!gp.player.invincible){
                gp.playSE(6);

                int damage = attack - gp.player.defense;
                if(damage < 0){
                    damage = 0;
                }
                gp.player.life -= damage;
                
                gp.player.invincible = true;    
            }
        }

        if (!collisionOn) {
            switch (direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }

        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }

        if(invincible){
            invincibleCounter++;
            if(invincibleCounter > 40){
                invincible = false;
                invincibleCounter =0 ;
            }
        }
    }

    public void draw(Graphics2D g2){

        BufferedImage image = null;

         // Calculate the screen position based on the object's world position
         int screenX = worldX - gp.player.worldX + gp.player.screenX;
         int screenY = worldY - gp.player.worldY + gp.player.screenY;
     
         // Only draw if the object is within the camera bounds (visible on screen)
         if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
             worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
             worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
             worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
     
             g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

             switch(direction){
                case "up" -> image = (spriteNum == 1) ? up1 : up2;
                case "down" -> image = (spriteNum == 1) ? down1 : down2;
                case "left" -> image = (spriteNum == 1) ? left1 : left2;
                case "right" -> image = (spriteNum == 1) ? right1 : right2;
            }

            // MONSTER HP BAR
            if(this instanceof MON_GreenSlime && hpBarOn){
                double oneScale = (double)gp.tileSize/maxLife;
                double hpBarValue = oneScale*life;

                g2.setColor(new Color(35,35,35));
                g2.fillRect(screenX-1, screenY-11, gp.tileSize+2, 12);
                g2.setColor(new Color(255,0,30));
                g2.fillRect(screenX, screenY-10, (int)hpBarValue, 10);

                hpBarCounter++;

                if(hpBarCounter > 600){
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if (invincible) {
                hpBarOn = true;
                hpBarCounter = 0;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            }

            if(dying){
                dyingAnimation(g2);
            }
    
            g2.drawImage(image, screenX, screenY, null);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

         }
    }

    public void dyingAnimation(Graphics2D g2){
        dyingCounter++;

        int interval = 5;
        
        // Change alpha every 5 counts
        float alpha = (dyingCounter / interval) % 2 == 0 ? 0f : 1f;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        if (dyingCounter > 40) {
            dying = false;
            alive = false;
        }

    }

    public BufferedImage setup(String imagePath, int width, int height){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream(imagePath +".png"));
            image = uTool.scaleImage(image, width, height);
        }catch(Exception e){
            e.printStackTrace();
        }

        return image;
    }
}

