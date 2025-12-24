package fr.bapti.esiea;

import fr.bapti.esiea.attack.Attack;
import fr.bapti.esiea.item.Item;
import fr.bapti.esiea.Player.Player;
import fr.bapti.esiea.monster.Monster;
import fr.bapti.esiea.monster.PlayerMonster;
import fr.bapti.esiea.parser.ParserAttacks;
import fr.bapti.esiea.parser.ParserMonster;
import fr.bapti.esiea.ui.ChoseItems;
import fr.bapti.esiea.ui.ChoseMonsters;
import fr.bapti.esiea.ui.PlayBattle;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Loading game data...");
        ArrayList<Monster> listMonster = ParserMonster.parse();
        ArrayList<Attack> listAttack = ParserAttacks.parse();
        
        if (listMonster.isEmpty() || listAttack.isEmpty()) {
            System.err.println("Error: Could not load monsters or attacks. Check your files.");
            return;
        }

        ChoseMonsters choseMonsters = new ChoseMonsters();
        ChoseItems choseItems = new ChoseItems();

        System.out.println("\n=== PLAYER 1 SETUP ===");
        ArrayList<PlayerMonster> p1Monsters = choseMonsters.chooseMonsters(listMonster, listAttack);
        ArrayList<Item> p1Items = choseItems.chooseItems();
        Player p1 = new Player("Player 1", p1Monsters, p1Items);

        System.out.println("\n=== PLAYER 2 SETUP ===");
        System.out.println("Player 2, please choose your team:");
        ArrayList<PlayerMonster> p2Monsters = choseMonsters.chooseMonsters(listMonster, listAttack);
        ArrayList<Item> p2Items = choseItems.chooseItems();
        Player p2 = new Player("Player 2", p2Monsters, p2Items);

        PlayBattle battle = new PlayBattle(p1, p2);
        battle.start();
    }
}
