package fr.bapti.esiea.attack;

import fr.bapti.esiea.monster.PlayerMonster;
import fr.bapti.esiea.Type;

import static fr.bapti.esiea.utils.RandomTools.getRandomFloat;

public class Attack {
    private String name;
    private Type type;
    private int useCount;
    private int power;
    private float failRate;
    private Effect effect;

    public Attack(String name, Type type, int useCount, int power, float failRate){
        this.name = name;
        this.type = type;
        this.useCount = useCount;
        this.power = power;
        this.failRate = failRate;
        this.effect = null;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public boolean use(PlayerMonster attacker, PlayerMonster defender) {
        if (this.useCount <= 0) {
            System.out.println(this.name + " has no uses left!");
            useUnarmed(attacker, defender);
            return false;
        }

        if (!attacker.getEtat().canAttack(attacker)) {
            return false;
        }

        if (Math.random() < failRate) {
            System.out.println(attacker.getName() + " used " + this.name + " but it failed!");
            this.useCount--;
            return false;
        }

        float advantage = (float) this.type.getAvantage(defender.getType());
        float damage = (float) (( (float) (11 * attacker.getAttackStat() * this.power) / (25 * defender.getDefenseStat()) + 2) * getRandomFloat(0.85f, 1) * advantage);

        defender.DecreaseHealth((int) damage);
        this.useCount--;
        if (this.useCount < 0) this.useCount = 0;

        System.out.println(attacker.getName() + " used " + this.name + " and dealt " + (int)damage + " damage to " + defender.getName());

        if (this.effect != null) {
            this.effect.apply(attacker, defender);
        }
        return true;
    }

    public static void useUnarmed(PlayerMonster attacker, PlayerMonster defender){
        if (!attacker.getEtat().canAttack(attacker)) {
            return;
        }

        float damage = 20 * ((float) attacker.getAttackStat() / defender.getDefenseStat()) * getRandomFloat(0.85f, 1);
        double advantage = attacker.getType().getAvantage(defender.getType());
        damage *= advantage;

        defender.DecreaseHealth((int) damage);
        System.out.println(attacker.getName() + " attacks unarmed and dealt " + (int)damage + " damage to " + defender.getName());
    }

    public String getName() { return name; }
    public Type getType() { return type; }
    public int getUseCount() { return useCount; }
    public int getPower() { return power; }
    public float getFailRate() { return failRate; }
}
