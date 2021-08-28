package edu.oldwestbury.vpajonas;

public interface Enemy extends Actor {
    public FloorItem getDroppedItem();
    public Statsheet getStats();
    public void setEnemyInteractMenu(EnemyInteractMenu enemyInteractMenuPassed);
    public void setWillBecomePartner(boolean toSet);
}
