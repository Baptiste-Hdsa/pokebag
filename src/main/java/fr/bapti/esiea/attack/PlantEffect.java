package fr.bapti.esiea.attack;

import fr.bapti.esiea.Etat;
import fr.bapti.esiea.monster.PlayerMonster;

public class PlantEffect implements Effect {

    @Override
    public void apply(PlayerMonster attacker, PlayerMonster defender) {
        if (Math.random() < 0.20) {
            attacker.setEtat(Etat.DEFAULT);
            attacker.setCurrentHealth(attacker.getHealthRange().getPair()[0]);
            System.out.println(attacker.getName() + " heals and cures status!");
        }
    }
}
