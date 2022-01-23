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

    /* Used to update all of the tower's variables and calls their other methods which needs to be checked every frame
     * Pre: Takes in an arraylist which is the enemies
     * Post: Doesn't return anything
     */
    @Override
    public void update(ArrayList<Enemy> enemies) {
        // Removes the projectile from the array list if the projectile is no longer active
        projectiles.removeIf(projectile -> !projectile.isActive());
        // Runs the fire method
        fire(enemies);
        for (Projectile projectile : projectiles) {
            // Calls the projectile's update method
            projectile.update();
            for(Enemy enemy : enemies) {
                // Checks if the projectile is still active and if it has collided with an enemy
                if (projectile.isActive() && projectile.collision(enemy)) {
                    if((enemy.isArmoured() && !this.armourHittable) || (enemy.isMagicProof() && !this.magicProofHittable) || (enemy.isCamo() && !this.camoHittable)) {
                        // Checks if this tower can hit the enemy and if it can't then the projectile is set to inactive to be removed in the next update
                        projectile.setActive(false);
                        break;
                    }
                    // If the tower can hit the enemy then the enemy's take damage method will be called and then the projectile's hit command will be called
                    else {
                        enemy.takeDamage(this.damage);
                        // Will also call the enemy's set freeze time method to make them frozen
                        enemy.setFreezeTime(this.freezeTime);
                        projectile.hit();
                    }
                }
            }
            // Calls the projectile's post update method for updates that need to occur after all of the other updates
            projectile.postUpdate();
        }
    }

    /* Used to make a copy of the current freeze tower
     * Pre: Takes in an int coordinate which is the tile location and a double which is the money multiplier to increase or decrease cost
     * Post: Returns the new copy of the freeze tower
     */
    @Override
    public FreezeTower copy(IntCoord tileLocation, double moneyMultiplier){
        return new FreezeTower(this.damage, this.range, (int) Math.round(this.cost * moneyMultiplier), (int) Math.round(this.totalCost * moneyMultiplier), this.fireRate, tileLocation, this.sprite, this.projectileType, this.camoHittable, this.magicProofHittable, this.armourHittable, this.freezeTime, this.towerUpgradePaths);
    }
}
