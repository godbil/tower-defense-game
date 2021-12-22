package main.tower;

import main.DoubleCoord;
import main.IntCoord;
import main.Map;
import main.enemy.Enemy;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Tower {
    private int damage;
    private int range;
    private int fireRate;
    private int timer;
    private IntCoord tileLocation;
    private final ArrayList<Projectile> projectiles;

    public Tower(int damage, int range, int fireRate, IntCoord tileLocation) {
        this.damage = damage;
        this.range = 4 * Map.TILE_SIZE;
        this.fireRate = fireRate;
        this.timer = 0;
        this.tileLocation = tileLocation;
        this.projectiles = new ArrayList<>();
    }

    public void update(ArrayList<Enemy> enemies) {
        if(timer <= 0) {
            for (Enemy enemy : enemies) {
                if (this.collision(enemy)) {
                    this.fireProjectile(enemy);
                    break;
                }
            }
            timer = this.fireRate;
        }
        else{
            timer--;
        }
        for (Projectile projectile : projectiles) {
            projectile.update();
            for(Enemy enemy : enemies) {
                if (projectile.collision(enemy)) {
                    enemy.takeDamage(this.damage);
                    projectile.hit();
                }
            }
        }
        projectiles.removeIf(projectile -> !projectile.isActive());
    }

    public boolean collision(Enemy enemy) {
        double distance = Math.pow(enemy.getCenter().x - this.getCenter().x, 2) + Math.pow(enemy.getCenter().y - this.getCenter().y, 2);
        return distance < Math.pow(enemy.getRadius() + range, 2);
    }

    public void paint(Graphics2D g) {
        double offset = Map.TILE_SIZE / 2.0 - this.range;
        for (Projectile projectile : projectiles) {
            projectile.paint(g);
        }

        Ellipse2D circle = new Ellipse2D.Double(this.tileLocation.x * Map.TILE_SIZE + offset, this.tileLocation.y * Map.TILE_SIZE + offset, this.range * 2, this.range * 2);
        Color rangeColor = new Color(0.4f,0.4f,0.4f,.3f );
        g.setPaint(rangeColor);
        g.draw(circle);
        g.fill(circle);

        Rectangle2D rect = new Rectangle(this.tileLocation.x * Map.TILE_SIZE, this.tileLocation.y * Map.TILE_SIZE, Map.TILE_SIZE, Map.TILE_SIZE);
        g.setPaint(Color.BLUE);
        g.draw(rect);
        g.fill(rect);

    }

    protected void fireProjectile(Enemy enemy) {
        Projectile project = new Projectile(20, this.getCenter(), 5, enemy.getCenter());
        projectiles.add(project);
    }

    private DoubleCoord getCenter(){
        return new DoubleCoord(this.tileLocation.x * Map.TILE_SIZE + Map.TILE_SIZE / 2.0, this.tileLocation.y * Map.TILE_SIZE + Map.TILE_SIZE / 2.0);
    }

}
