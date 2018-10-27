/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lazarusgame;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;

/**
 * This is the space in which the game object live.
 * It draws the background and all objects.
 */
public class GameSpace extends JPanel
{

    private int backspeed;
    private int movex = 0, movey = 0;
    private double backdirection;
    private Image tile;
    
    public GameSpace(Image tile)
    {
        super();
        this.tile = tile;
    }

    public void drawBackground(Graphics2D g2)
    {
        int h = this.getHeight();
        int w = this.getWidth();
        int TileWidth = tile.getWidth(this);
        int TileHeight = tile.getHeight(this);
        int NumberX = (int) (w / TileWidth);
        int NumberY = (int) (h / TileHeight);

        for (int i = -1; i <= NumberY + 1; i++){
            for (int j = -1; j <= NumberX + 1; j++){
                g2.drawImage(tile, j * TileWidth, i * TileHeight
                        ,TileWidth, TileHeight, this);
            }
        }
    }

    //goes through and draws everything
    public void drawHere(ArrayList<ArrayList> everything, Graphics2D g2)
    {
        Thing temp;
        Iterator<ArrayList> it = everything.listIterator();
        while (it.hasNext()){
            Iterator<Thing> it2 = it.next().listIterator();
            while (it2.hasNext()){
                temp = it2.next();
                temp.draw(g2, this);
            }
        }
    }

    public void setTile(Image tile){
        this.tile = tile;
    }
    
    public Image getTile(){
        return tile;
    }

}
