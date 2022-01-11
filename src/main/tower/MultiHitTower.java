package main.tower;

import main.IntCoord;
import main.enemy.Enemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MultiHitTower extends Tower{
    private int hitQuota;

    public MultiHitTower(int damage, int range, int cost, int fireRate, BufferedImage image, Projectile projectileType, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, int hitQuota, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, fireRate, image, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.hitQuota = hitQuota;
    }

    public MultiHitTower(int damage, int range, int cost, int fireRate, IntCoord tileLocation, BufferedImage image, Projectile projectileType, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, int hitQuota, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, fireRate, tileLocation, image, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.hitQuota = hitQuota;
    }

    @Override
    public MultiHitTower copy(IntCoord tileLocation, double moneyMultiplier){
        return new MultiHitTower(this.damage, this.range, (int) Math.round(this.cost * moneyMultiplier), this.fireRate, tileLocation, this.sprite, this.projectileType, this.camoHittable, this.magicProofHittable, this.armourHittable, this.hitQuota, this.towerUpgradePaths);
    }

    @Override
    public void fire(ArrayList<Enemy> enemies) {
        int count = 0;
        if (timer <= 0) {
            for (Enemy enemy : enemies) {
                if (this.collision(enemy)) {
                    if (this.fireProjectile(enemy)) {
                        count++;
                    }
                    if(count >= hitQuota) {
                        break;
                    }
                }
            }
            timer = this.fireRate;
        }
        else {
            timer--;
        }
    }
}
