package main.tower;

import main.DoubleCoord;
import main.IntCoord;
import main.Map;
import main.enemy.Enemy;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Tower {
    protected int damage;
    protected int range;
    protected int cost;

    protected int fireRate;
    protected int timer;

    protected IntCoord tileLocation;

    protected final ArrayList<Projectile> projectiles;
    protected Projectile projectileType;

    private BufferedImage sprite;

    public Tower(int damage, int range, int cost, int fireRate, BufferedImage image, Projectile projectileType) {
        this.damage = damage;
        this.range = range;
        this.cost = cost;
        this.fireRate = fireRate;
        this.timer = 0;

        this.projectiles = new ArrayList<>();
        this.projectileType = projectileType;

        this.sprite = image;
    }

    public Tower(int damage, int range, int cost, int fireRate, IntCoord tileLocation, BufferedImage image, Projectile projectileType) {
        this.damage = damage;
        this.range = range;
        this.cost = cost;
        this.fireRate = fireRate;
        this.timer = 0;
        this.tileLocation = tileLocation;

        this.projectiles = new ArrayList<>();
        this.projectileType = projectileType;

        this.sprite = image;
    }

    public void update(ArrayList<Enemy> enemies) {
        fire(enemies);
        for (Projectile projectile : projectiles) {
            projectile.update();
            for(Enemy enemy : enemies) {
                if (projectile.isActive() && projectile.collision(enemy)) {
                    enemy.takeDamage(this.damage);
                    projectile.hit();
                }
            }
            projectile.postUpdate();
        }
        projectiles.removeIf(projectile -> !projectile.isActive());
    }

    public void fire(ArrayList<Enemy> enemies) {
        if(timer <= 0) {
            for (Enemy enemy : enemies) {
                if (this.collision(enemy)) {
                    if(this.fireProjectile(enemy)) {
                        break;
                    }
                }
            }
            timer = this.fireRate;
        }
        else{
            timer--;
        }
    }

    public boolean collision(Enemy enemy) {
        double distance = Math.pow(enemy.getCenter().x - this.getCenter().x, 2) + Math.pow(enemy.getCenter().y - this.getCenter().y, 2);
        return distance < Math.pow(enemy.getRadius() + range, 2);
    }

    public void paint(Graphics2D g) {
        paint(g, false, false);
    }

    public void paint(Graphics2D g, boolean isValid, boolean isRange) {
        double offset = Map.TILE_SIZE / 2.0 - this.range;
        for (Projectile projectile : projectiles) {
            projectile.paint(g);
        }
        g.drawImage(sprite, this.tileLocation.x * Map.TILE_SIZE, this.tileLocation.y * Map.TILE_SIZE, null);

        if(isRange) {
            Ellipse2D circle = new Ellipse2D.Double(this.tileLocation.x * Map.TILE_SIZE + offset, this.tileLocation.y * Map.TILE_SIZE + offset, this.range * 2, this.range * 2);
            Color rangeColor;
            if(isValid) {
                rangeColor = new Color(0.6f, 0.6f, 0.6f, .4f);
            }
            else{
                rangeColor = new Color(1f, 0.0f, 0.0f, .4f);
            }
            g.setPaint(rangeColor);
            g.draw(circle);
            g.fill(circle);
        }
    }

    public void setTileLocation(IntCoord tileLocation) {
        this.tileLocation = tileLocation;
    }

    public int getCost() {
        return this.cost;
    }

    public IntCoord getTileLocation() {
        return this.tileLocation;
    }

    public Tower copy(IntCoord tileLocation){
        return new Tower(this.damage, this.range, this.cost, this.fireRate, tileLocation, this.sprite, this.projectileType);
    }

    public BufferedImage getSprite() {
        return this.sprite;
    }

    protected boolean fireProjectile(Enemy enemy) {
        Projectile projectile = this.projectileType.copy(this.getCenter(), enemy.getCenter());
        projectiles.add(projectile);
        return true;
    }

    protected DoubleCoord getCenter(){
        return new DoubleCoord(this.tileLocation.x * Map.TILE_SIZE + Map.TILE_SIZE / 2.0, this.tileLocation.y * Map.TILE_SIZE + Map.TILE_SIZE / 2.0);
    }

}
