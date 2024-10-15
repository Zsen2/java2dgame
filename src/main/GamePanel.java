package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{

    //SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; //768 px
    public final int screenHeight = tileSize * maxScreenRow; //576 px

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxScreenCol;
    public final int worldHight = tileSize * maxScreenRow;


    //FPS
    int FPS = 60;


    //SYSTEM
    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    public CollisionChecker colChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public UI ui = new UI(this);

    //ENTITY OBJECT
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[10];
    public Entity npc[] = new Entity[10];

    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame(){
        aSetter.setObject();
        aSetter.setNPC();
        gameState = titleState;
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run(){
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null){
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += currentTime - lastTime;
            lastTime = currentTime;

            if(delta >= 1){
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if(timer >= 1000000000){
                System.out.println("Fps:" + drawCount);
                drawCount =0;
                timer = 0;
            }
            
        }
    }

    public void update(){
        if(gameState == playState){
            player.update();
            for(Entity npc : npc){
                if(npc != null){
                    npc.update();
                }
            }
        }
        if(gameState == pauseState){

        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        // DEBUG
        long drawStart = 0;
        if(keyH.checkDrawTime){
            drawStart = System.nanoTime();
        }
        // TITLE SCREEN
        if(gameState == titleState){
            ui.draw(g2);
        }
        else{
            tileM.draw(g2);

            for(SuperObject obj : obj){
                if(obj != null){
                    obj.draw(g2, this);
                }
            }
    
            // NPC
            for(Entity npc : npc){
                if(npc != null){
                    npc.draw(g2);
                }
            }
    
            // PLAYER
            player.draw(g2);
    
            ui.draw(g2);
        }

        // DEBUG
        if(keyH.checkDrawTime){
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw time:" + passed, 10, 400);
            System.out.println("Draw Time:" + passed);
        }
        

        g2.dispose();
    }

    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
         
    }

    public void stopMusic(){
        music.stop();

    }

    public void playSE(int i){
        se.setFile(i);
        se.play();
    }

}

