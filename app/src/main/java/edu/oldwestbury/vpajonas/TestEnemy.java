package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.Vector;

public class TestEnemy implements Enemy {
    private Bitmap sprite;
    private int tileX;
    private int tileY;
    private int xSpd;
    private int ySpd;
    private int range;
    private int rangeX;
    private int rangeY;
    private int rangeXtwo;
    private int rangeYtwo;
    private Player player;
    private Rect shape;
    private boolean willBecomePartner;

    private String tickText;
    private String errorText;

    private ScreenXYPositionFinder screenXYPosFin;


    private boolean isSolid;
    private DTile[][] tileSet;

    private ActTypes behavior;

    private boolean busy;
    private boolean touchable;

    private FloorItem droppedItem;

    private boolean inRange;

    private int sound;
    private int[] soundSet;

    private Actor target;

    private Stats stats;
    private EnemyInteractMenu enemyInteractMenu;
    private int interactMenuTimer;
    private boolean decInteractMenuTimer;
    private float reasonPercent;

    private SpriteIndex spriteIndex;

    public TestEnemy(Context context, DTile[][] tileSetPassed, Player passedPlayer,int startX, int startY){
        BitmapFactory bitmapFac = new BitmapFactory();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        sprite = bitmapFac.decodeResource(context.getResources(),R.drawable.vintagetv_spritesheet,options);
        tileSet = tileSetPassed;
        player = passedPlayer;

        tileX = startX;
        tileY = startY;
        tileSet[tileX][tileY].setActor(this);
        xSpd = 0;
        ySpd = 0;

        isSolid = true;
        touchable = true;
        busy = false;
        willBecomePartner = false;

        sound = 0;
        soundSet = new int[2];
        soundSet[0] = R.raw.hitsound;

        range = 4;
        rangeX = tileX - range;
        rangeY = tileY - range;
        rangeXtwo = tileX + range;
        rangeYtwo = tileY + range;

        while(rangeX < 0){
            rangeX++;
        }
        while(rangeY < 0){
            rangeY++;
        }
        while(rangeXtwo > tileSet.length-1){
            rangeXtwo--;
        }
        while(rangeYtwo > tileSet[0].length-1){
            rangeYtwo--;
        }

        screenXYPosFin = passedPlayer.getScreenXYPosFin();
        refreshShape();

        stats = new Stats();

        tickText = "";
        errorText = "";

        behavior = ActTypes.ACT_DONOTHING;

        droppedItem = new FloorItem(context,null,1,player,tileX,tileY);
        target = null;

        interactMenuTimer = 90;
        decInteractMenuTimer = false;

        reasonPercent = 100f;


        spriteIndex = SpriteIndex.IDLE_FRONT;
    }

    @Override
    public void update() {
        if(droppedItem != null) {
            droppedItem.update();
        }
        try {
            try {


                rangeX = tileX - range;
                rangeY = tileY - range;
                rangeXtwo = tileX + range;
                rangeYtwo = tileY + range;
                while(rangeX < 0){
                    rangeX++;
                }
                while(rangeY < 0){
                    rangeY++;
                }
                while(rangeXtwo > tileSet.length-1){
                    rangeXtwo--;
                }
                while(rangeYtwo > tileSet[0].length-1){
                    rangeYtwo--;
                }

                if(!busy) {
                    refreshShape();
                }
                if(decInteractMenuTimer){
                    interactMenuTimer--;
                }


                inRange = false;

                scanRangeForPlayer();


            }
            catch(Exception e){

                errorText += "TestEnemy.update() ERROR 1 -" + e.toString();
            }

            if (stats.getHP() <= 0) {
                tickText += "-Enemy Defeated!";
                behavior = ActTypes.ACT_DONOTHING;
                return;
            }

            if(canAttack()){
                return;
            }

            else {
                try {

                    if (inRange) {
                        canMove();

                    }
                }catch (Exception e){

                    errorText += "TestEnemy.update() ERROR 3 -" + e.toString();
                }
            }


        }catch (Exception e){
            errorText += "TestEnemy.update() ERROR 2 -" + e.toString();
        }

    }
    @Override
    public Actor act(){

        tickText = "";
        try {

            if (behavior == ActTypes.ACT_MOVE) {
                try {
                    tileSet[tileX][tileY].setActor(null);
                    tileX += xSpd;
                    tileY += ySpd;

                    tileSet[tileX][tileY].setActor(this);


                    rangeX = tileX - range;
                    rangeY = tileY - range;
                    rangeXtwo = tileX + range;
                    rangeYtwo = tileY + range;

                    while (rangeX < 0) {
                        rangeX++;
                    }
                    while (rangeY < 0) {
                        rangeY++;
                    }
                    while (rangeXtwo > tileSet.length - 1) {
                        rangeXtwo--;
                    }

                    while (rangeYtwo > tileSet[0].length - 1) {
                        rangeYtwo--;
                    }

                    xSpd = 0;
                    ySpd = 0;
                }catch (Exception e){

                    errorText += "TestEnemy.act() ERROR 2 -" + e.toString();
                }
            } else if (behavior == ActTypes.ACT_ATTACK) { //attacking player
                try {
                    if(target == player) {
                        player.getPlayerStats().setHP(player.getPlayerStats().getHP() - stats.getATK() + player.getPlayerStats().getDEF());
                        tickText += "-Enemy did " + stats.getATK() + " damage to the player!";
                    }
                    else if(target instanceof Partner){
                        ((Partner)target).getStats().setHP(((Partner)target).getStats().getHP() - stats.getATK() + ((Partner)target).getStats().getDEF());
                        tickText += "-Enemy did " + stats.getATK() + " damage to the partner!";
                    }
                }catch (Exception e){

                    errorText += "TestEnemy.act() ERROR 3 -" + e.toString();
                }
                xSpd = 0;
                ySpd = 0;
                return target;
            }

            refreshShape();
        }catch (Exception e){
            errorText += "TestEnemy.act() ERROR 1 -" + e.toString();
        }

        return null;
    }

    @Override
    public String getTickText() {
        return tickText;
    }

    @Override
    public ActTypes react() {
        tickText = "";
        if(willBecomePartner){
            return ActTypes.REACT_BECOMEPARTNER;
        }

        if (stats.getHP() <= 0){
            if(droppedItem != null){
                return ActTypes.REACT_DELETEMEDROPITEM;
            }
            return ActTypes.REACT_DELETEME;
        }


        return ActTypes.REACT_DONOTHING;
    }

    @Override
    public Bitmap getSprite(){
        return sprite;
    }
    @Override
    public void onTouch(View view, MotionEvent event){

        if(touchable) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                if (event.getX() >= shape.left &&
                        event.getX() <= shape.right &&
                        event.getY() >= shape.top &&
                        event.getY() <= shape.bottom) {

                    decInteractMenuTimer = true;
                }
            }


            if (event.getAction() == MotionEvent.ACTION_UP) { //player presses down
                if ((interactMenuTimer <= 0) && (tileSet[tileX+1][tileY].getActor() == player ||
                        tileSet[tileX-1][tileY].getActor() == player ||
                        tileSet[tileX][tileY+1].getActor() == player ||
                        tileSet[tileX][tileY-1].getActor() == player)){

                    enemyInteractMenu.getDialogueBox().setMessages(stats.getMessages());
                    enemyInteractMenu.setActive(7);
                    enemyInteractMenu.setEnemy(this);
                    interactMenuTimer = 90;

                    return;
                }
                else{

                    interactMenuTimer = 90;
                }

                if (player.getPlayerStats().getCurrentWeapon().inRange(player,this,tileSet)){
                    screenXYPosFin.setViewPortXYtiles(player);


                    if (event.getX() >= shape.left &&
                            event.getX() <= shape.right &&
                            event.getY() >= shape.top &&
                            event.getY() <= shape.bottom) {

                        player.attackEnemy(this);
                        sound = R.raw.bumpsound;
                    }
                }

            }
        }
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
    public Statsheet getStats(){
        return stats;
    }

    @Override
    public Rect getShape(){
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
    public FloorItem getDroppedItem() {
        return droppedItem;
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
        return behavior;
    }

    @Override
    public void setEnemyInteractMenu(EnemyInteractMenu enemyInteractMenuPassed){
        enemyInteractMenu = enemyInteractMenuPassed;
    }

    @Override
    public void refreshShape(){
        shape = new Rect((tileX + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles)* screenXYPosFin.tileW * screenXYPosFin.modifierW,
                (tileY + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles)* screenXYPosFin.tileH * screenXYPosFin.modifierH,
                (tileX+1+ screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles)* screenXYPosFin.tileW * screenXYPosFin.modifierW,
                (tileY+1+ screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles)* screenXYPosFin.tileH * screenXYPosFin.modifierH);
    }

    @Override
    public Rect getSpritePart() {
        if(spriteIndex == SpriteIndex.IDLE_FRONT){
            return new Rect(0,0,25,25);

        }
        else if(spriteIndex == SpriteIndex.IDLE_LEFT){
            return new Rect(25,0,50,25);

        }
        else if(spriteIndex == SpriteIndex.IDLE_BACK){
            return new Rect(50,0,75,25);

        }
        else if(spriteIndex == SpriteIndex.IDLE_RIGHT){
            return new Rect(75,0,100,25);

        }


        return new Rect(0,0,150,150);
    }

    private boolean scanRangeForPlayer(){
        try {
            for (int i = rangeX; i <= rangeXtwo; i++) { //scanning for player
                if (i < 0) {
                    i = 0;
                }
                for (int j = rangeY; j <= rangeYtwo; j++) {
                    if (j < 0) {
                        j = 0;
                    }
                    if (i == tileX && j == tileY) {
                        j++;
                    }
                    if (tileSet[i][j] != null) {
                        if (tileSet[i][j].getActor() == player) {
                            inRange = true;
                            return true;
                        } else if (tileSet[i][j].getActor() instanceof Partner) {
                            inRange = true;
                            return true;
                        }
                    }
                }
            }
        }catch (Exception e){
            errorText += "TestEnemy.scanRangeForPlayer() ERROR 1 -" + e.toString();
        }
            return false;

    }
    private boolean canAttack(){

        try {

            if ((tileSet[tileX + 1][tileY].getActor() == player || tileSet[tileX + 1][tileY].getActor() instanceof Partner) && inRange) {
                xSpd = 1;
                ySpd = 0;
                behavior = ActTypes.ACT_ATTACK;
                target = tileSet[tileX + 1][tileY].getActor();

                spriteIndex = SpriteIndex.IDLE_RIGHT;
                return true;
            } else if ((tileSet[tileX - 1][tileY].getActor() == player || tileSet[tileX - 1][tileY].getActor() instanceof Partner) && inRange) {
                xSpd = -1;
                ySpd = 0;
                behavior = ActTypes.ACT_ATTACK;
                target = tileSet[tileX - 1][tileY].getActor();

                spriteIndex = SpriteIndex.IDLE_LEFT;
                return true;
            } else if ((tileSet[tileX][tileY + 1].getActor() == player || tileSet[tileX][tileY + 1].getActor() instanceof Partner) && inRange) {
                xSpd = 0;
                ySpd = 1;
                behavior = ActTypes.ACT_ATTACK;
                target = tileSet[tileX][tileY + 1].getActor();

                spriteIndex = SpriteIndex.IDLE_FRONT;
                return true;
            } else if ((tileSet[tileX][tileY - 1].getActor() == player || tileSet[tileX][tileY - 1].getActor() instanceof Partner) && inRange) {

                xSpd = 0;
                ySpd = -1;
                behavior = ActTypes.ACT_ATTACK;
                target = tileSet[tileX][tileY - 1].getActor();

                spriteIndex = SpriteIndex.IDLE_BACK;
                return true;

            }
        }
        catch (Exception e){
            errorText += "TestEnemy.canAttack() ERROR 1 -" + e.toString();
        }
        return false;
    }

    public void setWillBecomePartner(boolean toSet){
        willBecomePartner = toSet;
    }

    @Override
    public float getReasonPercent() {
        return reasonPercent;
    }


    private boolean canMove(){
        int xDif = player.getTileX() - tileX;
        int yDif = player.getTileY() - tileY;

        if (Math.abs(xDif) >= Math.abs(yDif)) {
            if (xDif > 0) {
                xSpd = 1;
                ySpd = 0;

                spriteIndex = SpriteIndex.IDLE_RIGHT;

            } else {
                xSpd = -1;
                ySpd = 0;

                spriteIndex = SpriteIndex.IDLE_LEFT;

            }
            if ((tileSet[tileX + xSpd][tileY + ySpd].getActor() != null || tileSet[tileX + xSpd][tileY + ySpd].getIsSolid()) && yDif != 0) {
                if (yDif > 0) {
                    xSpd = 0;
                    ySpd = 1;


                    spriteIndex = SpriteIndex.IDLE_FRONT;

                } else {
                    xSpd = 0;
                    ySpd = -1;


                    spriteIndex = SpriteIndex.IDLE_BACK;
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
            if ((tileSet[tileX + xSpd][tileY + ySpd].getActor() != null || tileSet[tileX + xSpd][tileY + ySpd].getIsSolid()) && xDif != 0) {
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
        if (tileSet[tileX + xSpd][tileY + ySpd].getActor() != null || tileSet[tileX + xSpd][tileY + ySpd].getIsSolid()) {
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

        private Vector<String> messages;

        public Stats(){
            maxHP = 60;
            HP = maxHP;
            ATK = 50;
            DEF = 20;

            messages = new Vector<>();
            messages.add("Hey umm");
            messages.add("Theres this girl and uhh");
            messages.add(">0<");
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

        public Vector<String> getMessages(){
            return messages;
        }



    }
}
