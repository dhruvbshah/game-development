/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lazarusgame;

import java.awt.event.*;

public class KeyControl extends KeyAdapter
{
    private GameEvents events;

    public KeyControl(GameEvents events){
        this.events = events;
    }
    @Override
    public void keyPressed(KeyEvent e){
        events.setKeys(e);
    }
    @Override
    public void keyReleased(KeyEvent e){
        events.setKeys(e);
    }
}
