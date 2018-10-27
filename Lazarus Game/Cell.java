/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lazarusgame;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 */
public class Cell {

    private int x, y, type;
    private boolean occupied, stopFlg;

    public Cell(int x, int y, boolean occupied, int type, boolean stopFlg) {
        this.x = x;
        this.y = y;
        this.occupied = occupied;
        this.type = type;
        this.stopFlg = stopFlg;
    }

    public int getCellX() {
        return this.x;
    }

    public boolean getCellStopFlg() {
        return this.stopFlg;
    }

    public void setCellStopFlg(boolean flg) {
        this.stopFlg = flg;
    }

    public int getCellY() {
        return this.y;
    }

    public boolean getCellOccupied() {
        return this.occupied;
    }

    public void setCellOccupied(boolean flg) {
        this.occupied = flg;
    }

    public int getCellType() {
        return this.type;
    }

}
