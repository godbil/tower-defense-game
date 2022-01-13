package main.enemy;

import main.DoubleCoord;
import main.Game;
import main.GameState;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SpawnerEnemy extends Enemy{
    private final int spawnTimer;
    private final int spawnAmount;
    private Enemy spawnedEnemy;
    private int tempTimer;
    private int timer;
    private int spawnQuota;

    public SpawnerEnemy(int maxHealth, double movementSpeed, int size, BufferedImage image, boolean camo, boolean magicProof, boolean armoured, int moneyGive, Enemy spawnedEnemy, int spawnTimer, int spawnAmount) {
        super(maxHealth, movementSpeed, size, image, camo, magicProof, armoured, moneyGive);
        this.spawnedEnemy = spawnedEnemy;
        this.spawnTimer = spawnTimer;
        this.spawnAmount = spawnAmount;
        this.tempTimer = 0;
        this.timer = 0;
        this.spawnQuota = spawnAmount;
    }

    public SpawnerEnemy(int maxHealth, double movementSpeed, DoubleCoord position, Direction direction, int size, BufferedImage image, boolean camo, boolean magicProof, boolean armoured, int moneyGive, Enemy spawnedEnemy, int spawnTimer, int spawnAmount) {
        super(maxHealth, movementSpeed, position, direction, size, image, camo, magicProof, armoured, moneyGive);
        this.spawnedEnemy = spawnedEnemy;
        this.spawnTimer = spawnTimer;
        this.spawnAmount = spawnAmount;
        this.tempTimer = 0;
        this.timer = 0;
        this.spawnQuota = spawnAmount;
    }

    @Override
    public SpawnerEnemy copy(DoubleCoord position, Direction direction, double movementMultiplier){
        return new SpawnerEnemy(this.maxHealth, this.movementSpeed * movementMultiplier, position, direction, this.size, this.sprite, this.camo, this.magicProof, this.armoured, this.moneyGive, this.spawnedEnemy, this.spawnTimer, this.spawnAmount);
    }

    public void spawnEnemy(GameState gameState, ArrayList<Enemy> enemies) {
        if(tempTimer <= 0) {
            if(spawnQuota > 0 && timer <= 0) {
                timer = Game.FPS;
                spawnQuota--;
                Enemy enemy = spawnedEnemy.copy(position, direction, gameState.getMovementMultiplier());
                enemies.add(enemy);
            }
            else if (timer > 0){
                timer--;
            }
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
