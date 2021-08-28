package edu.oldwestbury.vpajonas;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;

import androidx.annotation.RequiresApi;

public class ScreenXYPositionFinder {
    public int screenW;
    public int screenH;

   public int tileW;
   public int tileH;

   public int modifierW;
   public int modifierH;

   public int viewportWtiles;
   public int viewportHtiles;

   public int viewportXOffsetTiles;
   public int viewportYOffsetTiles;

   public int viewPortXtiles;
   public int viewPortYtiles;

   public int viewPortX;
   public int viewPortY;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public ScreenXYPositionFinder(Context context,int numOfXTiles, int numOfYTiles){
        DisplayMetrics displayMetrics = new DisplayMetrics();

        ((Activity)context).getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);

        screenW = displayMetrics.widthPixels;
        screenH = displayMetrics.heightPixels;

        tileW = screenW / numOfXTiles;

        tileH = screenH / numOfYTiles;

        modifierW =  screenW / 1080; //my screen width
        modifierH = screenH / 2160; //my screen height

        viewportWtiles = screenW / tileW; //in tiles
        viewportHtiles = screenH / tileH; //in tiles

        viewportXOffsetTiles = 0;
        viewportYOffsetTiles = -1;

        viewPortX = 0;
        viewPortY = 0;


    }

    public void setViewPortXYtiles(Player player){
        viewPortXtiles = player.getTileX() - viewportWtiles/2;
        viewPortYtiles = player.getTileY() - viewportHtiles/2;


    }

    public void setViewPortXY(Player player){
        //the x and y where the viewport starts
        viewPortX = player.getTileX()*tileW - viewportWtiles/2*(tileW);
        viewPortY = player.getTileY()*tileH - viewportHtiles/2*(tileH);
    }


}
