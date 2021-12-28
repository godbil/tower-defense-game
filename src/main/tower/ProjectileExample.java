package main.tower;

import main.DoubleCoord;

import java.awt.image.BufferedImage;

public class ProjectileExample extends Projectile {
    public ProjectileExample(double speed, DoubleCoord position, int size, DoubleCoord target, BufferedImage sprite) {
        super(speed, position, size, target, sprite);
    }

    /*@Override
    public void hit(){
    }*/
}
