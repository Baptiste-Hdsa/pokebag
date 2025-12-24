package fr.bapti.esiea.utils;

import fr.bapti.esiea.Type;

public class Parser {

    public static Type ParseType(String string) {
        return switch (string.toUpperCase()) {
            case "FIRE" -> Type.FIRE;
            case "WATER" -> Type.WATER;
            case "EARTH" -> Type.EARTH;
            case "ELECTRIC" -> Type.ELECTRIC;
            case "GRASS", "PLANT" -> Type.GRASS;
            case "INSECT" -> Type.INSECT;
            case "NORMAL" -> Type.NORMAL;
            default -> throw new IllegalStateException("Unexpected value: " + string);
        };
    }
}
