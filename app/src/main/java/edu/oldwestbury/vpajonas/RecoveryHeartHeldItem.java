package edu.oldwestbury.vpajonas;

public class RecoveryHeartHeldItem implements HeldItem {
    private String name;
    private String description;
    private boolean usable;
    private PlayerStats playerStats;
    private Player player;

    private int amountHealed;

    public RecoveryHeartHeldItem(PlayerStats passedPlayerStats){
        name = "Recovery Heart";
        description = "Recovers HP.";
        usable = true;
        amountHealed = 3;
        playerStats = passedPlayerStats;
        player = passedPlayerStats.getPlayer();
    }

    @Override
    public void Use() {
        if (usable){
            playerStats.setHP(playerStats.getHP() + amountHealed);

        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
