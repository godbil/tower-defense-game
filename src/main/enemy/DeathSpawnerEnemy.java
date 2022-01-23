package main.enemy;

import main.DoubleCoord;
import main.GameState;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DeathSpawnerEnemy extends Enemy{
    // The amount this enemy spawns and the specific enemy it spawns
    private final int spawnAmount;
    private Enemy spawnedEnemy;
    // The timer and spawn quota of this enemy
    private int timer;
    private int spawnQuota;

    public DeathSpawnerEnemy(int maxHealth, double movementSpeed, int size, BufferedImage image, boolean camo, boolean magicProof, boolean armoured, int moneyGive, Enemy spawnedEnemy, int spawnAmount) {
        super(maxHealth, movementSpeed, size, image, camo, magicProof, armoured, moneyGive);
        this.spawnedEnemy = spawnedEnemy;
        this.spawnAmount = spawnAmount;
        this.timer = 0;
        this.spawnQuota = spawnAmount;
    }

    public DeathSpawnerEnemy(int maxHealth, double movementSpeed, DoubleCoord position, Direction direction, int size, BufferedImage image, boolean camo, boolean magicProof, boolean armoured, int moneyGive, Enemy spawnedEnemy, int spawnAmount) {
        super(maxHealth, movementSpeed, position, direction, size, image, camo, magicProof, armoured, moneyGive);
        this.spawnedEnemy = spawnedEnemy;
        this.spawnAmount = spawnAmount;
        this.timer = 0;
        this.spawnQuota = spawnAmount;
    }

    /* Used to make a copy of the current death spawner enemy
     * Pre: Takes in a double coordinate which is the enemies position, a direction enum and a double which is the movement multiplier of the enemy
     * Post: Returns the copied death spawner enemy
     */
    @Override
    public DeathSpawnerEnemy copy(DoubleCoord position, Direction direction, double movementMultiplier){
        return new DeathSpawnerEnemy(this.maxHealth, this.movementSpeed * movementMultiplier, position, direction, this.size, this.sprite, this.camo, this.magicProof, this.armoured, this.moneyGive, this.spawnedEnemy, this.spawnAmount);
    }

    /* Used to spawn enemies on death
     * Pre: Takes in a gameState object and an array list of enemies
     * Post: Doesn't return anything
     */
    public void deathSpawnEnemy(GameState gameState, ArrayList<Enemy> enemies) {
        // Checks if this enemy has died
        if(this.health <= 0) {
            // Keeps spawning by copying the spawnedEnemy and adding that enemy to the arraylist until the spawn quota has run out
            while(spawnQuota > 0) {
                spawnQuota--;
                Enemy enemy = spawnedEnemy.copy(position, direction, gameState.getMovementMultiplier());
                enemies.add(enemy);
            }
        }
    }
}
