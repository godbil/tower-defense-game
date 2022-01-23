package main.tower;

import main.Game;
import main.IntCoord;
import main.Map;
import main.enemy.Enemy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class FarmTower extends Tower{
    // The amount of money gained each time
    private final int moneyGain;
    // Total amount of stored money in the farm
    private int storedMoney;
    // The maximum amount of money that you can store in the farm
    private final int maxStoredMoney;
    // The timer for showing the money being added to the farm
    private int moneyShowTimer;

    public FarmTower(int damage, int range, int cost, int totalCost, int fireRate, BufferedImage image, Projectile projectileType, int moneyGain, int maxStoredMoney, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, totalCost, fireRate, image, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.moneyGain = moneyGain;
        this.maxStoredMoney = maxStoredMoney;
        this.storedMoney = 0;
        this.moneyShowTimer = 0;
    }

    public FarmTower(int damage, int range, int cost, int totalCost,  int fireRate, IntCoord tileLocation, BufferedImage image, Projectile projectileType, int moneyGain, int maxStoredMoney, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, totalCost, fireRate, tileLocation, image, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.moneyGain = moneyGain;
        this.maxStoredMoney = maxStoredMoney;
        this.storedMoney = 0;
        this.moneyShowTimer = 0;
    }

    /* Used to override the fire method and to see when to add money to the farm
     * Pre: Takes in an arraylist of enemies
     * Post: Doesn't return anything
     */
    @Override
    public void fire(ArrayList<Enemy> enemies) {
        if(storedMoney < maxStoredMoney) {
            // Checks if the current stored money has reached the max and if not then it checks the timer
            if (timer <= 0) {
                // Once the timer is less than or equal to zero then it will add money to the stored amount and then reset the timer
                storedMoney += moneyGain;
                timer = this.fireRate;
                // Sets the money show timer to 60 frames so the amount gained will show for 1 second
                moneyShowTimer = Game.FPS;
            }
            else {
                // If the timer isn't at 0 yet then the timer will keep going down
                timer--;
            }
        }
    }

    /* Used to paint the tower, projectiles and it's range when necessary
     * Pre: Takes in a Graphics2D object which paints things
     * Post: Doesn't return anything
     */
    @Override
    public void paint(Graphics2D g, boolean isValid, boolean isRange) {
        super.paint(g, isValid, isRange);
        if(moneyShowTimer > 0) {
            // If the timer for the money visual is greater than 0 then it will paint the amount of money gained
            g.setPaint(Color.white);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g.drawString("+$" + moneyGain, this.tileLocation.x * Map.TILE_SIZE + Map.TILE_SIZE / 6,this.tileLocation.y * Map.TILE_SIZE);
            // Subtracts until the money show timer gets to 0 which will make the visual stop showing
            moneyShowTimer--;
        }
    }

    /* Used to make a copy of the current farm tower
     * Pre: Takes in an int coordinate which is the tile location and a double which is the money multiplier to increase or decrease cost
     * Post: Returns the new copy of the farm tower
     */
    @Override
    public FarmTower copy(IntCoord tileLocation, double moneyMultiplier){
        return new FarmTower(this.damage, this.range, (int)Math.round(this.cost * moneyMultiplier), (int)Math.round(this.totalCost * moneyMultiplier), this.fireRate, tileLocation, this.sprite, this.projectileType, this.moneyGain, this.maxStoredMoney, this.camoHittable, this.magicProofHittable, this.armourHittable, this.towerUpgradePaths);
    }

    /* Used to get the amount of stored money that the farm currently holds
     * Pre: Doesn't take in any parameters
     * Post: Returns the int which is the stored money
     */
    public int getStoredMoney() {
        return storedMoney;
    }

    /* Used to reset the amount of stored money after it gets collected
     * Pre: Doesn't take in any parameters
     * Post: Doesn't return anything
     */
    public void resetStoredMoney() {
        storedMoney = 0;
    }
}
