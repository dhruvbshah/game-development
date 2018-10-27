/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazarusgame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Iterator;
/**
 *
 * @author drew
 */
abstract public class Thing
{
    //x and y coordinate from the top right corner
    private int x, y;
    //facing in radians with 0 pointing down, increasing counterclockwise
    private double direction;
    //speed in pixels per frame
    private int speed;
    //the image of the object
    private Image[] img;
    //determines if the object should execute its dead method
    private boolean done;
    //marks the object for deletion
    private boolean reallyDone;
    //an Observable object that every game object needs to access
    private GameEvents events;
    //determines which image is currently showing (for animation)
    private int imgIndex;
    private int playerNo;
    private String objName;
    private int objId;
    private static int seqNo=0;
    private int score;
    private ArrayList<ArrayList> ev;
    
    public Thing(int x, int y, double direction, int speed, Image[] img,
            GameEvents events, String objName, ArrayList ev)
    {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = speed;
        this.img = img;
        this.events = events;
        this.done = false;
        this.reallyDone = false;
        this.imgIndex = 0;
        this.objName = objName;
        this.seqNo++;
        this.objId= this.seqNo;
        this.ev = ev;
    };
    
    public Thing(int x, int y, double direction, int speed, Image[] img,
            GameEvents events, int playerNo, String objName, ArrayList ev)
    {       
        this(x,y,direction,speed,img,events,objName,ev);
        this.playerNo = playerNo;
    }

    //draws the object with the given Graphics2D performimg rotation and
    //translation to get the proper facing and postion
    public void draw(Graphics2D g2, ImageObserver obs)
    {
        int w = img[imgIndex].getWidth(obs);
        int h = img[imgIndex].getHeight(obs);
        AffineTransform trans = new AffineTransform();
 
        trans.translate(x - w/2, y - h/2);
        //used to rotate lazarus
        trans.rotate(-direction, w/2, h/2);

        if (!done){
         g2.drawImage(img[imgIndex], trans, obs); 
//           g2.drawImage(img[imgIndex], x-w/2, y-h/2, obs);
 
        }
        
        imgIndex++;
        if (imgIndex >= img.length){
            imgIndex = 0;
        }
    }

    //called every frame, calls dead or action and move
    //before calling move, it checks if the object is out of bounds and sets it
    //to be deleted or other dealt with
    public void updateThing(int w, int h)
    {
        if (done){
            dead();
        } else {
            action();
            if ((x < -50) || (x > w + 50) || (y < -50) || (y > h + 50)) {
                reallyDone = true;
                done = true;
            }
            move();
        }
    }
    
    public void setImg(Image[] img){
        this.img = img;
    }
    //checks for collision
    public boolean collision(int x, int y, int w, int h)
    {
        if(getDone()) {
            return false;
        }
        
//  The x and y parameters to Rectangle should be for the upper left corner of the rectangle.
       Rectangle o1_box = new Rectangle(x-w/2,y-h/2,w,h);
       
       Rectangle o2_box = new Rectangle( getX()-getImage().getWidth(null)/2
                                        ,getY()-getImage().getHeight(null)/2
                                        ,getImage().getWidth(null)
                                        ,getImage().getHeight(null));
// check for overlap of two rectangles indicating a collision.
        if (o1_box.intersects(o2_box)) {
            return true;
        } else {
            return false;
        }

    }

    //movement type actions
  //  abstract public void move();
 
    public void dead() {
        setRDone(true);
    } 
    
    public void itHit(Unit u){};

    //what to do when the object hits another object
    public void iHitU(Unit u){};

    //dummy for action type things (so it does not need to be overridden when
    //not used
    public void action(){};
    
    //various get and set type methods for parameters after this point
    public void setX(int x){
        this.x = x;
    }

    public int getX(){
        return x;
    }

    public void changeX(int change){
        this.x += change;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getY(){
        return y;
    }

    public void changeY(int change){
        this.y += change;
    }

    public void setDirection(double d){
        this.direction = d;
    }
    public double getDirection(){
        return direction;
    }
    public void changeDirection(double change){
        this.direction += change;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public int getSpeed(){
        return speed;
    }

    public void changeSpeed(int change){
        this.speed += change;
    }
    
    public Image getImage(){
        return img[imgIndex];
    }

    public void setDone(boolean done){
        this.done = done;
    }
    
    public boolean getDone(){
        return done;
    }
    
    public void setRDone(boolean rdone){
        reallyDone = rdone;
    }
    
    public boolean getRDone(){
        return reallyDone;
    }
    
    public int getHeight(){
        return img[imgIndex].getHeight(null);
    }
    
    public int getWidth(){
        return img[imgIndex].getWidth(null);
    }
    
    public GameEvents getEvents(){
        return events;
    }
    public int getPlayerNo(){
        return this.playerNo;
    }
    public String getObjName(){
        return this.objName;
    }
    public int getScore(){
        return this.score;
    }
    public int getObjId(){
        return this.objId;
    }
    public void addScore(int i){
        this.score += i;
    }
    public void move()
    {
        Unit temp;
        
        //compute dx and dy for bullet
        changeX((int)(-1*getSpeed() * Math.sin(getDirection())));
        changeY((int)(-1*getSpeed() * Math.cos(getDirection())));
        if (ev == null){
            return;
        }
        //check for collision due to movement.
        Iterator<ArrayList> it = ev.listIterator(1);
        while (it.hasNext()){
            Iterator<Unit> it2 = it.next().listIterator();
            while (it2.hasNext()){
                temp = it2.next();
                if((temp.getPlayerNo() != this.getPlayerNo()) && (temp.collision(getX(), getY(), getWidth(), getHeight()))){
                    getEvents().setCollision(this, temp);
                }
            }
        } 
    }
}
