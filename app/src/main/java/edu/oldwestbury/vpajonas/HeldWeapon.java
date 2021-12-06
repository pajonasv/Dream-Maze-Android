package edu.oldwestbury.vpajonas;

public class HeldWeapon implements HeldItem {

    private String name;
    private String description;
    private boolean usable;
    private PlayerStats playerStats;
    private Player player;
    private Weapon weapon;

    public HeldWeapon(PlayerStats passedPlayerStats, Weapon weaponPassed){
        weapon = weaponPassed;
        name = weapon.getName();
        description = "Use to equip";
        usable = true;
        playerStats = passedPlayerStats;
        player = passedPlayerStats.getPlayer();

    }

    @Override
    public void Use() {
        if(usable) {
            playerStats.addWeapon(weapon);
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
