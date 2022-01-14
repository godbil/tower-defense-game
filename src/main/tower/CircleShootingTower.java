package main.tower;

import main.IntCoord;
import main.Map;
import main.enemy.Enemy;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CircleShootingTower extends Tower {
    protected int circleShooterDamage;
    protected int circleShooterTimer;
    protected int tempTimer;
    protected int pulseSize;
    protected boolean isShooting;

    public CircleShootingTower(int damage, int range, int cost, int totalCost, int fireRate, BufferedImage image, Projectile projectileType, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, int circleShooterDamage, int circleShooterTimer, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, totalCost, fireRate, image, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.circleShooterTimer = circleShooterTimer;
        this.circleShooterDamage = circleShooterDamage;
        this.tempTimer = circleShooterTimer;
        this.pulseSize = 0;
        this.isShooting = false;
    }

    public CircleShootingTower(int damage, int range, int cost, int totalCost,  int fireRate, IntCoord tileLocation, BufferedImage image, Projectile projectileType, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, int circleShooterDamage, int circleShooterTimer, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, totalCost, fireRate, tileLocation, image, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.circleShooterTimer = circleShooterTimer;
        this.circleShooterDamage = circleShooterDamage;
        this.tempTimer = circleShooterTimer;
        this.pulseSize = 0;
        this.isShooting = false;
    }

    @Override
    public void update(ArrayList<Enemy> enemies) {
        super.update(enemies);
        aquaRingDamage(enemies);
    }

    @Override
    public void paint(Graphics2D g) {
        if(isShooting) {
            if(pulseSize <= this.range * 2) {
                this.pulseSize += 10;
                Ellipse2D aquaPulse = new Ellipse2D.Double(this.getCenter().x - pulseSize / 2.0, this.getCenter().y - pulseSize / 2.0, pulseSize, pulseSize);
                g.setPaint(new Color(0.5f, 0.5f, 0.5f, 0.6f));
                g.draw(aquaPulse);
                g.fill(aquaPulse);
            }
            else {
                isShooting = false;
                this.pulseSize = 0;
            }
        }
        paint(g, false, false);
    }

    @Override
    public CircleShootingTower copy(IntCoord tileLocation, double moneyMultiplier){
        return new CircleShootingTower(this.damage, this.range, (int) Math.round(this.cost * moneyMultiplier), (int) Math.round(this.totalCost * moneyMultiplier), this.fireRate, tileLocation, this.sprite, this.projectileType, this.camoHittable, this.magicProofHittable, this.armourHittable, this.circleShooterDamage, this.circleShooterTimer, this.towerUpgradePaths);
    }

    private void aquaRingDamage(ArrayList<Enemy> enemies) {
        if(tempTimer <= 0) {
            for(Enemy enemy : enemies) {
                if(this.collision(enemy)) {
                    isShooting = true;
                    break;
                }
            }
            this.tempTimer = circleShooterTimer;
        }
        else {
            tempTimer--;
        }
        for(Enemy enemy : enemies) {
            if(this.aquaRingCollision(enemy)) {
                enemy.takeDamage(circleShooterDamage);
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
