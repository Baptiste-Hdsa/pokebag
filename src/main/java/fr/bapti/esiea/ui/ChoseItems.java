package fr.bapti.esiea.ui;

import fr.bapti.esiea.Etat;
import fr.bapti.esiea.item.Item;
import fr.bapti.esiea.item.Drug;
import fr.bapti.esiea.item.Potion;
import fr.bapti.esiea.monster.PlayerMonster;

import java.util.ArrayList;
import java.util.Scanner;

import static fr.bapti.esiea.utils.RandomTools.getRandomInt;

public class ChoseItems {
    public static ArrayList<Item> chooseItems(boolean human) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Item> selected = new ArrayList<>();
        ArrayList<Item> availableItems = new ArrayList<>();

        availableItems.add(new Drug("Health Potion", 50));
        availableItems.add(new Drug("Super Potion", 100));
        availableItems.add(new Potion("Burn Heal", Etat.BURNED));
        availableItems.add(new Potion("Paralyze Heal", Etat.PARALYZED));
        availableItems.add(new Potion("Antidote", Etat.POISONED));

        System.out.println("Choose up to 5 Items by entering their number (type 'done' to finish):");
        for (int i = 0; i < availableItems.size(); i++) {
            System.out.println(i + " : " + availableItems.get(i).getName());
        }

        while (selected.size() < 5) {
            System.out.print("Enter index (" + selected.size() + "/5) or 'done': ");
            String line;
            if (human) {
                line = scanner.nextLine().trim();
            } else {
                line = Integer.toString(getRandomInt(0, availableItems.size() - 1));
                System.out.println(line);
            }

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
            if (idx < 0 || idx >= availableItems.size()) {
                System.out.println("Index out of range. Choose a number between 0 and " + (availableItems.size() - 1) + ".");
                continue;
            }

            Item chosen = availableItems.get(idx);
            selected.add(chosen);
            System.out.println(chosen.getName() + " added. (" + selected.size() + "/5)");
        }

        System.out.println("Selection complete. Selected items:");
        for (Item i : selected) {
            System.out.println("- " + i.getName());
        }

        return selected;
    }
}
