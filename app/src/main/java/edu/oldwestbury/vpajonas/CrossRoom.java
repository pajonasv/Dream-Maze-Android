package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CrossRoom implements Room {

    private DTile[][] tiles;
    private int xOffset;
    private int yOffset;
    private int buildDirection;
    private boolean locked;
    private DTile[] enemySpawnPoints;
    private DTile[] itemSpawnPoints;

    private int width;
    private int height;
    //only works if width = height
    public CrossRoom(Context context, FloorTheme floorTheme, int width, int height, int passedXOffset, int passedYOffset, int direction){

        xOffset = passedXOffset;
        yOffset = passedYOffset;

        tiles = new DTile[width][height];

        buildDirection = direction;

        BitmapFactory bitmapFactory = new BitmapFactory();
        Bitmap emptyTileSprite = floorTheme.getFloorSprite();
        Bitmap downwallTileSprite = floorTheme.getDownWallSprite();
        Bitmap upwallTileSprite = floorTheme.getUpWallSprite();
        Bitmap leftwallTileSprite = floorTheme.getLeftWallSprite();
        Bitmap rightwallTileSprite = floorTheme.getRightWallSprite();

        Bitmap upleftwallTileSprite = floorTheme.getUpLeftWallSprite();
        Bitmap uprightwallTileSprite = floorTheme.getUpRightWallSprite();
        Bitmap downleftwallTileSprite = floorTheme.getDownLeftWallSprite();
        Bitmap downrightwallTileSprite = floorTheme.getDownRightWallSprite();
        Bitmap pitTileSprite = floorTheme.getPitSprite();

        locked = false;

        for (int i = 1; i < width-1; i++){ //top and bottom edges
            tiles[i][0] = new WallDTile(downwallTileSprite,i,0);
            tiles[i][height-1] = new WallDTile(upwallTileSprite,i,height-1);

        }
        //left and right edges
        for (int j = 1; j < height-1; j++){
            tiles[0][j] = new WallDTile(leftwallTileSprite,0,j);
            tiles[width-1][j] = new WallDTile(rightwallTileSprite,width-1,j);

        }

        for(int i = 1; i < width-1;i++){
            for(int j = 1; j < height-1;j++){
                tiles[i][j] = new EmptyDTile(emptyTileSprite,i,j);
            }
        }

        int sizeModifier = 1;

        if(width > 10){
            sizeModifier = 2;
        }

        int xOffset = 2*sizeModifier;
        int yOffset = 2*sizeModifier;
        int pitLength = 2*sizeModifier;


        for(int k = 0; k < 4; k++){
            switch(k){
                case 1:
                    xOffset = pitLength*3;
                    yOffset = pitLength*1;
                    break;
                case 2:

                    xOffset = pitLength*1;
                    yOffset = pitLength*3;
                    break;
                case 3:
                    xOffset = pitLength*3;
                    yOffset = pitLength*3;
                    break;
            }


            for(int i = xOffset; i < xOffset + pitLength;i++) {
                for(int j = yOffset; j < yOffset + pitLength;j++) {
                    tiles[i][j] = new PitDTile(pitTileSprite,i,j);

                }
            }


        }




        tiles[0][0] = new WallDTile(upleftwallTileSprite,0,0);
        tiles[width-1][0] = new WallDTile(uprightwallTileSprite,width-1,0);
        tiles[0][height-1] = new WallDTile(downleftwallTileSprite,0,height-1);
        tiles[width-1][height-1] = new WallDTile(downrightwallTileSprite,width-1,height-1);

        for(int i = 0; i < width;i++){

            for(int j = 0; j < height;j++){
                if(tiles[i][j] == null){
                    tiles[i][j] = new EmptyDTile(emptyTileSprite,i,j);
                }
            }
        }

        enemySpawnPoints = new DTile[]{
                tiles[2][2],
                tiles[2][height-2],
                tiles[width-2][2],
                tiles[width-2][height-2]

        };
        itemSpawnPoints = new DTile[]{
                tiles[width-2][1],
                tiles[1][height-2],
                tiles[width/2 - 1][height/2]
        };



    }

    @Override
    public DTile[][] getTiles(){
        return tiles;
    }

    @Override
    public int getXOffset(){
        return xOffset;

    }

    @Override
    public int getYOffset(){
        return yOffset;

    }

    @Override
    public int getWidth(){
        return tiles.length;

    }

    @Override
    public int getHeight(){
        return tiles[0].length;

    }

    @Override
    public void setXOffset(int toSet){
        xOffset = toSet;

    }

    @Override
    public void setYOffset(int toSet){
        yOffset = toSet;

    }

    @Override
    public int getBuildDirection(){
        return  buildDirection;
    }


    @Override
    public boolean isLocked(){
        return locked;
    }

    @Override
    public void setLocked(boolean b){
        locked = b;
    }

    @Override
    public DTile[] getEnemySpawnPoints() {
        return enemySpawnPoints;
    }

    @Override
    public DTile[] getItemSpawnPoints() {
        return itemSpawnPoints;
    }

}
