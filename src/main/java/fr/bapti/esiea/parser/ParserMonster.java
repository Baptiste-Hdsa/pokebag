package fr.bapti.esiea.parser;

import fr.bapti.esiea.monster.Monster;
import fr.bapti.esiea.utils.Pair;
import fr.bapti.esiea.Type;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static fr.bapti.esiea.utils.Parser.ParseType;

public class ParserMonster {
    public static ArrayList<Monster> parse() {
        File file = new File("Monsters.txt");
        if (!file.exists()) {
            file = new File("Pokebag/Monsters.txt");
        }
        
        ArrayList<Monster> monsters = new ArrayList<>();

        try (Scanner scanner = new Scanner(file)) {
            String name = null;
            Type type = null;
            Pair hp = new Pair(0, 0);
            Pair attack = new Pair(0, 0);
            Pair defense = new Pair(0, 0);
            Pair speed = new Pair(0, 0);
            float flood = 0;
            float fall = 0;
            float paralysis = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split("\\s+");
                
                if (line.equalsIgnoreCase("Monster")) {
                    name = null;
                    type = null;
                    hp = new Pair(0, 0);
                    attack = new Pair(0, 0);
                    defense = new Pair(0, 0);
                    speed = new Pair(0, 0);
                    flood = 0;
                    fall = 0;
                    paralysis = 0;

                } else if (parts[0].equalsIgnoreCase("Name")) {
                    name = parts[1];
                } else if (parts[0].equalsIgnoreCase("Type")) {
                    type = ParseType(parts[1]);
                } else if (parts[0].equalsIgnoreCase("HP")) {
                    hp = new Pair(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                } else if (parts[0].equalsIgnoreCase("Speed")) {
                    speed = new Pair(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                } else if (parts[0].equalsIgnoreCase("Attack")) {
                    attack = new Pair(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                } else if (parts[0].equalsIgnoreCase("Defense")) {
                    defense = new Pair(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                } else if (parts[0].equalsIgnoreCase("Flood")) {
                    flood = Float.parseFloat(parts[1]);
                } else if (parts[0].equalsIgnoreCase("Fall")) {
                    fall = Float.parseFloat(parts[1]);
                } else if (parts[0].equalsIgnoreCase("Paralysis")) {
                    paralysis = Float.parseFloat(parts[1]);
                }

                else if (line.equalsIgnoreCase("EndMonster")) {
                    if (name != null) {
                        monsters.add(new Monster(name, hp, attack, defense, speed, type, new ArrayList<>(), flood, fall, paralysis));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while parsing the monsters file: " + file.getAbsolutePath());
            e.printStackTrace();
        }
        return monsters;
    }
}
