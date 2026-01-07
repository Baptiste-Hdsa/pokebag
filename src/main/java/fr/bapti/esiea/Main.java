package fr.bapti.esiea;

import fr.bapti.esiea.attack.Attack;
import fr.bapti.esiea.item.Item;
import fr.bapti.esiea.monster.Monster;
import fr.bapti.esiea.monster.PlayerMonster;
import fr.bapti.esiea.parser.ParserAttacks;
import fr.bapti.esiea.parser.ParserMonster;
import fr.bapti.esiea.ui.ChooseMonsters;
import fr.bapti.esiea.ui.ChoseItems;
import fr.bapti.esiea.ui.battle.PlayBattleMulti;
import fr.bapti.esiea.player.Player;
import fr.bapti.esiea.ui.battle.PlayBattleSolo;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ArrayList<Monster> listMonster = ParserMonster.parse();
        ArrayList<Attack> listAttack = ParserAttacks.parse();
        Player p1;
        Player p2;
        
        if (listMonster.isEmpty() || listAttack.isEmpty()) {
            System.err.println("Error: Could not load monsters or attacks. Check your files.");
            return;
        }

        ChooseMonsters chooseMonsters = new ChooseMonsters();
        ChoseItems choseItems = new ChoseItems();
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nSOLO OR MULTI? \n1. Solo \n2. Multi\n");
        int answer = scanner.nextInt();

        if (answer == 2) {
            System.out.println("\n=== PLAYER 1 SETUP ===");
            ArrayList<PlayerMonster> p1Monsters = chooseMonsters.chooseMonsters(listMonster, listAttack, true);
            ArrayList<Item> p1Items = choseItems.chooseItems(true);
            p1 = new Player("Player1", p1Monsters, p1Items);

            System.out.println("\n=== PLAYER 2 SETUP ===");
            System.out.println("\nChose your name");
            String name2 = scanner.nextLine();
            ArrayList<PlayerMonster> p2Monsters = chooseMonsters.chooseMonsters(listMonster, listAttack, true);
            ArrayList<Item> p2Items = choseItems.chooseItems(true);
            p2 = new Player("Player2", p2Monsters, p2Items);

            PlayBattleMulti battle = new PlayBattleMulti(p1, p2);
            battle.start();
        } else {
            System.out.println("\n=== PLAYER SETUP ===");
            ArrayList<PlayerMonster> p1Monsters = chooseMonsters.chooseMonsters(listMonster, listAttack, true);
            ArrayList<Item> p1Items = choseItems.chooseItems(true);
            p1 = new Player("Player", p1Monsters, p1Items);

            ArrayList<PlayerMonster> p2Monsters = chooseMonsters.chooseMonsters(listMonster, listAttack, false);
            ArrayList<Item> p2Items = choseItems.chooseItems(false);
            p2 = new Player("Robot", p2Monsters, p2Items);

            PlayBattleSolo battle = new PlayBattleSolo(p1, p2);
            battle.start();
        }
    }
}
