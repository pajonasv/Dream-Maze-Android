package edu.oldwestbury.vpajonas;


import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import java.util.Vector;

public class Floor {
    private DTile[][] tiles;
    private Vector<Actor> actors;
    private Player player;
    private Context context;
    private ScreenXYPositionFinder screenXYPosFin;
    private String errorText;
    private FloorTheme floorTheme;


    private int floorNum;
    public Floor(Context passedContext){
        context = passedContext;
        errorText = "";
        floorNum = 1;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        floorTheme = new BasicFloorTheme(context);
        TreeMapGenerator treeMap = new TreeMapGenerator(context,null,floorTheme, floorNum);
        tiles = treeMap.getTiles();

        errorText += treeMap.getErrorText();

        player = treeMap.getPlayer();

        screenXYPosFin = treeMap.getScreenXYPositionFinder();
        screenXYPosFin.setViewPortXYtiles(treeMap.getPlayer());

        actors = treeMap.getActors();


    }

    public DTile[][] getTiles(){
        return tiles;
    }

    public Vector<Actor> getActors(){
        return actors;
    }
    public Player getPlayer(){
        return player;
    }




    public void newFloor(){
        floorNum++;
        TreeMapGenerator treeMap = new TreeMapGenerator(context,player,floorTheme, floorNum);
        tiles = treeMap.getTiles();

        errorText += treeMap.getErrorText();

        actors = treeMap.getActors();

    }

    public ScreenXYPositionFinder getScreenXYPositionFinder(){
        return screenXYPosFin;
    }

    public String getErrorText() {

        return errorText;
    }

    public int getFloorNum(){
        return floorNum;
    }
}
