package main.tower;

import main.DoubleCoord;
import main.IntCoord;
import main.Map;
import main.enemy.Enemy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class MortarTower extends Tower{
    // Target position of the mortar tower
    private DoubleCoord targetPosition;
    // Image of the target
    private BufferedImage target;
    // The quota of projectiles that can be fire and how many enemies are hit
    private int hitQuota;
    private int hitCount;
    // The timer to make gaps between mortar shots
    private int spawnTimer;

    public MortarTower(int damage, int range, int cost, int totalCost, int fireRate, BufferedImage sprite, Projectile projectileType, BufferedImage target, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, int hitQuota, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, totalCost, fireRate, sprite, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.target = target;
        targetPosition = new DoubleCoord(Map.TILE_SIZE + Map.TILE_SIZE / 2.0, 4 * Map.TILE_SIZE + Map.TILE_SIZE / 2.0);
        this.hitQuota = hitQuota;
        this.hitCount = 0;
        this.spawnTimer = 0;
    }

    public MortarTower(int damage, int range, int cost, int totalCost, int fireRate, IntCoord tileLocation, BufferedImage sprite, Projectile projectileType, BufferedImage target, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, int hitQuota, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, totalCost, fireRate, tileLocation, sprite, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.target = target;
        targetPosition = new DoubleCoord(Map.TILE_SIZE + Map.TILE_SIZE / 2.0, 4 * Map.TILE_SIZE + Map.TILE_SIZE / 2.0);
        this.hitQuota = hitQuota;
        this.hitCount = 0;
        this.spawnTimer = 0;
    }

    /* Used to set the target position of the mortar, chosen by the player
     * Pre: Takes in a double coordinate which is the new position
     * Post: Doesn't return anything
     */
    public void setTargetPosition(DoubleCoord position) {
        targetPosition = position;
        // Flips the tower sprite if the target position is to the right
        this.isFlipped = this.getCenter().x < this.targetPosition.x;
    }

    /* Used to make a copy of the current mortar tower
     * Pre: Takes in an int coordinate which is the tile location and a double which is the money multiplier to increase or decrease cost
     * Post: Returns the new copy of the mortar tower
     */
    @Override
    public MortarTower copy(IntCoord tileLocation, double moneyMultiplier){
        return new MortarTower(this.damage, this.range, (int)Math.round(this.cost * moneyMultiplier), (int)Math.round(this.totalCost * moneyMultiplier), this.fireRate, tileLocation, this.sprite, this.projectileType, this.target, this.camoHittable, this.magicProofHittable, this.armourHittable, this.hitQuota, this.towerUpgradePaths);
    }

    /* Used to override the super fire method
     * Pre: Takes in an arraylist of enemies
     * Post: Doesn't return anything
     */
    @Override
    public void fire(ArrayList<Enemy> enemies) {
        if (timer <= 0) {
            // If the timer reaches 0, it will always shoot regardless of whether there is an enemy or not
            if (this.collision(null)) {
                if (hitCount < hitQuota && this.spawnTimer <= 0) {
                    // Checks if the amount of shot projectiles are less than the quota and if the spawn timer is less than or equal to zero to fire a projectile
                    this.spawnTimer = 20;
                    if (this.fireProjectile(null)) {
                        // Adds one to the hit count when a projectile is made
                        hitCount++;
                    }
                }
                // If the spawn timer is still not at zero then it will keep counting down
                else if(this.spawnTimer > 0){
                    this.spawnTimer--;
                }
                else {
                    // Else the hitCount would have reached the quota so the count is reset and the timer is also reset
                    hitCount = 0;
                    timer = this.fireRate;
                }
            }
        }
        else {
            // If the timer isn't at 0 yet then it keeps counting down
            timer--;
        }
    }

    /* Used to override the super collision class
     * Pre: Takes in an enemy object
     * Post: Returns true because the mortar tower will always shoot, regardless of if there are enemies or not
     */
    @Override
    public boolean collision(Enemy enemy) {
        return true;
    }

    /* Used to paint the tower, projectiles and it's range when necessary
     * Pre: Takes in a Graphics2D object which paints things, a boolean for if the tower placement is valid and a boolean for if the range should be shown
     * Post: Doesn't return anything
     */
    @Override
    public void paint(Graphics2D g, boolean isValid, boolean isRange) {
        // Calls the super paint method
        super.paint(g, isValid, isRange);
        if(isRange) {
            // If the range is showing then it will also paint the target sprite
            g.drawImage(target, (int)targetPosition.x - Map.TILE_SIZE / 2, (int)targetPosition.y - Map.TILE_SIZE / 2, null);
        }
    }

    /* Used to create a projectile
     * Pre: Takes in the enemy that the tower will fire the projectile at
     * Post: Returns a boolean which tells the fire method to break after creating one projectile
     */
    @Override
    protected boolean fireProjectile(Enemy enemy) {
        // Does the same thing as the super method except doesn't flip the tower because it is flipped in another method
        Projectile projectile = this.projectileType.copy(this.getCenter(), this.targetPosition);
        projectiles.add(projectile);
        return true;
    }
}
