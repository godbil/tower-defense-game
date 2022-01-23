package main.tower;

import main.DoubleCoord;

import java.awt.image.BufferedImage;

public class RotatableProjectile extends Projectile{

    public RotatableProjectile(double speed, int size, BufferedImage sprite) {
        super(speed, size, sprite);
    }

    public RotatableProjectile(double speed, DoubleCoord position, int size, DoubleCoord target, BufferedImage sprite) {
        super(speed, position, size, target, sprite);
        // Calls the rotate method to rotate this projectile's sprite
        rotate(findAngle(target));
    }

    /* Used to make a copy of the current rotatable projectile
     * Pre: Takes in a double coordinate which is the position of the projectile and another double coordinate which is the target of the projectile
     * Post: Returns the new copy of the rotatable projectile
     */
    @Override
    public RotatableProjectile copy(DoubleCoord position, DoubleCoord target){
        return new RotatableProjectile(this.speed, position, this.size, target, this.sprite);
    }

    /* Used to find the angle of the of rotation by using the target's position
     * Pre: Takes in a double coordinate which is the target of the projectile
     * Post: Returns a double which is the angle of rotation
     */
    private double findAngle(DoubleCoord target) {
        // Takes the width and height of a triangle between the position and the target
        double width = this.position.x - target.x;
        double height = this.position.y - target.y;
        // Use inverse of tan to find the angle of the triangle
        return -Math.atan2(width, height);
    }
}
