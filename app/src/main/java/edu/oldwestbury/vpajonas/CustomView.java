package edu.oldwestbury.vpajonas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;

public class CustomView extends View {


    private GameEnvironment gameEnvironment;
    private String debugText;
    private Bitmap bmp;

    private TreeMapGenerator treeMap; //delete me
    private ScreenXYPositionFinder screenXYPositionFinder;

    public int screenW;
    public int screenH;

    public int tileW;
    public int tileH;

    public int modifierW;
    public int modifierH;

    public int viewportW;
    public int viewportH;

    public int viewportXOffset;
    public int viewportYOffset;

    public int viewPortX;
    public int viewPortY;

    private String errorText;
    private NarrarationBox debugDisplay;
    private NarrarationBox playerHUD;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public CustomView(Context context, GameEnvironment passedGameEnvironment){
        super(context);
        gameEnvironment = passedGameEnvironment;

        setFlags();


        this.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                for (int i = 0; i < gameEnvironment.getDungeon().getCurrentFloor().getActors().size(); i++){
                    gameEnvironment.getDungeon().getCurrentFloor().getActors().elementAt(i).onTouch(v,event);
                }
                for (int i = 0; i < gameEnvironment.getHUDelements().size();i++){
                    gameEnvironment.getHUDelements().elementAt(i).onTouch(v,event);
                }

                return true;
            }

        });


        BitmapFactory bitmapFactory = new BitmapFactory();

        bmp = bitmapFactory.decodeResource(context.getResources(),R.drawable.screen_layout);

        screenXYPositionFinder = gameEnvironment.getDungeon().getCurrentFloor().getScreenXYPositionFinder();
        screenW = screenXYPositionFinder.screenW;
        screenH = screenXYPositionFinder.screenH;

        tileW = screenXYPositionFinder.tileW;
        tileH = screenXYPositionFinder.tileH;
        modifierW = screenXYPositionFinder.modifierW;
        modifierH = screenXYPositionFinder.modifierH;
        viewportW = screenXYPositionFinder.viewportWtiles;
        viewportH = screenXYPositionFinder.viewportHtiles;
        viewportXOffset = screenXYPositionFinder.viewportXOffsetTiles;
        viewportYOffset = screenXYPositionFinder.viewportYOffsetTiles;

        debugText = "";
        errorText = "";
        debugDisplay = new NarrarationBox(context,10,300,screenW-20,screenH-10);


        playerHUD = new NarrarationBox(context,10,10,screenW-20,280);

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas){
        Floor currentFloor = gameEnvironment.getDungeon().getCurrentFloor();

        viewPortX = screenXYPositionFinder.viewPortXtiles;
        viewPortY = screenXYPositionFinder.viewPortYtiles;

        errorText += gameEnvironment.getErrorText();

        //black background
        canvas.drawColor(Color.BLACK);

        //TILES
        for(int i = viewPortX -1; i < viewPortX + screenXYPositionFinder.viewportWtiles +1; i++){
            if (i >= 0 && i < currentFloor.getTiles().length) { //if within floor bounds

                    for (int j = viewPortY - 1; j < viewPortY + screenXYPositionFinder.viewportHtiles + 1; j++) {
                        try {
                            if (j >= 0 && j < currentFloor.getTiles()[i].length) { //if within floor bounds

                                if (currentFloor.getTiles()[i][j] != null) { //nullptr exception prevention
                                    try {
                                        canvas.drawBitmap(currentFloor.getTiles()[i][j].getSprite(),
                                                null,
                                                new Rect((int) (((i + viewportXOffset) * screenXYPositionFinder.tileW * modifierW) - screenXYPositionFinder.viewPortX),
                                                        (int) (((j + viewportYOffset) * screenXYPositionFinder.tileH * modifierH) - screenXYPositionFinder.viewPortY),
                                                        (int) (((i + 1 + viewportXOffset) * screenXYPositionFinder.tileW * modifierW) - screenXYPositionFinder.viewPortX),
                                                        (int) (((j + 1 + viewportYOffset) * screenXYPositionFinder.tileH * modifierH) - screenXYPositionFinder.viewPortY)),
                                                null);
                                    } catch (Exception e) {
                                        errorText += "TILE DRAWING ERROR 2 - Error at tile [" + i + "][" + j + "]: " + e.getLocalizedMessage() + "^";
                                    }


                                }
                            }
                        }catch (Exception e){
                            errorText += "TILE DRAWING ERROR 1 - Error at tile [" + i + "][" + j + "]: " + e.getLocalizedMessage() + currentFloor.getTiles()[i][j].getActor() + "^";

                        }
                    }

            }
        }

        //ACTORS AND ITEMS
        for(int i = 0; i < viewportW - viewportXOffset; i++){

            if (viewPortX + i >= 0 && viewPortX + i < currentFloor.getTiles().length) { //if within room bounds

                    for (int j = 0; j < viewportH - viewportYOffset; j++) {
                        try {
                            if (viewPortY + j >= 0 && viewPortY + j < currentFloor.getTiles()[viewPortX + i].length) { //if within room bounds
                                if (currentFloor.getTiles()[viewPortX + i][viewPortY + j] != null) { //nullptr exception prevention

                                    if (viewPortX + i > -1 && viewPortX + i < currentFloor.getTiles().length &&
                                            viewPortY + j > -1 && viewPortY + j < currentFloor.getTiles()[viewPortX + i].length) {
                                        try {
                                            if (currentFloor.getTiles()[viewPortX + i][viewPortY + j].getItem() != null) {

                                                canvas.drawBitmap(
                                                        currentFloor.getTiles()[viewPortX + i][viewPortY + j].getItem().getSprite(),
                                                        null,
                                                        currentFloor.getTiles()[viewPortX + i][viewPortY + j].getItem().getShape(),
                                                        null);


                                            }
                                        } catch (Exception e) {
                                            errorText += "ITEM DRAWING ERROR 1 - Error at tile [" + i + "][" + j + "]: " + e.getLocalizedMessage() + "^";
                                        }

                                        try {

                                            if (currentFloor.getTiles()[viewPortX + i][viewPortY + j].getActor() != null) {
                                                canvas.drawBitmap(
                                                        currentFloor.getTiles()[viewPortX + i][viewPortY + j].getActor().getSprite(),
                                                        null,

                                                        currentFloor.getTiles()[viewPortX + i][viewPortY + j].getActor().getShape(),
                                                        null);

                                            }
                                        } catch (Exception e) {
                                            errorText += "ACTOR DRAWING ERROR 1 - Error at tile [" + i + "][" + j + "]: " + e.getLocalizedMessage() + "^";
                                        }
                                    }


                                }
                            }
                        }catch(Exception e){
                            errorText += "ACTOR/ITEM DRAWING ERROR 2 -  Error at tile [" + i + "][" + j + "]: " + e.getLocalizedMessage() + "^";
                        }

                    }



            }
        }

        Paint textColor = new Paint();
        textColor.setTextSize(40f);
        textColor.setARGB(255,0,255,0);


        //HUD
        int maxChars;
        int textIndex;
        int textYOffset;
        try {

            for (int i = 0; i < gameEnvironment.getHUDelements().size(); i++) {

                if (gameEnvironment.getHUDelements().elementAt(i).getIsVisible()) {
                    canvas.drawBitmap(gameEnvironment.getHUDelements().elementAt(i).getBmp(),
                            null,
                            gameEnvironment.getHUDelements().elementAt(i).getShape(), gameEnvironment.getHUDelements().elementAt(i).getPaint());
                    if(gameEnvironment.getHUDelements().elementAt(i).getTexts() != null) {


                        for (int j = 0; j < gameEnvironment.getHUDelements().elementAt(i).getTexts().size(); j++) {

                            String currentText = gameEnvironment.getHUDelements().elementAt(i).getTexts().elementAt(j).text;



                                canvas.drawText(gameEnvironment.getHUDelements().elementAt(i).getTexts().elementAt(j).text,
                                        gameEnvironment.getHUDelements().elementAt(i).getTexts().elementAt(j).shape.left,
                                        gameEnvironment.getHUDelements().elementAt(i).getTexts().elementAt(j).shape.top,
                                        gameEnvironment.getHUDelements().elementAt(i).getTexts().elementAt(j).paint);


                        }
                    }


                    if (gameEnvironment.getHUDelements().elementAt(i) instanceof NarrarationBox) {
                        maxChars = 57;
                        textIndex = 0;
                        textYOffset = 0;


                        while (textIndex < ((NarrarationBox) gameEnvironment.getHUDelements().elementAt(i)).getText().length()) {
                            String partitionedText = partitionText(((NarrarationBox) gameEnvironment.getHUDelements().elementAt(i)).getText(),textIndex, maxChars);
                            canvas.drawText(partitionedText, gameEnvironment.getHUDelements().elementAt(i).getShape().left + 15, gameEnvironment.getHUDelements().elementAt(i).getShape().top + 30 + textYOffset, textColor);
                            textIndex += partitionedText.length();
                            if (partitionedText.length() < maxChars) {
                                textIndex++;
                            }
                            textYOffset += 40;
                        }
                    }


                }
            }
        }catch (Exception e){
            errorText += "HUDELEMENT DRAWING ERROR 1- " + e.getLocalizedMessage();
        }

        PlayerStats tempPlayStats = gameEnvironment.getDungeon().getPlayer().getPlayerStats();



        maxChars = 57;
        textIndex = 0;
        textYOffset = 0;
        playerHUD.setText( "HP - " + tempPlayStats.getHP() + "/" + tempPlayStats.getMaxHP() + "     Money - " + tempPlayStats.getMoney()
                + "     Keys - " + tempPlayStats.getKeys()
        );
        while(textIndex < playerHUD.getText().length()) {

            String partitionedText = playerHUD.partitionText(textIndex, maxChars);
            canvas.drawText(partitionedText, playerHUD.getShape().left+ 15, playerHUD.getShape().top + 30 + textYOffset, textColor);
            textIndex += partitionedText.length();
            if (partitionedText.length() < maxChars) {
                textIndex++;
            }
            textYOffset += 40;
        }




         maxChars = 57;
         textIndex = 0;
         textYOffset = 0;
        debugDisplay.setText(errorText);
        while(textIndex < debugDisplay.getText().length()) {

            String partitionedText = debugDisplay.partitionText(textIndex, maxChars);
            canvas.drawText(partitionedText, debugDisplay.getShape().left + 15, debugDisplay.getShape().top + 30 + textYOffset, textColor);
            textIndex += partitionedText.length();
            if (partitionedText.length() < maxChars) {
                textIndex++;
            }
            textYOffset += 40;
        }
        //DEBUG_TEXT
        canvas.drawText(debugText,  10,100,textColor);



    }

    private String partitionText(String text,int index, int end){
        String partition = "";

        for(int i = index; i < index + end;i++){
            if(i >= text.length() || text.charAt(i) == '^') {
                break;
            }
            partition += text.charAt(i);
        }
        return partition;
    }

    public void setFlags(){
        this.setSystemUiVisibility(this.SYSTEM_UI_FLAG_FULLSCREEN | this.SYSTEM_UI_FLAG_HIDE_NAVIGATION | this.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
