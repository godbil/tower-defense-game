package main.tower;

import main.IntCoord;
import main.Map;
import main.enemy.Enemy;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class AquaRingTower extends Tower {
    private int aquaRingDamage;
    private int aquaRingTimer;
    private int tempTimer;
    private int pulseSize;
    private boolean isAquaRinging;

    public AquaRingTower(int damage, int range, int cost, int fireRate, BufferedImage image, Projectile projectileType, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, int aquaRingDamage, int aquaRingTimer, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, fireRate, image, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.aquaRingTimer = aquaRingTimer;
        this.aquaRingDamage = aquaRingDamage;
        this.tempTimer = aquaRingTimer;
        this.pulseSize = 0;
        this.isAquaRinging = false;
    }

    public AquaRingTower(int damage, int range, int cost, int fireRate, IntCoord tileLocation, BufferedImage image, Projectile projectileType, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, int aquaRingDamage, int aquaRingTimer, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, fireRate, tileLocation, image, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.aquaRingTimer = aquaRingTimer;
        this.aquaRingDamage = aquaRingDamage;
        this.tempTimer = aquaRingTimer;
        this.pulseSize = 0;
        this.isAquaRinging = false;
    }

    @Override
    public void update(ArrayList<Enemy> enemies) {
        super.update(enemies);
        aquaRingDamage(enemies);
    }

    @Override
    public void paint(Graphics2D g) {
        if(isAquaRinging) {
            if(pulseSize <= this.range * 2) {
                this.pulseSize += 10;
                Ellipse2D aquaPulse = new Ellipse2D.Double(this.getCenter().x - pulseSize / 2.0, this.getCenter().y - pulseSize / 2.0, pulseSize, pulseSize);
                g.setPaint(new Color(0.2f, 0.2f, 0.5f, 0.6f));
                g.draw(aquaPulse);
                g.fill(aquaPulse);
            }
            else {
                isAquaRinging = false;
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

    @Override
    public AquaRingTower copy(IntCoord tileLocation, double moneyMultiplier){
        return new AquaRingTower(this.damage, this.range, (int) Math.round(this.cost * moneyMultiplier), this.fireRate, tileLocation, this.sprite, this.projectileType, this.camoHittable, this.magicProofHittable, this.armourHittable, this.aquaRingDamage, this.aquaRingTimer, this.towerUpgradePaths);
    }

    private void aquaRingDamage(ArrayList<Enemy> enemies) {
        if(tempTimer <= 0) {
            for(Enemy enemy : enemies) {
                if(this.collision(enemy)) {
                    isAquaRinging = true;
                    break;
                }
            }
            this.tempTimer = aquaRingTimer;
        }
        else {
            tempTimer--;
        }
        for(Enemy enemy : enemies) {
            if(this.aquaRingCollision(enemy)) {
                enemy.takeDamage(aquaRingDamage);
            }
        }
    }

    private boolean aquaRingCollision(Enemy enemy) {
        if (enemy.isCamo() && !this.camoHittable || (enemy.isMagicProof() && !this.magicProofHittable) || (enemy.isArmoured() && !this.armourHittable)){
            return false;
        }
        else {
            double distance = Math.pow(enemy.getCenter().x - this.getCenter().x, 2) + Math.pow(enemy.getCenter().y - this.getCenter().y, 2);
            return distance < Math.pow(enemy.getRadius() + this.pulseSize / 2.0, 2);
        }
    }
}
