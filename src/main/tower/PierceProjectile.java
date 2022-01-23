package main.tower;

import main.DoubleCoord;

import java.awt.image.BufferedImage;

public class PierceProjectile extends RotatableProjectile {
    // The amount of enemies that can be hit before becoming inactive
    private int pierceCap;

    public PierceProjectile(double speed, int size, BufferedImage sprite, int pierceCap) {
        super(speed, size, sprite);
        this.pierceCap = pierceCap;
    }

    public PierceProjectile(double speed, DoubleCoord position, int size, DoubleCoord target, BufferedImage sprite, int pierceCap) {
        super(speed, position, size, target, sprite);
        this.pierceCap = pierceCap;
    }

    /* Used to set the projectile to inactive once it hits the pierce cap
     * Pre: Doesn't take in any parameters
     * Post: Doesn't return anything
     */
    @Override
    public void hit() {
        // Increases the amount of enemies that have been hit
        this.hitCount++;
        if(this.hitCount >= this.pierceCap) {
            // If the amount of enemies hit are above or equal to the pierce cap then it gets set to inactive
            this.active = false;
        }
    }

    /* Used to make a copy of the current pierce projectile
     * Pre: Takes in a double coordinate which is the position of the projectile and another double coordinate which is the target of the projectile
     * Post: Returns the new copy of the pierce projectile
     */
    @Override
    public PierceProjectile copy(DoubleCoord position, DoubleCoord target){
        return new PierceProjectile(this.speed, position, this.size, target, this.sprite, this.pierceCap);
    }
}
