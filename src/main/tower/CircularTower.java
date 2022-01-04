package main.tower;

import main.DoubleCoord;
import main.IntCoord;
import main.Map;
import main.enemy.Enemy;

import java.awt.image.BufferedImage;

public class CircularTower extends Tower {
    private int projectileCount;

    public CircularTower(int damage, int range, int cost, int fireRate, BufferedImage image, Projectile projectileType, int projectileCount, boolean camoHittable, boolean magicProofHittable, boolean armourHittable) {
        super(damage, range, cost, fireRate, image, projectileType, camoHittable, magicProofHittable, armourHittable);
        this.projectileCount = projectileCount;
    }

    public CircularTower(int damage, int range, int cost, int fireRate, IntCoord tileLocation, BufferedImage image, Projectile projectileType, int projectileCount, boolean camoHittable, boolean magicProofHittable, boolean armourHittable) {
        super(damage, range, cost, fireRate, tileLocation, image, projectileType, camoHittable, magicProofHittable, armourHittable);
        this.projectileCount = projectileCount;
    }

    @Override
    public CircularTower copy(IntCoord tileLocation){
        return new CircularTower(this.damage, this.range, this.cost, this.fireRate, tileLocation, this.sprite, this.projectileType, this.projectileCount, this.camoHittable, this.magicProofHittable, this.armourHittable);
    }

    @Override
    protected boolean fireProjectile(Enemy enemy) {
        double angle = Math.PI;
        for(double i = angle; i < Math.PI * 2 + angle; i += Math.PI * 2 / (double)projectileCount) {
            Projectile projectile = this.projectileType.copy(new DoubleCoord(Map.TILE_SIZE / 2.0 * Math.cos(i) + getCenter().x, Map.TILE_SIZE / 2.0 * Math.sin(i) + getCenter().y), new DoubleCoord(Math.cos(i) * 10000, (Math.sin(i)* 10000)));
            projectiles.add(projectile);
            projectile.rotate(i);
        }
        return true;
    }

}
