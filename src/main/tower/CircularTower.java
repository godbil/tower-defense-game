package main.tower;

import main.DoubleCoord;
import main.IntCoord;
import main.Map;
import main.enemy.Enemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CircularTower extends Tower {
    // The amount of projectiles that will be fired from the circular tower
    private int projectileCount;

    public CircularTower(int damage, int range, int cost, int totalCost, int fireRate, BufferedImage image, Projectile projectileType, int projectileCount, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, totalCost, fireRate, image, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.projectileCount = projectileCount;
    }

    public CircularTower(int damage, int range, int cost, int totalCost, int fireRate, IntCoord tileLocation, BufferedImage image, Projectile projectileType, int projectileCount, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, fireRate, totalCost, tileLocation, image, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.projectileCount = projectileCount;
    }

    /* Used to make a copy of the current circular tower
     * Pre: Takes in an int coordinate which is the tile location and a double which is the money multiplier to increase or decrease cost
     * Post: Returns the new copy of the circular tower
     */
    @Override
    public CircularTower copy(IntCoord tileLocation, double moneyMultiplier){
        return new CircularTower(this.damage, this.range, (int)Math.round(this.cost * moneyMultiplier), (int)Math.round(this.totalCost * moneyMultiplier), this.fireRate, tileLocation, this.sprite, this.projectileType, this.projectileCount, this.camoHittable, this.magicProofHittable, this.armourHittable, this.towerUpgradePaths);
    }

    /* Used to override the super fire method to fire projectiles in a circular shape
     * Pre: Takes in an enemy object
     * Post: Returns a boolean to see if the projectile has been fired
     */
    @Override
    protected boolean fireProjectile(Enemy enemy) {
        double angle = Math.PI;
        for(double i = angle; i < Math.PI * 2 + angle; i += Math.PI * 2 / (double)projectileCount) {
            // Gets an angle depending on the amount of projectiles that get fired and then puts that angle into a cos and sin function for the projectile's starting position and then sets a position really far in the same direction as the target so they continue
            Projectile projectile = this.projectileType.copy(new DoubleCoord(Map.TILE_SIZE / 2.0 * Math.cos(i) + getCenter().x, Map.TILE_SIZE / 2.0 * Math.sin(i) + getCenter().y), new DoubleCoord(Math.cos(i) * 10000, (Math.sin(i)* 10000)));
            // Adds the newly created projectile to the array list of projectiles
            projectiles.add(projectile);
            // Calls the rotate method with the angle so the sprites will be turned in the direction of where they are going
            projectile.rotate(i);
        }
        return true;
    }

}
