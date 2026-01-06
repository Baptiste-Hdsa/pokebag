package fr.bapti.esiea;

public enum Type {

    FIRE,
    WATER,
    ELECTRIC,
    EARTH,
    INSECT,
    GRASS,
    NORMAL;

    public boolean isStrongAgainst(Type defender) {
        return switch (this) {
            case FIRE -> (defender == GRASS || defender == INSECT);
            case WATER -> defender == FIRE;
            case ELECTRIC -> defender == WATER;
            case EARTH -> defender == ELECTRIC;
            case GRASS, INSECT -> defender == EARTH;
            default -> false;
        };
    }

    public boolean isWeakAgainst(Type defender) {
        return switch (this) {
            case FIRE -> defender == WATER;
            case WATER -> defender == ELECTRIC;
            case ELECTRIC -> defender == EARTH;
            case EARTH -> (defender == GRASS || defender == INSECT);
            case GRASS, INSECT -> defender == FIRE;
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
