package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.Vector;

public class NarrarationBox implements HUDelement {
    private Bitmap bmp;
    private HUDelement[] subHUDelements;
    private Rect shape;
    private Paint paint;
    private boolean isActive;

    private boolean isVisible;

    private String text;

       public NarrarationBox(Context context, int xPos, int yPos, int width, int height){
        BitmapFactory bitmapFac = new BitmapFactory();
        bmp = bitmapFac.decodeResource(context.getResources(),R.drawable.textbox);

        shape = new Rect(xPos,yPos,xPos+width,yPos+height);

        isVisible = true;
        isActive = true;
        text = "";


           subHUDelements = new HUDelement[0];

    }

    @Override
    public void update(){

    }
    @Override
    public void onTouch(View v, MotionEvent event){

    }

    @Override
    public Bitmap getBmp(){
        return bmp;
    }
    @Override
    public HUDelement[] getSubHUDelement(){
        return subHUDelements;
    }
    @Override
    public Rect getShape(){
           return shape;
    }

    @Override
    public boolean getIsVisible(){
        return isVisible;
    }

    @Override
    public Vector<HUDText> getTexts() {
        return null;
    }


    @Override
    public Paint getPaint() {
        return paint;
    }

    public String getText(){
        return text;
    }
    public void setText(String toSet){
        text = toSet;
    }

    public String partitionText(int index, int end){
        String partition = "";

        for(int i = index; i < index + end;i++){
            if(i >= text.length() || text.charAt(i) == '^') {
                break;
            }
            partition += text.charAt(i);
        }

        return partition;
    }

    @Override
    public boolean pauseWhenActive(){
        return false;
    }
    @Override
    public boolean getActive(){
           return isActive;
    }


}
