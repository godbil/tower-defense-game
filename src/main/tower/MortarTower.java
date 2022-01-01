package main.tower;

import main.DoubleCoord;
import main.IntCoord;
import main.Map;
import main.enemy.Enemy;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;


public class MortarTower extends Tower{
    private DoubleCoord targetPosition;
    private BufferedImage target;

    public MortarTower(int damage, int range, int cost, int fireRate, BufferedImage sprite, Projectile projectileType, BufferedImage target) {
        super(damage, range, cost, fireRate, sprite, projectileType);
        this.target = target;
        targetPosition = new DoubleCoord(Map.TILE_SIZE + Map.TILE_SIZE / 2.0, 4 * Map.TILE_SIZE + Map.TILE_SIZE / 2.0);
    }

    public MortarTower(int damage, int range, int cost, int fireRate, IntCoord tileLocation, BufferedImage sprite, Projectile projectileType, BufferedImage target) {
        super(damage, range, cost, fireRate, tileLocation, sprite, projectileType);
        this.target = target;
        targetPosition = new DoubleCoord(Map.TILE_SIZE + Map.TILE_SIZE / 2.0, 4 * Map.TILE_SIZE + Map.TILE_SIZE / 2.0);
    }

    public void setTargetPosition(DoubleCoord position) {
        targetPosition = position;
    }

    @Override
    public MortarTower copy(IntCoord tileLocation){
        return new MortarTower(this.damage, this.range, this.cost, this.fireRate, tileLocation, this.sprite, this.projectileType, this.target);
    }

    @Override
    public void paint(Graphics2D g, boolean isValid, boolean isRange) {
        double offset = Map.TILE_SIZE / 2.0 - this.range;
        for (Projectile projectile : projectiles) {
            projectile.paint(g);
        }
        if(isFlipped){
            g.drawImage(sprite, this.tileLocation.x * Map.TILE_SIZE + sprite.getWidth(), this.tileLocation.y * Map.TILE_SIZE, -sprite.getWidth(), sprite.getHeight(), null);
        }
        else {
            g.drawImage(sprite, this.tileLocation.x * Map.TILE_SIZE, this.tileLocation.y * Map.TILE_SIZE, null);
        }

        if(isRange) {
            Ellipse2D circle = new Ellipse2D.Double(this.tileLocation.x * Map.TILE_SIZE + offset, this.tileLocation.y * Map.TILE_SIZE + offset, this.range * 2, this.range * 2);
            g.drawImage(target, (int)targetPosition.x - Map.TILE_SIZE / 2, (int)targetPosition.y - Map.TILE_SIZE / 2, null);
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

    @Override
    protected boolean fireProjectile(Enemy enemy) {
        this.isFlipped = this.getCenter().x < this.targetPosition.x;
        Projectile projectile = this.projectileType.copy(this.getCenter(), this.targetPosition);
        projectiles.add(projectile);
        return true;
    }
}
