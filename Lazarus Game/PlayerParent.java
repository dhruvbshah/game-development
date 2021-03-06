/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazarusgame;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.*;

abstract public class PlayerParent extends Unit
{
    //these contain the Key codes for the controls
    int left, right, up, down, fire, spfire;
    boolean mvLeft, mvRight, mvUp, mvDown, isFiring, isSp;
    
    int power;
    int shotDelay;
    int shotTime;
    int fastShotTime;
    int deadTimer;
    int deadTime;
    int score;
    int lives;
  
    public PlayerParent(int x, int y, double direction, int speed, Image[] img,
            GameEvents events, int maxdamage, int damageto, 
            int left, int right, int up, int down
            , int fire, int spfire,
            int shotTime, int fastShotTime, int deadTime, int playerNo, ArrayList ev, String objName)
    {
        super(x, y, direction, speed, img, events, maxdamage, damageto, playerNo, objName, ev);
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        this.fire = fire;
        this.spfire = spfire;
        this.shotTime = shotTime;
        this.fastShotTime = fastShotTime;
        this.deadTime = deadTime;
        shotDelay = 0;
        deadTimer = 0;
        power = 0;
        mvLeft = false;
        mvRight = false;
        mvUp = false;
        mvDown = false;
        isFiring = false;
        isSp = false;
        lives = 3;
    }
    
   //checks event type and calls the method for dealing with that
   // @Override
    public void update(Observable o, Object arg)
    {   
        GameEvents events = (GameEvents) arg;
        if(events.getType() == 1){             
           super.update(o, arg);              //calls parent's method on collision.
        } else if(events.getType() == 2){     //key pressed
            controls((KeyEvent)events.getTarget());
        };
    }
    
    //dealing with key events
    public void controls(KeyEvent e)
    {
        int key = e.getKeyCode();
        if(key == left || key == right || key == up || key == down){
            keyMove(e);
        }
        else if(key == fire || key == spfire){
            keyFire(e);
        }
    }
    
    //setting up key booleans based on the event. These are used in move() to
    //move the player
    public void keyMove(KeyEvent key)
        {
            //this eliminates the delay for the second key press when holding
            //down a key
            if (key.getID() == KeyEvent.KEY_PRESSED)
            {
                if (key.getKeyCode() == getLeft() && !mvLeft){
                    mvLeft = true;
                } else if (key.getKeyCode() == getRight() && !mvRight){
                    mvRight = true;
                } else if (key.getKeyCode() == getUp() && !mvUp){
                    mvUp = true;
                } else if (key.getKeyCode() == getDown() && !mvDown){
                    mvDown = true;
                }
            }
            
            if (key.getID() == KeyEvent.KEY_RELEASED)
            {
                if (key.getKeyCode() == getLeft() && mvLeft){
                    mvLeft = false;
                } else if (key.getKeyCode() == getRight() && mvRight){
                    mvRight = false;
                } else if (key.getKeyCode() == getUp() && mvUp){
                    mvUp = false;
                } else if (key.getKeyCode() == getDown() && mvDown){
                    mvDown = false;
                }
            }
        }
    
    //setting up firing booleans. These are used in action() to to actions.
    public void keyFire(KeyEvent key)
        {
            if(key.getID() == KeyEvent.KEY_PRESSED)
            {
                if(key.getKeyCode() == getFire() && !isFiring){
                    isFiring = true;
                }
                if(key.getKeyCode() == getSpFire() && !isSp){
                    isSp = true;
                }
            }
            
            if(key.getID() == KeyEvent.KEY_RELEASED)
            {
                if(key.getKeyCode() == getFire() && isFiring) {
                    isFiring = false;
                }
                
                if(key.getKeyCode() == getSpFire() && isSp){
                    isSp = false;
                }
            }

        }
    
    //get and set methods follow
    public int getPower(){
        return power;
    }
    
    public void setPower(int power){
        this.power = power;
    }
    
    public int getShotDelay(){
        return shotDelay;
    }
    
    public void setShotDelay(int delay){
        this.shotDelay = delay;
    }
    
    public void changeShotDelay(int change){
        this.shotDelay += change;
    }
    
    public int getShotTime(){
        return shotTime;
    }
    
    public int getFastShotTime(){
        return fastShotTime;
    }
    
    public int getLeft(){
        return left;
    }
    
    public int getRight(){
        return right;
    }
    
    public int getUp(){
        return up;
    }
    
    public int getDown(){
        return down;
    }
    
    public int getFire(){
        return fire;
    }
    
    public int getSpFire(){
        return spfire;
    }
    
    public int getDeadTimer(){
        return deadTimer;
    }
    
    public void setDeadTimer(int deadTimer){
        this.deadTimer = deadTimer;
    }
    
    public void decDeadTimer(){
        deadTimer--;
    }
    
    public int getDeadTime(){
        return deadTime;
    }
    public boolean getMvLeft(){
        return mvLeft;
    }
    public void setMvLeft(boolean flg){
        this.mvLeft = flg;
    }   
    public boolean getMvRight(){
        return mvRight;
    }
    public void setMvRight(boolean flg){
        this.mvRight = flg;
    }   
    public boolean getMvUp(){
        return mvUp;
    }
    public boolean getMvDown(){
        return mvDown;
    }
    public boolean getIsFiring(){
        return isFiring;
    }
    public boolean getIsSp(){
        return isSp;
    }
    public int getLives(){
        return this.lives;
    }
    public void changeLives(int i){
        this.lives = this.lives + i ;
    }
}