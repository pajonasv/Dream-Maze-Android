package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class FloorItem implements Actor {

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

    private boolean gotMe;

    private int amount; //used for money
    private int sound;
    private int[] soundSet;
    private HeldItem heldItem;
    private int keyOrMoney;

    public FloorItem(Context context, DTile[][] tileSetPassed, HeldItem heldItemPassed ,Player passedPlayer, int startX, int startY){

        keyOrMoney = -1;
        amount = 0;

        tileX = startX;
        tileY = startY;
        heldItem = heldItemPassed;

        BitmapFactory bitmapFac = new BitmapFactory();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        sprite = bitmapFac.decodeResource(context.getResources(),R.drawable.item_spritesheet,options);


        if(tileSetPassed != null) {
            tileSet = tileSetPassed;
            tileSet[tileX][tileY].setItem(this);
        }
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
    public FloorItem(Context context, DTile[][] tileSetPassed, int keyOrMoneyPassed,Player passedPlayer, int startX, int startY){

        keyOrMoney = keyOrMoneyPassed;

        tileX = startX;
        tileY = startY;

        BitmapFactory bitmapFac = new BitmapFactory();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        if(keyOrMoney == 0){
            sprite = bitmapFac.decodeResource(context.getResources(),R.drawable.key_spritesheet,options);
        }
        else if (keyOrMoney == 1){
            sprite = bitmapFac.decodeResource(context.getResources(),R.drawable.gold_spritesheet,options);
            amount = 3;
        }

        if(tileSetPassed != null) {
            tileSet = tileSetPassed;
            tileSet[tileX][tileY].setItem(this);
        }
        isSolid = false;

        tickText = "";
        screenXYPosFin = passedPlayer.getScreenXYPosFin();


        refreshShape();

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
            shape = new Rect((tileX + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles) * screenXYPosFin.tileW * screenXYPosFin.modifierW,
                    (tileY + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles) * screenXYPosFin.tileH * screenXYPosFin.modifierH,
                    (tileX + 1 + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles) * screenXYPosFin.tileW * screenXYPosFin.modifierW,
                    (tileY + 1 + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles) * screenXYPosFin.tileH * screenXYPosFin.modifierH);

        }catch (Exception e){
            errorText += "FloorItem.update() ERROR 1 -" + e.toString();
        }

    }
    @Override
    public Actor act(){

        tickText = "";
        try{
            if(tileSet[tileX][tileY].getActor() instanceof Player ||  tileSet[tileX][tileY].getActor() instanceof Partner){
                if (tileSet[tileX][tileY].getActor() instanceof Player) {
                    onGet(((Player) tileSet[tileX][tileY].getActor()).getPlayerStats());
                }
                else if(tileSet[tileX][tileY].getActor() instanceof Partner){
                    onGet(((Player)((Partner)tileSet[tileX][tileY].getActor()).getFollowing()).getPlayerStats());
                }
                return this;

            }
        }catch (Exception e){
            errorText += "FloorItem.act ERROR 1 -" + e.toString();
        }
        return null;
    }

    @Override
    public String getTickText() {
        return tickText;
    }

    @Override
    public ActTypes react() {
        if(tileSet[tileX][tileY].getActor() instanceof Player || tileSet[tileX][tileY].getActor() instanceof Partner  ){
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


    public void onGet(PlayerStats playerStats){
        if(!gotMe) {
            gotMe = true;
            sound = R.raw.getitemsound;

            if(keyOrMoney == -1){
                tickText = "-Got " + heldItem.getName();
                if(heldItem instanceof Key){
                    playerStats.incKeys();
                    gotMe = true;
                    return;
                }
                else if(heldItem instanceof HeldWeapon){
                    ((HeldWeapon)heldItem).Use();
                    return;
                }


                playerStats.addItem(heldItem);
            }
            else if(keyOrMoney == 0){

                playerStats.incKeys();
                tickText = "-Got a Key.";
            }
            else if(keyOrMoney == 1){
                playerStats.incMoney(amount);
                tickText = "-Got " + amount + " Money!";
            }




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
    public int[] getSoundSet(){
        return soundSet;
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
}
