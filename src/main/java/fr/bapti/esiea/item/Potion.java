package fr.bapti.esiea.item;

import fr.bapti.esiea.Etat;
import fr.bapti.esiea.monster.PlayerMonster;

public class Potion implements Item {
    private String name;
    private Etat targetStatus;

    public Potion(String name, Etat targetStatus) {
        this.name = name;
        this.targetStatus = targetStatus;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void use(PlayerMonster monster) {
        if (monster.getEtat() == targetStatus) {
            monster.setEtat(Etat.DEFAULT);
            System.out.println(monster.getName() + " is cured of " + targetStatus + " thanks to " + name + "!");
        } else {
            System.out.println(name + " had no effect on " + monster.getName());
        }
    }
}
