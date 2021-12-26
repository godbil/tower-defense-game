package main;

public class GameState {
    public final int MAX_WAVE = 10;

    private int health;
    private int money;
    private int wave;

    public void init() {
        this.health = 50;
        this.money = 300;
        this.wave = 1;
    }

    public int getHealth() {
        return this.health;
    }

    public int getMoney() {
        return this.money;
    }

    public int getWave(){
        return this.wave;
    }

    public void subtractHealth(int healthDecrease) {
        this.health -= healthDecrease;
        if(this.health < 0) {
            this.health = 0;
        }
    }

    public void subtractMoney(int moneyDecrease) {
        this.money -= moneyDecrease;
    }

    public void addMoney(int moneyIncrease) {
        this.money += moneyIncrease;
    }

    public void nextWave() {
        this.wave++;
    }

}
