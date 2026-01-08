package fr.bapti.esiea.attack;

import fr.bapti.esiea.monster.PlayerMonster;
import fr.bapti.esiea.Type;

import static fr.bapti.esiea.utils.RandomTools.getRandomFloat;

public class Attack {
    protected String name;
    protected Type type;
    protected int useCount;
    protected int power;
    protected float failRate;
    protected Effect effect;

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
