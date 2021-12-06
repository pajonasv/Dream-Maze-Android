package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.Vector;

public class StatsMenu implements HUDelement {

    private Bitmap bmp;
    private HUDelement[] subHUDelements;
    private Rect shape;

    private boolean isActive;
    private boolean isVisible;

    private Vector<HUDText> texts;


    private Player player;

    private PlayerStats playerStats;

    private int delay;
    private Paint paint;
    private short tab;

    public StatsMenu(Context context, Player playerPassed, int xPos, int yPos, int width, int height){
        BitmapFactory bitmapFac = new BitmapFactory();
        bmp = bitmapFac.decodeResource(context.getResources(),R.drawable.textbox);

        shape = new Rect(xPos,yPos,xPos+width,yPos+height);

        isActive = false;
        isVisible = false;

        subHUDelements = new HUDelement[0];

        player = playerPassed;

        playerStats = player.getPlayerStats();

        tab = 0;

        texts = new Vector<>();
        Paint paint = new Paint();
        paint.setTextSize(80f);
        paint.setARGB(255,255,255,255);

        texts.add(new HUDText("",paint,20,100));
        texts.add(new HUDText("",paint,20,200));
        texts.add(new HUDText("",paint,20,300));
        texts.add(new HUDText("",paint,20,400));

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

                    if(event.getX() > 900 && event.getY() > 1500){
                        setActive(-1);
                    }
                    else{
                        if(tab == 0 && player.getPartner() != null){
                            tab = 1;
                            setTextBasedOnTab();
                        }
                        else{
                            tab = 0;
                            setTextBasedOnTab();
                        }
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
            tab = 0;
            setTextBasedOnTab();

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

    private void setTextBasedOnTab(){
        if(tab == 0){
            texts.elementAt(0).text = "Player";
            texts.elementAt(1).text = "HP - " + playerStats.getHP() + "/" + playerStats.getMaxHP();
            texts.elementAt(2).text = "ATK - " + playerStats.getATK();
            texts.elementAt(3).text = "DEF - " + playerStats.getDEF();
        }
        else{
            texts.elementAt(0).text = "Partner";
            texts.elementAt(1).text = "HP - " + player.getPartner().getStats().getHP() + "/" + player.getPartner().getStats().getMaxHP();
            texts.elementAt(2).text = "ATK - " + player.getPartner().getStats().getATK();
            texts.elementAt(3).text = "DEF - " + player.getPartner().getStats().getDEF();


        }

    }





}
