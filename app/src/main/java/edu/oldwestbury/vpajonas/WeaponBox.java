package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.Vector;

public class WeaponBox implements HUDelement {
    private Bitmap bmp;
    private Rect shape;

    private boolean isVisible;
    private boolean isActive;

    private PlayerStats playerStats;
    private HUDelement[] subHUDelements;
    private Paint paint;
    public WeaponBox(Context context, PlayerStats playerStatsPassed,int xPos, int yPos, int width, int height){
        BitmapFactory bitmapFac = new BitmapFactory();
        bmp = bitmapFac.decodeResource(context.getResources(),R.drawable.textbox);
        shape = new Rect(xPos,yPos,xPos+width,yPos+height);
        playerStats = playerStatsPassed;

        isVisible = true;
        isActive = true;


        subHUDelements = new HUDelement[1];
        WeaponIcon weaponIcon = new WeaponIcon(playerStats,shape.left+10,shape.top+10,shape.right-10,shape.bottom-10);
        subHUDelements[0] = weaponIcon;

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

                    playerStats.nextWeapon();

            }
        }
    }

    @Override
    public Bitmap getBmp() {
        return bmp;
    }

    @Override
    public HUDelement[] getSubHUDelement() {
        return subHUDelements;
    }

    @Override
    public Rect getShape(){
        return shape;
    }

    @Override
    public boolean getIsVisible() {
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
    public boolean pauseWhenActive(){
        return false;
    }

    @Override
    public boolean getActive() {
        return isActive;
    }



    private class WeaponIcon implements  HUDelement{
        private Bitmap bmp;
        private Rect shape;
        private boolean isVisible;
        private boolean isActive;
        private PlayerStats playerStats;
        private HUDelement[] subHUDelements;

        private Paint paint;

        public WeaponIcon(PlayerStats playerStatsPassed,int xPos, int yPos, int xPostwo, int yPostwo){

            shape = new Rect(xPos,yPos,xPostwo,yPostwo);
            playerStats = playerStatsPassed;

            bmp = playerStats.getCurrentWeapon().getSprite();

            isActive = true;
            isVisible = true;
            subHUDelements = new HUDelement[0];


        }


        @Override
        public void update() {
            bmp = playerStats.getCurrentWeapon().getSprite();

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
            return subHUDelements;
        }

        @Override
        public Rect getShape(){
            return shape;
        }

        @Override
        public boolean getIsVisible() {
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
        public boolean pauseWhenActive(){
            return false;
        }
        @Override
        public boolean getActive() {
            return isActive;
        }

    }
}
