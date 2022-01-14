package main.tower;

import main.IntCoord;
import main.enemy.Enemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class FreezeTower extends Tower{
    private int freezeTime;

    public FreezeTower(int damage, int range, int cost, int totalCost, int fireRate, BufferedImage image, Projectile projectileType, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, int freezeTime, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, totalCost, fireRate, image, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.freezeTime = freezeTime;
    }

    public FreezeTower(int damage, int range, int cost, int totalCost, int fireRate, IntCoord tileLocation, BufferedImage image, Projectile projectileType, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, int freezeTime, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, totalCost, fireRate, tileLocation, image, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.freezeTime = freezeTime;
    }

    @Override
    public void update(ArrayList<Enemy> enemies) {
        projectiles.removeIf(projectile -> !projectile.isActive());
        fire(enemies);
        for (Projectile projectile : projectiles) {
            projectile.update();
            for(Enemy enemy : enemies) {
                if (projectile.isActive() && projectile.collision(enemy)) {
                    if((enemy.isArmoured() && !this.armourHittable) || (enemy.isMagicProof() && !this.magicProofHittable) || (enemy.isCamo() && !this.camoHittable)) {
                        projectile.setActive(false);
                        break;
                    }
                    else {
                        enemy.takeDamage(this.damage);
                        enemy.setFreezeTime(this.freezeTime);
                        projectile.hit();
                    }
                }
            }
            projectile.postUpdate();
        }
    }

    @Override
    public FreezeTower copy(IntCoord tileLocation, double moneyMultiplier){
        return new FreezeTower(this.damage, this.range, (int) Math.round(this.cost * moneyMultiplier), (int) Math.round(this.totalCost * moneyMultiplier), this.fireRate, tileLocation, this.sprite, this.projectileType, this.camoHittable, this.magicProofHittable, this.armourHittable, this.freezeTime, this.towerUpgradePaths);
    }
}
