package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.Vector;

public class DialogueBox implements HUDelement {
    private Bitmap bmp;
    private HUDelement[] subHUDelements;
    private Rect shape;
    private Paint paint;

    private boolean isActive;
    private boolean isVisible;

    private Vector<HUDText> texts;

    private Vector<String> messages;
    private int currentMessage;

    public DialogueBox(Context context, int xPos, int yPos, int width, int height){
        BitmapFactory bitmapFac = new BitmapFactory();
        bmp = bitmapFac.decodeResource(context.getResources(),R.drawable.textbox);

        shape = new Rect(xPos,yPos,xPos+width,yPos+height);

        isVisible = false;
        isActive = false;

        messages = new Vector<>();
        currentMessage = 0;

        subHUDelements = new HUDelement[0];
        texts = new Vector<>();
        texts.setSize(10);
        Paint paint = new Paint();
        paint.setTextSize(40f);
        paint.setARGB(255,255,255,255);
        for(int i = 0; i < texts.size(); i++) {
            texts.setElementAt(new HUDText("", paint, shape.left + 20, shape.top + 20 + (i+1)*(int)paint.getTextSize()), i);
        }


    }

    @Override
    public void update(){
        isVisible = false;
        if (isActive){
            setIsVisible(true);
        }
    }
    @Override
    public void onTouch(View v, MotionEvent event){
        if(isActive) {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getX() <= shape.right &&
                        event.getX() >= shape.left &&
                        event.getY() <= shape.bottom &&
                        event.getY() >= shape.top) {
                    currentMessage++;
                    if (currentMessage >= messages.size()) {
                        setIsActive(false);
                        currentMessage = 0;
                    }
                    else{
                        setIsVisible(true);
                    }

                }
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
        return texts;
    }


    @Override
    public Paint getPaint() {
        return paint;
    }
    public void setIsVisible(boolean toSet){

        isVisible = toSet;
        if(isVisible){

            int maxChars = 20;
            int textIndex = 0;
            for(int i = 0; i < texts.size(); i++) {
                texts.elementAt(i).text = partitionText(messages.elementAt(currentMessage),textIndex,maxChars);
                textIndex += texts.elementAt(i).text.length();
                if (texts.elementAt(i).text.length() < maxChars) {
                    textIndex++;
                }
            }

        }
    }


    public void setMessages(Vector<String> toSet){
        messages = toSet;
    }

    public String partitionText(String text,int index, int end){
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
        return true;
    }
    @Override
    public boolean getActive(){
        return isActive;
    }

    public void setIsActive(boolean toSet){
        isActive = toSet;
    }

}
