package main.tower;

import main.DoubleCoord;
import main.enemy.Enemy;

import java.awt.image.BufferedImage;

public class SplashProjectile extends RotatableProjectile {
    // The size of the splash radius which is separate from the projectile's size
    private int splashSize;
    // A boolean for seeing if the projectile is splashing
    private boolean isSplash;

    public SplashProjectile(double speed, int size, BufferedImage sprite, int splashSize) {
        super(speed, size, sprite);
        this.splashSize = splashSize;
        this.isSplash = false;
    }

    public SplashProjectile(double speed, DoubleCoord position, int size, DoubleCoord target, BufferedImage sprite, int splashSize) {
        super(speed, position, size, target, sprite);
        this.splashSize = splashSize;
        this.isSplash = false;
    }

    /* Used to add to the hit count
     * Pre: Doesn't take in any parameters
     * Post: Doesn't return anything
     */
    @Override
    public void hit() {
        this.hitCount++;
    }

    /* Used to set the size of the projectile to the splash size and sets the projectile to inactive once it hits something
     * Pre: Doesn't take in any parameters
     * Post: Doesn't return anything
     */
    @Override
    public void postUpdate() {
        if(isSplash) {
            // Sets size to the splash size if the projectile is splashing
            this.size = this.splashSize;
        }
        if(this.hitCount > 0) {
            // Makes the projectile inactive if the projectile hit something
            this.active = false;
        }
    }

    /* Used to check for collision between the projectile and the enemy
     * Pre: Takes in an enemy object
     * Post: Returns a boolean which is if there is collision or not
     */
    @Override
    public boolean collision(Enemy enemy) {
        if(super.collision(enemy)) {
            // Checks if there is collision between the projectile and the enemy before checking if the size is equal to the splash size
            if(this.size != this.splashSize) {
                // If not then isSplash is set to true and then it will return false so the collision can run again with the size being splash size
                isSplash = true;
            }
            else {
                // If it is already the same size then it will return true
                return true;
            }
        }
        return false;
    }

    /* Used to make a copy of the current splash projectile
     * Pre: Takes in a double coordinate which is the position of the projectile and another double coordinate which is the target of the projectile
     * Post: Returns the new copy of the splash projectile
     */
    @Override
    public SplashProjectile copy(DoubleCoord position, DoubleCoord target){
        return new SplashProjectile(this.speed, position, this.size, target, this.sprite, this.splashSize);
    }
}
