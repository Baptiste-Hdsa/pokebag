package fr.bapti.esiea.monster;

import fr.bapti.esiea.Etat;
import fr.bapti.esiea.Type;
import fr.bapti.esiea.attack.Attack;
import fr.bapti.esiea.utils.Pair;
import fr.bapti.esiea.utils.RandomTools;

import java.util.ArrayList;
import java.util.List;

public class PlayerMonster extends Monster {
    
    private int currentHealth;
    private int currentAttack;
    private int currentDefense;
    private int currentSpeed;
    
    private Etat etat;
    private List<Attack> currentAttacks;

    private boolean isUnderground = false;
    private int undergroundTurns = 0;
    private int originalDefense;

    public PlayerMonster(Monster model, ArrayList<Attack> chosenAttacks){
        super(model.getName(), model.getHealthRange(), model.getAttack(), model.getDefense(),
              model.getSpeed(), model.getType(), model.getAttacks(),
              model.getFlood(), model.getFall(), model.getParalysis());
        
        this.currentHealth = RandomTools.getRandomIntPair(model.getHealthRange());
        this.currentAttack = RandomTools.getRandomIntPair(model.getAttack());
        this.currentDefense = RandomTools.getRandomIntPair(model.getDefense());
        this.currentSpeed = RandomTools.getRandomIntPair(model.getSpeed());
        
        this.originalDefense = this.currentDefense;

        this.currentAttacks = new ArrayList<>(chosenAttacks);
        this.etat = Etat.DEFAULT;
    }

    public int getCurrentHealth() { return currentHealth; }
    public int getAttackStat() { return currentAttack; }
    public int getDefenseStat() { return currentDefense; }
    public int getSpeedStat() { return currentSpeed; }
    public Etat getEtat() { return etat; }
    
    @Override
    public List<Attack> getAttacks() { return currentAttacks; }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public void DecreaseHealth(int damage){
        this.currentHealth -= damage;
        if (this.currentHealth < 0) this.currentHealth = 0;
    }
    
    public void IncreaseHealth(int amount){
        this.currentHealth += amount;
    }
    
    public boolean isAlive() {
        return currentHealth > 0;
    }

    public void setCurrentHealth(int amount) {
        this.currentHealth = amount;
    }

    public void goUnderground(int turns) {
        if (!isUnderground) {
            this.isUnderground = true;
            this.undergroundTurns = turns;
            this.currentDefense *= 2; // Double defense
            System.out.println(getName() + " burrows underground! Defense doubled.");
        }
    }
    public void updateUnderground() {
        if (isUnderground) {
            undergroundTurns--;
            if (undergroundTurns <= 0) {
                isUnderground = false;
                this.currentDefense = this.originalDefense; // Restore defense
                System.out.println(getName() + " resurfaced.");
            }
        }
    }
}
