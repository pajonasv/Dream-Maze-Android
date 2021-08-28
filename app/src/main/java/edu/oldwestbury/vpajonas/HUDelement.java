package edu.oldwestbury.vpajonas;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.Vector;


public interface HUDelement {

    public void update();
    public void onTouch(View v, MotionEvent event);

    public Bitmap getBmp();
    public HUDelement[] getSubHUDelement();
    public Rect getShape();
    public boolean getIsVisible();
    public Vector<HUDText> getTexts();
    public Paint getPaint();
    public boolean pauseWhenActive();
    public boolean getActive();


}
