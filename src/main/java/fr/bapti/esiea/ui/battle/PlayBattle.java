package fr.bapti.esiea.ui.battle;


import fr.bapti.esiea.ActionType;
import fr.bapti.esiea.Etat;
import fr.bapti.esiea.Type;
import fr.bapti.esiea.attack.Attack;
import fr.bapti.esiea.item.Item;
import fr.bapti.esiea.monster.PlayerMonster;
import fr.bapti.esiea.player.Player;
import fr.bapti.esiea.utils.Action;

import java.util.Scanner;

public abstract class PlayBattle {
    protected Player player1;
    protected Player player2;
    protected Scanner scanner;

    protected boolean isFlooded = false;
    protected int floodTurns = 0;

    public abstract void start();
    public void startTurn() {
        System.out.println("\n=== NEW TURN ===");

        if (isFlooded) {
            floodTurns--;
            System.out.println("The terrain is flooded (" + floodTurns + " turns left).");
            if (floodTurns <= 0) {
                isFlooded = false;
                System.out.println("The water drained away.");
            }
        }
        Action action1 = askAction(player1);
        Action action2 = askAction(player2);

        if (action1.type == ActionType.SWITCH) executeSwitch(player1, action1);
        if (action2.type == ActionType.SWITCH) executeSwitch(player2, action2);
        if (action1.type == ActionType.ITEM) executeItem(player1, action1);
        if (action2.type == ActionType.ITEM) executeItem(player2, action2);

        Player first = player1;
        Player second = player2;
        Action firstAction = action1;
        Action secondAction = action2;

        if (action1.type == ActionType.ATTACK && action2.type == ActionType.ATTACK) {
            if (player2.getCurrentMonster().getSpeedStat() > player1.getCurrentMonster().getSpeedStat()) {
                first = player2; second = player1;
                firstAction = action2; secondAction = action1;
            }
        } else if (action2.type == ActionType.ATTACK && action1.type != ActionType.ATTACK) {

            first = player2; second = player1;
            firstAction = action2; secondAction = action1;
        }

        if (firstAction.type == ActionType.ATTACK && first.getCurrentMonster().isAlive()) {
            executeAttack(first, second, firstAction);
        }

        if (secondAction.type == ActionType.ATTACK && second.getCurrentMonster().isAlive()) {
            executeAttack(second, first, secondAction);
        }

        applyEndOfTurnEffects(player1);
        applyEndOfTurnEffects(player2);
    }

    public void applyEndOfTurnEffects(Player p) {
        PlayerMonster m = p.getCurrentMonster();
        if (!m.isAlive()) return;

        if (isFlooded && (m.getType() == Type.GRASS || m.getType() == Type.INSECT)) {
            int heal = m.getCurrentHealth() / 20;
            m.IncreaseHealth(Math.max(5, heal));
            System.out.println(m.getName() + " regenerates health thanks to the flood!");

            if (m.getType() == Type.INSECT && m.getEtat() == Etat.POISONED) {
                m.setEtat(Etat.DEFAULT);
                System.out.println(m.getName() + " washes away the poison!");
            }
        }

        if (isFlooded && m.getEtat() == Etat.BURNED) {
            m.setEtat(Etat.DEFAULT);
            System.out.println(m.getName() + " is cooled down by the water!");
        }
    }

    public abstract Action askAction(Player player);
    public abstract Action askAttack(Player p);
    public abstract Action askItem(Player p);
    public abstract Action askSwitch(Player p);

    public void executeSwitch(Player p, Action a) {
        if (a.index >= 0) {
            p.setCurrentMonster(p.getMonsters().get(a.index));
            System.out.println(p.getName() + " sends out " + p.getCurrentMonster().getName() + "!");

            if (isFlooded && p.getCurrentMonster().getType() != Type.WATER) {
            }
        }
    }

    public void executeItem(Player p, Action a) {
        if (a.index >= 0 && a.index < p.getItems().size()) {
            Item item = p.getItems().remove(a.index);
            item.use(p.getCurrentMonster());
        }
    }

    public void executeAttack(Player attacker, Player defender, Action a) {
        PlayerMonster attM = attacker.getCurrentMonster();
        PlayerMonster defM = defender.getCurrentMonster();

        attM.getEtat().onTurnStart(attM);
        if (!attM.isAlive()) return;

        if (!attM.getEtat().canAttack(attM)) return;

        if (isFlooded) {
            if (Math.random() < 0.25) {
                System.out.println(attM.getName() + " slipped on the water!");
                attM.DecreaseHealth(attM.getAttackStat() / 4);
                return;
            }
        }

        if (a.index == -1) {
            Attack.useUnarmed(attM, defM);
        } else {
            Attack attack = attM.getAttacks().get(a.index);
            attack.use(attM, defM);

            handleGlobalEffects(attM, attack);
        }
    }

    public void handleGlobalEffects(PlayerMonster attacker, Attack attack) {
        if (attacker.getType() == Type.WATER && attack.getType() == Type.WATER) {
            if (Math.random() < attacker.getFlood()) {
                isFlooded = true;
                floodTurns = (int)(1 + Math.random() * 3);
                System.out.println("The terrain is flooded for " + floodTurns + " turns!");
            }
        }
    }
}
