package edu.oldwestbury.vpajonas;

import java.util.Vector;

public class TickHandler {
    private Player player;
    private boolean tickEnabled;
    private Actor focus;
    private Vector<Actor> actors;
    public TickHandler(Player playerPassed, Vector<Actor> actorsPassed)
    {
        player = playerPassed;
        actors = actorsPassed;
    }
    public boolean getTickEnabled(){
        return tickEnabled;
    }
    public void setTickEnabled(boolean b){
        tickEnabled = b;
    }
    public void setActorsBusy(boolean b){
        for(int i = 0;i < actors.size();i++){
            actors.elementAt(i).setBusy(b);
        }

    }
    public void setActorsTouchable(boolean b){
        for(int i = 0;i < actors.size();i++){
            actors.elementAt(i).setTouchable(b);
        }

    }
    public void setFocus(Actor actor){
        if(focus != null){
            focus.setBusy(true);
        }
        focus = actor;
        focus.setBusy(false);
    }
}
