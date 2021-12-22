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

    public Tower(int damage, int range, int fireRate, IntCoord tileLocation) {
        this.damage = damage;
        this.range = 4 * Map.TILE_SIZE;
        this.fireRate = fireRate;
        this.timer = fireRate;
        this.tileLocation = tileLocation;
    }

    public void update(ArrayList<Enemy> enemies) {
        if(timer <= 0) {
            for (Enemy enemy : enemies) {
                if (this.collision(enemy)) {
                    enemy.takeDamage(this.damage);
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
        double offset = Map.TILE_SIZE / 2.0 - this.range;
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

    private DoubleCoord getCenter(){
        return new DoubleCoord(this.tileLocation.x * Map.TILE_SIZE + Map.TILE_SIZE / 2.0, this.tileLocation.y * Map.TILE_SIZE + Map.TILE_SIZE / 2.0);
    }

}
