package edu.oldwestbury.vpajonas;

import android.graphics.Bitmap;
import android.graphics.Rect;

public interface DTile {

    public DTileTypes getType();
    public Bitmap getSprite();
    public int getX();
    public int getY();
    public void setXY(int xPassed, int yPassed);
    public boolean getIsSolid();
    public Actor getActor();
    public void setActor(Actor toSet);

    public FloorItem getItem();
    public void setItem(FloorItem toSet);
    public Rect getShape();
    public void refreshShape(ScreenXYPositionFinder screenXYPositionFinder);

}
