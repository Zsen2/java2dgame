package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entity.Entity;
import object.OBJ_Heart;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
    BufferedImage keyImage, heart_full, heart_half, heart_blank;
    public boolean messageOn = false;
    // int messageCounter = 0;
    // public String message = "";
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;
    public int commandNum = 0;
    public int slotCol =0;
    public int slotRow =0;
    
    public String currentDialogue;

    public UI(GamePanel gp){
        this.gp = gp;

        arial_40 = new Font("Arial", Font.PLAIN,40);
        arial_80B = new Font("Arial", Font.BOLD,80);
        // OBJ_Key key = new OBJ_Key(gp);
        // keyImage = key.image;

        //CREATE HUD OBJECT
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
    }

    

    public void addMessage(String text){
        message.add(text);
        messageCounter.add(0);
    }

    public void draw(Graphics2D g2){
        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(Color.white);

        if(gp.gameState == gp.titleState){
            drawTitleScreen();
        }
        // PLAY STATE
        if(gp.gameState == gp.playState){
            drawPlayerLife();
            drawMessage();
        }
        if(gp.gameState == gp.pauseState){
            drawPlayerLife();
            drawPauseScreen();
        }
        // DIALOGUE STATE
        if(gp.gameState == gp.dialogueState){
            drawPlayerLife();
            drawDialogueScreen();
        }
        // CHARACTER STATE
        if(gp.gameState == gp.characterState){
            drawCharacterScreen();
            drawInventory();
        }
        
    }

    public void drawPlayerLife() {
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;

        // Draw hearts (both blank and current life)
        for (int i = 0; i < gp.player.maxLife / 2; i++) {
            // Draw the blank heart
            g2.drawImage(heart_blank, x, y, null);
            
            // Draw the current life (full or half)
            if (i < gp.player.life / 2) {
                g2.drawImage(heart_full, x, y, null);
            } else if (i == gp.player.life / 2 && gp.player.life % 2 == 1) {
                g2.drawImage(heart_half, x, y, null);
            }
            
            x += gp.tileSize;
        }

    }

    public void drawMessage(){
        int messageX = gp.tileSize;
        int messageY = gp.tileSize*4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

        for(int i = 0; i < message.size(); i++){
            if(message.get(i) != null){
                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX+2, messageY+2);
                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1;
                messageCounter.set(i , counter);
                messageY += 50;

                if(messageCounter.get(i) > 180){
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }

    public void drawTitleScreen() {
        g2.setColor(new Color(0,0,0));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80F));
        String text = "Blue Boy Adventure";
        int x = getXforCenteredText(text);
        int y = gp.tileSize*3;

        // SHADOW
        g2.setColor(Color.GRAY);
        g2.drawString(text, x+5, y+5);

        // MAIN COLOR
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);

        //BLUE BOY IMAGE
        x = gp.screenWidth/2 - (gp.tileSize*2)/2;
        y += gp.tileSize*2;
        g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);

        //MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F));

        text = "NEW GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize*3.5;
        g2.drawString(text, x, y);
        if(commandNum == 0){
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "LOAD GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(commandNum == 1){
            g2.drawString(">", x - gp.tileSize, y);
        }

        
        text = "QUIT";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(commandNum == 2){
            g2.drawString(">", x - gp.tileSize, y);
        }


    }

    public void drawDialogueScreen() {
        // WINDOW

        int x = gp.tileSize*2;
        int y = gp.tileSize/2;
        int width = gp.screenWidth - (gp.tileSize*4);
        int height = gp.tileSize*4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        x += gp.tileSize;
        y += gp.tileSize;

        for(String line : currentDialogue.split("#")){
            g2.drawString(line,x,y);
            y+=40;
        }
    }

    public void drawCharacterScreen() {
        // CREATE A FRAME
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
    
        // TEXT SETTINGS
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(30F));
    
        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 35;
        final int padding = 20;
    
        // NAMES
        String[] names = {"Level", "Life", "Strength", "Dexterity", "Attack", "Defense", "Exp", "Next Level", "Coin", "Weapon", "Shield"};
        for (String name : names) {
            g2.drawString(name, textX, textY);
            if(name == "Coin") textY += lineHeight + 20;
            else if(name == "Weapon") textY += lineHeight + 15;
            else textY += lineHeight;
        }
    
        // VALUES
        int tailX = (frameX + frameWidth) - padding; 
        textY = frameY + gp.tileSize; // Reset textY for values
        String[] values = {
            String.valueOf(gp.player.level),
            String.valueOf(gp.player.life + "/" + gp.player.maxLife),
            String.valueOf(gp.player.strength),
            String.valueOf(gp.player.dexterity),
            String.valueOf(gp.player.attack),
            String.valueOf(gp.player.defense),
            String.valueOf(gp.player.exp),
            String.valueOf(gp.player.nextLevelExp),
            String.valueOf(gp.player.coin)
        };
    
        for (String value : values) {
            textX = getXforAlignToRightText(value, tailX);
            g2.drawString(value, textX, textY);
            textY += lineHeight;
        }

        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY-15, null);
        textY += gp.tileSize;

        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY-15, null);
    }

    public void drawInventory() {

        // FRAME
        int frameX = gp.tileSize * 9;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 5;
    
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
    
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        final int itemsPerRow = 5;
        
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.tileSize+3;
    
        for (int i = 0; i < gp.player.inventory.size(); i++) {
            g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);
    
            slotX += slotSize;
    
            if ((i + 1) % itemsPerRow == 0) {
                slotX = slotXstart;
                slotY += slotSize;
            }
        }
    
        // CURSOR
        int cursorX = slotXstart + (slotSize * slotCol);
        int cursorY = slotYstart + (slotSize * slotRow);
        
        // Draw cursor
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, gp.tileSize, gp.tileSize, 10, 10);

        // DESCRIPTION FRAME
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gp.tileSize*3;
        drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);

        // DRAW DESC TEXT
        int textX = dFrameX +20;
        int textY = dFrameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(28F));
        
        int itemIndex = getItemIndexOnSlot();

        if(itemIndex < gp.player.inventory.size()){
            String[] lines = gp.player.inventory.get(itemIndex).description.split("\n");
            
            for(String line : lines){
                g2.drawString(line, textX, textY);
                textY += 32;
            }
        }

    }    

    public int getItemIndexOnSlot(){
        int itemIndex = slotCol + (slotRow*5);
        return itemIndex;
    }

    public void drawSubWindow(int x, int y, int width, int height){
        Color c = new Color(0,0,0, 200);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    public void drawPauseScreen(){
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";
        int x = getXforCenteredText(text);;
        int y = gp.screenHeight/2;

        g2.drawString(text,x,y);
    }

    public int getXforCenteredText(String text){
        int len = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - len/2;

        return x;
    }

    public int getXforAlignToRightText(String text, int tailX){
        int len = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - len;

        return x;
    }
    
}
