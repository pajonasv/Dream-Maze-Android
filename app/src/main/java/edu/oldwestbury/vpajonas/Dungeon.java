package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Vector;

public class Dungeon {
    private Floor floor;
    private String tickText;
    private int index;
    private Actor current;
    private boolean busy;
    private TickHandler tickHandler;
    private String errorText;
    private Vector<Integer> sounds;
    private boolean letPartnerActEarly;

    private SoundBSHandler soundBSHandler;

    private int music;


    public Dungeon(Context context){
        floor = new Floor(context);

        BitmapFactory bitmapFactory = new BitmapFactory();

        tickText = "";
        index = 0;
        busy = false;

        getCurrentFloor().getScreenXYPositionFinder().setViewPortXYtiles(getPlayer()); //update so that Actors all align properly
        getCurrentFloor().getScreenXYPositionFinder().setViewPortXY(getPlayer());

        tickHandler = new TickHandler(getPlayer(),floor.getActors());

        music = R.raw.temporaltest;

        errorText = "";
        sounds = new Vector<Integer>();

        letPartnerActEarly = false;

        soundBSHandler = new SoundBSHandler(context,music);
        try {
            for (int i = 0; i < getCurrentFloor().getActors().size(); i++) {
                for (int j = 0; j < getCurrentFloor().getActors().elementAt(i).getSoundSet().length; j++) {
                    if (getCurrentFloor().getActors().elementAt(i).getSoundSet()[j] != 0) {
                        soundBSHandler.loadSound(getCurrentFloor().getActors().elementAt(i).getSoundSet()[j]);

                    }

                }
                if (getCurrentFloor().getActors().elementAt(i) instanceof Enemy) {
                    Enemy tempEnemy = ((Enemy)(getCurrentFloor().getActors().elementAt(i)));

                    if (tempEnemy.getDroppedItem() != null) {
                        for (int k = 0; k < tempEnemy.getDroppedItem().getSoundSet().length; k++) {
                            if (tempEnemy.getDroppedItem().getSoundSet()[k] != 0){
                                soundBSHandler.loadSound(tempEnemy.getDroppedItem().getSoundSet()[k]);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){

            errorText += "Dungeon.constructor() soundBSHandler ERROR 1-" + e.toString();
        }
    }

    public Player getPlayer(){
        return getCurrentFloor().getPlayer();
    }

    public Floor getCurrentFloor(){
        return floor;
    }


    public String getTickText(){
        return tickText;
    }

    public void run(){
        try {
            if (!soundBSHandler.isPaused()) {
                soundBSHandler.playMusic(true);
            }


            if (index == 0) {
                errorText = "";
            }
            errorText += floor.getErrorText();

            current = getCurrentFloor().getActors().elementAt(index);
            if (current instanceof Partner && letPartnerActEarly) {
                index++;
                letPartnerActEarly = false;
                if (index >= getCurrentFloor().getActors().size()) {


                    if (tickHandler.getTickEnabled()) {

                        getPlayer().setTickEnabled(false);
                        tickHandler.setTickEnabled(false);
                        tickHandler.setActorsBusy(false);
                        tickHandler.setActorsTouchable(true);
                    }
                    index = 0;
                    return;

                } else {
                    run();
                    return;
                }
            }

            Actor reactor;
            if (!busy) {

                current.update();

                if (index == 0 && getCurrentFloor().getActors().elementAt(index) instanceof Player) { //PLAYER IS ALWAYS AT 0
                    tickHandler.setTickEnabled(((Player) current).getTickEnabled());

                }
            }

            if (tickHandler.getTickEnabled()) {
                if (index == 0 && getCurrentFloor().getActors().elementAt(index) instanceof Player) {
                    if (((Player) current).getPartner() != null) {
                        if (((Player) current).getPartner().getBehavior() == ActTypes.ACT_MOVE || ((Player) current).getPartner().getBehavior() == ActTypes.ACT_KEEPMOVING) {
                            letPartnerActEarly = true;
                            if (!busy) {
                                ((Player) current).getPartner().update();

                            }
                        }
                    }

                    tickHandler.setActorsBusy(true);
                    tickHandler.setActorsTouchable(false);
                    tickHandler.setFocus(current);


                    if (!handleAnimationPlayer(current, letPartnerActEarly)) {

                        busy = true;
                        return;
                    } else {
                        busy = false;
                    }
                } else if (actorIsOnScreen(current)) {

                    if (!handleAnimation(current)) {
                        busy = true;
                        return;
                    } else {
                        busy = false;
                    }
                }


                if (index == 0) {
                    tickText = "";
                }


                reactor = current.act();


                if (letPartnerActEarly && current instanceof Player) {

                    getCurrentFloor().getScreenXYPositionFinder().setViewPortXYtiles(getPlayer()); //update so that Actors all align properly
                    getCurrentFloor().getScreenXYPositionFinder().setViewPortXY(getPlayer());
                    ((Player) current).getPartner().act();
                }


                tickText += getCurrentFloor().getActors().elementAt(index).getTickText();
                tickText += "^";
                handleReactions(reactor);

                getCurrentFloor().getScreenXYPositionFinder().setViewPortXYtiles(getPlayer()); //update so that Actors all align properly
                getCurrentFloor().getScreenXYPositionFinder().setViewPortXY(getPlayer());

                try {
                    soundBSHandler.playSound(current.getSound(), 0);
                    if (reactor != null) {
                        soundBSHandler.playSound(reactor.getSound(), 0);
                    }
                } catch (Exception e) {
                    errorText += "dungeon.run() ERROR 1-" + e.toString();
                }
            }


            String tempErrorText = current.getErrorText();
            if (tempErrorText != "") {
                errorText += tempErrorText;
                errorText += "^";
            }


            index++;

            if (getCurrentFloor().getTiles()[getPlayer().getTileX()][getPlayer().getTileY()] instanceof DownstairsDTile) {
                getCurrentFloor().newFloor();
                index = getCurrentFloor().getActors().size() - 1;
                return;
            }


            if (index >= getCurrentFloor().getActors().size()) {
                if (tickHandler.getTickEnabled()) {

                    getPlayer().setTickEnabled(false);
                    tickHandler.setTickEnabled(false);
                    tickHandler.setActorsBusy(false);
                    tickHandler.setActorsTouchable(true);
                }
                index = 0;

            } else {
                run();
            }

        }
        catch (Exception e){
            errorText += "dungeon.run() ERROR 2-" + e.toString();
        }

    }
    private boolean handleAnimationPlayer(Actor actor, boolean animatePartner) {

        if(animatePartner){
            if(((Player)actor).getPartner() != null) {
                handleAnimationPartner(((Player) actor).getPartner());
            }
        }

        ScreenXYPositionFinder screenXYPosFin = getCurrentFloor().getScreenXYPositionFinder();
        short speed = (short)(screenXYPosFin.tileW/5);
        short direction = actor.getDirection();
        if(actor.getBehavior() == ActTypes.ACT_MOVE || actor.getBehavior() == ActTypes.ACT_KEEPMOVING) {
            if (direction == 0) {
                return true;
            } else if (direction == 1) {
                screenXYPosFin.viewPortY -= speed;
                for (int i = 1; i < getCurrentFloor().getActors().size(); i++) {
                    getCurrentFloor().getActors().elementAt(i).getShape().top += speed;
                    getCurrentFloor().getActors().elementAt(i).getShape().bottom += speed;
                }

                if (screenXYPosFin.viewPortY <= (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewportHtiles / 2) * screenXYPosFin.tileH * screenXYPosFin.modifierH) {

                    screenXYPosFin.viewPortY = (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewportHtiles / 2) * screenXYPosFin.tileH * screenXYPosFin.modifierH;
                    return true;
                }
            } else if (direction == 2) {
                screenXYPosFin.viewPortY += speed;
                for (int i = 1; i < getCurrentFloor().getActors().size(); i++) {
                    getCurrentFloor().getActors().elementAt(i).getShape().top -= speed;
                    getCurrentFloor().getActors().elementAt(i).getShape().bottom -= speed;
                }

                if (screenXYPosFin.viewPortY >= (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewportHtiles / 2 + 2) * screenXYPosFin.tileH * screenXYPosFin.modifierH) {

                    screenXYPosFin.viewPortY = (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewportHtiles / 2 + 2) * screenXYPosFin.tileH * screenXYPosFin.modifierH;
                    return true;
                }
            } else if (direction == 3) {
                screenXYPosFin.viewPortX -= speed;
                for (int i = 1; i < getCurrentFloor().getActors().size(); i++) {
                    getCurrentFloor().getActors().elementAt(i).getShape().left += speed;
                    getCurrentFloor().getActors().elementAt(i).getShape().right += speed;
                }

                if (screenXYPosFin.viewPortX <= (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewportWtiles / 2 - 1) * screenXYPosFin.tileW * screenXYPosFin.modifierW) {

                    screenXYPosFin.viewPortX = (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewportWtiles / 2 - 1) * screenXYPosFin.tileW * screenXYPosFin.modifierW;
                    return true;
                }
            } else if (direction == 4) {
                screenXYPosFin.viewPortX += speed;
                for (int i = 1; i < getCurrentFloor().getActors().size(); i++) {
                    getCurrentFloor().getActors().elementAt(i).getShape().left -= speed;
                    getCurrentFloor().getActors().elementAt(i).getShape().right -= speed;
                }

                if (screenXYPosFin.viewPortX >= (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewportWtiles / 2 + 1) * screenXYPosFin.tileW * screenXYPosFin.modifierW) {
                    screenXYPosFin.viewPortX = (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewportWtiles / 2 + 1) * screenXYPosFin.tileW * screenXYPosFin.modifierW;
                    return true;
                }
            }
            return false;
        }
        else if(actor.getBehavior() == ActTypes.ACT_ATTACK){

            if (direction == 0) {
                return true;
            } else if (direction == 1) {
                actor.getShape().top -= speed;
                actor.getShape().bottom -= speed;

                if (actor.getShape().top <= (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles - 0.5) * screenXYPosFin.tileH * screenXYPosFin.modifierH) {

                    actor.getShape().top = (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles) * screenXYPosFin.tileH * screenXYPosFin.modifierH;
                    actor.getShape().bottom = (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles + 1) * screenXYPosFin.tileH * screenXYPosFin.modifierH;
                    return true;
                }
            } else if (direction == 2) {
                actor.getShape().top += speed;
                actor.getShape().bottom += speed;

                if (actor.getShape().top >= (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles + 0.5) * screenXYPosFin.tileH * screenXYPosFin.modifierH) {

                    actor.getShape().top = (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles) * screenXYPosFin.tileH * screenXYPosFin.modifierH;
                    actor.getShape().bottom = (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles + 1) * screenXYPosFin.tileH * screenXYPosFin.modifierH;
                    return true;
                }
            } else if (direction == 3) {
                actor.getShape().left -= speed;
                actor.getShape().right -= speed;

                if (actor.getShape().left <= (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles - 0.5) * screenXYPosFin.tileW * screenXYPosFin.modifierW) {

                    actor.getShape().left = (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles) * screenXYPosFin.tileW * screenXYPosFin.modifierW;
                    actor.getShape().right = (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles + 1) * screenXYPosFin.tileW * screenXYPosFin.modifierW;
                    return true;
                }
            } else if (direction == 4) {
                actor.getShape().left += speed;
                actor.getShape().right += speed;

                if (actor.getShape().left >= (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles + 0.5) * screenXYPosFin.tileW * screenXYPosFin.modifierW) {
                    actor.getShape().left = (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles) * screenXYPosFin.tileW * screenXYPosFin.modifierW;
                    actor.getShape().right = (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles + 1) * screenXYPosFin.tileW * screenXYPosFin.modifierW;
                    return true;
                }
            }


            return false;
        }


        return true;
    }


    private boolean handleAnimationPartner(Actor actor) {

        if(actor.getBehavior() == ActTypes.ACT_DONOTHING){
            return true;
        }

        ScreenXYPositionFinder screenXYPosFin = getCurrentFloor().getScreenXYPositionFinder();
        short speed = (short)(screenXYPosFin.tileW/5);
        short direction = actor.getDirection();

        if(actor.getBehavior() == ActTypes.ACT_MOVE || actor.getBehavior() == ActTypes.ACT_KEEPMOVING) {
            if (direction == 0) {
                return true;
            } else if (direction == 1) {
                actor.getShape().top -= speed;
                actor.getShape().bottom -= speed;

                if (actor.getShape().top <= (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles - 1) * screenXYPosFin.tileH * screenXYPosFin.modifierH) {

                   return true;
                }
            } else if (direction == 2) {
                actor.getShape().top += speed;
                actor.getShape().bottom += speed;

                if (actor.getShape().top >= (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles + 1) * screenXYPosFin.tileH * screenXYPosFin.modifierH) {

                    return true;
                }
            } else if (direction == 3) {
                actor.getShape().left -= speed;
                actor.getShape().right -= speed;

                if (actor.getShape().left <= (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles - 1) * screenXYPosFin.tileW * screenXYPosFin.modifierW) {

                    return true;
                }
            } else if (direction == 4) {
                actor.getShape().left += speed;
                actor.getShape().right += speed;

                if (actor.getShape().left >= (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles + 1) * screenXYPosFin.tileW * screenXYPosFin.modifierW) {
                     return true;
                }
            }


            return false;
        }


        return true;

    }

    private boolean handleAnimation(Actor actor) {

        if(actor.getBehavior() == ActTypes.ACT_DONOTHING){
            return true;
        }

        ScreenXYPositionFinder screenXYPosFin = getCurrentFloor().getScreenXYPositionFinder();
        short speed = (short)(screenXYPosFin.tileW/5);
        short direction = actor.getDirection();

        if(actor.getBehavior() == ActTypes.ACT_MOVE || actor.getBehavior() == ActTypes.ACT_KEEPMOVING) {
            if (direction == 0) {
                return true;
            } else if (direction == 1) {
                actor.getShape().top -= speed;
                actor.getShape().bottom -= speed;

                if (actor.getShape().top <= (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles - 1) * screenXYPosFin.tileH * screenXYPosFin.modifierH) {

                    actor.getShape().top = (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles) * screenXYPosFin.tileH * screenXYPosFin.modifierH;
                    actor.getShape().bottom = (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles + 1) * screenXYPosFin.tileH * screenXYPosFin.modifierH;
                    return true;
                }
            } else if (direction == 2) {
                actor.getShape().top += speed;
                actor.getShape().bottom += speed;

                if (actor.getShape().top >= (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles + 1) * screenXYPosFin.tileH * screenXYPosFin.modifierH) {

                    actor.getShape().top = (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles) * screenXYPosFin.tileH * screenXYPosFin.modifierH;
                    actor.getShape().bottom = (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles + 1) * screenXYPosFin.tileH * screenXYPosFin.modifierH;
                    return true;
                }
            } else if (direction == 3) {
                actor.getShape().left -= speed;
                actor.getShape().right -= speed;

                if (actor.getShape().left <= (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles - 1) * screenXYPosFin.tileW * screenXYPosFin.modifierW) {

                    actor.getShape().left = (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles) * screenXYPosFin.tileW * screenXYPosFin.modifierW;
                    actor.getShape().right = (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles + 1) * screenXYPosFin.tileW * screenXYPosFin.modifierW;
                    return true;
                }
            } else if (direction == 4) {
                actor.getShape().left += speed;
                actor.getShape().right += speed;

                if (actor.getShape().left >= (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles + 1) * screenXYPosFin.tileW * screenXYPosFin.modifierW) {
                    actor.getShape().left = (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles) * screenXYPosFin.tileW * screenXYPosFin.modifierW;
                    actor.getShape().right = (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles + 1) * screenXYPosFin.tileW * screenXYPosFin.modifierW;
                    return true;
                }
            }


            return false;
        }
        else if(actor.getBehavior() == ActTypes.ACT_ATTACK){

            if (direction == 0) {
                return true;
            } else if (direction == 1) {
                actor.getShape().top -= speed;
                actor.getShape().bottom -= speed;

                if (actor.getShape().top <= (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles - 0.5) * screenXYPosFin.tileH * screenXYPosFin.modifierH) {

                    actor.getShape().top = (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles) * screenXYPosFin.tileH * screenXYPosFin.modifierH;
                    actor.getShape().bottom = (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles + 1) * screenXYPosFin.tileH * screenXYPosFin.modifierH;
                    return true;
                }
            } else if (direction == 2) {
                actor.getShape().top += speed;
                actor.getShape().bottom += speed;

                if (actor.getShape().top >= (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles + 0.5) * screenXYPosFin.tileH * screenXYPosFin.modifierH) {

                    actor.getShape().top = (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles) * screenXYPosFin.tileH * screenXYPosFin.modifierH;
                    actor.getShape().bottom = (actor.getTileY() + screenXYPosFin.viewportYOffsetTiles - screenXYPosFin.viewPortYtiles + 1) * screenXYPosFin.tileH * screenXYPosFin.modifierH;
                    return true;
                }
            } else if (direction == 3) {
                actor.getShape().left -= speed;
                actor.getShape().right -= speed;

                if (actor.getShape().left <= (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles - 0.5) * screenXYPosFin.tileW * screenXYPosFin.modifierW) {

                    actor.getShape().left = (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles) * screenXYPosFin.tileW * screenXYPosFin.modifierW;
                    actor.getShape().right = (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles + 1) * screenXYPosFin.tileW * screenXYPosFin.modifierW;
                    return true;
                }
            } else if (direction == 4) {
                actor.getShape().left += speed;
                actor.getShape().right += speed;

                if (actor.getShape().left >= (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles + 0.5) * screenXYPosFin.tileW * screenXYPosFin.modifierW) {
                    actor.getShape().left = (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles) * screenXYPosFin.tileW * screenXYPosFin.modifierW;
                    actor.getShape().right = (actor.getTileX() + screenXYPosFin.viewportXOffsetTiles - screenXYPosFin.viewPortXtiles + 1) * screenXYPosFin.tileW * screenXYPosFin.modifierW;
                    return true;
                }
            }


            return false;
        }


        return true;

    }

    private void handleReactions(Actor reactor){
        if(reactor == null){
            return;
        }

            ActTypes reaction = reactor.react();
            tickText += reactor.getTickText();
            if (reactor.getTickText() != "") {
                tickText += "^";
            }

            if(reaction == ActTypes.REACT_DELETEME){
                getCurrentFloor().getTiles()[reactor.getTileX()][reactor.getTileY()].setActor(null);

                for (int j = 0; j < getCurrentFloor().getActors().size(); j++) {
                    if(getCurrentFloor().getActors().elementAt(j) == reactor){

                        getCurrentFloor().getActors().remove(j);
                        break;
                    }
                }

            }
        else if(reaction == ActTypes.REACT_DELETEMEDROPITEM){

            if(reactor instanceof Enemy){

                getCurrentFloor().getActors().add(((Enemy) reactor).getDroppedItem());
                ((Enemy) reactor).getDroppedItem().setTileX(reactor.getTileX());
                ((Enemy) reactor).getDroppedItem().setTileY(reactor.getTileY());
                getCurrentFloor().getTiles()[reactor.getTileX()][reactor.getTileY()].setItem(((Enemy) reactor).getDroppedItem());
                ((Enemy) reactor).getDroppedItem().setTiles(getCurrentFloor().getTiles());
            }

            getCurrentFloor().getTiles()[reactor.getTileX()][reactor.getTileY()].setActor(null);

            for (int j = 0; j < getCurrentFloor().getActors().size(); j++) {
                if(getCurrentFloor().getActors().elementAt(j) == reactor){

                    getCurrentFloor().getActors().remove(j);
                    break;
                }
            }

        }
            else if(reaction == ActTypes.REACT_DELETEMEACTORS){
                for (int j = 0; j < getCurrentFloor().getActors().size(); j++) {
                    if(getCurrentFloor().getActors().elementAt(j) == reactor){
                        getCurrentFloor().getActors().remove(j);
                        break;
                    }
                }
            }
            else if(reaction == ActTypes.REACT_DELETEITEM){
                getCurrentFloor().getTiles()[reactor.getTileX()][reactor.getTileY()].setItem(null);

                for (int j = 0; j < getCurrentFloor().getActors().size(); j++) {
                    if(getCurrentFloor().getActors().elementAt(j) == reactor){
                        getCurrentFloor().getActors().remove(j);
                        break;
                    }
                }
            }
        else if(reaction == ActTypes.REACT_BECOMEPARTNER){
            getCurrentFloor().getTiles()[reactor.getTileX()][reactor.getTileY()].setActor(getPlayer().getPartner());

            for (int j = 0; j < getCurrentFloor().getActors().size(); j++) {
                if(getCurrentFloor().getActors().elementAt(j) == reactor){

                    getCurrentFloor().getActors().remove(j);
                    break;
                }
            }
            getCurrentFloor().getActors().add(getPlayer().getPartner());

        }
    }

    private boolean actorIsOnScreen(Actor actor){
        Rect shape = actor.getShape();
        ScreenXYPositionFinder screenXYPosFin = getPlayer().getScreenXYPosFin();
        if(shape.top >= 0 && shape.left >= 0 && shape.bottom <= screenXYPosFin.screenH && shape.right <= screenXYPosFin.screenW){
            return true;
        }
        return false;
    }

    public int getMusic(){

        return music;
    }

    public Vector<Integer> getSounds(){
        return sounds;
    }

    public String getErrorText(){
        return errorText;
    }
    public TickHandler getTickHandler(){
        return  tickHandler;
    }
    public SoundBSHandler getSoundBSHandler(){
        return soundBSHandler;
    }
}
