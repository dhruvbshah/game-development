/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lazarusgame;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.awt.image.*;
import java.net.URL;
import java.text.AttributedString;
import java.util.*;
import javax.sound.midi.*;
import javax.swing.*;
import java.io.File;
import java.net.URL;
import javax.sound.sampled.*;

public class LazarusGame extends Game
{
    private GameSpace screen;
    private ScorePanel scorepanel;

    private ArrayList<ArrayList> everything;
    private ArrayList<Thing> things;
    private ArrayList<PlayerParent> players;
    private ArrayList<Wall> walls;
    private ArrayList<Boxx> box;

    private int boxSpeed = 2;
    private int maxGridX=0, maxGridY=0;
    private int boxWidth, boxHeight;
    private Image[] playerimg[], boximg[], smallexpl, largeexpl, wallimg[], stopFlgimg[];
    private Image[] declareVictoryimg[], gameOverimg[];
    private Cell[][] grid = new Cell[50][50];
    private int currGridX, currGridY, stopFlagGridX, stopFlagGridY;
    private GameController gcontroller;
    private GameEvents events;

    private int level=1;
    private boolean gameover;
    private boolean destroy = false;
    private int lives;
    private PlayerParent lazarusObj;
    
    private URL[] explsoundurl;
    private URL musicSquish;

    //creates and adds all the game panel to the applet
    //also sets up images, sounds, and creates and initializes state for most
    //variables and objects.
    @Override
    public void init()
    {      
        super.init();
    
        screen = new GameSpace(getMyImage("Resources/Background.png"));
        scorepanel = new ScorePanel(getMyImage(""));

        add(screen, BorderLayout.CENTER);
        add(scorepanel, BorderLayout.SOUTH);
        setBackground(Color.white);

        everything = new ArrayList<ArrayList>(); // 2 dimensional arraylist contain all images
        things = new ArrayList<Thing>();
        everything.add(things);
        players = new ArrayList<PlayerParent>(); // 1 dimensional arraylist for the lazaruss (i.e. players)
        everything.add(players);                
        walls = new ArrayList<Wall>();           // 1 dimensional arraylist for soft and hard walls.
        everything.add(walls);
        box = new ArrayList<Boxx>();             // 1 dimensional arraylist for all boxes.
        everything.add(box);

        events = new GameEvents();

        KeyControl keys = new KeyControl(events);
        addKeyListener(keys);
        
        gcontroller = new GameController();
        gameover = false;
        
        Object[] options = {"Start"};
        //seek input from user
        int n = JOptionPane.showOptionDialog(this, "", "Welcome",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                options, options[0]);
    }
    
    //getting all image files and loads in arraylists
    @Override
    public void initImages()
    {
        try
        {
            playerimg = new Image[2][1];
            playerimg[0][0] = getMyImage("Resources/lazarus.png");
            playerimg[1][0] = getMyImage("Resources/lazarus.png");

            wallimg       = new Image[2][1];
            wallimg[0][0] = getMyImage("Resources/Wall.gif");
            wallimg[1][0] = getMyImage("Resources/Wall.gif");
            
            stopFlgimg = new  Image[1][1];
            stopFlgimg[0][0] = getMyImage("Resources/Button.gif");
            
            declareVictoryimg = new  Image[1][1];
            declareVictoryimg[0][0] = getMyImage("Resources/youWin.png");
            
            gameOverimg = new  Image[1][1];
            gameOverimg[0][0] = getMyImage("Resources/gameOver.png");

            boximg = new Image[4][1];
            boximg[0][0] = getMyImage("Resources/CardBox.gif");
            boximg[1][0] = getMyImage("Resources/WoodBox.gif");
            boximg[2][0] = getMyImage("Resources/MetalBox.gif");
            boximg[3][0] = getMyImage("Resources/StoneBox.gif");

            smallexpl = new Image[6];
            for (int i = 0; i < 6; i++){
                smallexpl[i] = getMyImage("Resources/explosion1_" + (i + 1) + ".png");
            }

            largeexpl = new Image[7];
            for (int i = 0; i < 7; i++){
                largeexpl[i] = getMyImage("Resources/explosion2_" + (i + 1) + ".png");
            }

        } catch (Exception e){
            System.out.println("Error in getting images: " + e.getMessage());
        }
    }
    
    //getting all sound files
    @Override
    public void initSound()
    {
        try
        {
            Sequence music;
            Sequencer seq;
            URL musicu = LazarusGame.class.getResource("Resources/lazarusMusic.mid");
            musicSquish = LazarusGame.class.getResource("Resources/Squished.wav");
            
            explsoundurl = new URL[2];
            explsoundurl[0] = LazarusGame.class.getResource("Resources/snd_explosion1.wav");
            explsoundurl[1] = LazarusGame.class.getResource("Resources/snd_explosion2.wav");

            music =  MidiSystem.getSequence(musicu);
            seq = MidiSystem.getSequencer();
            seq.open();
            seq.setSequence(music);
            seq.start();
            seq.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        }
        catch(Exception e){
            System.out.println("Error in midi: " + e.getMessage());
        }
    }

//Updates all Things and then draws everything
//when the game is resetting, this method will also 
    @Override
    public void drawAll(int w, int h, Graphics2D g2)
    {
        Thing temp;
   
        // draw background
        screen.drawBackground(g2);

        //removes images which are not active
        Iterator<ArrayList> it = everything.listIterator();
        while (it.hasNext()){
            Iterator<Thing> it2 = it.next().listIterator();
            while (it2.hasNext()){
                if (gameover){
                    break;
                }
                temp = it2.next();
                temp.updateThing(w, h);
                if (temp.getRDone()){
                    it2.remove();
                }
            }
            if (gameover){
                break;
            }
        }

        //draw all active images from 'everything'
        screen.drawHere(everything, g2);

        //display current scores 
        scorepanel.drawScore(w, h, g2);

        //clear arraylist and reset values if game is over.
        if(destroy){
            it = everything.listIterator();
            while(it.hasNext()){
                Iterator<Thing> it2 = it.next().listIterator();
                while(it2.hasNext()){
                    it2.next();
                    it2.remove();
                }
            }
            
            gcontroller = new GameController();
            destroy = false;
            gameover = false;
            events.deleteObservers();
            this.requestFocus();
        }
        
        gcontroller.timeline();
    }
    
    //this spawns players, walls, powerUp
    public class GameController
    {
        private int timer, nextSpawnTimer, boxType;
        private Wall cornerWall;
//        private PlayerParent lazarusObj;
        
        public GameController(){
            timer = 0;
            nextSpawnTimer=100;
            boxType=1;
        }

        public void timeline(){
            int sw=screen.getWidth();
            int sh=screen.getHeight();
            int ww=wallimg[0][0].getWidth(null);
            int wh=wallimg[0][0].getHeight(null);
            int r1=0,r2=0,tempX=0,tempY=0,tempGridX=0,tempGridY=0;
            Iterator<Wall> it;                    
            Wall tempWall; 
            PlayerParent temp;
            
            boxWidth  = boximg[0][0].getWidth(null);
            boxHeight = boximg[0][0].getHeight(null);   

            switch (timer){
                case 0:
                    //part of initialization.
                    maxGridX=0;
                    maxGridY=0;                 
                    for (int i=0; i < sw-2*ww; i += boxWidth){
                        maxGridX++;  
                    }
                    for (int i=0; i < sh-2*wh; i += boxHeight){
                        maxGridY++;                        
                    } 
                    sw = maxGridX*boxWidth  + 2*ww;
                    sh = maxGridY*boxHeight + 2*wh;

                    if (maxGridX == 1){
                        currGridX=1;
                    }else{
                        currGridX=(int)(maxGridX/2);
                    }
                    currGridY=1;
                    
                    for (int i=1,j=1,x=0,y=0; i <= maxGridX; i++){
                        for (j=1; j <= maxGridY; j++){
                            x = (int)((1+i-0.5)*boxWidth);
                            y = (int)((1+maxGridY-j+0.5)*boxHeight);                            
                            grid[i][j] = new Cell(x,y,false,0, false);
                        }
                    }
                    
                    for (int i=0; i <= maxGridX+1; i++){                    
                        //top
                        tempX = (int)((i+0.5)*boxWidth);
                        tempY = (int)((0+0.5)*boxHeight);
                        walls.add(new Wall(tempX,tempY, wallimg[0], events, 100000, "WallHard"));
                        //bottom
                        tempY = (int)((maxGridY+1+0.5)*boxHeight);
                        walls.add(new Wall(tempX,tempY, wallimg[0], events, 100000, "WallHard"));                        
                    }   
                    for (int i=0; i <= maxGridY+1; i++){                    
                        //left
                        tempX = (int)((0+0.5)*boxWidth);
                        tempY = (int)((i+0.5)*boxHeight);
                        tempWall = new Wall(tempX,tempY, wallimg[0], events, 100000, "WallHard");
                        walls.add(tempWall); 
                        if (i == (maxGridY)){
                            System.out.println("cornerwall i="+i);
                            cornerWall = tempWall;
                        }
                        //right
                        tempX = (int)((maxGridX+1+0.5)*boxWidth);
                        walls.add(new Wall(tempX,tempY, wallimg[0], events, 100000, "WallHard"));
                    }   
                    //Level 1
                    if (level == 1){
                        for (int i=1; i <= 2; i++){                    
                            tempY = (int)((maxGridY+1+0.5-i)*boxHeight);
                            for (int j=5; j <= 7; j++){                    
                                tempX = (int)((j+0.5)*boxWidth);
                                walls.add(new Wall(tempX,tempY, wallimg[0], events, 100000, "WallHard"));
                            }
                        }  
                        stopFlagGridX = 5;
                        stopFlagGridY = 3;
                        grid[stopFlagGridX][stopFlagGridY].setCellStopFlg(true);
                        tempX = (int)((stopFlagGridX+0.5)*boxWidth);
                        tempY = (int)((maxGridY+1+0.5-stopFlagGridY)*boxHeight);                                    
                        walls.add(new Wall(tempX,tempY, stopFlgimg[0], true, events, 100000, "WallHard"));
                    }
                    //Level 2                    
                    if (level == 2){
                        for (int i=1; i <= 4; i++){                    
                            tempY = (int)((maxGridY+1+0.5-i)*boxHeight);
                            for (int j=5; j <= 15; j++){                    
                                tempX = (int)((j+0.5)*boxWidth);
                                walls.add(new Wall(tempX,tempY, wallimg[0], events, 100000, "WallHard"));
                           }
                        }    
                        stopFlagGridX = 5;  
                        stopFlagGridY = 5;                                    
                        grid[stopFlagGridX][stopFlagGridY].setCellStopFlg(true);                                    
                        tempX = (int)((stopFlagGridX+0.5)*boxWidth);
                        tempY = (int)((maxGridY+1+0.5-stopFlagGridY)*boxHeight);                                    
                        walls.add(new Wall(tempX,tempY, stopFlgimg[0], true, events, 100000, "WallHard"));
                    }
                    //Level 3                   
                    if (level == 3){
                        currGridX = 32;
                        for (int i=1; i <= 18; i+=2){                    
                            tempY = (int)((maxGridY+1+0.5-i)*boxHeight);
                            for (int j=5; j <= 30-2*i; j++){  
                                if (j != 9){
                                    tempX = (int)((j+0.5)*boxWidth);
                                    walls.add(new Wall(tempX,tempY, wallimg[0], events, 100000, "WallHard"));
                                    tempY = (int)((maxGridY+1+0.5+1-i)*boxHeight);  
                                    walls.add(new Wall(tempX,tempY, wallimg[0], events, 100000, "WallHard"));
                                }
                            }
                        }
                        stopFlagGridX = 4; 
                        stopFlagGridY = 1;                                    
                        grid[stopFlagGridX][stopFlagGridY].setCellStopFlg(true);                                    
                        tempX = (int)((stopFlagGridX+0.5)*boxWidth);
                        tempY = (int)((maxGridY+1+0.5-stopFlagGridY)*boxHeight);                                    
                        walls.add(new Wall(tempX,tempY, stopFlgimg[0], true, events, 100000, "WallHard"));
                    }                    
                    if (level == 4){
                        //Level 4                        
                        currGridX = 32;
                        for (int i=1; i <= 15; i++){                    
                            tempY = (int)((maxGridY+1+0.5-2*i)*boxHeight);
                            for (int j=5+2*i; j <= 30-2*i; j++){                    
                                tempX = (int)((j+0.5)*boxWidth);
//                                if (i != 15 && j != (int)(maxGridX/2+1)){
                                if (i != 15 && j != (int)(maxGridX/2+1) && j != (int)(maxGridX/2+2)){
                                    walls.add(new Wall(tempX,tempY, wallimg[0], events, 100000, "WallHard"));
                                }
                            }
                        }    
                        tempX = (int)((20+0.5)*boxWidth);
                        tempY = (int)((maxGridY+1+0.5-9)*boxHeight);
                        walls.add(new Wall(tempX,tempY, wallimg[0], events, 100000, "WallHard"));

                        stopFlagGridX = 19; 
                        stopFlagGridY = 9;                                    
                        grid[stopFlagGridX][stopFlagGridY].setCellStopFlg(true);                                    
                        tempX = (int)((stopFlagGridX+0.5)*boxWidth);
                        tempY = (int)((maxGridY+1+0.5-stopFlagGridY)*boxHeight);                                    
                        walls.add(new Wall(tempX,tempY, stopFlgimg[0], true, events, 100000, "WallHard"));
                    }
                    
                    it = walls.listIterator();                  
                    while(it.hasNext()){
                        tempWall = it.next();
                        tempX= tempWall.getX();
                        tempY= tempWall.getY();
                        tempGridX = (int)(tempX/boxWidth);
                        tempGridY = maxGridY+1-(int)(tempY/boxHeight);
                        if (tempGridX >= 1 && tempGridX <= maxGridX 
                            && tempGridY >= 1 && tempGridY <= maxGridY
                            && (tempWall.getStopFlg() == false)){
                            grid[tempGridX][tempGridY].setCellOccupied(true);
                        }    
                    }
                //    origGridX = currGridX;
                //    origGridY = currGridY;
                    lazarusObj = new PlayerLazarus(grid[currGridX][currGridY].getCellX()
                        ,grid[currGridX][currGridY].getCellY(),
                        0., 1, playerimg[0],
                        events, 1, 0, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
                        KeyEvent.VK_UP, KeyEvent.VK_DOWN);
                    
                    players.add(lazarusObj);
                    lives = 0;
                    boxType=1;

                    break;
            }
            
            if ((timer >= nextSpawnTimer) && (!gameover)){
                spawnBox(boxType); 
                //randomly obtain box type.
                r1=0;
                while ((r1 < 1) || (r1 > 4)){
                    r1 = ((int)(10000*Math.random()))%5;
                }
                boxType=r1;
                cornerWall.setImg(boximg[boxType-1]);
                //next spawn time is also obtained using random function.
                nextSpawnTimer += (int)(Math.max(75,500*Math.random()));
            }
            timer++;
            
        }
  
        private void spawnBox(int type) {
            int maxdamage=0;
            int damageto=0;
            int x,y;
            maxdamage=type+1;
            damageto=type+1;            
            x = grid[currGridX][maxGridY].getCellX();
            y = grid[currGridX][maxGridY].getCellY();
            box.add(new Boxx(x, y, 0, boxSpeed, boximg[type-1], events, everything, maxdamage, damageto, type, currGridX, maxGridY, grid, lazarusObj));
            return;
        }
        
    }
    
// class representing walls.
    public class Wall extends Unit
    {
        private boolean stopFlg = false;
        public Wall(int x, int y, Image[] img, GameEvents events, int maxdamage, String objName)
        {
            super(x, y, 0, 0, img, events, maxdamage, 0, objName, everything);
            this.stopFlg=false;
        }
        public Wall(int x, int y, Image[] img, boolean stopFlg, GameEvents events, int maxdamage, String objName)
        {
            super(x, y, 0, 0, img, events, maxdamage, 0, objName, everything);
            this.stopFlg=stopFlg;
        }
        public boolean getStopFlg(){
            return(this.stopFlg);
        }
        @Override
        public void hitMe(Thing t){
            t.iHitU(this);
        }

    }

 

//******************************************************************************
//represents Lazarus/Player
    public class PlayerLazarus extends PlayerParent
    {
 //       private int lazarusStatus=0;
        
        public PlayerLazarus(int x, int y, double direction, int speed, Image[] img,
                GameEvents events, int maxdamage, int damageto,  
                int left, int right, int up, int down)
        {
            super(x, y, direction, speed, img, events, maxdamage, damageto,
                    left, right, up, down,0,0,0,0,0,1,everything,"Lazarus");
 //           this.lazarusStatus = 1; //1 - Active, 2 - Won, 3 - Lost
        }

        //lazarus movement.
         public void move()
        {
            double dx=0,dy=0,Yspeed=5;
            Thing temp;
            boolean collision_flg;
            int origGridX = currGridX;
            int origGridY = currGridY;
            
            if(getMvLeft()) {
                setMvLeft(false);
                if (currGridX > 1){
                    //check left grid
                    if (grid[currGridX-1][currGridY].getCellOccupied() == false){
                        currGridX--;
                        while ((currGridY > 1) && grid[currGridX][currGridY-1].getCellOccupied() == false){//check if lower is empty
                            currGridY--;
                        }
                    }else {
                        if (grid[currGridX-1][currGridY+1].getCellOccupied() == false){// check left and 1 upper grid
                            if (currGridY < maxGridY){
                                currGridX--;  
                                currGridY++; 
                            }
                        }
                    }
                    dx=0;
                    if (currGridX != origGridX){
                        dx = grid[currGridX][currGridY].getCellX() - grid[origGridX][origGridY].getCellX();
                    }
                    dy=0;
                    if (currGridY != origGridY){
                        dy = grid[currGridX][currGridY].getCellY() - grid[origGridX][origGridY].getCellY();
                    }
                    changeX((int)(dx));  
                    changeY((int)(dy));  
                }
            }
            
            if(getMvRight()) {
                setMvRight(false);                
                if (currGridX < maxGridX){
                    //check right grid
                    if (grid[currGridX+1][currGridY].getCellOccupied() == false){
                        currGridX++;
                        while ((currGridY > 1) && (grid[currGridX][currGridY-1].getCellOccupied() == false)){//check if lower is empty
                            currGridY--;
                        }
                    }else{
                        if (grid[currGridX+1][currGridY+1].getCellOccupied() == false){// check right and 1 upper grid
                            if (currGridY < maxGridY){
                                currGridX++;  
                                currGridY++; 
                            }
                        }
                    }
                    dx=0;
                    if (currGridX != origGridX){
                        dx = grid[currGridX][currGridY].getCellX() - grid[origGridX][origGridY].getCellX();
                    }
                    dy=0;
                    if (currGridY != origGridY){
                        dy = grid[currGridX][currGridY].getCellY() - grid[origGridX][origGridY].getCellY();
                    }
                    changeX((int)(dx));  
                    changeY((int)(dy));  
                }
            }
            if ((currGridX != origGridX) || (currGridY != origGridY)){
                if (grid[currGridX][currGridY].getCellStopFlg() == true){
                    declareVictory();
                }
            }


        }
            
            
        public void declareVictory(){
            int tempX, tempY;
            tempX = (int)((stopFlagGridX+0.5)*boxWidth);
            tempY = (int)((maxGridY+1+0.5-stopFlagGridY-3)*boxHeight);                                    
            walls.add(new Wall(tempX,tempY, declareVictoryimg[0], true, events, 100000, "WallDeclareVictory"));
            gameover = true;
            PlayAgain playAgain = new PlayAgain("Play Again");
            playAgain.setVisible(true);
        }
        
        
        
        //explodes, then set up the scores or respawns depending on lives
//        @Override
        public void dead()
        {   
            int tempX, tempY;
 
   //         if(this.getLives() <= 0) {
                gameover = true;
                tempX = (int)((currGridX+0.5)*boxWidth);
                tempY = (int)((maxGridY+1+0.5-currGridY-3)*boxHeight);                                    
                walls.add(new Wall(tempX,tempY, gameOverimg[0], true, events, 100000, "WallGameOver"));
                try{
                    AudioInputStream explSound;
                    Clip clip;
                    explSound = AudioSystem.getAudioInputStream(musicSquish);
                    clip = AudioSystem.getClip();
                    clip.open(explSound);
                    clip.start();
                }catch(Exception e){
                    System.out.println("Error in sqishing sound: " + e.getMessage());
                }
                things.add(new Explosion(getX(), getY(), largeexpl, events, 2, explsoundurl[1]));
                PlayAgain playAgain = new PlayAgain("Play Again");
                playAgain.setVisible(true);

 //           } else {
 //               things.add(new Explosion(getX(), getY(), largeexpl, events, 2, explsoundurl[1]));
 //               try{
 //                   Thread.sleep(300);
 //               }catch(Exception e){
 //                   System.out.println(                "Error: in sleeping.");
 //               }
 //               if (getDamage() >= getMax()){
 //                   lives--;
 //                   this.changeLives(-1); 
 //               }
 //               destroy = true;
 //           }
            
            
            

        }
        
        //starting mercy timer
        @Override
        public void hitMe(Thing t){
            t.iHitU(this);
        }
    }
    
    
//******************************************************************************
//draws score and lives for each player.
    public class ScorePanel extends JPanel
    {
        private Image img;

        public ScorePanel(Image img)
        {
            super();
            this.img = img;
            Dimension d = new Dimension(img.getWidth(null), img.getHeight(null));

            this.setPreferredSize(d);
        }

        // displays score for each player
        public void drawScore(int w, int h, Graphics2D g2)
        {
            String s;
            AttributedString as;
            PlayerParent temp;
            Font bigLetters = new Font("Monospaced", Font.BOLD, 40);

            Iterator<PlayerParent> it = players.listIterator();
            int[] disp = new int[20];
            
            int i=0,j=0,x=0,y=0,w1,h1;
            while(it.hasNext()){
                temp = it.next();
  //              disp[i++] = temp.getLives();
  //              disp[i++] = temp.getMax() - temp.getDamage();            
                disp[i++] = temp.getScore();                
            }
            
            x=(int)(4*w/16);
            y=h;            
            s = "Level="+level + "              Score="+disp[0];
            as = new AttributedString(s);
            as.addAttribute(TextAttribute.FOREGROUND, Color.CYAN);
            as.addAttribute(TextAttribute.FONT, bigLetters);
            g2.drawString(as.getIterator(),x,y);
//            x += 390;
//            g2.setColor(Color.GREEN);
//            if (disp[1] >= 10){
//                g2.fillRect(x, h-20, w1, h1);
//            }
//            if (disp[1] >= 20){
//                g2.fillRect(x+32, h-20, w1, h1);
//            }
//            if (disp[1] >= 30){
//                g2.fillRect(x+32+32, h-20, w1, h1);
//            }  
//            
//            x=(int)(8.75*w/16);
//          
//            s = "Life="+disp[3] + "  Score="+disp[2];
//            as = new AttributedString(s);
//            as.addAttribute(TextAttribute.FOREGROUND, Color.CYAN);
//            as.addAttribute(TextAttribute.FONT, bigLetters);
//            g2.drawString(as.getIterator(),x,y);
//            x += 390;
//            g2.setColor(Color.GREEN);
//            if (disp[4] >= 10){
//                g2.fillRect(x, h-20, w1, h1);
//            }
//            if (disp[4] >= 20){
//                g2.fillRect(x+32, h-20, w1, h1);
//            }
//            if (disp[4] >= 30){
//                g2.fillRect(x+32+32, h-20, w1, h1);
//            }  
            
        }
    }    
    
    
   
    //this is used only after the game is over, to initiate the next game.
    public class PlayAgain extends JFrame implements ActionListener
    {
        public PlayAgain(String title)
        {
            super(title);
            this.setLocation(screen.getWidth()/2, screen.getHeight()/2);
            this.setLayout(new GridLayout(4,0));
            
            JButton one = new JButton("Level One");
            one.setActionCommand("one");
            this.add(one);
            
            JButton two = new JButton("Level Two");
            two.setActionCommand("two");
            this.add(two);
            
            JButton three = new JButton("Level Three");
            three.setActionCommand("three");
            this.add(three);

            JButton four = new JButton("Level Four");
            four.setActionCommand("four");
            this.add(four)
                    ;            
            one.addActionListener(this);
            two.addActionListener(this);
            three.addActionListener(this);
            four.addActionListener(this);     
            
            this.addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosing(WindowEvent e){
                    actionPerformed(new ActionEvent(this,
                            ActionEvent.RESERVED_ID_MAX+1, "None"));
                }
            });
            this.pack();
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if("one".equals(e.getActionCommand())){
                level = 1;
            }else if("two".equals(e.getActionCommand())){
                level = 2;
            }else if("three".equals(e.getActionCommand())){
                level = 3;
            }else if("four".equals(e.getActionCommand())){
                level = 4;
            };
            destroy = true;
            this.dispose();
        }
    }
    
//***************** MAIN *******************************************************
    public static void main(String[] args)
    {
        final LazarusGame game = new LazarusGame();
        game.init();
        final JFrame f = new JFrame("LazarusWar Game");
        
        // done on closing window
        f.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                f.dispose();
                System.exit(0);
            }
        });
        
        f.getContentPane().add("Center", game);
        f.pack();
        f.setSize(new Dimension(1366, 768)); 
        f.setVisible(true);
        f.setResizable(false);
        game.start();
    }
}
