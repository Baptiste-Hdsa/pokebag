package fr.bapti.esiea.ui.battle;

import fr.bapti.esiea.ActionType;
import fr.bapti.esiea.Etat;
import fr.bapti.esiea.Type;
import fr.bapti.esiea.attack.Attack;
import fr.bapti.esiea.item.Drug;
import fr.bapti.esiea.item.Item;
import fr.bapti.esiea.item.Potion;
import fr.bapti.esiea.monster.PlayerMonster;
import fr.bapti.esiea.player.Player;
import fr.bapti.esiea.utils.Action;

import java.util.Scanner;

public class PlayBattleSolo extends PlayBattle{
    
    public PlayBattleSolo(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
        this.player2.setHuman(false);
        this.scanner = new Scanner(System.in);
    }
    
    private Player opponentOf(Player p) { return (p == player1) ? player2 : player1; }

    private float expectedDamage(Attack a, PlayerMonster attacker, PlayerMonster defender) {
        if (a.getUseCount() <= 0) return 0f;
        float atk = attacker.getAttackStat();
        float def = Math.max(1, defender.getDefenseStat());
        float avgRand = (0.85f + 1f) / 2f;
        float advantage = (float) a.getType().getAvantage(defender.getType());
        float base = ((11f * atk * a.getPower()) / (25f * def) + 2f);
        float dmg = base * avgRand * advantage * (1f - a.getFailRate());
        if (a.getType() == attacker.getType()) dmg *= 1.05f;
        if (isFlooded && attacker.getType() != Type.WATER) {
            float slipProb = (theFlooder != null) ? theFlooder.getFall() : 0.25f;
            dmg *= (1f - slipProb);
        }
        return Math.max(0f, dmg);
    }

    private int chooseBestAttackIndex(Player p) {
        PlayerMonster m = p.getCurrentMonster();
        PlayerMonster enemy = opponentOf(p).getCurrentMonster();
        float bestScore = -1f;
        int bestIdx = -1;
        for (int i = 0; i < m.getAttacks().size(); i++) {
            Attack a = m.getAttacks().get(i);
            float score = expectedDamage(a, m, enemy);
            if (score > bestScore) { bestScore = score; bestIdx = i; }
        }
        return bestIdx;
    }

    private boolean hasBetterSwitchOption(Player p) {
        PlayerMonster enemy = opponentOf(p).getCurrentMonster();
        Type enemyType = enemy.getType();
        Type currentType = p.getCurrentMonster().getType();
        boolean currentWeak = currentType.isWeakAgainst(enemyType);
        boolean currentStrong = currentType.isStrongAgainst(enemyType);

        for (PlayerMonster cand : p.getMonsters()) {
            if (cand == p.getCurrentMonster() || !cand.isAlive()) continue;
            if (cand.getType().isStrongAgainst(enemyType)) return true;
        }
        if (currentWeak) {
            for (PlayerMonster cand : p.getMonsters()) {
                if (cand == p.getCurrentMonster() || !cand.isAlive()) continue;
                if (!cand.getType().isWeakAgainst(enemyType)) return true;
            }
        }
        if (isFlooded && p.getCurrentMonster().getType() != Type.WATER) {
            for (PlayerMonster cand : p.getMonsters()) {
                if (cand == p.getCurrentMonster() || !cand.isAlive()) continue;
                if (cand.getType() == Type.WATER) return true;
            }
        }
        return false;
    }

    private int chooseBestSwitchIndex(Player p) {
        PlayerMonster enemy = opponentOf(p).getCurrentMonster();
        Type enemyType = enemy.getType();
        int bestIdx = -1;
        int bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < p.getMonsters().size(); i++) {
            PlayerMonster cand = p.getMonsters().get(i);
            if (cand == p.getCurrentMonster() || !cand.isAlive()) continue;
            int score = 0;
            if (cand.getType().isStrongAgainst(enemyType)) score += 3;
            if (cand.getType().isWeakAgainst(enemyType)) score -= 3;
            score += Math.min(2, cand.getSpeedStat() / 50);
            score += Math.min(2, cand.getCurrentHealth() / 50);
            if (isFlooded && cand.getType() == Type.WATER) score += 2;
            if (score > bestScore) { bestScore = score; bestIdx = i; }
        }
        return bestIdx;
    }


    private String switchReason(Player p, PlayerMonster candidate) {
        PlayerMonster enemy = opponentOf(p).getCurrentMonster();
        Type enemyType = enemy.getType();
        Type currentType = p.getCurrentMonster().getType();
        if (isFlooded && p.getCurrentMonster().getType() != Type.WATER && candidate.getType() == Type.WATER)
            return "to avoid slipping on the flooded terrain";
        if (!currentType.isStrongAgainst(enemyType) && candidate.getType().isStrongAgainst(enemyType))
            return "to gain type advantage";
        if (currentType.isWeakAgainst(enemyType) && !candidate.getType().isWeakAgainst(enemyType))
            return "to avoid being at a type disadvantage";
        return "for a better overall option";
    }

    @Override
    public void start() {
        System.out.println("Battle Start! " + player1.getName() + " vs a Bot.");

        while (player1.hasAliveMonsters() && player2.hasAliveMonsters()) {
            startTurn();
        }
        
        if(player1.hasAliveMonsters()) {
            System.out.println(player1.getName() + " wins!");
        } else {
            System.out.println("The Bot wins!");
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
            PlayerMonster me = player.getCurrentMonster();
            PlayerMonster foe = opponentOf(player).getCurrentMonster();
            float hpRatio = (float) me.getCurrentHealth() / Math.max(1, me.getHealthRange().getPair()[1]);

            if (isFlooded && me.getType() != Type.WATER && hasBetterSwitchOption(player)) {
                int idx = chooseBestSwitchIndex(player);
                if (idx >= 0) {
                    PlayerMonster cand = player.getMonsters().get(idx);
                    System.out.println("Bot: switches to " + cand.getName() + " (" + switchReason(player, cand) + ")");
                    return new Action(ActionType.SWITCH, idx);
                }
            }

            if (hasBetterSwitchOption(player)) {
                int idx = chooseBestSwitchIndex(player);
                if (idx >= 0) {
                    PlayerMonster cand = player.getMonsters().get(idx);
                    System.out.println("Bot: switches to " + cand.getName() + " (" + switchReason(player, cand) + ")");
                    return new Action(ActionType.SWITCH, idx);
                }
            }

            if (me.getEtat() != Etat.DEFAULT) {
                Action maybeCure = askItem(player);
                if (maybeCure.type == ActionType.ITEM) return maybeCure;
            }
            if (hpRatio <= 0.35f) {
                Action maybeHeal = askItem(player);
                if (maybeHeal.type == ActionType.ITEM) return maybeHeal;
            }

            return askAttack(player);
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
            } catch (Exception e) {
            }
        } else {
            int idx = chooseBestAttackIndex(p);
            if (idx >= 0) {
                PlayerMonster enemy = opponentOf(p).getCurrentMonster();
                Attack a = m.getAttacks().get(idx);
                float est = expectedDamage(a, m, enemy);
                return new Action(ActionType.ATTACK, idx);
            } else {
                System.out.println("Bot: no usable attack left, uses unarmed attack.");
            }
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
            } catch (Exception e) {
            }
        } else {
            PlayerMonster me = p.getCurrentMonster();
            for (int i = 0; i < p.getItems().size(); i++) {
                Item it = p.getItems().get(i);
                if (it instanceof Potion potion) {
                    if (me.getEtat() != Etat.DEFAULT && potion.getTargetStatus() == me.getEtat()) {
                        System.out.println("Bot: uses item " + it.getName() + " to cure status " + me.getEtat());
                        return new Action(ActionType.ITEM, i);
                    }
                }
            }
            float hpRatio = (float) me.getCurrentHealth() / Math.max(1, me.getHealthRange().getPair()[1]);
            if (hpRatio <= 0.35f) {
                for (int i = 0; i < p.getItems().size(); i++) {
                    if (p.getItems().get(i) instanceof Drug) {
                        System.out.println("Bot: uses a healing item to recover HP");
                        return new Action(ActionType.ITEM, i);
                    }
                }
            }
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
            } catch (Exception e) {
            }

            if (p.getCurrentMonster().isAlive()) return askAttack(p);
        } else {
            int idx = chooseBestSwitchIndex(p);
            if (idx >= 0) {
                PlayerMonster cand = p.getMonsters().get(idx);
                System.out.println("Bot: sends out " + cand.getName() + " (" + switchReason(p, cand) + ")");
                return new Action(ActionType.SWITCH, idx);
            }
        }
        return new Action(ActionType.SWITCH, -1);
    }

}
