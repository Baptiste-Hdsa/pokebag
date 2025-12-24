package fr.bapti.esiea.parser;

import fr.bapti.esiea.Etat;
import fr.bapti.esiea.Type;
import fr.bapti.esiea.attack.Attack;
import fr.bapti.esiea.attack.StatusEffect;
import fr.bapti.esiea.attack.EarthEffect;
import fr.bapti.esiea.attack.PlantEffect;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static fr.bapti.esiea.utils.Parser.ParseType;

public class ParserAttacks {
    public static ArrayList<Attack> parse(){
        File file = new File("Attacks.txt");
        if (!file.exists()) {
            file = new File("Pokebag/Attacks.txt");
        }
        
        ArrayList<Attack> attacks = new ArrayList<>();

        try (Scanner scanner = new Scanner(file)){
            String name = null;
            Type type = null;
            int useCount = 0;
            int power = 0;
            float failRate = 0;

            while(scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split("\\s+");
                
                if (line.equalsIgnoreCase("Attack")){
                    name = null;
                    type = null;
                    useCount = 0;
                    power = 0;
                    failRate = 0;

                } else if (parts[0].equalsIgnoreCase("Name")) {
                    name = parts[1];
                } else if (parts[0].equalsIgnoreCase("Type")) {
                    type = ParseType(parts[1]);
                } else if (parts[0].equalsIgnoreCase("Power")) {
                    power = Integer.parseInt(parts[1]);
                } else if (parts[0].equalsIgnoreCase("NbUse")) {
                    useCount = Integer.parseInt(parts[1]);
                } else if (parts[0].equalsIgnoreCase("Fail")) {
                    failRate = Float.parseFloat(parts[1]);
                }
                else if (line.equalsIgnoreCase("EndAttack")) {
                    if (name !=null){
                        Attack attack = new Attack(name, type, useCount, power, failRate);

                        if (type == Type.FIRE) {
                            attack.setEffect(new StatusEffect(Etat.BURNED, 0.2f));
                        } else if (type == Type.ELECTRIC) {
                            attack.setEffect(new StatusEffect(Etat.PARALYZED, 0.2f));
                        } else if (type == Type.INSECT) {
                            attack.setEffect(new StatusEffect(Etat.POISONED, 0.33f));
                        } else if (type == Type.EARTH) {
                            attack.setEffect(new EarthEffect());
                        } else if (type == Type.GRASS) {
                            attack.setEffect(new PlantEffect());
                        }
                        
                        attacks.add(attack);
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred while parsing the attacks file: " + file.getAbsolutePath());
            e.printStackTrace();
        }
        return attacks;
    }
}
