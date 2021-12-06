package edu.oldwestbury.vpajonas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class UnarmedWeapon implements Weapon {
    private String name;
    private int ATK;
    private Bitmap sprite;
    private int range;
    public UnarmedWeapon(Context context){

        BitmapFactory bitmapFac = new BitmapFactory();
        sprite = bitmapFac.decodeResource(context.getResources(),R.drawable.unarmed_icon);
        name = "Unarmed";
        ATK = 10;
        range = 1;
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
        for(int i = 0;i <= range; i++) {
            if (tileSet[x + i][y].getActor() == enemy) {
                return true;

            } else if (tileSet[x - i][y].getActor() == enemy) {
                return true;

            } else if (tileSet[x][y + i].getActor() == enemy) {
                return true;

            } else if (tileSet[x][y - i].getActor() == enemy) {
                return true;
            }
        }



        return false;
    }

}
