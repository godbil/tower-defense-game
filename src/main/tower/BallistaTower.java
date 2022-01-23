package main.tower;

import main.DoubleCoord;
import main.IntCoord;
import main.Map;
import main.enemy.Enemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BallistaTower extends Tower{
    // The target position of where the ballista will shoot
    private DoubleCoord targetPosition;

    public BallistaTower(int damage, int range, int cost, int totalCost, int fireRate, BufferedImage image, Projectile projectileType, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, totalCost, fireRate, image, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        targetPosition = new DoubleCoord(Map.TILE_SIZE, Map.TILE_SIZE);
    }

    public BallistaTower(int damage, int range, int cost, int totalCost, int fireRate, IntCoord tileLocation, BufferedImage image, Projectile projectileType, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, totalCost, fireRate, tileLocation, image, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        targetPosition = new DoubleCoord(Map.TILE_SIZE, Map.TILE_SIZE);
    }

    /* Used to set the target position of the ballista
     * Pre: Takes in a double coordinate which is the new position
     * Post: Doesn't return anything
     */
    public void setTargetPosition(DoubleCoord position) {
        // Sets the new position and then checks whether the ballista's sprite should be flipped depending on where it's target is
        this.targetPosition = position;
        this.isFlipped = this.getCenter().x < this.targetPosition.x;
    }

    /* Used to make a copy of the current ballista tower
     * Pre: Takes in an int coordinate which is the tile location and a double which is the money multiplier to increase or decrease cost
     * Post: Returns the new copy of the ballista tower
     */
    @Override
    public BallistaTower copy(IntCoord tileLocation, double moneyMultiplier){
        return new BallistaTower(this.damage, this.range, (int)Math.round(this.cost * moneyMultiplier), (int)Math.round(this.totalCost * moneyMultiplier), this.fireRate, tileLocation, this.sprite, this.projectileType, this.camoHittable, this.magicProofHittable, this.armourHittable, this.towerUpgradePaths);
    }

    /* Used to override the super class' collision method
     * Pre: Takes in an enemy object
     * Post: Returns a boolean for when the tower sees an enemy
     */
    @Override
    public boolean collision(Enemy enemy) {
        // Always returns true so the ballista will always shoot when there are enemies instead of only shooting when enemies are in range
        return true;
    }

    /* Used to override the super's fire projectile method
     * Pre: Takes in an enemy object
     * Post: Returns a boolean for if a projectile has been fired
     */
    @Override
    protected boolean fireProjectile(Enemy enemy) {
        // Does the same thing as the super method but does not set isFlipped here because it should be set based on where the player chooses to fire and not on the enemy's position
        Projectile projectile = this.projectileType.copy(this.getCenter(), this.targetPosition);
        projectiles.add(projectile);
        return true;
    }
}
