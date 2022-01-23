package main.enemy;

import main.DoubleCoord;
import main.Game;
import main.GameState;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SpawnerEnemy extends Enemy{
    // The amount that is spawned each timer and the timer for how often enemies will be spawned
    private final int spawnTimer;
    private final int spawnAmount;
    // The enemy that is spawned
    private Enemy spawnedEnemy;
    // A temporary timer (for spawning each time) that is counted down and reset
    private int tempTimer;
    // A timer (for gaps between spawns)
    private int timer;
    // The spawn quota for this enemy
    private int spawnQuota;

    public SpawnerEnemy(int maxHealth, double movementSpeed, int size, BufferedImage image, boolean camo, boolean magicProof, boolean armoured, int moneyGive, Enemy spawnedEnemy, int spawnTimer, int spawnAmount) {
        super(maxHealth, movementSpeed, size, image, camo, magicProof, armoured, moneyGive);
        this.spawnedEnemy = spawnedEnemy;
        this.spawnTimer = spawnTimer;
        this.spawnAmount = spawnAmount;
        // Sets the initial spawn time to 0 so it spawns right away
        this.tempTimer = 0;
        this.timer = 0;
        this.spawnQuota = spawnAmount;
    }

    public SpawnerEnemy(int maxHealth, double movementSpeed, DoubleCoord position, Direction direction, int size, BufferedImage image, boolean camo, boolean magicProof, boolean armoured, int moneyGive, Enemy spawnedEnemy, int spawnTimer, int spawnAmount) {
        super(maxHealth, movementSpeed, position, direction, size, image, camo, magicProof, armoured, moneyGive);
        this.spawnedEnemy = spawnedEnemy;
        this.spawnTimer = spawnTimer;
        this.spawnAmount = spawnAmount;
        // Sets the initial spawn time to 0 so it spawns right away
        this.tempTimer = 0;
        this.timer = 0;
        this.spawnQuota = spawnAmount;
    }

    /* Used to make a copy of the current spawner enemy
     * Pre: Takes in a double coordinate which is the enemies position, a direction enum and a double which is the movement multiplier of the enemy
     * Post: Returns the copied spawner enemy
     */
    @Override
    public SpawnerEnemy copy(DoubleCoord position, Direction direction, double movementMultiplier){
        return new SpawnerEnemy(this.maxHealth, this.movementSpeed * movementMultiplier, position, direction, this.size, this.sprite, this.camo, this.magicProof, this.armoured, this.moneyGive, this.spawnedEnemy, this.spawnTimer, this.spawnAmount);
    }

    /* Used to spawn enemies
     * Pre: Takes in a gameState object and an array list of enemies
     * Post: Doesn't return anything
     */
    public void spawnEnemy(GameState gameState, ArrayList<Enemy> enemies) {
        // Check the temporary timer is 0 to know when to spawn
        if(tempTimer <= 0) {
            // Checks the spawn quota and timer for gaps between spawns
            if(spawnQuota > 0 && timer <= 0) {
                timer = Game.FPS;
                spawnQuota--;
                Enemy enemy = spawnedEnemy.copy(position, direction, gameState.getMovementMultiplier());
                enemies.add(enemy);
            }
            // If the gaps between spawn timer isn't less than or equal to 0 yet then it will count down
            else if (timer > 0){
                timer--;
            }
            // Resets the spawn quota and the spawn timer
            else {
                spawnQuota = spawnAmount;
                tempTimer = spawnTimer;
            }
        }
        else{
            tempTimer--;
        }
    }
}
