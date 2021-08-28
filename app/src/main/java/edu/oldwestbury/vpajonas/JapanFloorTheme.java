package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class JapanFloorTheme implements FloorTheme {
    private BitmapFactory bitmapFac;
    private Bitmap floorSprite;
    private Bitmap leftWallSprite;
    private Bitmap rightWallSprite;
    private Bitmap upWallSprite;
    private Bitmap downWallSprite;
    private Bitmap upstairsSprite;
    private Bitmap downstairsSprite;

    private Bitmap upRightWallSprite;
    private Bitmap upLeftWallSprite;
    private Bitmap downRightWallSprite;
    private Bitmap downLeftWallSprite;

    private Bitmap pitSprite;

    private int music;

    public JapanFloorTheme(Context context){
        bitmapFac = new BitmapFactory();

        floorSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.japanfloor_tile);
        leftWallSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.japanleftwall_tile);
        rightWallSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.japanrightwall_tile);
        upWallSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.japanupwall_tile);
        downWallSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.japandownwall_tile);
        downstairsSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.down_stairs_tile);
        upstairsSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.up_stairs_tile);

        downLeftWallSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.japandownleftwall_tile);
        downRightWallSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.japandownrightwall_tile);
        upLeftWallSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.japanupleftwall_tile);
        upRightWallSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.japanuprightwall_tile);

        pitSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.basic_pit);

        music = R.raw.temporaltest;

    }

    @Override
    public Bitmap getFloorSprite(){
        return floorSprite;
    }
    @Override
    public Bitmap getLeftWallSprite(){
        return leftWallSprite;
    }
    @Override
    public Bitmap getRightWallSprite(){
        return rightWallSprite;
    }
    @Override
    public Bitmap getUpWallSprite(){
        return upWallSprite;
    }
    @Override
    public Bitmap getDownWallSprite(){
        return downWallSprite;
    }
    @Override
    public Bitmap getUpstairsSprite(){
        return upstairsSprite;
    }
    @Override
    public Bitmap getDownstairsSprite(){
        return downstairsSprite;
    }


    @Override
    public Bitmap getUpRightWallSprite() {
        return upRightWallSprite;
    }

    @Override
    public Bitmap getUpLeftWallSprite() {
        return upLeftWallSprite;
    }

    @Override
    public Bitmap getDownRightWallSprite() {
        return downRightWallSprite;
    }

    @Override
    public Bitmap getDownLeftWallSprite() {
        return downLeftWallSprite;
    }

    @Override
    public Bitmap getPitSprite() {
        return pitSprite;
    }

    @Override
    public int getMusic(){
        return music;
    }
}
