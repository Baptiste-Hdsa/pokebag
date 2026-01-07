package fr.bapti.esiea.ui.battle;

import fr.bapti.esiea.ActionType;
import fr.bapti.esiea.attack.Attack;
import fr.bapti.esiea.monster.PlayerMonster;
import fr.bapti.esiea.player.Player;
import fr.bapti.esiea.utils.Action;

import java.util.Scanner;

import static fr.bapti.esiea.utils.RandomTools.getRandomInt;

public class PlayBattleSolo extends PlayBattle{
    
    public PlayBattleSolo(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
        this.player2.setHuman(false);
        this.scanner = new Scanner(System.in);
    }
    
    @Override
    public void start() {
        System.out.println("Battle Start! " + player1.getName() + " vs a Robot.");
        
        while (player1.hasAliveMonsters() && player2.hasAliveMonsters()) {
            startTurn();
        }
        
        if(player1.hasAliveMonsters()) {
            System.out.println(player1.getName() + " wins!");
        } else {
            System.out.println("The Robot wins!");
        }
    }


    @Override
    public Action askAction(Player player) {
        if (!player.getCurrentMonster().isAlive()) {
            System.out.println(player.getName() + ", " + player.getCurrentMonster().getName() + " is KO! You must switch.");
            return askSwitch(player);
        }

        if (player.getHuman()) {
            System.out.println("\n" + player.getName() + ", choose your action:");
            System.out.println("1. Attack");
            System.out.println("2. Item");
            System.out.println("3. Switch");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1": return askAttack(player);
                case "2": return askItem(player);
                case "3": return askSwitch(player);
                default: return askAttack(player);
            }
        } else {
            int number = getRandomInt(1,3);
            switch (Integer.toString(number)) {
                case "1": return askAttack(player);
                case "2": return askItem(player);
                case "3": return askSwitch(player);
                default: return askAttack(player);
            }
        }

    }

    @Override
    public Action askAttack(Player p) {
        PlayerMonster m = p.getCurrentMonster();
        if (p.getHuman()) {
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
            } catch (Exception e) {}
        } else {
            try {
                int idx = getRandomInt(0, m.getAttacks().size() - 1);
                if (idx >= 0 && idx < m.getAttacks().size()) {
                    return new Action(ActionType.ATTACK, idx);
                }
            } catch (Exception e) {}
        }

        return new Action(ActionType.ATTACK, -1);
    }

    @Override
    public Action askItem(Player p) {
        if (p.getItems().isEmpty()) {
            System.out.println("No items! Attacking instead.");
            return askAttack(p);
        }
        if (p.getHuman()) {
            System.out.println("Choose item:");
            for (int i = 0; i < p.getItems().size(); i++) {
                System.out.println(i + ": " + p.getItems().get(i).getName());
            }
            try {
                int idx = Integer.parseInt(scanner.nextLine());
                if (idx >= 0 && idx < p.getItems().size()) {
                    return new Action(ActionType.ITEM, idx);
                }
            } catch (Exception e) {}
        } else {
            try {
                int idx = getRandomInt(0, p.getItems().size() - 1);
                if (idx >= 0 && idx < p.getItems().size()) {
                    return new Action(ActionType.ITEM, idx);
                }
            } catch (Exception e) {}
        }

        return askAttack(p);
    }

    @Override
    public Action askSwitch(Player p){
        if (p.getHuman()) {
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
            } catch (Exception e) {}

            if (p.getCurrentMonster().isAlive()) return askAttack(p);
        } else {
            int idx = getRandomInt(0, p.getMonsters().size() - 1);
            if (idx >= 0 && idx < p.getMonsters().size() && p.getMonsters().get(idx).isAlive()) {
                return new Action(ActionType.SWITCH, idx);
            }
        }
        return new Action(ActionType.SWITCH, -1);
    };

}
