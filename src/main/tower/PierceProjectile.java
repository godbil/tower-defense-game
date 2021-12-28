package main.tower;

import main.DoubleCoord;

import java.awt.image.BufferedImage;

public class PierceProjectile extends Projectile {
    private int pierceCap;

    public PierceProjectile(double speed, int size, BufferedImage sprite, int pierceCap) {
        super(speed, size, sprite);
        this.pierceCap = pierceCap;
    }

    public PierceProjectile(double speed, DoubleCoord position, int size, DoubleCoord target, BufferedImage sprite, int pierceCap) {
        super(speed, position, size, target, sprite);
        this.pierceCap = pierceCap;
    }

    @Override
    public void hit() {
        this.hitCount++;
        if(this.hitCount >= this.pierceCap) {
            this.active = false;
        }
    }

    @Override
    public PierceProjectile copy(DoubleCoord position, DoubleCoord target){
        return new PierceProjectile(this.speed, position, this.size, target, this.sprite, this.pierceCap);
    }
}
