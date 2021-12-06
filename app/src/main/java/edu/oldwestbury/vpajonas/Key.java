package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class Key implements ObtainableItem {
    private Bitmap sprite;
    private int tileX;
    private int tileY;
    private DTile[][] tileSet;
    private boolean isSolid;
    private String tickText;
    private ScreenXYPositionFinder screenXYPosFin;
    private Rect shape;
    private boolean busy;
    private boolean touchable;
    private String errorText;
    private int sound;
    private int[] soundSet;

    private boolean gotMe;

    public Key(Context context, DTile[][] tileSetPassed,Player passedPlayer, int startX, int startY){

        BitmapFactory bitmapFac = new BitmapFactory();
        sprite = bitmapFac.decodeResource(context.getResources(),R.drawable.key);
        tileX = startX;
        tileY = startY;
        tileSet = tileSetPassed;
        //tileSet[tileX][tileY].setItem(this);

        isSolid = false;

        tickText = "";
        screenXYPosFin = passedPlayer.getScreenXYPosFin();


        shape = new Rect((tileX + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles) * screenXYPosFin.tileW * screenXYPosFin.modifierW,
                (tileY + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles) * screenXYPosFin.tileH * screenXYPosFin.modifierH,
                (tileX + 1 + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles) * screenXYPosFin.tileW * screenXYPosFin.modifierW,
                (tileY + 1 + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles) * screenXYPosFin.tileH * screenXYPosFin.modifierH);

        touchable  = true;
        gotMe = false;
        errorText = "";

        sound = 0;
        soundSet = new int[1];
        soundSet[0] = R.raw.getitemsound;
    }

    @Override
    public void update(){
        try {
                refreshShape();

        }catch (Exception e){
        errorText += "Key.update() ERROR 1 -" + e.toString();
    }

    }
    @Override
    public Actor act(){

        tickText = "";
        try{
        if(tileSet[tileX][tileY].getActor() instanceof Player  ||  tileSet[tileX][tileY].getActor() instanceof Partner ){
            onGet(((Player) tileSet[tileX][tileY].getActor()).getPlayerStats());
            return this;

        }
        }catch (Exception e){
            errorText += "Key.act ERROR 1 -" + e.toString();
        }
        return null;
    }

    @Override
    public String getTickText() {
        return tickText;
    }

    @Override
    public ActTypes react() {
        if(tileSet[tileX][tileY].getActor() instanceof Player  ){
            tickText = "";
            return ActTypes.REACT_DELETEITEM;


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
    public void onGet(PlayerStats playerStats){
        if(!gotMe) {
            playerStats.incKeys();
            tickText = "-Got Key";
            gotMe = true;
            sound = R.raw.getitemsound;
        }

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
    public ActTypes getBehavior(){
        return ActTypes.ACT_DONOTHING;
    }

    @Override
    public void refreshShape() {
        shape = new Rect((tileX + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles) * screenXYPosFin.tileW * screenXYPosFin.modifierW,
                (tileY + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles) * screenXYPosFin.tileH * screenXYPosFin.modifierH,
                (tileX + 1 + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles) * screenXYPosFin.tileW * screenXYPosFin.modifierW,
                (tileY + 1 + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles) * screenXYPosFin.tileH * screenXYPosFin.modifierH);
    }

    @Override
    public Rect getSpritePart() {
        return null;
    }

    @Override
    public int[] getSoundSet(){
        return soundSet;
    }


}
