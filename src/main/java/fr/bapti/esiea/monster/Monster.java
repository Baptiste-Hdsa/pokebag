package fr.bapti.esiea.monster;

import fr.bapti.esiea.Type;
import fr.bapti.esiea.attack.Attack;
import fr.bapti.esiea.utils.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Monster {
    private final String name;
    private final Type type;
    private final Pair health;
    private final Pair attack;
    private final Pair defense;
    private final Pair speed;
    private final float flood;
    private final float fall;
    private final float paralysis;
    private final List<Attack> attacks;

    public Monster(String name, Pair health, Pair attack, Pair defense, Pair speed, Type type, List<Attack> attacks, float flood, float fall, float paralysis){
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.type = type;
        this.attacks = new ArrayList<>(attacks);
        this.speed = speed;
        this.flood = flood;
        this.fall = fall;
        this.paralysis = paralysis;
    }

    public String getName() { return name; }
    public Type getType() { return type; }
    public Pair getHealthRange() { return health; }
    public Pair getAttack() { return attack; }
    public Pair getDefense() { return defense; }
    public Pair getSpeed() { return speed; }
    public float getFlood() { return flood; }
    public float getFall() { return fall; }
    public float getParalysis() { return paralysis; }
    public List<Attack> getAttacks() { return Collections.unmodifiableList(attacks); }

    public boolean isWeakAgainst(Type other) {
        return this.type.isWeakAgainst(other);
    }

    public boolean isStrongAgainst(Type other) {
        return this.type.isStrongAgainst(other);
    }

    public List<Type> strengths() {
        return Stream.of(Type.values()).filter(t -> this.type.isStrongAgainst(t)).collect(Collectors.toList());
    }

    public List<Type> weaknesses() {
        return Stream.of(Type.values()).filter(t -> this.type.isWeakAgainst(t)).collect(Collectors.toList());
    }
}
