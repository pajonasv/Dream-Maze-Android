package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class LockedDoor implements Actor {
    private Bitmap sprite;
    private int tileX;
    private int tileY;
    private boolean isSolid;
    private DTile[][] tileSet;
    private Player player;
    private Rect shape;
    private ScreenXYPositionFinder screenXYPosFin;
    private boolean busy;
    private boolean touchable;
    private String errorText;

    private int sound;
    private int[] soundSet;

    private String tickText;
    public LockedDoor(Context context, DTile[][] tileSetPassed,Player passedPlayer, int startX, int startY) {

        BitmapFactory bitmapFac = new BitmapFactory();
        sprite = bitmapFac.decodeResource(context.getResources(), R.drawable.locked_door_tile);
        tileX = startX;
        tileY = startY;
        tileSet = tileSetPassed;
        tileSet[tileX][tileY].setActor(this);

        isSolid = true;

        tickText = "";
        screenXYPosFin = passedPlayer.getScreenXYPosFin();



        shape = new Rect((tileX + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles)* screenXYPosFin.tileW * screenXYPosFin.modifierW,
                (tileY + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles)* screenXYPosFin.tileH * screenXYPosFin.modifierH,
                (tileX+1+ screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles)* screenXYPosFin.tileW * screenXYPosFin.modifierW,
                (tileY+1+ screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles)* screenXYPosFin.tileH * screenXYPosFin.modifierH);

        touchable = true;
        sound = 0;
        soundSet = new int[1];

        errorText = "";
    }
    @Override
    public void update(){

        shape = new Rect((tileX + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles)* screenXYPosFin.tileW * screenXYPosFin.modifierW,
                (tileY + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles)* screenXYPosFin.tileH * screenXYPosFin.modifierH,
                (tileX+1+ screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles)* screenXYPosFin.tileW * screenXYPosFin.modifierW,
                (tileY+1+ screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles)* screenXYPosFin.tileH * screenXYPosFin.modifierH);


    }
    @Override
    public Actor act(){
        tickText = "";
        if(tileSet[tileX][tileY].getActor() == this) {
            if (tileSet[tileX + 1][tileY].getActor() instanceof Player) {
                if (((Player) tileSet[tileX + 1][tileY].getActor()).getPlayerStats().getKeys() > 0) {
                    isSolid = false;
                }

                player = (Player) tileSet[tileX + 1][tileY].getActor();
            } else if (tileSet[tileX - 1][tileY].getActor() instanceof Player) {
                if (((Player) tileSet[tileX - 1][tileY].getActor()).getPlayerStats().getKeys() > 0) {
                    isSolid = false;
                }

                player = (Player) tileSet[tileX - 1][tileY].getActor();
            } else if (tileSet[tileX][tileY + 1].getActor() instanceof Player) {
                if (((Player) tileSet[tileX][tileY + 1].getActor()).getPlayerStats().getKeys() > 0) {
                    isSolid = false;
                }

                player = (Player) tileSet[tileX][tileY + 1].getActor();
            } else if (tileSet[tileX][tileY - 1].getActor() instanceof Player) {
                if (((Player) tileSet[tileX][tileY - 1].getActor()).getPlayerStats().getKeys() > 0) {
                    isSolid = false;
                }

                player = (Player) tileSet[tileX][tileY - 1].getActor();
            }
        }
        else if (player.getTileX() == tileX && player.getTileY() == tileY) {
            return this;
        }

        return null;
    }

    @Override
    public String getTickText() {
        return tickText;
    }

    @Override
    public ActTypes react() {
        if(player != null) {
            if (player.getTileX() == tileX && player.getTileY() == tileY) {
                tileSet[tileX][tileY].setActor(player);
                player.getPlayerStats().decKeys();

                tickText = "- Key used.";
                return ActTypes.REACT_DELETEMEACTORS;
            }
        }

        return ActTypes.REACT_DONOTHING;
    }

    @Override
    public void onTouch(View view, MotionEvent event){

    }
    @Override
    public Bitmap getSprite(){
        return sprite;
    }
    @Override
    public int getTileX(){
        return tileX;
    }

    @Override
    public int getTileY(){
        return tileY;
    }

    @Override
    public void setTileX(int toSet){
        tileX = toSet;
    }
    @Override
    public void setTileY(int toSet){
        tileY = toSet;
    }

    @Override
    public boolean getIsSolid(){
        return isSolid;
    }

    @Override
    public Rect getShape(){
        return shape;
    }
    @Override
    public short getDirection() {
        return 0;
    }
    @Override
    public void setBusy(boolean b) {
        busy = b;
    }
    @Override
    public void setTouchable(boolean b) {
        touchable = b;
    }

    @Override
    public boolean getTouchable() {
        return touchable;
    }
    @Override
    public void setTiles(DTile[][] toSet){
        tileSet = toSet;
    }
    @Override
    public String getErrorText(){
        String temp = errorText;
        errorText = "";
        return temp;
    }

    @Override
    public int getSound(){
        int temp = sound;
        sound = 0;
        return temp;
    }

    @Override
    public int[] getSoundSet(){
        return soundSet;
    }

    @Override
    public ActTypes getBehavior(){
        return ActTypes.ACT_DONOTHING;
    }

    @Override
    public void refreshShape() {
        shape = new Rect((tileX + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles)* screenXYPosFin.tileW * screenXYPosFin.modifierW,
                (tileY + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles)* screenXYPosFin.tileH * screenXYPosFin.modifierH,
                (tileX+1+ screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles)* screenXYPosFin.tileW * screenXYPosFin.modifierW,
                (tileY+1+ screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles)* screenXYPosFin.tileH * screenXYPosFin.modifierH);

    }

    @Override
    public Rect getSpritePart() {
        return null;
    }


}
