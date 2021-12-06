package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.Vector;

public class EquipMenu implements HUDelement {
    private Bitmap bmp;
    private HUDelement[] subHUDelements;
    private Rect shape;

    private boolean isActive;
    private boolean isVisible;

    private Vector<HUDText> texts;


    private PlayerStats playerStats;

    private int delay;
    private Paint paint;

    public EquipMenu(Context context, PlayerStats playerStatsPassed, int xPos, int yPos, int width, int height){
        BitmapFactory bitmapFac = new BitmapFactory();
        bmp = bitmapFac.decodeResource(context.getResources(),R.drawable.textbox);

        shape = new Rect(xPos,yPos,xPos+width,yPos+height);

        isActive = false;
        isVisible = false;

        subHUDelements = new HUDelement[0];

        playerStats = playerStatsPassed;

        texts = new Vector<>();
        Paint paint = new Paint();
        paint.setTextSize(80f);
        paint.setARGB(255,255,255,255);
        texts.add(new HUDText("Weapon - ", paint, 100, 100));
        texts.add(new HUDText(playerStats.getCurrentWeapon().getName(), paint, 500, 100));
        texts.add(new HUDText("ATK - "+playerStats.getCurrentWeapon().getATK(), paint,500,300));


        delay = -1;
    }

    @Override
    public void update() {
        isVisible = false;
        if(isActive){
            isVisible = true;
        }


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
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() <= texts.elementAt(1).shape.right &&
                            event.getX() >= texts.elementAt(1).shape.left &&
                            event.getY() <= texts.elementAt(1).shape.bottom &&
                            event.getY() >= texts.elementAt(1).shape.top) {

                            playerStats.nextWeapon();
                            texts.elementAt(1).text = playerStats.getCurrentWeapon().getName();
                            texts.elementAt(2).text = "ATK - " + playerStats.getCurrentWeapon().getATK();
                    }


                        if(event.getX() > 900 && event.getY() > 1500){
                        setActive(-1);
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
            texts.elementAt(1).text = playerStats.getCurrentWeapon().getName();
            texts.elementAt(2).text = "ATK - " + playerStats.getCurrentWeapon().getATK();

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
