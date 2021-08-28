package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.Vector;

public class FloorIntroText implements HUDelement {
    private Bitmap bmp;
    private HUDelement[] subHUDelements;
    private Rect shape;


    private Vector<HUDText> texts;

    private boolean isVisible;
    private boolean isActive;

    private int alpha;
    private Floor floor;
    private int lastFloor;
    private Paint paint;
    private int timer;

    public FloorIntroText(Context context, ScreenXYPositionFinder screenXYPosFin,Floor floorPassed){
        BitmapFactory bitmapFac = new BitmapFactory();
        bmp = bitmapFac.decodeResource(context.getResources(),R.drawable.textbox);

        shape = new Rect(0,0,screenXYPosFin.screenW,screenXYPosFin.screenH);

        isActive = true;
        isVisible = true;

        subHUDelements = new HUDelement[0];

        floor = floorPassed;

        timer = 60;
        alpha = 255;
        texts = new Vector<>();
        paint = new Paint();

        paint.setTextSize(80f);
        paint.setARGB(alpha,255,255,255);
        texts.add(new HUDText("Floor 0", paint, screenXYPosFin.screenW/2 - 100, screenXYPosFin.screenH/2));


        lastFloor = floor.getFloorNum();

    }


    @Override
    public void update() {
        if(lastFloor < floor.getFloorNum()){
            alpha = 255;
            timer = 60;
        }
        texts.elementAt(0).text = "Floor " + floor.getFloorNum();
        lastFloor = floor.getFloorNum();
        if(timer <= 0) {
            if (alpha > 0) {
                alpha-=4;
                if(alpha < 0){
                    alpha = 0;
                }
            }
        }
        timer--;

        paint.setARGB(alpha,255,255,255);
    }


    @Override
    public void onTouch(View v, MotionEvent event) {
    }

    @Override
    public Bitmap getBmp() {
        return bmp;
    }

    @Override
    public HUDelement[] getSubHUDelement() {
        return new HUDelement[0];
    }

    @Override
    public Rect getShape() {
        return shape;
    }

    @Override
    public boolean getIsVisible() {
        return isVisible;
    }

    @Override
    public Vector<HUDText> getTexts() {
        return texts;
    }

    @Override
    public Paint getPaint() {
        return paint;
    }
    @Override
    public boolean getActive(){
        return isActive;
    }
    @Override
    public boolean pauseWhenActive(){
        return false;
    }

}
