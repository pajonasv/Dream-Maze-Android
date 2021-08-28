package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.Vector;

public class EnemyInteractMenu implements HUDelement {
    private Bitmap bmp;
    private HUDelement[] subHUDelements;
    private Rect shape;

    private boolean isActive;
    private boolean isVisible;

    private Vector<HUDText> texts;
    private int heldItemIndex;

    private int delay;
    private Paint paint;
    private DialogueBox dialogueBox;
    private Player player;
    private Enemy enemy;

    public EnemyInteractMenu(Context context, DialogueBox dialogueBoxPassed, Player playerPassed ,int xPos, int yPos, int width, int height){
        BitmapFactory bitmapFac = new BitmapFactory();
        bmp = bitmapFac.decodeResource(context.getResources(),R.drawable.textbox);

        shape = new Rect(xPos,yPos,xPos+width,yPos+height);

        dialogueBox = dialogueBoxPassed;
        player = playerPassed;

        isActive = false;
        isVisible = false;

        subHUDelements = new HUDelement[0];


        texts = new Vector<>();
        Paint paint = new Paint();
        paint.setTextSize(80f);
        paint.setARGB(255,255,255,255);
        texts.add(new HUDText("Talk", paint, 100, 100));
        texts.add(new HUDText("Reason", paint, 100, 200));

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

            if(event.getAction() == MotionEvent.ACTION_UP) {

                if (event.getX() <= texts.elementAt(0).shape.right &&
                        event.getX() >= texts.elementAt(0).shape.left &&
                        event.getY() <= texts.elementAt(0).shape.bottom &&
                        event.getY() >= texts.elementAt(0).shape.top) {

                    isActive = false;
                    dialogueBox.setIsActive(true);
                    player.talkTo(enemy, ActTypes.ACT_TALK);

                }
                if (event.getX() <= texts.elementAt(1).shape.right &&
                        event.getX() >= texts.elementAt(1).shape.left &&
                        event.getY() <= texts.elementAt(1).shape.bottom &&
                        event.getY() >= texts.elementAt(1).shape.top) {

                    isActive = false;
                    dialogueBox.setIsActive(true);
                    player.talkTo(enemy,ActTypes.ACT_REASON);

                }
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

    @Override
    public boolean pauseWhenActive(){
        return true;
    }

    public void setActive(int delayPassed){
        delay = delayPassed;
    }

    @Override
    public boolean getActive(){
        return isActive;
    }

    public DialogueBox getDialogueBox(){
        return dialogueBox;
    }

    public void setEnemy(Enemy toSet){
        enemy = toSet;
    }
}
