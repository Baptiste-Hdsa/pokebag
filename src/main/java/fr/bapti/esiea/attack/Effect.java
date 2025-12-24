package fr.bapti.esiea.attack;

import fr.bapti.esiea.monster.PlayerMonster;

public interface Effect {

    void apply(PlayerMonster attacker, PlayerMonster defender);
}
