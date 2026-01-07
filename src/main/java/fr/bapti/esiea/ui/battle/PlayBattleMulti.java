package fr.bapti.esiea.ui.battle;

import fr.bapti.esiea.ActionType;
import fr.bapti.esiea.Etat;
import fr.bapti.esiea.player.Player;
import fr.bapti.esiea.Type;
import fr.bapti.esiea.attack.Attack;
import fr.bapti.esiea.item.Item;
import fr.bapti.esiea.monster.PlayerMonster;
import fr.bapti.esiea.utils.Action;

import java.util.Scanner;

public class PlayBattleMulti extends PlayBattle {

    public PlayBattleMulti(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void start() {
        System.out.println("Battle Start! " + player1.getName() + " vs " + player2.getName());

        while (player1.hasAliveMonsters() && player2.hasAliveMonsters()) {
            startTurn();
        }

        if (player1.hasAliveMonsters()) {
            System.out.println(player1.getName() + " wins!");
        } else {
            System.out.println(player2.getName() + " wins!");
        }
    }

    @Override
    public Action askAction(Player player) {
        if (!player.getCurrentMonster().isAlive()) {
            System.out.println(player.getName() + ", " + player.getCurrentMonster().getName() + " is KO! You must switch.");
            return askSwitch(player);
        }

        System.out.println("\n" + player.getName() + ", choose your action:");
        System.out.println("1. Attack");
        System.out.println("2. Item");
        System.out.println("3. Switch");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                return askAttack(player);
            case "2":
                return askItem(player);
            case "3":
                return askSwitch(player);
            default:
                return askAttack(player);
        }
    }

    @Override
    public Action askAttack(Player p) {
        PlayerMonster m = p.getCurrentMonster();
        System.out.println("Choose attack:");
        for (int i = 0; i < m.getAttacks().size(); i++) {
            Attack a = m.getAttacks().get(i);
            System.out.println(i + ": " + a.getName() + " (" + a.getUseCount() + " UseCount, Power: " + a.getPower() + ")");
        }
        try {
            int idx = Integer.parseInt(scanner.nextLine());
            if (idx >= 0 && idx < m.getAttacks().size()) {
                return new Action(ActionType.ATTACK, idx);
            }
        } catch (Exception e) {
        }
        return new Action(ActionType.ATTACK, -1);
    }

    @Override
    public Action askItem(Player p) {
        if (p.getItems().isEmpty()) {
            System.out.println("No items! Attacking instead.");
            return askAttack(p);
        }
        System.out.println("Choose item:");
        for (int i = 0; i < p.getItems().size(); i++) {
            System.out.println(i + ": " + p.getItems().get(i).getName());
        }
        try {
            int idx = Integer.parseInt(scanner.nextLine());
            if (idx >= 0 && idx < p.getItems().size()) {
                return new Action(ActionType.ITEM, idx);
            }
        } catch (Exception e) {
        }
        return askAttack(p);
    }

    public Action askSwitch(Player p) {
        System.out.println("Choose new monster:");
        for (int i = 0; i < p.getMonsters().size(); i++) {
            PlayerMonster m = p.getMonsters().get(i);
            if (m.isAlive() && m != p.getCurrentMonster()) {
                System.out.println(i + ": " + m.getName() + " (HP: " + m.getCurrentHealth() + ")");
            }
        }
        try {
            int idx = Integer.parseInt(scanner.nextLine());
            if (idx >= 0 && idx < p.getMonsters().size() && p.getMonsters().get(idx).isAlive()) {
                return new Action(ActionType.SWITCH, idx);
            }
        } catch (Exception e) {
        }

        if (p.getCurrentMonster().isAlive()) return askAttack(p);
        return new Action(ActionType.SWITCH, -1);
    }

}
