package main.tower;

import main.IntCoord;
import main.enemy.Enemy;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CircleShootingTower extends Tower {
    // Statistics for the circle shooting tower like it's damage and how often it shoots
    protected int circleShooterDamage;
    protected int circleShooterTimer;
    protected int tempTimer;
    // Pulse size which is used for the growing circle effect and a boolean for if the tower shoots
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

    /* Used to update all of the tower's variables and calls their other methods which needs to be checked every frame
     * Pre: Takes in an arraylist which is the enemies
     * Post: Doesn't return anything
     */
    @Override
    public void update(ArrayList<Enemy> enemies) {
        // Calls the super update because it is also needed
        super.update(enemies);
        // Calls this method will checks for if enemies get damaged by the circle
        circleShootDamage(enemies);
        if(isShooting && pulseSize <= this.range * 2) {
            // Checks if the tower is shooting the circle and if the pulse size hasn't reached the range of the tower to add 10 pixels the the pulse size
            this.pulseSize += 10;
        }
        else {
            // If the pulse size has reached the range size then it will set the tower to not shooting and reset the pulse size
            isShooting = false;
            this.pulseSize = 0;
        }
    }

    /* Used to paint the tower, projectiles and it's range when necessary
     * Pre: Takes in a Graphics2D object which paints things
     * Post: Doesn't return anything
     */
    @Override
    public void paint(Graphics2D g) {
        if(isShooting) {
            // Checks if the tower is shooting and if it is then it will draw the circle pulse which grows in size until reaching the range size
            Ellipse2D circlePulse = new Ellipse2D.Double(this.getCenter().x - pulseSize / 2.0, this.getCenter().y - pulseSize / 2.0, pulseSize, pulseSize);
            g.setPaint(new Color(0.5f, 0.5f, 0.5f, 0.6f));
            g.draw(circlePulse);
            g.fill(circlePulse);
        }
        super.paint(g, false, false);
    }

    /* Used to make a copy of the current circle shooting tower
     * Pre: Takes in an int coordinate which is the tile location and a double which is the money multiplier to increase or decrease cost
     * Post: Returns the new copy of the circle shooting tower
     */
    @Override
    public CircleShootingTower copy(IntCoord tileLocation, double moneyMultiplier){
        return new CircleShootingTower(this.damage, this.range, (int) Math.round(this.cost * moneyMultiplier), (int) Math.round(this.totalCost * moneyMultiplier), this.fireRate, tileLocation, this.sprite, this.projectileType, this.camoHittable, this.magicProofHittable, this.armourHittable, this.circleShooterDamage, this.circleShooterTimer, this.towerUpgradePaths);
    }

    /* Used to check for which enemies got damaged by the circle
     * Pre: Takes in an arraylist of all the enemies
     * Post: Doesn't return anything
     */
    private void circleShootDamage(ArrayList<Enemy> enemies) {
        if(tempTimer <= 0) {
            // If the circle shooting timer has reached 0 then it will check for collision between its range and all enemies
            for(Enemy enemy : enemies) {
                if(this.collision(enemy)) {
                    // Sets isShooting to true and then breaks out of the for loop
                    isShooting = true;
                    break;
                }
            }
            // Resetting the timer
            this.tempTimer = circleShooterTimer;
        }
        else {
            // If the timer hasn't reached 0 yet then it will keep counting down
            tempTimer--;
        }
        for(Enemy enemy : enemies) {
            // Checks all of the enemies to see who got hit by the circle
            if(this.circleShootCollision(enemy)) {
                enemy.takeDamage(circleShooterDamage);
            }
        }
    }

    /* Used to check collision between the circle pulse and enemies
     * Pre: Takes in an enemy object
     * Post: Returns a boolean for whether the enemy was hit or not
     */
    private boolean circleShootCollision(Enemy enemy) {
        // Checks if the circle shoot tower can hit the enemy
        if (enemy.isCamo() && !this.camoHittable || (enemy.isMagicProof() && !this.magicProofHittable) || (enemy.isArmoured() && !this.armourHittable)){
            return false;
        }
        else {
            // Finds the distance between the enemy's center and the tower's center
            double distance = Math.pow(enemy.getCenter().x - this.getCenter().x, 2) + Math.pow(enemy.getCenter().y - this.getCenter().y, 2);
            // Checks if the distance is less then the enemy's radius and the pulse size radius to the power of 2
            return distance < Math.pow(enemy.getRadius() + this.pulseSize / 2.0, 2);
        }
    }
}
