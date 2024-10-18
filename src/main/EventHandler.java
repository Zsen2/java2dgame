package main;

public class EventHandler {
    GamePanel gp;
    EventRect eventRect[][];

    int previousEventX, previousEventY;
    boolean canTouchEvent = true;

    private static final int EVENT_RECT_X = 23;
    private static final int EVENT_RECT_Y = 23;
    private static final int EVENT_RECT_WIDTH = 2;
    private static final int EVENT_RECT_HEIGHT = 2;

    public EventHandler(GamePanel gp) {
        this.gp = gp;
        eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];

        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                eventRect[col][row] = new EventRect();
                eventRect[col][row].x = EVENT_RECT_X;
                eventRect[col][row].y = EVENT_RECT_Y;
                eventRect[col][row].width = EVENT_RECT_WIDTH;
                eventRect[col][row].height = EVENT_RECT_HEIGHT;
                eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
                eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;
            }
        }
    }


    public void checkEvent() {
        // CHECK IF PLAYER IS AWAY FROM PREVIOUS EVENT
        int distance = Math.max(Math.abs(gp.player.worldX - previousEventX), Math.abs(gp.player.worldY - previousEventY));
        if (distance > gp.tileSize) {
            canTouchEvent = true;
        }
    
        if (canTouchEvent) {
            if (hit(27, 16, "right")) {
                damagePit(27, 16, gp.dialogueState);
            }
            if (hit(23, 7, "up")) {
                healingPool(23, 7, gp.dialogueState);
            }
        }
    }
    

    public void damagePit(int col, int row, int gameState) {
        gp.gameState = gameState;
        gp.ui.currentDialogue = "you fell int a pit";
        gp.player.life -= 1;
        //eventRect[col][row].eventDone =true;
        canTouchEvent = false;
    }

    public boolean hit(int col, int row, String reqDirection) {
        boolean hit = false;
    
        // Update positions of the solid areas
        gp.player.solidArea.x += gp.player.worldX;
        gp.player.solidArea.y += gp.player.worldY;
        eventRect[col][row].x += col * gp.tileSize;
        eventRect[col][row].y += row * gp.tileSize;
    
        // Check for intersection and direction
        if (gp.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].eventDone) {
            if (reqDirection.equals("any") || gp.player.direction.equals(reqDirection)) {
                hit = true;
                previousEventX = gp.player.worldX;
                previousEventY = gp.player.worldY;
            }
        }
    
        // Reset solid areas to defaults
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;
    
        return hit;
    }
    

    public void healingPool(int col, int row, int gameState){
        if(gp.keyH.spacePressed){
            gp.gameState = gameState;
            gp.ui.currentDialogue = "You're drinking the water#Your life has been recovered";
            gp.player.life = gp.player.maxLife;
        }
    }

    // public void drawDebug(Graphics2D g2) {
    //     // Set color to red for debugging
    //     g2.setColor(Color.RED);
    
    //     // Get world position of the event rectangle
    //     int screenX = eventRect.x - gp.player.worldX + gp.player.screenX;
    //     int screenY = eventRect.y - gp.player.worldY + gp.player.screenY;
    
    //     // Draw the event rectangle (adjust size to your event rectangle size)
    //     g2.drawRect(screenX, screenY, eventRect.width * gp.tileSize, eventRect.height * gp.tileSize);
    // }
    

}
