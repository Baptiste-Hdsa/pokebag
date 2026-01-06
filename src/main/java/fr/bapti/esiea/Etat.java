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
        }

        @Override
        public boolean canAttack(PlayerMonster m) {
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
            int damage = Math.max(1, m.getAttackStat() / 10);
            m.DecreaseHealth(damage);
            System.out.println(m.getName() + " is hurt by its burn (" + damage + " dmg)!");
        }

        @Override
        public boolean canAttack(PlayerMonster m) {
            return true;
        }
    };

    public abstract void onTurnStart(PlayerMonster m);
    public abstract boolean canAttack(PlayerMonster m);
}
