package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.Vector;

public class InventoryIcon implements HUDelement {
    private Bitmap bmp;
    private HUDelement[] subHUDelements;
    private Rect shape;

    private Paint paint;
    private boolean isVisible;
    private boolean isActive;

    private String text;
    private InventoryMenu inventory;
    TickHandler tickHandler;

    public InventoryIcon(Context context, InventoryMenu inventoryPassed,TickHandler tickHandlerPassed, int xPos, int yPos, int width, int height){
        BitmapFactory bitmapFac = new BitmapFactory();
        bmp = bitmapFac.decodeResource(context.getResources(),R.drawable.textbox);

        shape = new Rect(xPos,yPos,xPos+width,yPos+height);

        isActive = true;
        isVisible = true;
        text = "";
        subHUDelements = new HUDelement[0];



        inventory = inventoryPassed;
        tickHandler = tickHandlerPassed;
    }

    @Override
    public void update() {

    }

    @Override
    public void onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getX() >= shape.left &&
                    event.getX() <= shape.right &&
                    event.getY() >= shape.top &&
                    event.getY() <= shape.bottom) {

                if(inventory.getActive()) {
                    inventory.setActive(-1);
                }
                else{
                    inventory.setActive(10);
                }
                tickHandler.setActorsTouchable(!inventory.getActive());

            }
        }
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
    @Override
    public boolean getActive(){
        return isActive;
    }
    @Override
    public boolean pauseWhenActive(){
        return false;
    }


}
