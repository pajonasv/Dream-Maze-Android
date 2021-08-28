package edu.oldwestbury.vpajonas;


public interface Room {

    public DTile[][] getTiles();
    public int getXOffset();
    public int getYOffset();
    public int getWidth();
    public int getHeight();
    public void setXOffset(int toSet);
    public void setYOffset(int toSet);
    public int getBuildDirection();
    public boolean isLocked();
    public void setLocked(boolean b);
    public DTile[] getEnemySpawnPoints();
    public DTile[] getItemSpawnPoints();

}
