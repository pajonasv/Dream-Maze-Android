package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class Player implements Actor {
    //for tile positioning
    private int tileX; //where you are
    private int tileY;
    private int xSpd; //how many tiles you move when gestured
    private int ySpd;



    //sound stuff
    private int sound;
    private int[] soundSet;

    //for onTouch
    private int timer;
    private float startingTouchX;
    private float startingTouchY;
    private float deadzone;

    //state booleans
    private boolean isSolid;
    private boolean tickEnabled;
    private boolean busy; //may be possible to get rid of this
    private boolean touchable;

    //Objects
    private DTile previousTile;
    private PlayerStats playerStats;
    private String tickText;
    private ActTypes behavior;
    private Actor target;
    private Rect shape;
    private String errorText;
    private ScreenXYPositionFinder screenXYPosFin;
    private Bitmap sprite;
    private DTile[][] tileSet;
    private Partner partner;

    //misc
    private int lastHP;//find a way to get rid of this

    private Context context;

    public Player(Context contextPassed, DTile[][] tileSetPassed,int startX, int startY){
        context = contextPassed;
        BitmapFactory bitmapFac = new BitmapFactory();
        sprite = bitmapFac.decodeResource(context.getResources(),R.drawable.redman);

        tileSet = tileSetPassed;
        tileX = startX;
        tileY = startY;
        xSpd = 0;
        ySpd = 0;
        tileSet[tileX][tileY].setActor(this);

        playerStats = new PlayerStats(context,this);

        startingTouchX = -1f;
        startingTouchY = -1f;
        deadzone = 200.0f;

        timer = 0;

        touchable = true;
        tickEnabled = false;
        isSolid = true;

        errorText = "";
        tickText = "";

        partner = null;

        screenXYPosFin = new ScreenXYPositionFinder(context,11,19);
        screenXYPosFin.setViewPortXYtiles(this);
        refreshShape();

        sound = 0;
        soundSet = new int[2];
        soundSet[0] = R.raw.hitsound;
        soundSet[1] = R.raw.bumpsound;

        lastHP = playerStats.getHP();

        behavior = ActTypes.ACT_DONOTHING;
    }

    @Override
    public void onTouch(View v, MotionEvent event) {

        if(touchable) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) { //player presses down
                startingTouchX = event.getX();
                startingTouchY = event.getY();

            }

            if (event.getAction() == MotionEvent.ACTION_MOVE) { //still pressing down but difference in xy

                if (event.getX() + deadzone < startingTouchX) {
                    xSpd = -1;
                    ySpd = 0;
                } else if (event.getX() - deadzone > startingTouchX) {
                    xSpd = 1;
                    ySpd = 0;
                } else if (event.getY() + deadzone < startingTouchY) {
                    ySpd = -1;
                    xSpd = 0;
                } else if (event.getY() - deadzone > startingTouchY) {
                    ySpd = 1;
                    xSpd = 0;
                }

                if (xSpd != 0 || ySpd != 0) {
                    timer++;
                }
            }
        }


            if (event.getAction() == MotionEvent.ACTION_UP) { //player takes finger off

                if (!busy) {
                    if (tileX + xSpd > -1 && tileY + ySpd > -1 && tileX + xSpd < tileSet.length && tileY + ySpd < tileSet[0].length) { //making sure in bounds
                        if (tileSet[tileX + xSpd][tileY + ySpd] != null) { //nullptr exception prevention
                            if (!tileSet[tileX + xSpd][tileY + ySpd].getIsSolid()) {

                                if (tileSet[tileX + xSpd][tileY + ySpd].getActor() != null) {

                                    if (!tileSet[tileX + xSpd][tileY + ySpd].getActor().getIsSolid()) {
                                        behavior = ActTypes.ACT_MOVE;
                                        tickEnabled = true;
                                    }

                                }
                                else {
                                    behavior = ActTypes.ACT_MOVE;
                                    tickEnabled = true;
                                }

                            }
                        }
                    }
                }


                timer = 0;

            }
    }

    @Override
    public void update() {
        lastHP = playerStats.getHP();

        try {


            if (!busy) {
                if (timer >= 15) {
                    if (tileX + xSpd > -1 && tileY + ySpd > -1 && tileX + xSpd < tileSet.length && tileY + ySpd < tileSet[0].length) { //making sure in bounds

                        if (tileSet[tileX + xSpd][tileY + ySpd] != null) { //nullptr exception prevention

                            if (!tileSet[tileX + xSpd][tileY + ySpd].getIsSolid()) {
                                if (tileSet[tileX + xSpd][tileY + ySpd].getActor() != null) {
                                    if (!tileSet[tileX + xSpd][tileY + ySpd].getActor().getIsSolid()) {
                                        behavior = ActTypes.ACT_KEEPMOVING;
                                        tickEnabled = true;
                                    }
                                } else {
                                    behavior = ActTypes.ACT_KEEPMOVING;
                                    tickEnabled = true;
                                }
                            }

                        }
                    }
                }
            }
        }catch (Exception e){
        errorText += "Player.update() ERROR 1 -" + e.toString();
    }


    }

    @Override
    public Actor act(){

        try {

            if (behavior == ActTypes.ACT_MOVE) {

                tileSet[tileX][tileY].setActor(null);
                previousTile = tileSet[tileX][tileY];
                tileX += xSpd;
                tileY += ySpd;
                //so that players speed is reset for next time, can cause problems if not


                switch (xSpd) {
                    case 1:
                        tickText = "- Moved right.";
                        break;
                    case -1:
                        tickText = "- Moved left.";
                        break;
                }

                switch (ySpd) {
                    case 1:
                        tickText = "- Moved down.";
                        break;
                    case -1:
                        tickText = "- Moved up.";
                        break;
                }
                xSpd = 0;
                ySpd = 0;

                tileSet[tileX][tileY].setActor(this);

            } else if (behavior == ActTypes.ACT_KEEPMOVING) {

                tileSet[tileX][tileY].setActor(null);
                previousTile = tileSet[tileX][tileY];
                tileX += xSpd;
                tileY += ySpd;

                switch (xSpd) {
                    case 1:
                        tickText = "- Moved right.";
                        break;
                    case -1:
                        tickText = "- Moved left.";
                        break;
                }

                switch (ySpd) {
                    case 1:
                        tickText = "- Moved down.";
                        break;
                    case -1:
                        tickText = "- Moved up.";
                        break;
                }

                tileSet[tileX][tileY].setActor(this);


            } else if (behavior == ActTypes.ACT_ATTACK) {
                Enemy enemy = (Enemy)target;
                int damage = playerStats.getATK() - enemy.getStats().getDEF();
                enemy.getStats().setHP(enemy.getStats().getHP()-damage);
                tickText = "Did " + damage + " damage to enemy!";
                xSpd = 0;
                ySpd = 0;
                return target;

            } else if(behavior == ActTypes.ACT_REASON){
                makePartnerFromEnemy((Enemy)target);
                ((Enemy)target).getStats().setHP(0);
                ((Enemy)target).setWillBecomePartner(true);
                return target;

            }
        }catch (Exception e){
            errorText += "Player.act() ERROR 1 -" + e.toString();
        }
        return null;

    }


    @Override
    public String getTickText(){
        return tickText;
    }

    @Override
    public ActTypes react() {
        tickText = "";
        if(playerStats.getHP() < lastHP){
            sound = R.raw.hitsound;
        }
        if (playerStats.getHP() <= 0){
            return ActTypes.REACT_DELETEME;
        }

        return ActTypes.REACT_DONOTHING;
    }
    @Override
    public Bitmap getSprite() {
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
    public boolean getIsSolid(){
        return isSolid;
    }

    @Override
    public void setTiles(DTile[][] toSet){
        tileSet = toSet;
    }
    @Override
    public Rect getShape(){
        return shape;
    }

    public boolean getTickEnabled(){
        if(tickEnabled == false) {
            return tickEnabled;
        }
        return true;
    }
    public void setTickEnabled(boolean b){
        tickEnabled = b;
    }

    public PlayerStats getPlayerStats(){
        return playerStats;
    }
    public void setTileX(int toSet){
        tileX = toSet;
    }

    public void setTileY(int toSet){
        tileY = toSet;
    }
    public void setTileSet(DTile tiles[][]){
        tileSet = tiles;
    }

    public DTile getPreviousTile(){
        return previousTile;
    }


    public void attackEnemy(TestEnemy enemy){

        behavior = ActTypes.ACT_ATTACK;
        target = enemy;

        if (target.getTileX() > tileX){
            xSpd = 1;
            ySpd = 0;
        }
        else if(target.getTileX() < tileX){
            xSpd = -1;
            ySpd = 0;
        }
        else if (target.getTileY() > tileY){
            xSpd = 0;
            ySpd = 1;
        }
        else {
            xSpd = 0;
            ySpd = -1;
        }

        tickEnabled = true;

    }

    public void talkTo(Actor actor, ActTypes actTypes){

        target = actor;
        behavior = actTypes;
        tickEnabled = true;
    }
    @Override
    public ActTypes getBehavior(){
        return behavior;
    }

    @Override
    public short getDirection() {
        short toReturn = 0;
        switch(xSpd){
            case 1:
                toReturn = 4;
                break;
            case -1:
                toReturn = 3;
                break;
        }
        switch(ySpd){
            case 1:
                toReturn = 2;
                break;
            case -1:
                toReturn = 1;
                break;
        }
        return toReturn;
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
    public ScreenXYPositionFinder getScreenXYPosFin(){
        return screenXYPosFin;
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

    public void setPartner(Partner toSet){
        partner = toSet;
    }
    public Partner getPartner(){
        return partner;
    }

    public int getXSpd(){
        return  xSpd;
    }
    public int getYSpd(){
        return  ySpd;
    }

    public void refreshShape(){
        shape = new Rect(((tileX + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles)* screenXYPosFin.tileW * screenXYPosFin.modifierW),
                ((tileY + screenXYPosFin.viewportYOffsetTiles  - screenXYPosFin.viewPortYtiles)* screenXYPosFin.tileH * screenXYPosFin.modifierH) ,
                ((tileX+1+ screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles)* screenXYPosFin.tileW * screenXYPosFin.modifierW),
                ((tileY+1+ screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles)* screenXYPosFin.tileH * screenXYPosFin.modifierH));

    }

    public void makePartnerFromEnemy(Enemy enemy){
        partner = new Partner(context,tileSet,this,this);
        partner.setSprite(enemy.getSprite());
        partner.setTileX(enemy.getTileX());
        partner.setTileY(enemy.getTileY());
        partner.getStats().setATK(enemy.getStats().getATK());
        partner.getStats().setDEF(enemy.getStats().getDEF());
        partner.getStats().setMaxHP(enemy.getStats().getMaxHP());
        partner.getStats().setHP(partner.getStats().getMaxHP());

    }
}
