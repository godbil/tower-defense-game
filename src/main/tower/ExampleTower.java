package main.tower;

import main.IntCoord;
import main.enemy.Enemy;

public class ExampleTower extends Tower {
    public ExampleTower(int damage, int range, int fireRate, IntCoord tileLocation) {
        super(damage, range, fireRate, tileLocation);
    }

    @Override
    public boolean fireProjectile(Enemy enemy) {
        Projectile project = new ProjectileExample(20, this.getCenter(), 5, enemy.getCenter());
        projectiles.add(project);
        return false;
    }
}
