package main.tower;

import main.IntCoord;
import main.Map;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class AquaRingTower extends CircleShootingTower{
    public AquaRingTower(int damage, int range, int cost, int totalCost, int fireRate, BufferedImage image, Projectile projectileType, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, int circleShooterDamage, int circleShooterTimer, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, totalCost, fireRate, image, projectileType, camoHittable, magicProofHittable, armourHittable, circleShooterDamage, circleShooterTimer, towerUpgradePaths);
    }

    public AquaRingTower(int damage, int range, int cost, int totalCost, int fireRate, IntCoord tileLocation, BufferedImage image, Projectile projectileType, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, int circleShooterDamage, int circleShooterTimer, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, totalCost, fireRate, tileLocation, image, projectileType, camoHittable, magicProofHittable, armourHittable, circleShooterDamage, circleShooterTimer, towerUpgradePaths);
    }

    @Override
    public AquaRingTower copy(IntCoord tileLocation, double moneyMultiplier){
        return new AquaRingTower(this.damage, this.range, (int) Math.round(this.cost * moneyMultiplier), (int) Math.round(this.totalCost * moneyMultiplier), this.fireRate, tileLocation, this.sprite, this.projectileType, this.camoHittable, this.magicProofHittable, this.armourHittable, this.circleShooterDamage, this.circleShooterTimer, this.towerUpgradePaths);
    }


    @Override
    public void paint(Graphics2D g) {
        if(isShooting) {
            if(pulseSize <= this.range * 2) {
                this.pulseSize += 10;
                Ellipse2D aquaPulse = new Ellipse2D.Double(this.getCenter().x - pulseSize / 2.0, this.getCenter().y - pulseSize / 2.0, pulseSize, pulseSize);
                g.setPaint(new Color(0.2f, 0.2f, 0.5f, 0.6f));
                g.draw(aquaPulse);
                g.fill(aquaPulse);
            }
            else {
                isShooting = false;
                this.pulseSize = 0;
            }
        }
        paint(g, false, false);
        double offset = Map.TILE_SIZE / 2.0 - this.range;
        Ellipse2D aquaRing = new Ellipse2D.Double(this.tileLocation.x * Map.TILE_SIZE + offset, this.tileLocation.y * Map.TILE_SIZE + offset, this.range * 2, this.range * 2);
        Ellipse2D aquaRingInside = new Ellipse2D.Double(this.tileLocation.x * Map.TILE_SIZE + offset, this.tileLocation.y * Map.TILE_SIZE + offset, this.range * 2, this.range * 2);
        g.setPaint(new Color(0.2f, 0.2f, 0.5f, 0.4f));
        g.draw(aquaRingInside);
        g.fill(aquaRingInside);
        g.setPaint(new Color(0.2f, 0.2f, 0.5f));
        g.draw(aquaRing);
    }

}
