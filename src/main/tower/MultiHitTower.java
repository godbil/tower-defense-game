package main.tower;

import main.IntCoord;
import main.enemy.Enemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MultiHitTower extends Tower{
    // The quota of the amount that the tower can shoot
    private int hitQuota;

    public MultiHitTower(int damage, int range, int cost, int totalCost, int fireRate, BufferedImage image, Projectile projectileType, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, int hitQuota, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, totalCost, fireRate, image, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.hitQuota = hitQuota;
    }

    public MultiHitTower(int damage, int range, int cost, int totalCost, int fireRate, IntCoord tileLocation, BufferedImage image, Projectile projectileType, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, int hitQuota, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, totalCost, fireRate, tileLocation, image, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.hitQuota = hitQuota;
    }

    /* Used to make a copy of the current multi hit tower
     * Pre: Takes in an int coordinate which is the tile location and a double which is the money multiplier to increase or decrease cost
     * Post: Returns the new copy of the multi hit tower
     */
    @Override
    public MultiHitTower copy(IntCoord tileLocation, double moneyMultiplier){
        return new MultiHitTower(this.damage, this.range, (int) Math.round(this.cost * moneyMultiplier), (int) Math.round(this.totalCost * moneyMultiplier), this.fireRate, tileLocation, this.sprite, this.projectileType, this.camoHittable, this.magicProofHittable, this.armourHittable, this.hitQuota, this.towerUpgradePaths);
    }

    /* Used to check whether the tower can fire yet and call the collision and fire projectile methods
     * Pre: Takes in an arraylist which is the enemies
     * Post: Doesn't return anything
     */
    @Override
    public void fire(ArrayList<Enemy> enemies) {
        // Makes a temporary count for the amount of projectiles fired
        int count = 0;
        // Check if the tower can shoot yet (fire rate)
        if (timer <= 0) {
            for (Enemy enemy : enemies) {
                // Goes through every single enemy and see if any of them are inside this tower's range
                if (this.collision(enemy)) {
                    // Calls the fire projectile method which will return true if it created a projectile causing this to break from the for loop
                    if (this.fireProjectile(enemy)) {
                        // Adds to count once a projectile is created
                        count++;
                    }
                    if(count >= hitQuota) {
                        // Only breaks out of the for loop once the amount of projectiles fired reaches the quota
                        break;
                    }
                }
            }
            // Resets the timer to equal to fire rate of this tower
            timer = this.fireRate;
        }
        else {
            // If the tower cannot shoot yet then it will count down the timer
            timer--;
        }
    }
}
