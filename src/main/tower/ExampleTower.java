package main.tower;

import main.IntCoord;
import main.enemy.Enemy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ExampleTower extends Tower {

    public ExampleTower(int damage, int range, int cost, int fireRate, IntCoord tileLocation, BufferedImage image, Projectile projectileType) {
        super(damage, range, cost, fireRate, tileLocation, image, projectileType);
    }

    @Override
    public boolean fireProjectile(Enemy enemy) {

        return true;
    }

    @Override
    public void fire(ArrayList<Enemy> enemies) {
        int count = 0;
        if (timer <= 0) {
            for (Enemy enemy : enemies) {
                if (this.collision(enemy)) {
                    if (this.fireProjectile(enemy)) {
                        count++;
                    }
                    if(count >= 2) {
                        break;
                    }
                }
            }
            timer = this.fireRate;
        }
        else {
            timer--;
        }
    }
}
