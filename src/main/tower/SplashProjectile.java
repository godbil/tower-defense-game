package main.tower;

import main.DoubleCoord;
import main.enemy.Enemy;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class SplashProjectile extends RotatableProjectile {
    private int splashSize;
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

    @Override
    public void hit() {
        this.hitCount++;
    }

    @Override
    public void postUpdate() {
        if(isSplash) {
            this.size = this.splashSize;
        }
        if(this.hitCount > 0) {
            this.active = false;
        }
    }

    @Override
    public boolean collision(Enemy enemy) {
        if(super.collision(enemy)) {
            if(this.size != this.splashSize) {
                isSplash = true;
            }
            else {
                return true;
            }
        }
        return false;
    }

    @Override
    public SplashProjectile copy(DoubleCoord position, DoubleCoord target){
        return new SplashProjectile(this.speed, position, this.size, target, this.sprite, this.splashSize);
    }
}
