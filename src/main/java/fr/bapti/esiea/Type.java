package fr.bapti.esiea;

public enum Type {

    FIRE,
    WATER,
    ELECTRIC,
    EARTH,
    INSECT, // Part of Nature category
    GRASS,  // Part of Nature category
    NORMAL;

    public boolean isStrongAgainst(Type defender) {
        return switch (this) {
            case FIRE -> (defender == GRASS || defender == INSECT); // Fire > Nature
            case WATER -> defender == FIRE;
            case ELECTRIC -> defender == WATER;
            case EARTH -> defender == ELECTRIC;
            case GRASS, INSECT -> defender == EARTH; // Nature > Earth
            default -> false;
        };
    }

    public boolean isWeakAgainst(Type defender) {
        return switch (this) {
            case FIRE -> defender == WATER;
            case WATER -> defender == ELECTRIC;
            case ELECTRIC -> defender == EARTH;
            case EARTH -> (defender == GRASS || defender == INSECT); // Earth < Nature
            case GRASS, INSECT -> defender == FIRE; // Nature < Fire
            default -> false;
        };
    }

    public double getAvantage(Type defender) {
        if (this.isStrongAgainst(defender)) {
            return 2.0;
        } else if (this.isWeakAgainst(defender)) {
            return 0.5;
        } else {
            return 1.0;
        }
    }
}
