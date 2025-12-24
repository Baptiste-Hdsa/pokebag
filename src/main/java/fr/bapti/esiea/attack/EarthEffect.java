package fr.bapti.esiea.attack;

import fr.bapti.esiea.monster.PlayerMonster;

public class EarthEffect implements Effect {

    @Override
    public void apply(PlayerMonster attacker, PlayerMonster defender) {
        if (Math.random() < 0.05) {
            int turns = (int)(1 + Math.random() * 3);
            attacker.goUnderground(turns);
        }
    }
}
