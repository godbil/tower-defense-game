package main.tower;

import main.DoubleCoord;
import main.IntCoord;
import main.Map;
import main.enemy.Enemy;

import java.awt.image.BufferedImage;

public class BallistaTower extends Tower{
    private DoubleCoord targetPosition;

    public BallistaTower(int damage, int range, int cost, int fireRate, BufferedImage image, Projectile projectileType) {
        super(damage, range, cost, fireRate, image, projectileType);
        targetPosition = new DoubleCoord(Map.TILE_SIZE, Map.TILE_SIZE);
    }

    public BallistaTower(int damage, int range, int cost, int fireRate, IntCoord tileLocation, BufferedImage image, Projectile projectileType) {
        super(damage, range, cost, fireRate, tileLocation, image, projectileType);
        targetPosition = new DoubleCoord(Map.TILE_SIZE, Map.TILE_SIZE);
    }

    public void setTargetPosition(DoubleCoord position) {
        this.targetPosition = position;
        this.isFlipped = this.getCenter().x < this.targetPosition.x;
    }

    @Override
    public BallistaTower copy(IntCoord tileLocation){
        return new BallistaTower(this.damage, this.range, this.cost, this.fireRate, tileLocation, this.sprite, this.projectileType);
    }

    @Override
    public boolean collision(Enemy enemy) {
        return true;
    }

    @Override
    protected boolean fireProjectile(Enemy enemy) {
        Projectile projectile = this.projectileType.copy(this.getCenter(), this.targetPosition);
        projectiles.add(projectile);
        return true;
    }

    private IntCoord findStart(Map map) {
        for(int i = 0; i < map.getMap().length; i++){
            for(int j = 0; j < map.getMap()[i].length; j++){
                if(map.getMap()[i][j] == Map.START){
                    return new IntCoord(i, j);
                }
            }
        }
        return new IntCoord(0, 0);
    }
}