package main.tower;

import main.IntCoord;
import main.enemy.Enemy;

import java.awt.image.BufferedImage;

public class ExampleTower extends Tower {
    public ExampleTower(int damage, int range, int fireRate, IntCoord tileLocation, BufferedImage image) {
        super(damage, range, fireRate, tileLocation, image);
    }

    @Override
    public boolean fireProjectile(Enemy enemy) {
        Projectile project = new ProjectileExample(20, this.getCenter(), 5, enemy.getCenter());
        projectiles.add(project);
        return false;
    }
}
