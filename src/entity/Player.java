package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.Rectangle;
import java.awt.AlphaComposite;
import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Key;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;

public class Player extends Entity {
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public int damageCooldown = 0;
    public boolean attackCancel = false;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;

    public Player(GamePanel gp, KeyHandler keyH){

        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle(9, 21, 30, 23); //PLAYER COLLISION AREA
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        attackArea.width = 36;
        attackArea.height = 36;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }

    public void setDefaultValues(){
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";

        // PLAYER STATUS
        level = 1;
        maxLife = 6;
        life = maxLife;
        strength = 1;
        dexterity = 1;
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        attack = getAttack();
        defense = getDefense();
    }

    public void setItems(){
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Key(gp));
        

    }

    public int getAttack(){
        return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense(){
        return defense = dexterity * currentShield.defenseValue;
    }

    public void getPlayerImage(){
       
        up1 = setup("/player/boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/player/boy_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/player/boy_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/player/boy_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/player/boy_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/player/boy_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/player/boy_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/player/boy_right_2", gp.tileSize, gp.tileSize);

       
    }

    public void getPlayerAttackImage(){
        attackUp1 = setup("/player/boy_attack_up_1", gp.tileSize, gp.tileSize*2);
        attackUp2 = setup("/player/boy_attack_up_2", gp.tileSize, gp.tileSize*2);
        attackDown1 = setup("/player/boy_attack_down_1", gp.tileSize, gp.tileSize*2);
        attackDown2 = setup("/player/boy_attack_down_2", gp.tileSize, gp.tileSize*2);
        attackLeft1 = setup("/player/boy_attack_left_1", gp.tileSize*2, gp.tileSize);
        attackLeft2 = setup("/player/boy_attack_left_2", gp.tileSize*2, gp.tileSize);
        attackRight1 = setup("/player/boy_attack_right_1", gp.tileSize*2, gp.tileSize);
        attackRight2 = setup("/player/boy_attack_right_2", gp.tileSize*2, gp.tileSize);
    }

    @Override
    public void move() {
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.spacePressed) {
            if (keyH.upPressed) direction = "up";
            else if (keyH.downPressed) direction = "down";
            else if (keyH.leftPressed) direction = "left";
            else if (keyH.rightPressed) direction = "right";

            collisionOn = false;
            gp.colChecker.checkTile(this);

            // Check collisions with entities, objects, and monsters
            int npcIndex = gp.colChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);
            int objIndex = gp.colChecker.checkObject(this, true);
            pickUpObject(objIndex);
            int monsterIndex = gp.colChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            gp.eHandler.checkEvent(); 

            if (!collisionOn && !keyH.spacePressed) {
                switch (direction) {
                    case "up" -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "left" -> worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }
            if(keyH.spacePressed && !attackCancel){
                attacking = true;
                spriteCounter = 0;
            }
            attackCancel = false;

            gp.keyH.spacePressed = false; 
        }
        else{
            spriteCounter = 0;
        }
    }

    public void attacking() {
        spriteCounter++;

        if(spriteCounter <= 5) spriteNum = 1;
        else if(spriteCounter > 5 && spriteCounter <= 25){
            spriteNum = 2;

            // Save the current worldX, worldY, solidArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // ADjust player's worldX/Y for the attackArea
            switch(direction){
                case "up" -> worldY -= attackArea.height;
                case "down" -> worldY += attackArea.height;
                case "left" -> worldX -= attackArea.height;
                case "right" -> worldX += attackArea.height;
            }

            // attackArea to solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            // Check monster collision with new worldX, worldY and solidArea
            int monsterIndex = gp.colChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex);

            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;

        } 
        else{
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void contactMonster(int i) {
        if(i == 999 || invincible){
            return;
        }

        Entity monster = gp.monster[i];

        gp.playSE(6);

        int damage = monster.attack - defense;
        if(damage < 0){
            damage = 0;
        }
        life -= damage;
        invincible = true;
    }

    public void damageMonster(int i) {
        if(i == 999 || gp.monster[i].invincible) {
            return;
        }
    
        Entity monster = gp.monster[i];
    
        gp.playSE(5);
    
        int damage = Math.max(attack - monster.defense, 0);
        monster.life -= damage;
        gp.ui.addMessage(damage + " damage!");
    
        monster.damageReaction();
    
        monster.invincible = true;
    
        if(monster.life <= 0) {
            monster.dying = true;
            gp.ui.addMessage("Killed the " + monster.name + "!");
            gp.ui.addMessage("Exp + " + monster.exp);
            exp += monster.exp;
            checkLevelUp();
        }
    }

    public void checkLevelUp() {
        if(exp >= nextLevelExp){
            level++;
            nextLevelExp *= 2;
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();

            gp.playSE(8);
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You leveled up!";
        }
    }

    @Override
    public void update() {
        if (attacking) {
            attacking(); 
        }
        else{
            move();

            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }

        if(invincible){
            invincibleCounter++;
            if(invincibleCounter > 60){
                invincible = false;
                invincibleCounter =0 ;
            }
        }
    } 

    public void pickUpObject(int i){
        if(i != 999){
            
        }
    }

    public void interactNPC(int i) {
        if(i == 999 || !gp.keyH.spacePressed) {
            return;
        }
        
        attackCancel = true;
        gp.gameState = gp.dialogueState;
        gp.npc[i].speak();
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        if(attacking){
            switch(direction) {
                case "up" -> {
                    tempScreenY = screenY - gp.tileSize;
                    image = spriteNum == 1 ? attackUp1 : attackUp2;
                }
                case "down" -> image = spriteNum == 1 ? attackDown1 : attackDown2;
                case "left" -> {
                    tempScreenX = screenX - gp.tileSize;
                    image = spriteNum == 1 ? attackLeft1 : attackLeft2;
                }
                case "right" -> image = spriteNum == 1 ? attackRight1 : attackRight2;
            }
        }
        else{
            switch(direction) {
                case "up" -> image = spriteNum == 1 ? up1 : up2;
                case "down" -> image = spriteNum == 1 ? down1 : down2;
                case "left" -> image = spriteNum == 1 ? left1 : left2;
                case "right" -> image = spriteNum == 1 ? right1 : right2;
            }
        }
        

        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null); 

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); 
    }

}
