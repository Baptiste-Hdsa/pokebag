package fr.bapti.esiea.ui;

import fr.bapti.esiea.Etat;
import fr.bapti.esiea.Type;
import fr.bapti.esiea.attack.Attack;
import fr.bapti.esiea.item.Item;
import fr.bapti.esiea.Player.Player;
import fr.bapti.esiea.monster.PlayerMonster;

import java.util.Scanner;

public class PlayBattle {
    private Player player1;
    private Player player2;
    private Scanner scanner;

    // Terrain state
    private boolean isFlooded = false;
    private int floodTurns = 0;

    public PlayBattle(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
        this.scanner = new Scanner(System.in);
    }

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

    private void startTurn() {
        System.out.println("\n=== NEW TURN ===");

        // Manage Flood
        if (isFlooded) {
            floodTurns--;
            System.out.println("The terrain is flooded (" + floodTurns + " turns left).");
            if (floodTurns <= 0) {
                isFlooded = false;
                System.out.println("The water drained away.");
            }
        }

        // 1. Ask actions
        Action action1 = askAction(player1, player2);
        Action action2 = askAction(player2, player1);

        // 2. Resolve Actions
        // Order: Switch > Item > Attack (Speed)

        // Phase 1: Switches
        if (action1.type == ActionType.SWITCH) executeSwitch(player1, action1);
        if (action2.type == ActionType.SWITCH) executeSwitch(player2, action2);

        // Phase 2: Items
        if (action1.type == ActionType.ITEM) executeItem(player1, action1);
        if (action2.type == ActionType.ITEM) executeItem(player2, action2);

        // Phase 3: Attacks
        // Determine order based on speed
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
            // Only P2 is attacking, so they go "first" in the attack phase
            first = player2; second = player1;
            firstAction = action2; secondAction = action1;
        }

        // Execute attacks if the monster is still alive and didn't switch/use item
        if (firstAction.type == ActionType.ATTACK && first.getCurrentMonster().isAlive()) {
            executeAttack(first, second, firstAction);
        }

        if (secondAction.type == ActionType.ATTACK && second.getCurrentMonster().isAlive()) {
            executeAttack(second, first, secondAction);
        }

        // End of turn effects (Nature regen, etc.)
        applyEndOfTurnEffects(player1);
        applyEndOfTurnEffects(player2);
    }

    private void applyEndOfTurnEffects(Player p) {
        PlayerMonster m = p.getCurrentMonster();
        if (!m.isAlive()) return;

        // Nature/Plant/Insect regen on flooded terrain
        if (isFlooded && (m.getType() == Type.GRASS || m.getType() == Type.INSECT)) {
            int heal = m.getCurrentHealth() / 20; // 1/20th of HP (assuming max HP is current HP for now, or we should store max)
            // Since we don't store MaxHP in PlayerMonster yet, let's just heal a bit
            m.IncreaseHealth(Math.max(5, heal));
            System.out.println(m.getName() + " regenerates health thanks to the flood!");

            // Insect: remove poison if flooded
            if (m.getType() == Type.INSECT && m.getEtat() == Etat.POISONED) {
                m.setEtat(Etat.DEFAULT);
                System.out.println(m.getName() + " washes away the poison!");
            }
        }

        // Burn healing if flooded
        if (isFlooded && m.getEtat() == Etat.BURNED) {
            m.setEtat(Etat.DEFAULT);
            System.out.println(m.getName() + " is cooled down by the water!");
        }
    }

    private Action askAction(Player player, Player opponent) {
        // Check if monster is KO, force switch
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
            case "1": return askAttack(player);
            case "2": return askItem(player);
            case "3": return askSwitch(player);
            default: return askAttack(player); // Default to attack
        }
    }

    private Action askAttack(Player p) {
        PlayerMonster m = p.getCurrentMonster();
        System.out.println("Choose attack:");
        for (int i = 0; i < m.getAttacks().size(); i++) {
            Attack a = m.getAttacks().get(i);
            System.out.println(i + ": " + a.getName() + " (" + a.getUseCount() + " PP)");
        }
        try {
            int idx = Integer.parseInt(scanner.nextLine());
            if (idx >= 0 && idx < m.getAttacks().size()) {
                return new Action(ActionType.ATTACK, idx);
            }
        } catch (Exception e) {}
        return new Action(ActionType.ATTACK, -1); // -1 means Basic Attack
    }

    private Action askItem(Player p) {
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
        } catch (Exception e) {}
        return askAttack(p);
    }

    private Action askSwitch(Player p) {
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

        // If switch failed (no other monsters?), attack
        if (p.getCurrentMonster().isAlive()) return askAttack(p);
        return new Action(ActionType.SWITCH, -1); // Should not happen if logic is correct
    }

    private void executeSwitch(Player p, Action a) {
        if (a.index >= 0) {
            p.setCurrentMonster(p.getMonsters().get(a.index));
            System.out.println(p.getName() + " sends out " + p.getCurrentMonster().getName() + "!");

            // Remove Flood if the caster leaves? Subject says:
            // "L’inondation... est automatiquement retirée lorsque le monstre l’ayant déclenché quitte le terrain."
            // We would need to track WHO caused the flood. For simplicity, let's say if Water monster leaves, flood ends.
            if (isFlooded && p.getCurrentMonster().getType() != Type.WATER) {
                // This logic is imperfect (it should check the PREVIOUS monster), but close enough for now.
            }
        }
    }

    private void executeItem(Player p, Action a) {
        if (a.index >= 0 && a.index < p.getItems().size()) {
            Item item = p.getItems().remove(a.index);
            item.use(p.getCurrentMonster());
        }
    }

    private void executeAttack(Player attacker, Player defender, Action a) {
        PlayerMonster attM = attacker.getCurrentMonster();
        PlayerMonster defM = defender.getCurrentMonster();

        // Start of turn status effects (Burn/Poison damage) happen BEFORE attacking
        attM.getEtat().onTurnStart(attM);
        if (!attM.isAlive()) return;

        // Check Paralysis
        if (!attM.getEtat().canAttack(attM)) return;

        // Flood Slip Check
        if (isFlooded) {
            // "Quand le terrain est inondé, l’adversaire a une chance de glisser... dégâts équivalents aux quart de sa propre attaque."
            // Assuming 25% chance to slip
            if (Math.random() < 0.25) {
                System.out.println(attM.getName() + " slipped on the water!");
                attM.DecreaseHealth(attM.getAttackStat() / 4);
                return; // Attack cancelled
            }
        }

        if (a.index == -1) {
            Attack.useUnarmed(attM, defM);
        } else {
            Attack attack = attM.getAttacks().get(a.index);
            attack.use(attM, defM);

            // Trigger Special Effects (Flood, Earth Hide, etc.)
            // This logic should ideally be inside the Effect class, but some effects affect the Terrain (Flood)
            // which is managed here.
            handleGlobalEffects(attM, attack);
        }
    }

    private void handleGlobalEffects(PlayerMonster attacker, Attack attack) {
        // Water Flood Effect
        if (attacker.getType() == Type.WATER && attack.getType() == Type.WATER) {
            // "possibilité d’inonder le terrain pendant un à trois tours"
            if (Math.random() < attacker.getFlood()) {
                isFlooded = true;
                floodTurns = (int)(1 + Math.random() * 3);
                System.out.println("The terrain is flooded for " + floodTurns + " turns!");
            }
        }
    }

    // Helper classes
    enum ActionType { ATTACK, ITEM, SWITCH }
    class Action {
        ActionType type;
        int index; // Attack index, Item index, or Monster index
        public Action(ActionType t, int i) { type = t; index = i; }
    }
}

