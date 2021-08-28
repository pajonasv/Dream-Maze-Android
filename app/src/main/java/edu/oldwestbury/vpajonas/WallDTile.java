package edu.oldwestbury.vpajonas;

import android.graphics.Bitmap;

public class WallDTile implements DTile {
    private DTileTypes type;
    private Bitmap sprite;
    private int x;
    private int y;
    private boolean isSolid;
    private Actor actor;
    private FloorItem item;

    public WallDTile(Bitmap passedSprite, int passedX, int passedY){
        type = DTileTypes.T_WALL;
        sprite = passedSprite;
        x = passedX;
        y = passedY;

        isSolid = true;
    }

    @Override
    public DTileTypes getType(){
        return type;
    }

    @Override
    public Bitmap getSprite(){
        return sprite;
    }

    @Override
    public int getX(){
        return x;
    }

    @Override
    public int getY(){
        return y;
    }

    @Override
    public void setXY(int xPassed, int yPassed){
        x = xPassed;
        y = yPassed;
    }
    @Override
    public boolean getIsSolid(){
        return isSolid;
    }


    @Override
    public Actor getActor(){
        return actor;
    }
    @Override
    public void setActor(Actor toSet){
        actor = toSet;
    }
    @Override
    public FloorItem getItem(){
        return item;
    }
    @Override
    public void setItem(FloorItem toSet){
        item = toSet;
    }


}
