package edu.oldwestbury.vpajonas;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class EmptyDTile implements DTile {
    private DTileTypes type;
    private Bitmap sprite;
    private int x;
    private int y;
    private boolean isSolid;
    private Actor actor;
    private FloorItem item;
    private Rect shape;

    public EmptyDTile(Bitmap passedSprite, int passedX, int passedY){
        type = DTileTypes.T_EMPTY;
        sprite = passedSprite;
        x = passedX;
        y = passedY;
        shape = new Rect();

        isSolid = false;
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

    @Override
    public Rect getShape() {
        return shape;
    }

    @Override
    public void refreshShape(ScreenXYPositionFinder screenXYPositionFinder) {
        shape.left = (int) (((x + screenXYPositionFinder.viewportXOffsetTiles) * screenXYPositionFinder.tileW * screenXYPositionFinder.modifierW) - screenXYPositionFinder.viewPortX);
        shape.top =  (int) (((y + screenXYPositionFinder.viewportYOffsetTiles) * screenXYPositionFinder.tileH * screenXYPositionFinder.modifierH) - screenXYPositionFinder.viewPortY);
        shape.right = (int) (((x + 1 + screenXYPositionFinder.viewportXOffsetTiles) * screenXYPositionFinder.tileW * screenXYPositionFinder.modifierW) - screenXYPositionFinder.viewPortX);
        shape. bottom = (int) (((y + 1 + screenXYPositionFinder.viewportYOffsetTiles) * screenXYPositionFinder.tileH * screenXYPositionFinder.modifierH) - screenXYPositionFinder.viewPortY);

    }
}
