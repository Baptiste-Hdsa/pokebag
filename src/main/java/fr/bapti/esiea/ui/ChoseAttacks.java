package fr.bapti.esiea.ui;

import fr.bapti.esiea.attack.Attack;
import fr.bapti.esiea.monster.Monster;

import java.util.ArrayList;
import java.util.Scanner;

public class ChoseAttacks {
    public ArrayList<Attack> chooseAttacks(Monster monster, ArrayList<Attack> listAttack) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Attack> selected = new ArrayList<>();

        System.out.println("Choose up to 4 attacks for " + monster.getName() + " by entering their number (type 'done' to finish):");
        for (int i = 0; i < listAttack.size(); i++) {
            System.out.println(i + " : " + listAttack.get(i).getName());
        }

        while (selected.size() < 4) {
            System.out.print("Enter index (" + selected.size() + "/4) or 'done': ");
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
            if (idx < 0 || idx >= listAttack.size()) {
                System.out.println("Index out of range. Choose a number between 0 and " + (listAttack.size() - 1) + ".");
                continue;
            }

            Attack chosen = listAttack.get(idx);
            if (selected.contains(chosen)) {
                System.out.println("Attack already selected. Choose a different one.");
                continue;
            }
            selected.add(chosen);
            System.out.println(chosen.getName() + " added. (" + selected.size() + "/4)");
        }

        System.out.println("Selection complete. Selected attacks:");
        for (Attack a : selected) {
            System.out.println("- " + a.getName());
        }

        return selected;
    }
}

