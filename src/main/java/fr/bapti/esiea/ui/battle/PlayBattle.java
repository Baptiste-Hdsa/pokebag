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
    protected PlayerMonster theFlooder;
    protected int floodTurns = 0;

    public abstract void start();
    public void startTurn() {
        System.out.println("\n=== NEW TURN ===");

        if (player1.getCurrentMonster() != null) player1.getCurrentMonster().updateUnderground();
        if (player2.getCurrentMonster() != null) player2.getCurrentMonster().updateUnderground();

        if (isFlooded) {
            floodTurns--;
            System.out.println("The terrain is flooded (" + floodTurns + " turns left).");
            if (floodTurns <= 0) {
                isFlooded = false;
                theFlooder = null;
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
            if (!second.getCurrentMonster().isAlive()) {
                handleKOSwitch(second);
            }
        }

        if (secondAction.type == ActionType.ATTACK && second.getCurrentMonster().isAlive()) {
            executeAttack(second, first, secondAction);
            if (!first.getCurrentMonster().isAlive()) {
                handleKOSwitch(first);
            }
        }

        handleKOSwitch(player1);
        handleKOSwitch(player2);

        applyEndOfTurnEffects(player1);
        applyEndOfTurnEffects(player2);
    }

    protected void handleKOSwitch(Player p) {
        PlayerMonster ko = p.getCurrentMonster();
        if (ko != null && !ko.isAlive()) {
            System.out.println(p.getName() + "'s " + ko.getName() + " is KO!");
            p.removeMonster(ko);
            if (isFlooded && theFlooder == ko) {
                isFlooded = false;
                theFlooder = null;
                floodTurns = 0;
                System.out.println("The flood ends as the flooding monster leaves the field.");
            }
            if (p.hasAliveMonsters()) {
                System.out.println(p.getName() + ", choose a new monster to send out.");
                Action forced = askSwitch(p);
                while (forced.type != ActionType.SWITCH || forced.index < 0 || forced.index >= p.getMonsters().size() || !p.getMonsters().get(forced.index).isAlive()) {
                    System.out.println("Invalid choice. You must select a living monster.");
                    forced = askSwitch(p);
                }
                executeSwitch(p, forced);
            } else {
                System.out.println(p.getName() + " has no monsters left!");
            }
        }
    }

    public void applyEndOfTurnEffects(Player p) {
        PlayerMonster m = p.getCurrentMonster();
        if (m == null || !m.isAlive()) return;

        if (isFlooded && m.getType().isNature()) {
            int heal = Math.max(1, m.getCurrentHealth() / 20);
            m.IncreaseHealth(heal);
            System.out.println(m.getName() + " regenerates health thanks to the flood!");
        }
        if (isFlooded) {
            if (m.getEtat() == Etat.BURNED || m.getEtat() == Etat.POISONED) {
                m.setEtat(Etat.DEFAULT);
                System.out.println(m.getName() + " is cured by the water!");
            }
        }

    }

    public abstract Action askAction(Player player);
    public abstract Action askAttack(Player p);
    public abstract Action askItem(Player p);
    public abstract Action askSwitch(Player p);

    public void executeSwitch(Player p, Action a) {
        if (a.index >= 0) {

            if (isFlooded && theFlooder != null && p.getCurrentMonster() == theFlooder) {
                isFlooded = false;
                theFlooder = null;
                floodTurns = 0;
                System.out.println("The flood ends as the flooding monster leaves the field.");
            }

            p.setCurrentMonster(p.getMonsters().get(a.index));
            System.out.println(p.getName() + " sends out " + p.getCurrentMonster().getName() + "!");
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
        if (!attM.isAlive()) {40 60
1. Raichu (ELECTRIC, Attack : 75 95, Health : 110 130, Defense :55 75
            handleKOSwitch(attacker);
            return;
        }

        if (!attM.getEtat().canAttack(attM)) return;

        if (isFlooded) {
            if (attM.getType() != Type.WATER) {
                float slipProb = (theFlooder != null) ? theFlooder.getFall() : 0.25f;
                if (Math.random() < slipProb) {
                    System.out.println(attM.getName() + " slipped on the water!");
                    attM.DecreaseHealth(Math.max(1, attM.getAttackStat() / 4));
                    if (!attM.isAlive()) handleKOSwitch(attacker);
                    return;
                }
            }
        }

        if (a.index == -1) {
            Attack.useUnarmed(attM, defM);
        } else {
            Attack attack = attM.getAttacks().get(a.index);
            boolean success = attack.use(attM, defM);

            if (success) {
                handleGlobalEffects(attM, defM, attack);
            }
        }
    }

    public void handleGlobalEffects(PlayerMonster attacker, PlayerMonster defender, Attack attack) {
        if (!isFlooded && attacker.getType() == Type.WATER && attack.getType() == Type.WATER) {
            if (Math.random() < attacker.getFlood()) {
                theFlooder = attacker;
                isFlooded = true;
                floodTurns = (int)(1 + Math.random() * 3);
                System.out.println("The terrain is flooded for " + floodTurns + " turns by " +  attacker.getName() + "!");
            }
        }

        if (attacker.getType() == Type.ELECTRIC && attack.getType() == Type.ELECTRIC) {
            if (Math.random() < attacker.getParalysis()) {
                if (defender.getEtat() != Etat.PARALYZED) {
                    defender.setEtat(Etat.PARALYZED);
                    defender.resetParalyzedTurns();
                    System.out.println(defender.getName() + " is now PARALYZED!");
                } else {
                    defender.resetParalyzedTurns();
                    System.out.println(defender.getName() + " is paralyzed again!");
                }
            }
        }

        if (attacker.getType() == Type.INSECT && attack.getType() == Type.INSECT) {
            attacker.incrementInsectSpecialCount();
            if (attacker.getInsectSpecialCount() % 3 == 0) {
                defender.setEtat(Etat.POISONED);
                System.out.println(defender.getName() + " is POISONED!");
            }
        }
    }
}
