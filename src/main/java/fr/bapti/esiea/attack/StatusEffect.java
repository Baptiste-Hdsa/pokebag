package fr.bapti.esiea.attack;

import fr.bapti.esiea.Etat;
import fr.bapti.esiea.monster.PlayerMonster;

public class StatusEffect implements Effect {
    private Etat statusToApply;
    private float chance;

    public StatusEffect(Etat statusToApply, float chance) {
        this.statusToApply = statusToApply;
        this.chance = chance;
    }

    @Override
    public void apply(PlayerMonster attacker, PlayerMonster defender) {
        if (Math.random() < chance) {
            if (defender.getEtat() == Etat.DEFAULT) {
                defender.setEtat(statusToApply);
                System.out.println(defender.getName() + " is now " + statusToApply + "!");
            }
            else if (defender.getEtat() == Etat.PARALYZED && statusToApply == Etat.PARALYZED) {
                defender.setEtat(Etat.PARALYZED);
                System.out.println(defender.getName() + " is again " + statusToApply + "!");
            }
        }
    }
}
