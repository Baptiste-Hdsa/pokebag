package fr.bapti.esiea.attack;

import fr.bapti.esiea.Type;
import fr.bapti.esiea.monster.PlayerMonster;

import static fr.bapti.esiea.utils.RandomTools.getRandomFloat;

public class PlayerAttack extends Attack{

    protected int currentUseCount;

    public PlayerAttack(Attack model) {
        super(model.name, model.type, model.useCount, model.power, model.failRate);
        this.currentUseCount = model.useCount;
        this.effect = model.effect;
    }

    public boolean use(PlayerMonster attacker, PlayerMonster defender) {
        if (this.currentUseCount <= 0) {
            System.out.println(this.name + " has no uses left!");
            useUnarmed(attacker, defender);
            return false;
        }

        if (!attacker.getEtat().canAttack(attacker)) {
            return false;
        }

        if (Math.random() < failRate) {
            System.out.println(attacker.getName() + " used " + this.name + " but it failed!");
            this.currentUseCount--;
            return false;
        }

        float advantage = (float) this.type.getAvantage(defender.getType());
        float damage = (float) (( (float) (11 * attacker.getAttackStat() * this.power) / (25 * defender.getDefenseStat()) + 2) * getRandomFloat(0.85f, 1) * advantage);

        defender.DecreaseHealth((int) damage);
        this.currentUseCount--;
        if (this.currentUseCount < 0) this.currentUseCount = 0;

        System.out.println(attacker.getName() + " used " + this.name + " and dealt " + (int)damage + " damage to " + defender.getName());

        if (this.effect != null) {
            this.effect.apply(attacker, defender);
        }
        return true;
    }
}
