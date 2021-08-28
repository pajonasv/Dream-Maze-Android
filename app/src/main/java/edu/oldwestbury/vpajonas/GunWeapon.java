package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GunWeapon implements  Weapon {
    private String name;
    private int ATK;
    private Bitmap sprite;
    private int range;
    public GunWeapon(Context context){

        BitmapFactory bitmapFac = new BitmapFactory();
        sprite = bitmapFac.decodeResource(context.getResources(),R.drawable.gun_icon);
        name = "Unarmed";
        ATK = 1;
        range = 2;
    }

    @Override
    public String getName(){
        return name;
    }
    @Override
    public int getATK(){
        return ATK;
    }
    @Override
    public Bitmap getSprite(){
        return sprite;
    }

    @Override
    public boolean inRange(Player player, Enemy enemy,DTile[][] tileSet) {
        int x = player.getTileX();
        int y = player.getTileY();
        for(int i = 0; i <= range; i++) {
            if(x + i < tileSet.length) {
                if(tileSet[x + i][y] != null) {
                    if (tileSet[x + i][y].getActor() == enemy) {
                        return true;

                    }
                }

                if(y + i < tileSet[player.getTileX()].length) {
                    if(tileSet[x + i][y + i] != null) {
                        if (tileSet[x + i][y + i].getActor() == enemy) {
                            return true;
                        }
                    }
                }
                if(y - i >= 0){
                    if(tileSet[x + i][y - i] != null) {
                        if (tileSet[x + i][y - i].getActor() == enemy) {
                            return true;
                        }
                    }
                }

            }

            if(x - i >= 0){
                if(tileSet[x - i][y] != null) {
                    if (tileSet[x - i][y].getActor() == enemy) {
                        return true;

                    }
                }

                if (y + i < tileSet[player.getTileX()].length) {
                    if (tileSet[x - i][y + i] != null) {
                        if (tileSet[x - i][y + i].getActor() == enemy) {
                            return true;

                        }
                    }
                }

                if(y - i >= 0){
                    if(tileSet[x - i][y - i] != null) {
                        if (tileSet[x - i][y - i].getActor() == enemy) {
                            return true;

                        }
                    }
                }
            }

            if(y + i < tileSet[player.getTileX()].length) {
                if (tileSet[x][y + i].getActor() == enemy) {
                    return true;

                }
            }
            if(y - i >= 0){
                if (tileSet[x][y - i].getActor() == enemy) {
                    return true;

                }
            }



        }




        return false;
    }

}
