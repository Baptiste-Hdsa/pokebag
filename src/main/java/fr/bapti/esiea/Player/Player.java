package fr.bapti.esiea.Player;

import fr.bapti.esiea.monster.PlayerMonster;
import fr.bapti.esiea.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private final String name;
    private final List<PlayerMonster> monsters;
    private final List<Item> items;
    private PlayerMonster currentMonster;

    private static final int MAX_MONSTERS = 3;
    private static final int MAX_ITEMS = 5;

    public Player(String name, ArrayList<PlayerMonster> monsters, ArrayList<Item> items) {
        if (monsters == null) throw new IllegalArgumentException("monsters cannot be null");
        if (items == null) throw new IllegalArgumentException("items cannot be null");
        if (monsters.size() > MAX_MONSTERS) throw new IllegalArgumentException("A player can have at most " + MAX_MONSTERS + " monsters");
        if (items.size() > MAX_ITEMS) throw new IllegalArgumentException("A player can have at most " + MAX_ITEMS + " items");

        this.name = name;
        this.monsters = new ArrayList<>(monsters);
        this.items = new ArrayList<>(items);
        if (!this.monsters.isEmpty()) {
            this.currentMonster = this.monsters.get(0);
        }
    }

    public String getName() {
        return name;
    }

    public List<PlayerMonster> getMonsters() {
        return Collections.unmodifiableList(monsters);
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public PlayerMonster getCurrentMonster() {
        return currentMonster;
    }

    public void setCurrentMonster(PlayerMonster currentMonster) {
        if (currentMonster == null) throw new IllegalArgumentException("currentMonster cannot be null");
        if (!monsters.contains(currentMonster)) throw new IllegalArgumentException("The monster must belong to this player");
        this.currentMonster = currentMonster;
    }

    public boolean hasAliveMonsters() {
        for (PlayerMonster m : monsters) {
            if (m.getCurrentHealth() > 0) {
                return true;
            }
        }
        return false;
    }
}
