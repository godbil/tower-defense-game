package main.tower;

import main.DoubleCoord;

import java.awt.image.BufferedImage;

public class RotatableProjectile extends Projectile{

    public RotatableProjectile(double speed, int size, BufferedImage sprite) {
        super(speed, size, sprite);
    }

    public RotatableProjectile(double speed, DoubleCoord position, int size, DoubleCoord target, BufferedImage sprite) {
        super(speed, position, size, target, sprite);
        rotate(findAngle(target));
    }

    @Override
    public RotatableProjectile copy(DoubleCoord position, DoubleCoord target){
        return new RotatableProjectile(this.speed, position, this.size, target, this.sprite);
    }

    private double findAngle(DoubleCoord target) {
        double width = this.position.x - target.x;
        double height = this.position.y - target.y;
        return -Math.atan2(width, height);
    }
}
