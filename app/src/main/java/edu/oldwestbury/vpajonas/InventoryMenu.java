package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.Vector;

public class InventoryMenu implements HUDelement {
    private Bitmap bmp;
    private HUDelement[] subHUDelements;
    private Rect shape;

    private boolean isActive;
    private boolean isVisible;

    private Vector<HUDText> texts;

    private ActionMenu actionMenu;

    private PlayerStats playerStats;

    private int delay;
    private Paint paint;

    public InventoryMenu(Context context, ActionMenu actionMenuPassed,PlayerStats playerStatsPassed, int xPos, int yPos, int width, int height){
        BitmapFactory bitmapFac = new BitmapFactory();
        bmp = bitmapFac.decodeResource(context.getResources(),R.drawable.textbox);

        shape = new Rect(xPos,yPos,xPos+width,yPos+height);

        isActive = false;
        isVisible = false;

        subHUDelements = new HUDelement[0];

        playerStats = playerStatsPassed;

        texts = new Vector<>();
        texts.setSize(playerStats.getItems().length);
        Paint paint = new Paint();
        paint.setTextSize(80f);
        paint.setARGB(255,255,255,255);
        for(int i = 0; i < texts.size();i++) {
            texts.set(i, new HUDText("", paint, 100, (i+1)*100));
        }

        actionMenu = actionMenuPassed;
        actionMenu.setInventoryMenu(this);

        delay = -1;
    }

    @Override
    public void update() {
        isVisible = false;
        if(isActive){
            isVisible = true;
        }
        actionMenu.update();




        if(delay > -1){
            delay--;
            if (delay == 0){
                isActive = true;
                delay = -1;
            }
        }
    }

    @Override
    public void onTouch(View v, MotionEvent event) {
        if(isActive) {
            try {
                int current = 0;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    while (playerStats.getItems()[current] != null) {
                        if (event.getX() <= texts.elementAt(current).shape.right &&
                                event.getX() >= texts.elementAt(current).shape.left &&
                                event.getY() <= texts.elementAt(current).shape.bottom &&
                                event.getY() >= texts.elementAt(current).shape.top) {

                            isActive = false;
                            actionMenu.setHeldItem(current);
                            actionMenu.setActive(10);
                            return;


                        }

                        current++;
                    }
                }
            }catch (Exception e){
                System.out.println(e.getLocalizedMessage());
            }
        }
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

    public void setActive(int delayPassed){
        delay = delayPassed;
        if (delay >= 0){
            int current = 0;


            while(playerStats.getItems()[current] != null) {
                texts.elementAt(current).text = playerStats.getItems()[current].getName();

                current++;
            }
            for (int i = current; i < texts.size();i++){
                texts.elementAt(i).text = "";
            }
        }
        else{
            isActive = false;
        }
    }
    @Override
    public boolean getActive(){
        return isActive;
    }

    @Override
    public boolean pauseWhenActive(){
        return true;
    }





}
