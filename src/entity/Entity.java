package entity;

import java.awt.image.BufferedImage;

import main.GamePanel;
import main.UtilityTool;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;

import java.awt.Rectangle;

public abstract class Entity {

    GamePanel gp;
    public int worldX, worldY;
    public int speed;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn =false;
    public int actionLockCounter = 0;

    public Entity(GamePanel gp){
        this. gp = gp;
    }

    public abstract void move();
    public void update(){
        move();

        collisionOn = false;
        gp.colChecker.checkTile(this);
        gp.colChecker.checkObject(this, false);
        gp.colChecker.checkPlayer(this);

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
    
            g2.drawImage(image, screenX, screenY, null);
         }
    }

    public BufferedImage setup(String imagePath){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream(imagePath +".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        }catch(Exception e){
            e.printStackTrace();
        }

        return image;
    }
}

