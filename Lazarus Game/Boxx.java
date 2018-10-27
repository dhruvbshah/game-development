/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lazarusgame;

import java.awt.Image;
import java.util.ArrayList;
import java.util.*;

public class Boxx extends Unit
{
    private int type;
    private ArrayList<ArrayList> ev;
    private int gridX,gridY;
    Cell[][] grid;
    Image firstImg;
    private int startX,startY,maxGridY;
    boolean boxScored=false;
    PlayerParent lazarusObj;
     
    public Boxx(int x, int y, double direction, int speed, Image[] img,
            GameEvents events, ArrayList ev, int maxdamage ,int damageto, int type, int gridX, int gridY, Cell[][] grid
            ,PlayerParent lazarusObj)
    {
        super(x, y, direction, speed, img, events, maxdamage ,damageto, "Box", ev);
        this.type = type;
        this.ev = ev;
        this.gridX = gridX;
        this.gridY = gridY;
        this.maxGridY = gridY;
        this.grid = grid;
        this.firstImg = img[0];
        if (gridX > 0 && gridY > 0){
            grid[gridX][gridY].setCellOccupied(true);
        }
        this.startX = x;
        this.startY = y;
        this.boxScored = false;
        this.lazarusObj = lazarusObj;
    }

    //check 
    @Override
    public void hitMe(Thing t){
        t.iHitU(this);
    }
    public boolean getBoxScored(){
        return(this.boxScored);
    }  
    public void setBoxScored(boolean flg){
        this.boxScored = flg;
    }  
    @Override
    public void move()
    {
        Unit temp;
//        Thing temp;        
//        Thing lazarusObj;
        int dx, dy, newGridY;
        boolean exit_flg=false;
 
        if (ev == null || lazarusObj == null){
            return;
        }
        dx=(int)(getSpeed() * Math.sin(getDirection()));
        dy=(int)(getSpeed() * Math.cos(getDirection()));
        changeX(dx);
        changeY(dy);
 
        Iterator<ArrayList> it = ev.listIterator(1);
        Iterator<Unit> it2;
        
        while (it.hasNext() && (!exit_flg)){
            it2 = it.next().listIterator();            
            while (it2.hasNext() && (!exit_flg)){
                temp = it2.next();
                if((temp.getObjId() != this.getObjId()) && temp.collision(getX(), getY(), getWidth(), getHeight())){
                    changeX(-dx);
                    changeY(-dy);
                    temp.setDamage(this.getDamageTo());
                    if ((!temp.getObjName().equals("Lazarus")) && (!this.getBoxScored())){
                        this.setBoxScored(true);
                        this.lazarusObj.addScore(10*this.type);
                    }
                    if (temp.getDamage() > temp.getMax()){
                        temp.setDone(true);
                        if (temp.getObjName().equals("Lazarus")){
                            temp.dead();
                        }
                    }
                    exit_flg=true;
                }
            }
        }
        
        newGridY = this.maxGridY - (int)((this.getY() - this.startY)/this.firstImg.getHeight(null));
        gridY=newGridY;
        if ((newGridY >= 1) && (newGridY <= this.maxGridY)){
            if (!grid[gridX][newGridY].getCellOccupied()){
                grid[gridX][newGridY].setCellOccupied(true);
            }
        }
        if ((newGridY-1 >= 1) && (newGridY-1 <= this.maxGridY)){
            if (!grid[gridX][newGridY-1].getCellOccupied()){
                grid[gridX][newGridY-1].setCellOccupied(true);
            }
        }  
        if ((newGridY+1 >= 1) && (newGridY+1 <= this.maxGridY)){
            if (grid[gridX][newGridY+1].getCellOccupied()){
                grid[gridX][newGridY+1].setCellOccupied(false);
            }
        }        

    }
            
}
