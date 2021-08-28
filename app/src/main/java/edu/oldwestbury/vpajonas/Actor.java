package edu.oldwestbury.vpajonas;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public interface Actor {
    public void update();
    public Actor act();
    public String getTickText();
    public ActTypes react();
    public Bitmap getSprite();
    public void onTouch(View v, MotionEvent event);
    public int getTileX();
    public int getTileY();
    public boolean getIsSolid();
    public Rect getShape();
    public short getDirection();
    public void setBusy(boolean b);
    public void setTouchable(boolean b);
    public boolean getTouchable();
    public void setTileX(int toSet);
    public void setTileY(int toSet);
    public void setTiles(DTile[][] toSet);
    public String getErrorText();
    public int getSound();
    public int[] getSoundSet();
    public ActTypes getBehavior();

}
