package edu.oldwestbury.vpajonas;

import android.content.Context;

import java.util.Vector;

public class PlayerStats implements Statsheet {
    private int maxHP;
    private int HP;
    private int ATK;
    private int DEF;
    private int keys;
    private int money;
    private Vector<Weapon> weapons;
    private int currentWeapon;
    private Player player;

    private HeldItem[] items;

    public PlayerStats(Context context, Player passedPlayer){
        player = passedPlayer;
        maxHP = 50;
        HP = maxHP;
        ATK = 1;
        DEF = 1;


        keys = 0;
        money = 0;
        weapons = new Vector<>();
        weapons.add(new UnarmedWeapon(context));
        weapons.add(new GunWeapon(context));
        currentWeapon = 0;

        items = new HeldItem[50];
    }
    @Override
    public int getHP(){
        return HP;
    }

    @Override
    public void setHP(int toSet){
        if(toSet <= maxHP && toSet > 0) {
            HP = toSet;
        }
        else if(toSet <= 0){
            HP = 0;
        }
        else{
            HP = maxHP;
        }
    }
    @Override
    public int getMaxHP(){
        return maxHP;
    }



    @Override
    public int getATK(){

        return ATK+weapons.elementAt(currentWeapon).getATK();
    }

    @Override
    public void setATK(int toSet){
        if(toSet >= 0){
            ATK = toSet;
        }
        else{
            ATK = 0;
        }
    }

    @Override
    public int getDEF(){
        return DEF;
    }

    @Override
    public void setDEF(int toSet){
        if(toSet >= 0){
            DEF = toSet;
        }
        else{
            DEF = 0;
        }
    }

    public void incKeys(){
        keys++;
    }
    public void decKeys(){
        keys--;
    }
    public int getKeys(){
        return keys;
    }
    public int getMoney(){
        return money;
    }
    public void incMoney(int modifier){
        money += modifier;
    }

    public Vector<Weapon> getWeapons(){
        return weapons;
    }
    public Weapon getCurrentWeapon(){
        return weapons.elementAt(currentWeapon);
    }
    public void nextWeapon(){
        currentWeapon++;
        if (currentWeapon >= weapons.size()){
            currentWeapon = 0;
        }
    }
    public void addItem(HeldItem item){
        int current = 0;
        while(items[current] != null){
            current++;
            if (current >= items.length){
                return;
            }
        }
        items[current] = item;

    }

    public String getAllItemNames(){
        String toReturn = "";
        int current = 0;
        while(items[current] != null){
            toReturn += items[current].getName() + '^';
            current++;
            if (current >= items.length){
              break;
            }
        }

        return toReturn;
    }

    public Player getPlayer(){
        return player;
    }

    public HeldItem[] getItems(){
        return items;
    }

    public void useItem(int index){
        items[index].Use();

        for(int i = index; i < items.length-1;i++){
            items[i] = items[i+1];
        }

    }
}
