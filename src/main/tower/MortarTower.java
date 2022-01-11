package main.tower;

import main.DoubleCoord;
import main.IntCoord;
import main.Map;
import main.enemy.Enemy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class MortarTower extends Tower{
    private DoubleCoord targetPosition;
    private BufferedImage target;
    private int hitQuota;
    private int hitCount;
    private int spawnTimer;

    public MortarTower(int damage, int range, int cost, int fireRate, BufferedImage sprite, Projectile projectileType, BufferedImage target, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, int hitQuota, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, fireRate, sprite, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.target = target;
        targetPosition = new DoubleCoord(Map.TILE_SIZE + Map.TILE_SIZE / 2.0, 4 * Map.TILE_SIZE + Map.TILE_SIZE / 2.0);
        this.hitQuota = hitQuota;
        this.hitCount = 0;
        this.spawnTimer = 0;
    }

    public MortarTower(int damage, int range, int cost, int fireRate, IntCoord tileLocation, BufferedImage sprite, Projectile projectileType, BufferedImage target, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, int hitQuota, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, fireRate, tileLocation, sprite, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.target = target;
        targetPosition = new DoubleCoord(Map.TILE_SIZE + Map.TILE_SIZE / 2.0, 4 * Map.TILE_SIZE + Map.TILE_SIZE / 2.0);
        this.hitQuota = hitQuota;
        this.hitCount = 0;
        this.spawnTimer = 0;
    }

    public void setTargetPosition(DoubleCoord position) {
        targetPosition = position;
        this.isFlipped = this.getCenter().x < this.targetPosition.x;
    }

    @Override
    public MortarTower copy(IntCoord tileLocation, double moneyMultiplier){
        return new MortarTower(this.damage, this.range, (int)Math.round(this.cost * moneyMultiplier), this.fireRate, tileLocation, this.sprite, this.projectileType, this.target, this.camoHittable, this.magicProofHittable, this.armourHittable, this.hitQuota, this.towerUpgradePaths);
    }

    @Override
    public void fire(ArrayList<Enemy> enemies) {
        if (timer <= 0) {
            //for (Enemy enemy : enemies) {
                if (this.collision(null)) {
                    if (hitCount < hitQuota && this.spawnTimer <= 0) {
                        this.spawnTimer = 20;
                        if (this.fireProjectile(null)) {
                            hitCount++;
                        }
                    }
                    else if(this.spawnTimer > 0){
                        this.spawnTimer--;
                    }
                    else {
                        hitCount = 0;
                        timer = this.fireRate;
                    }
                //}
            }
        }
        else {
            timer--;
        }
    }

    @Override
    public boolean collision(Enemy enemy) {
        return true;
    }

    @Override
    public void paint(Graphics2D g, boolean isValid, boolean isRange) {
        super.paint(g, isValid, isRange);
        if(isRange) {
            g.drawImage(target, (int)targetPosition.x - Map.TILE_SIZE / 2, (int)targetPosition.y - Map.TILE_SIZE / 2, null);
        }
    }

    @Override
    protected boolean fireProjectile(Enemy enemy) {
        Projectile projectile = this.projectileType.copy(this.getCenter(), this.targetPosition);
        projectiles.add(projectile);
        return true;
    }
}
