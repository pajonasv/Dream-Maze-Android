package edu.oldwestbury.vpajonas;

import android.graphics.Bitmap;

public interface Weapon {
    public String getName();
    public int getATK();
    public Bitmap getSprite();
    public boolean inRange(Player player, Enemy enemy,DTile[][] tileSet);

}
