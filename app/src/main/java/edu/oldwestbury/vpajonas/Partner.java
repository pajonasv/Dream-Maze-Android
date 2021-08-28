package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class Partner implements Actor {
    private Bitmap sprite;
    private DTile[][] tileSet;
    private Actor following;
    private Rect shape;
    private Player player;
    private int tileX;
    private int tileY;
    private int xSpd;
    private int ySpd;
    private boolean busy;
    private String errorText;
    private String tickText;
    private ActTypes behavior;

    private int sound;
    private int[] soundSet;
    private boolean isSolid;
    private boolean touchable;

    private Actor target;
    private Stats stats;

    private ScreenXYPositionFinder screenXYPosFin;

    public Partner(Context context, DTile[][] tileSetPassed, Player playerPassed ,Actor followingPassed){
        BitmapFactory bitmapFac = new BitmapFactory();
        sprite = bitmapFac.decodeResource(context.getResources(),R.drawable.enemy);

        following = followingPassed;
        player = playerPassed;

        tileSet = tileSetPassed;
        tileX = player.getTileX()+1;
        tileY = player.getTileY();
        xSpd = 0;
        ySpd = 0;
        tileSet[tileX][tileY].setActor(this);



        busy = false;
        isSolid = false;
        touchable = false;

        errorText = "";
        tickText = "";

        behavior = ActTypes.ACT_DONOTHING;

        screenXYPosFin = player.getScreenXYPosFin();
        refreshShape();

        soundSet = new int[0];
        sound = 0;

        target = null;

        stats = new Stats();
    }

    @Override
    public void update() {
        try {
            behavior = ActTypes.ACT_DONOTHING;
            if (stats.getHP() <= 0) {
                tickText += "-Partner was defeated!";
                return;
            }
            if (!busy){
                refreshShape();
            }
            if(player.getTileX() + player.getXSpd() == tileX && player.getTileY() + player.getYSpd() == tileY){
                behavior = ActTypes.ACT_MOVE;
                switch(player.getDirection()){
                    case 1:
                        xSpd = 0;
                        ySpd = 1;
                        break;
                    case 2:
                        xSpd = 0;
                        ySpd = -1;
                        break;
                    case 3:
                        xSpd = 1;
                        ySpd = 0;
                        break;
                    case 4:
                        xSpd = -1;
                        ySpd = 0;
                        break;
                }
                return;
            }

            if(canAttack()){
                return;
            }

            canMove();


        }catch (Exception e){
            errorText += "Partner.update() ERROR 1 -" + e.toString();
        }

    }

    @Override
    public Actor act() {

            tickText = "";

            if (behavior == ActTypes.ACT_MOVE) {
                try {
                    if(tileSet[tileX][tileY].getActor() != player) {
                        tileSet[tileX][tileY].setActor(null);
                    }
                    tileX += xSpd;
                    tileY += ySpd;

                    tileSet[tileX][tileY].setActor(this);

                    xSpd = 0;
                    ySpd = 0;


                } catch (Exception e) {

                    errorText += "Partner.act() ERROR 1 -" + e.toString();
                }
            }
            else if (behavior == ActTypes.ACT_ATTACK) {
                Enemy enemy = (Enemy)target;
                int damage = stats.getATK() - enemy.getStats().getDEF();
                enemy.getStats().setHP(enemy.getStats().getHP()-damage);
                tickText = "Partner did " + damage + " damage to enemy!";
                xSpd = 0;
                ySpd = 0;
                return target;
            }

            refreshShape();
        return null;
    }

    @Override
    public String getTickText() {
        return tickText;
    }

    @Override
    public ActTypes react() {
        tickText = "";



        if (stats.getHP() <= 0){
            player.setPartner(null);
            return ActTypes.REACT_DELETEME;
        }


        return ActTypes.REACT_DONOTHING;
    }

    @Override
    public Bitmap getSprite() {
        return sprite;
    }
    public void setSprite(Bitmap spritePassed){
        sprite = spritePassed;
    }

    @Override
    public void onTouch(View v, MotionEvent event) {

    }

    @Override
    public int getTileX() {
        return tileX;
    }

    @Override
    public int getTileY() {
        return tileY;
    }

    @Override
    public boolean getIsSolid() {
        return isSolid;
    }

    @Override
    public Rect getShape() {
        return shape;
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

    @Override
    public void setTileX(int toSet) {
        tileX = toSet;
    }

    @Override
    public void setTileY(int toSet) {
        tileY = toSet;
    }

    @Override
    public void setTiles(DTile[][] toSet) {
            tileSet = toSet;
    }

    @Override
    public String getErrorText() {
        return errorText;
    }

    @Override
    public int getSound() {
        int temp = sound;
        sound = 0;
        return temp;
    }

    @Override
    public int[] getSoundSet() {
        return soundSet;
    }

    @Override
    public ActTypes getBehavior() {
        return behavior;
    }

    public Actor getFollowing(){
        return following;
    }

    public Stats getStats(){
        return stats;
    }

    private void refreshShape(){
        shape = new Rect((tileX + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles) * screenXYPosFin.tileW * screenXYPosFin.modifierW,
                (tileY + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles) * screenXYPosFin.tileH * screenXYPosFin.modifierH,
                (tileX + 1 + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles) * screenXYPosFin.tileW * screenXYPosFin.modifierW,
                (tileY + 1 + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles) * screenXYPosFin.tileH * screenXYPosFin.modifierH);

    }
    private boolean canAttack(){

        if (tileSet[tileX + 1][tileY].getActor() instanceof Enemy) {
            xSpd = 1;
            ySpd = 0;
            behavior = ActTypes.ACT_ATTACK;
            target = tileSet[tileX + 1][tileY].getActor();
            return true;
        } else if (tileSet[tileX - 1][tileY].getActor() instanceof Enemy) {
            xSpd = -1;
            ySpd = 0;
            behavior = ActTypes.ACT_ATTACK;
            target = tileSet[tileX - 1][tileY].getActor();
            return true;
        } else if (tileSet[tileX][tileY + 1].getActor() instanceof Enemy) {
            xSpd = 0;
            ySpd = 1;
            behavior = ActTypes.ACT_ATTACK;
            target = tileSet[tileX][tileY + 1].getActor();
            return true;
        } else if (tileSet[tileX][tileY - 1].getActor() instanceof Enemy) {
            xSpd = 0;
            ySpd = -1;
            behavior = ActTypes.ACT_ATTACK;
            target = tileSet[tileX][tileY - 1].getActor();
            return true;

        }
        return false;
    }

    private boolean canMove(){
        int xDif = following.getTileX() - tileX;
        int yDif = following.getTileY() - tileY;

        if (Math.abs(xDif) >= Math.abs(yDif)) {
            if (xDif > 0) {
                xSpd = 1;
                ySpd = 0;

            } else {
                xSpd = -1;
                ySpd = 0;

            }

            if ((((tileSet[tileX + xSpd][tileY + ySpd].getActor() != null) && (tileSet[tileX + xSpd][tileY + ySpd].getActor() != following)) || tileSet[tileX + xSpd][tileY + ySpd].getIsSolid()) && yDif != 0) {
                if (yDif > 0) {
                    xSpd = 0;
                    ySpd = 1;

                } else {
                    xSpd = 0;
                    ySpd = -1;
                }
            }
        } else if (Math.abs(xDif) < Math.abs(yDif)) {
            if (yDif > 0) {
                xSpd = 0;
                ySpd = 1;

            } else {
                xSpd = 0;
                ySpd = -1;
            }
            if ((((tileSet[tileX + xSpd][tileY + ySpd].getActor() != null) && (tileSet[tileX + xSpd][tileY + ySpd].getActor() != following)) || tileSet[tileX + xSpd][tileY + ySpd].getIsSolid()) && xDif != 0) {
                if (xDif > 0) {
                    xSpd = 1;
                    ySpd = 0;

                } else {
                    xSpd = -1;
                    ySpd = 0;

                }
            }
        }
        behavior = ActTypes.ACT_MOVE;

        if (((tileSet[tileX + xSpd][tileY + ySpd].getActor() != null) && (tileSet[tileX + xSpd][tileY + ySpd].getActor() != following)) || tileSet[tileX + xSpd][tileY + ySpd].getIsSolid()
                || tileSet[tileX + xSpd][tileY + ySpd] == tileSet[player.getTileX() + player.getXSpd()][player.getTileY() + player.getYSpd()]) {
            behavior = ActTypes.ACT_DONOTHING;
            return false;

        }
        return true;
    }

    public class Stats implements Statsheet{
        private int maxHP;
        private int HP;
        private int ATK;
        private int DEF;

        public Stats(){
            maxHP = 10;
            HP = maxHP;
            ATK = 2;
            DEF = 1;
        }
        @Override
        public int getHP(){
            return HP;
        }
        @Override
        public int getMaxHP(){
            return maxHP;
        }

        public void setMaxHP(int toSet){
            maxHP = toSet;
        }

        @Override
        public void setHP(int toSet){
            if(toSet <= maxHP && toSet >= 0) {
                HP = toSet;
            }
            else if(toSet < 0){
                HP = 0;
            }
            else{
                HP = maxHP;
            }
        }

        @Override
        public int getATK(){
            return ATK;
        }

        @Override
        public void setATK(int toSet){
            if(toSet >= 0){
                ATK = toSet;
            }
            else{
                ATK = 0;
            }
        }

        @Override
        public int getDEF(){
            return DEF;
        }

        @Override
        public void setDEF(int toSet){
            if(toSet >= 0){
                DEF = toSet;
            }
            else{
                DEF = 0;
            }
        }



    }
}
