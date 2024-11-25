package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
    GamePanel gp;

    public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed;

    // DEBUG

    boolean checkDrawTime = false;

    public KeyHandler(GamePanel gp){
        this.gp =gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
       
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // TITLE STATEA
        if(gp.gameState == gp.titleState){
            titleState(code);
        }
        // PLAY STATE
        else if(gp.gameState == gp.playState){
            playState(code);
        }
        // PAUSE STATE
        else if(gp.gameState == gp.pauseState){
            pauseState(code);
        }
        // DIALOGUE STATE
        else if(gp.gameState == gp.dialogueState){
            dialogueState(code);
        }
        // CHARACTER STATE
        else if(gp.gameState == gp.characterState){
            characterState(code);
        }
    }

    public void titleState(int code) {
        switch (code) {
            case KeyEvent.VK_W:
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2;
                }
                break;
    
            case KeyEvent.VK_S:
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
                break;
    
            case KeyEvent.VK_SPACE:
                switch (gp.ui.commandNum) {
                    case 0:
                        gp.gameState = gp.playState;
                        gp.playMusic(0);
                        break;
                    case 1:
                        break;
                    case 2:
                        System.exit(0);
                        break;
                }
                break;
    
            default:
                break;
        }
    }
    

    public void playState(int code) {
        switch (code) {
            case KeyEvent.VK_W:
                upPressed = true;
                break;
    
            case KeyEvent.VK_A:
                leftPressed = true;
                break;
    
            case KeyEvent.VK_S:
                downPressed = true;
                break;
    
            case KeyEvent.VK_D:
                rightPressed = true;
                break;
    
            case KeyEvent.VK_P:
                gp.gameState = gp.pauseState;
                break;
    
            case KeyEvent.VK_C:
                gp.gameState = gp.characterState;
                break;
    
            case KeyEvent.VK_SPACE:
                spacePressed = true;
                break;
    
            // DEBUG
            case KeyEvent.VK_U:
                checkDrawTime = !checkDrawTime;
                break;
    
            default:
                break;
        }
    }    

    public void pauseState(int code){
        if(code == KeyEvent.VK_P){
            gp.gameState = gp.playState;
        }
    }
    public void dialogueState(int code){
        if(code == KeyEvent.VK_SPACE){
            gp.gameState = gp.playState;
        }
    }
    public void characterState(int code){
        switch (code) {
            case KeyEvent.VK_C:
                gp.gameState = gp.playState;
                break;
        
            case KeyEvent.VK_W:
                if (gp.ui.slotRow > 0) {
                    gp.ui.slotRow--;
                    gp.playSE(9);
                }
                break;
        
            case KeyEvent.VK_A:
                if (gp.ui.slotCol > 0) {
                    gp.ui.slotCol--;
                    gp.playSE(9);
                }
                break;
        
            case KeyEvent.VK_S:
                if (gp.ui.slotRow < 3) {
                    gp.ui.slotRow++;
                    gp.playSE(9);
                }
                break;
        
            case KeyEvent.VK_D:
                if (gp.ui.slotCol < 4) {
                    gp.ui.slotCol++;
                    gp.playSE(9);
                }
                break;
        
            default:
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
    
        switch (code) {
            case KeyEvent.VK_W:
                upPressed = false;
                break;
            case KeyEvent.VK_A:
                leftPressed = false;
                break;
            case KeyEvent.VK_S:
                downPressed = false;
                break;
            case KeyEvent.VK_D:
                rightPressed = false;
                break;
            default:
                break;
        }
    }
    


}

