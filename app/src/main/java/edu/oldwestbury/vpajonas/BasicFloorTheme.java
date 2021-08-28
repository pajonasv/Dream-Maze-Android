package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BasicFloorTheme implements FloorTheme {
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

    public BasicFloorTheme(Context context){
        bitmapFac = new BitmapFactory();

        floorSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.floor);
        leftWallSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.basic_left);
        rightWallSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.basic_right);
        upWallSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.basic_up);
        downWallSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.basic_down);
        downstairsSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.down_stairs_tile);
        upstairsSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.up_stairs_tile);

        downLeftWallSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.basic_down_left);
        downRightWallSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.basic_down_right);
        upLeftWallSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.basic_up_left);
        upRightWallSprite = bitmapFac.decodeResource(context.getResources(),R.drawable.basic_up_right);

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
