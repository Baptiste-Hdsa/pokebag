package fr.bapti.esiea;

import fr.bapti.esiea.monster.PlayerMonster;

public enum Etat {

    DEFAULT {
        @Override
        public void onTurnStart(PlayerMonster m) {
        }

        @Override
        public boolean canAttack(PlayerMonster m) {
            return true;
        }
    },
    POISONED {
        @Override
        public void onTurnStart(PlayerMonster m) {
            // "A poisoned target takes one-tenth of its attack damage at the start of its turn"
            int damage = Math.max(1, m.getAttackStat() / 10);
            m.DecreaseHealth(damage);
            System.out.println(m.getName() + " suffers from poison (" + damage + " dmg)!");
        }

        @Override
        public boolean canAttack(PlayerMonster m) {
            return true;
        }
    },
    PARALYZED {
        @Override
        public void onTurnStart(PlayerMonster m) {
            // "A monster can recover from this state at the start of each turn..."
            // Logic to recover from paralysis to be implemented if we track the number of turns
        }

        @Override
        public boolean canAttack(PlayerMonster m) {
            // "Under paralysis, a monster has a one in four chance of successfully attacking"
            if (Math.random() < 0.25) {
                return true;
            } else {
                System.out.println(m.getName() + " is paralyzed and cannot move!");
                return false;
            }
        }
    },
    BURNED {
        @Override
        public void onTurnStart(PlayerMonster m) {
            // "When a monster is burned, it takes one-tenth of its attack damage at the start of each turn"
            int damage = Math.max(1, m.getAttackStat() / 10);
            m.DecreaseHealth(damage);
            System.out.println(m.getName() + " is hurt by its burn (" + damage + " dmg)!");
        }

        @Override
        public boolean canAttack(PlayerMonster m) {
            return true;
        }
    };

    // Abstract methods that each state must implement
    public abstract void onTurnStart(PlayerMonster m);
    public abstract boolean canAttack(PlayerMonster m);
}
