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
    public Tile[] tile;
    public int mapTileNum[][];
    
    public TileManager(GamePanel gp){
        this.gp = gp;

        tile = new Tile[10];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/maps/worldmap01.txt");

    }

    public void getTileImage() {
        try {
            createTile(0, "/tiles/002.png", false); // grass
            createTile(1, "/tiles/032.png", true);  // wall
            createTile(2, "/tiles/019.png", true);  // running water
            createTile(3, "/tiles/017.png", false); // dirt
            createTile(4, "/tiles/016.png", true);  // tree
            createTile(5, "/tiles/003.png", false); // sand
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void createTile(int index, String imagePath, boolean collision) throws IOException {
        tile[index] = new Tile();
        tile[index].image = ImageIO.read(getClass().getResourceAsStream(imagePath));
        tile[index].collision = collision;
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
       
        int leftCol = Math.max(0, (gp.player.worldX - gp.player.screenX) / gp.tileSize);
        int rightCol = Math.min(gp.maxWorldCol - 1, (gp.player.worldX + gp.screenWidth) / gp.tileSize) + 1;
        int topRow = Math.max(0, (gp.player.worldY - gp.player.screenY) / gp.tileSize);
        int bottomRow = Math.min(gp.maxWorldRow - 1, (gp.player.worldY + gp.screenHeight) / gp.tileSize) + 1;
        
        
        for (int worldCol = leftCol; worldCol <= rightCol; worldCol++) {
            for (int worldRow = topRow; worldRow <= bottomRow; worldRow++) {
    
                int worldX = worldCol * gp.tileSize;
                int worldY = worldRow * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;
    
               
                if (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
                   
                    int tileNum = mapTileNum[worldCol][worldRow];
                    g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                } else {
                    g2.setColor(Color.BLACK);  
                    g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize); 
                }
            }
        }
    }
    
    
}
