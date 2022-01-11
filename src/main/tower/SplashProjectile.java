package main.tower;

import main.DoubleCoord;

import java.awt.image.BufferedImage;

public class SplashProjectile extends RotatableProjectile {

    public SplashProjectile(double speed, int size, BufferedImage sprite) {
        super(speed, size, sprite);
    }

    public SplashProjectile(double speed, DoubleCoord position, int size, DoubleCoord target, BufferedImage sprite) {
        super(speed, position, size, target, sprite);
    }

    @Override
    public void hit() {
        this.hitCount++;
    }

    @Override
    public void postUpdate() {
        if(this.hitCount > 0) {
            this.active = false;
        }
    }

    @Override
    public SplashProjectile copy(DoubleCoord position, DoubleCoord target){
        return new SplashProjectile(this.speed, position, this.size, target, this.sprite);
    }
}
