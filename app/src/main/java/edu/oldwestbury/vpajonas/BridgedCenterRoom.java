package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BridgedCenterRoom implements Room {

    private DTile[][] tiles;
    private int xOffset;
    private int yOffset;
    private int buildDirection;
    private boolean locked;
    private DTile[] enemySpawnPoints;
    private DTile[] itemSpawnPoints;

    private int width;
    private int height;
    //can only be 10x10
    public BridgedCenterRoom(Context context, FloorTheme floorTheme, int passedXOffset, int passedYOffset, int direction){

        xOffset = passedXOffset;
        yOffset = passedYOffset;

        width = 10;
        height = 10;

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

        //outer floor
        for (int i = 1; i < width-1; i++){ //top and bottom edges
            tiles[i][1] = new EmptyDTile(emptyTileSprite,i,1);
            tiles[i][height-2] = new EmptyDTile(emptyTileSprite,i,height-2);

        }
        for (int j = 1; j < height-1; j++){
            tiles[1][j] = new EmptyDTile(emptyTileSprite,1,j);
            tiles[width-2][j] = new EmptyDTile(emptyTileSprite,width-2,j);

        }

        for(int i = 2; i < width-2; i++){
            for (int j = 2; j < height - 2; j++) {
                tiles[i][j] = new PitDTile(pitTileSprite, i, j);
            }
        }
        for(int i = 3; i < width-3;i++){
            for(int j = 3; j < height-3;j++){
                tiles[i][j]  = new EmptyDTile(emptyTileSprite,i,j);
            }
        }
        tiles[4][6]  = new EmptyDTile(emptyTileSprite,4,6);
        tiles[4][7]  = new EmptyDTile(emptyTileSprite,4,6);
        tiles[5][6]  = new EmptyDTile(emptyTileSprite,5,6);
        tiles[5][7]  = new EmptyDTile(emptyTileSprite,5,7);




        tiles[0][0] = new WallDTile(upleftwallTileSprite,0,0);
        tiles[width-1][0] = new WallDTile(uprightwallTileSprite,width-1,0);
        tiles[0][height-1] = new WallDTile(downleftwallTileSprite,0,height-1);
        tiles[width-1][height-1] = new WallDTile(downrightwallTileSprite,width-1,height-1);

        enemySpawnPoints = new DTile[]{
                tiles[1][1],
                tiles[8][8],
                tiles[1][8],
                tiles[8][1]
        };
        itemSpawnPoints = new DTile[]{
                tiles[4][4],
                tiles[4][5],
                tiles[5][4],
                tiles[5][5]
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
