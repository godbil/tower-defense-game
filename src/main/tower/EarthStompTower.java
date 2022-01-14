package main.tower;

import main.IntCoord;
import main.Map;
import main.enemy.Enemy;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EarthStompTower extends Tower{
    private int earthStompTimer;
    private int tempTimer;
    private int imageTimer;
    private float alpha;
    private boolean isEarthStomping;

    public EarthStompTower(int damage, int range, int cost, int totalCost, int fireRate, BufferedImage image, Projectile projectileType, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, int earthStompTimer, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, totalCost, fireRate, image, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.earthStompTimer = earthStompTimer;
        this.tempTimer = earthStompTimer;
        this.imageTimer = 20;
        this.alpha = 1f;
        this.isEarthStomping = false;
    }

    public EarthStompTower(int damage, int range, int cost, int totalCost, int fireRate, IntCoord tileLocation, BufferedImage image, Projectile projectileType, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, int earthStompTimer, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, totalCost, fireRate, tileLocation, image, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.earthStompTimer = earthStompTimer;
        this.tempTimer = earthStompTimer;
        this.imageTimer = 20;
        this.alpha = 1f;
        this.isEarthStomping = false;
    }

    @Override
    public void update(ArrayList<Enemy> enemies) {
        super.update(enemies);
        earthStompDamage(enemies);
    }

    @Override
    public void paint(Graphics2D g) {
        double offset = Map.TILE_SIZE / 2.0 - this.range;
        if(isEarthStomping) {
            Ellipse2D earthStomp = new Ellipse2D.Double(this.tileLocation.x * Map.TILE_SIZE + offset, this.tileLocation.y * Map.TILE_SIZE + offset, this.range * 2, this.range * 2);
            g.setPaint(new Color(0.3f, 0.2f, 0.2f, alpha));
            g.draw(earthStomp);
            g.fill(earthStomp);
            alpha -= 0.1f;
            if(alpha <= 0f) {
                this.isEarthStomping = false;
                alpha = 1f;
            }
        }
        paint(g, false, false);
    }

    @Override
    public EarthStompTower copy(IntCoord tileLocation, double moneyMultiplier){
        return new EarthStompTower(this.damage, this.range, (int) Math.round(this.cost * moneyMultiplier), (int) Math.round(this.totalCost * moneyMultiplier), this.fireRate, tileLocation, this.sprite, this.projectileType, this.camoHittable, this.magicProofHittable, this.armourHittable, this.earthStompTimer, this.towerUpgradePaths);
    }

    private void earthStompDamage(ArrayList<Enemy> enemies) {
        if(tempTimer <= 0) {
            for(Enemy enemy : enemies) {
                if(this.collision(enemy)) {
                    enemy.takeDamage(this.damage);
                    isEarthStomping = true;
                }
            }
            this.tempTimer = earthStompTimer;
        }
        else {
            tempTimer--;
        }
    }
}
