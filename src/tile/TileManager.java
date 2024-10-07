package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {
    GamePanel gp;
    Tile[] tile;
    int mapTileNum[][];
    
    public TileManager(GamePanel gp){
        this.gp = gp;

        tile = new Tile[10];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/maps/worldmap01.txt");

    }

    public void getTileImage(){
        try{
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/002.png")); //grass

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/032.png")); //wall

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/019.png")); //running water

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/017.png")); //dirt

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/016.png")); //tree

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/003.png")); //sand

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath){
        try{
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new  BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row=0;

            while(col<gp.maxWorldCol && row<gp.maxWorldRow){
                String line = br.readLine();

                while(col < gp.maxWorldCol){
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                col = 0;
                row++;
            }
            br.close();

        }catch(Exception e){}
    }

    public void draw(Graphics2D g2) {
    
        // Calculate the visible tile range based on the player's position
        int leftCol = Math.max(0, (gp.player.worldX - gp.player.screenX) / gp.tileSize);
        int rightCol = Math.min(gp.maxWorldCol - 1, (gp.player.worldX + gp.screenWidth) / gp.tileSize) + 1;
        int topRow = Math.max(0, (gp.player.worldY - gp.player.screenY) / gp.tileSize);
        int bottomRow = Math.min(gp.maxWorldRow - 1, (gp.player.worldY + gp.screenHeight) / gp.tileSize) + 1;
        
        // Loop through visible tiles, including handling out-of-bounds tiles
        for (int worldCol = leftCol; worldCol <= rightCol; worldCol++) {
            for (int worldRow = topRow; worldRow <= bottomRow; worldRow++) {
    
                int worldX = worldCol * gp.tileSize;
                int worldY = worldRow * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;
    
                // Check if the tile is within the world bounds
                if (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
                    // Get the tile number and draw the tile
                    int tileNum = mapTileNum[worldCol][worldRow];
                    g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                } else {
                    // Draw a default "void" tile or background color when out of world bounds
                    g2.setColor(Color.BLACK);  // Set to black or any other color for the void
                    g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);  // Fill void space
                }
            }
        }
    }
    
    
    
}
