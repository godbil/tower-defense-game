package main.enemy;

import main.DoubleCoord;
import main.GameState;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DeathSpawnerEnemy extends Enemy{
    private final int spawnAmount;
    private Enemy spawnedEnemy;
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

    @Override
    public DeathSpawnerEnemy copy(DoubleCoord position, Direction direction, double movementMultiplier){
        return new DeathSpawnerEnemy(this.maxHealth, this.movementSpeed * movementMultiplier, position, direction, this.size, this.sprite, this.camo, this.magicProof, this.armoured, this.moneyGive, this.spawnedEnemy, this.spawnAmount);
    }

    public void deathSpawnEnemy(GameState gameState, ArrayList<Enemy> enemies) {
        if(this.health <= 0) {
            if(spawnQuota > 0 && timer <= 0) {
                timer = 10;
                spawnQuota--;
                Enemy enemy = spawnedEnemy.copy(position, direction, gameState.getMovementMultiplier());
                enemies.add(enemy);
            }
            else if (timer > 0){
                timer--;
            }
            else {
                spawnQuota = spawnAmount;
            }
        }
    }
}
