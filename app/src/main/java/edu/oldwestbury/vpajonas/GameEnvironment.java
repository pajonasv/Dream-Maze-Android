package edu.oldwestbury.vpajonas;

import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;


public class GameEnvironment extends TimerTask{
    private CustomView customView;
    private Dungeon dungeon;
    private Timer timer;
    private String errorText;

    private Vector<HUDelement> HUDelements;

    private Vector<Integer> ids;

    private boolean pauseDungeonLoop;
    private PlayerStats playerStats;
    private int lastFloorNum;
    public GameEnvironment(Context context){
        errorText = "";
        dungeon = new Dungeon(context);


        HUDelements = new Vector<HUDelement>();


        ScreenXYPositionFinder screenXYPosFin = dungeon.getPlayer().getScreenXYPosFin();

        NarrarationBox narrarationBox = new NarrarationBox(context,0,screenXYPosFin.screenH-480,screenXYPosFin.screenW,480);
        HUDelements.add(narrarationBox);


        ActionMenu actionMenu = new ActionMenu(context,dungeon.getPlayer().getPlayerStats(),0,0,screenXYPosFin.screenW,screenXYPosFin.screenH);
        InventoryMenu inventory = new InventoryMenu(context,actionMenu,dungeon.getPlayer().getPlayerStats(),0,0,screenXYPosFin.screenW,screenXYPosFin.screenH);
        HUDelements.add(inventory);
        HUDelements.add(actionMenu);

        EquipMenu equipMenu = new EquipMenu(context,dungeon.getPlayer().getPlayerStats(),0,0,screenXYPosFin.screenW,screenXYPosFin.screenH);
        HUDelements.add(equipMenu);

        StatsMenu statsMenu = new StatsMenu(context,dungeon.getPlayer(),0,0,screenXYPosFin.screenW,screenXYPosFin.screenH);
        HUDelements.add(statsMenu);

        FloorIntroText floorIntroText = new FloorIntroText(context, screenXYPosFin, dungeon.getCurrentFloor());
        HUDelements.add(floorIntroText);

        DialogueBox dialogueBox = new DialogueBox(context,0,screenXYPosFin.screenH-480,screenXYPosFin.screenW,480);
        HUDelements.add(dialogueBox);

        EnemyInteractMenu enemyInteractMenu = new EnemyInteractMenu(context, dialogueBox,dungeon.getPlayer(),0,0,screenXYPosFin.screenW,screenXYPosFin.screenH);
        HUDelements.add(enemyInteractMenu);


        PlayerDoubleTapMenu playerDoubleTapMenu = new PlayerDoubleTapMenu(context,dungeon.getPlayer(),inventory,equipMenu,statsMenu);
        dungeon.getPlayer().setPlayerDoubleTapMenu(playerDoubleTapMenu);
        HUDelements.add(playerDoubleTapMenu);

        for(int i = 0; i < HUDelements.size();i++){
            HUDelement current = HUDelements.elementAt(i);
            for(int j = 0; j < current.getSubHUDelement().length;j++){
                HUDelements.add(current.getSubHUDelement()[j]);

            }
        }
        for(int i = 0; i < dungeon.getCurrentFloor().getActors().size();i++){
            if (dungeon.getCurrentFloor().getActors().elementAt(i) instanceof Enemy){
                ((Enemy) dungeon.getCurrentFloor().getActors().elementAt(i)).setEnemyInteractMenu(enemyInteractMenu);
            }
        }



        timer = new Timer();
        timer.scheduleAtFixedRate(this,0,1000/60);

        ids = new Vector<Integer>();

        pauseDungeonLoop = false;

        lastFloorNum = 0;
    }
    public void setCustomView(CustomView passedCustomView){
        customView = passedCustomView;
    }

    public Dungeon getDungeon(){
        return dungeon;
    }

    @Override
    public void run() {


        for(int i = 0; i < HUDelements.size();i++){
            if(HUDelements.elementAt(i).getActive() && HUDelements.elementAt(i).pauseWhenActive()){
                pauseDungeonLoop = true;
                break;
            }
            for(int j = 0; j < HUDelements.elementAt(i).getSubHUDelement().length;j++){
                if(HUDelements.elementAt(i).getSubHUDelement()[j].getActive() && HUDelements.elementAt(i).getSubHUDelement()[j].pauseWhenActive()){
                    pauseDungeonLoop = true;
                    break;
                }
            }

        }

        if(lastFloorNum < dungeon.getCurrentFloor().getFloorNum()){
            for(int i = 0; i < HUDelements.size();i++) {
                if(HUDelements.elementAt(i) instanceof EnemyInteractMenu) {
                    for (int j = 0; j < dungeon.getCurrentFloor().getActors().size(); j++) {
                        if (dungeon.getCurrentFloor().getActors().elementAt(j) instanceof Enemy) {
                            ((Enemy) dungeon.getCurrentFloor().getActors().elementAt(j)).setEnemyInteractMenu((EnemyInteractMenu) HUDelements.elementAt(i));
                        }
                    }
                    break;
                }
            }
        }
        lastFloorNum = dungeon.getCurrentFloor().getFloorNum();

        if(!pauseDungeonLoop) {
            dungeon.run();
        }
        pauseDungeonLoop = false;



        errorText+= dungeon.getErrorText();

        for(int i = 0; i < HUDelements.size();i++){
            if(HUDelements.elementAt(i) instanceof NarrarationBox){
                ((NarrarationBox) HUDelements.elementAt(i)).setText(dungeon.getTickText());
            }
            HUDelements.elementAt(i).update();
            for(int j = 0; j < HUDelements.elementAt(i).getSubHUDelement().length;j++){
                HUDelements.elementAt(i).getSubHUDelement()[j].update();

            }

        }







        if(customView != null) {
            customView.invalidate();
        }



    }

    public Vector<HUDelement> getHUDelements(){
        return HUDelements;
    }

    public String getErrorText(){
        return errorText;
    }

}