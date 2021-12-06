package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.Vector;

public class PlayerDoubleTapMenu implements HUDelement {
    private Bitmap bmp;
    private HUDelement[] subHUDelements;
    private Rect shape;

    private boolean isActive;
    private boolean isVisible;

    private Vector<HUDText> texts;


    private int delay;
    private Paint paint;

    public PlayerDoubleTapMenu(Context context, Player player, InventoryMenu inventoryMenu, EquipMenu equipMenu, StatsMenu statsMenu){
        BitmapFactory bitmapFac = new BitmapFactory();
        bmp = bitmapFac.decodeResource(context.getResources(),R.drawable.textbox);

        shape = new Rect(player.getShape().left - (3*player.getScreenXYPosFin().tileW),
                player.getShape().top - (2*player.getScreenXYPosFin().tileH),
                player.getShape().right + (3*player.getScreenXYPosFin().tileW),
                player.getShape().top);

        isActive = false;
        isVisible = false;

        subHUDelements = new HUDelement[]{
          new SelectionWindow(bitmapFac.decodeResource(context.getResources(),R.drawable.items_icon), inventoryMenu,shape.left,shape.top,player.getScreenXYPosFin().tileW*2,player.getScreenXYPosFin().tileH*2),
                new SelectionWindow(bitmapFac.decodeResource(context.getResources(),R.drawable.equip_icon), equipMenu,player.getShape().left - player.getScreenXYPosFin().tileW/2,shape.top,player.getScreenXYPosFin().tileW*2,player.getScreenXYPosFin().tileH*2),
                new SelectionWindow(bitmapFac.decodeResource(context.getResources(),R.drawable.stats_icon), statsMenu,player.getShape().right + player.getScreenXYPosFin().tileW,shape.top,player.getScreenXYPosFin().tileW*2,player.getScreenXYPosFin().tileH*2)

        };

        texts = new Vector<>();

        Paint paint = new Paint();
        paint.setTextSize(80f);
        paint.setARGB(255,255,255,255);

        delay = -1;
    }

    @Override
    public void update() {
        isVisible = false;
        if(isActive){
            isVisible = true;
        }
        for(int i = 0; i < subHUDelements.length;i++){
            if(((SelectionWindow)subHUDelements[i]).HUDe.getActive()){
                setActive(-1);
            }
        }




        if(delay > -1){
            delay--;
            if (delay == 0){
                isActive = true;
                for(int i = 0; i < subHUDelements.length;i++){
                    ((SelectionWindow)subHUDelements[i]).setActive(10);
                }
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
                    if (!(event.getX() >= shape.left &&
                            event.getX() <= shape.right &&
                            event.getY() >= shape.top &&
                            event.getY() <= shape.bottom)) {
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
        return subHUDelements;
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
        if (delay < 0){
            isActive = false;
            for(int i = 0; i < subHUDelements.length;i++){
                ((SelectionWindow)subHUDelements[i]).setActive(-1);
            }
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

    private class SelectionWindow implements HUDelement{
        private Bitmap bmp;
        private HUDelement[] subHUDelements;
        private Rect shape;

        private boolean isActive;
        private boolean isVisible;

        private Vector<HUDText> texts;


        private int delay;
        private Paint paint;

        public HUDelement HUDe;

        public SelectionWindow(Bitmap bmpPassed, HUDelement HUDePassed,int xPos, int yPos, int width, int height){

            bmp = bmpPassed;

            shape = new Rect(xPos,yPos,xPos+width,yPos+height);

            HUDe = HUDePassed;

            isActive = false;
            isVisible = false;

            subHUDelements = new HUDelement[0];


            texts = new Vector<>();

            Paint paint = new Paint();
            paint.setTextSize(80f);
            paint.setARGB(255,255,255,255);


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
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() >= shape.left &&
                            event.getX() <= shape.right &&
                            event.getY() >= shape.top &&
                            event.getY() <= shape.bottom) {

                        if(HUDe.getActive()) {
                            if(HUDe instanceof InventoryMenu) {
                                ((InventoryMenu)HUDe).setActive(-1);
                            }
                            else if(HUDe instanceof EquipMenu) {
                                ((EquipMenu)HUDe).setActive(-1);
                            }
                            else if(HUDe instanceof StatsMenu){
                                ((StatsMenu)HUDe).setActive(-1);
                            }
                        }
                        else{

                            if(HUDe instanceof InventoryMenu) {
                                ((InventoryMenu) HUDe).setActive(10);
                            }
                            else if(HUDe instanceof EquipMenu) {
                                ((EquipMenu)HUDe).setActive(10);
                            }
                            else if(HUDe instanceof StatsMenu){
                                ((StatsMenu)HUDe).setActive(10);
                            }
                        }

                    }
                }
                for(int i = 0; i < subHUDelements.length;i++){
                    subHUDelements[i].onTouch(v,event);
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
            if (delay < 0){
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


}
