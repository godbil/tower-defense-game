package main.tower;

import main.Game;
import main.IntCoord;
import main.Map;
import main.enemy.Enemy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class FarmTower extends Tower{
    private final int moneyGain;
    private int storedMoney;
    private final int maxStoredMoney;
    private int moneyShowTimer;

    public FarmTower(int damage, int range, int cost, int fireRate, BufferedImage image, Projectile projectileType, int moneyGain, int maxStoredMoney, boolean camoHittable, boolean magicProofHittable, boolean armourHittable) {
        super(damage, range, cost, fireRate, image, projectileType, camoHittable, magicProofHittable, armourHittable);
        this.moneyGain = moneyGain;
        this.maxStoredMoney = maxStoredMoney;
        this.storedMoney = 0;
        this.moneyShowTimer = 0;
    }

    public FarmTower(int damage, int range, int cost, int fireRate, IntCoord tileLocation, BufferedImage image, Projectile projectileType, int moneyGain, int maxStoredMoney, boolean camoHittable, boolean magicProofHittable, boolean armourHittable) {
        super(damage, range, cost, fireRate, tileLocation, image, projectileType, camoHittable, magicProofHittable, armourHittable);
        this.moneyGain = moneyGain;
        this.maxStoredMoney = maxStoredMoney;
        this.storedMoney = 0;
        this.moneyShowTimer = 0;
    }

    @Override
    public void fire(ArrayList<Enemy> enemies) {
        if(storedMoney <= maxStoredMoney) {
            if (timer <= 0) {
                storedMoney += moneyGain;
                timer = this.fireRate;
                moneyShowTimer = Game.FPS;
            }
            else {
                timer--;
            }
        }
    }

    @Override
    public void paint(Graphics2D g, boolean isValid, boolean isRange) {
        super.paint(g, isValid, isRange);
        if(moneyShowTimer > 0) {
            g.setPaint(Color.white);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g.drawString("+$" + moneyGain, this.tileLocation.x * Map.TILE_SIZE + Map.TILE_SIZE / 6,this.tileLocation.y * Map.TILE_SIZE);
            moneyShowTimer--;
        }
    }

    @Override
    public FarmTower copy(IntCoord tileLocation, double moneyMultiplier){
        return new FarmTower(this.damage, this.range, (int)Math.round(this.cost * moneyMultiplier), this.fireRate, tileLocation, this.sprite, this.projectileType, this.moneyGain, this.maxStoredMoney, this.camoHittable, this.magicProofHittable, this.armourHittable);
    }

    public int getStoredMoney() {
        return storedMoney;
    }

    public void resetStoredMoney() {
        storedMoney = 0;
    }
}
