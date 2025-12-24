package fr.bapti.esiea.item;

import fr.bapti.esiea.monster.PlayerMonster;

public class Drug implements Item {
    private String name;
    private int healAmount;

    public Drug(String name, int healAmount) {
        this.name = name;
        this.healAmount = healAmount;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void use(PlayerMonster monster) {
        monster.IncreaseHealth(healAmount);
        System.out.println(monster.getName() + " uses " + name + " and recovers " + healAmount + " HP.");
    }
}
