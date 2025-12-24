package fr.bapti.esiea.ui;

import fr.bapti.esiea.Type;
import fr.bapti.esiea.monster.Monster;
import fr.bapti.esiea.monster.PlayerMonster;
import fr.bapti.esiea.attack.Attack;

import java.util.ArrayList;
import java.util.Scanner;

public class ChoseMonsters {
    private static final int MAX_MONSTERS = 3; // Selon l'énoncé

    public ArrayList<PlayerMonster> chooseMonsters(ArrayList<Monster> listMonster, ArrayList<Attack> listAttack){
        Scanner scanner = new Scanner(System.in);
        ArrayList<PlayerMonster> selected = new ArrayList<>();
        ChoseAttacks choseAttacks = new ChoseAttacks();

        System.out.println("Choose up to " + MAX_MONSTERS + " Monsters by entering their number (type 'done' to finish):");
        for (int i = 0; i < listMonster.size(); i++) {
            System.out.println(i + " : " + listMonster.get(i).getName() + " (" + listMonster.get(i).getType() + ")");
        }

        while (selected.size() < MAX_MONSTERS) {
            System.out.print("Enter index (" + selected.size() + "/" + MAX_MONSTERS + ") or 'done': ");
            String line = scanner.nextLine().trim();

            if (line.equalsIgnoreCase("done")) {
                break;
            }
            int idx;
            try {
                idx = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number or 'done'.");
                continue;
            }
            if (idx < 0 || idx >= listMonster.size()) {
                System.out.println("Index out of range.");
                continue;
            }

            Monster chosen = listMonster.get(idx);

            boolean alreadySelected = false;
            for(PlayerMonster pm : selected) {
                if(pm.getName().equals(chosen.getName())) {
                    alreadySelected = true;
                    break;
                }
            }

            if (alreadySelected) {
                System.out.println("Monster already selected. Choose a different one.");
                continue;
            }

            ArrayList<Attack> availableAttacks = new ArrayList<>();
            for (Attack a : listAttack) {
                if (a.getType() == Type.NORMAL || a.getType() == chosen.getType()) {
                    availableAttacks.add(a);
                }
            }

            ArrayList<Attack> chosenAttacks = choseAttacks.chooseAttacks(chosen, availableAttacks);

            PlayerMonster playerMonster = new PlayerMonster(chosen, chosenAttacks);

            selected.add(playerMonster);
            System.out.println(chosen.getName() + " added. (" + selected.size() + "/" + MAX_MONSTERS + ")");
        }

        if (selected.isEmpty()) {
            System.out.println("No monsters selected, selecting the first available monster by default.");
            Monster m = listMonster.get(0);
            ArrayList<Attack> availableAttacks = new ArrayList<>();
            for (Attack a : listAttack) if (a.getType() == Type.NORMAL || a.getType() == m.getType()) availableAttacks.add(a);
            selected.add(new PlayerMonster(m, choseAttacks.chooseAttacks(m, availableAttacks)));
        }

        return selected;
    }
}

