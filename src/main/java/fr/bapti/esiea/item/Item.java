package fr.bapti.esiea.item;

import fr.bapti.esiea.monster.PlayerMonster;

public interface Item {
    String getName();
    void use(PlayerMonster monster);
}
