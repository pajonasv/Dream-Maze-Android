package edu.oldwestbury.vpajonas;

import android.graphics.Paint;
import android.graphics.Rect;

public class HUDText {
    public String text;
    public Rect shape;
    public Paint paint;

    public HUDText(String passedText, Paint passedPaint,int passedX, int passedY){
        text = passedText;

        paint = passedPaint;
        shape = new Rect(passedX,passedY,passedX + 2000,passedY + (int)paint.getTextSize());

    }



}
