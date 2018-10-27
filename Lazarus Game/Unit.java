/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazarusgame;

import java.awt.Image;
import java.util.*;

/**
 *
 * @author drew
 */
abstract public class Unit extends Thing implements Observer {

    //damage is the object current damage
    //in general, when the current damage >= to the max damage, the object is dead
    private int damage;
    private int maxdamage;

    //damageto is the amount of damage done to an object colliding with this one
    //in general
    private int damageto;

    public Unit(int x, int y, double direction, int speed, Image[] img,
            GameEvents events, int maxdamage, int damageto, String objName, ArrayList ev) {
        super(x, y, direction, speed, img, events, objName, ev);
        this.damage = 0;
        this.maxdamage = maxdamage;
        this.damageto = damageto;

        //adds itself to the Observer list
        events.addObserver(this);
    }

    //second constructor has tankNo also, which is passed to Thing
    public Unit(int x, int y, double direction, int speed, Image[] img,
            GameEvents events, int maxdamage, int damageto, int playerNo, String objName, ArrayList ev) {
        super(x, y, direction, speed, img, events, playerNo, objName, ev);
        this.damage = 0;
        this.maxdamage = maxdamage;
        this.damageto = damageto;

        //adds itself to the Observer list
        events.addObserver(this);
    }

    //calls when a Thing hits this Unit
    abstract public void hitMe(Thing caller);

    //this is what this Thing does to a Unit that hit it
//    @Override
    public void itHit(Unit u) {
        u.changeDamage(getDamageTo());
    }

    public int getDamage() {
        return this.damage;
    }

    public void changeDamage(int i) {
        this.damage += i;
    }

    public void setDamage(int i) {
        this.damage = i;
    }

    public int getMax() {
        return this.maxdamage;
    }

    public int getDamageTo() {
        return this.damageto;
    }

    //@Override
    public void move() {   //wall does not move.
        //if the damage it takes is more than the max it can take, the wall is marked to be removed from arraylist.
        if (getDamage() > getMax()) {
            setDone(true);
        }
        super.move();
    }
    //  @Override

    public void update(Observable o, Object arg) {
        GameEvents ev = (GameEvents) arg;

        if (ev.getType() == 1) {
            if (ev.getTarget() == this) {
                hitMe((Thing) ev.getCaller());
            }
        }
    }

}
