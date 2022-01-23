package main.tower;

import main.IntCoord;
import main.Map;
import main.enemy.Enemy;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EarthStompTower extends Tower{
    // Timer for how often the earth stomp will occur
    private int earthStompTimer;
    private int tempTimer;
    // Alpha which determines how transparent the earth stomp will be
    private float alpha;
    // Boolean for when the tower is doing its earth stomp
    private boolean isEarthStomping;

    public EarthStompTower(int damage, int range, int cost, int totalCost, int fireRate, BufferedImage image, Projectile projectileType, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, int earthStompTimer, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, totalCost, fireRate, image, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.earthStompTimer = earthStompTimer;
        this.tempTimer = earthStompTimer;
        this.alpha = 1f;
        this.isEarthStomping = false;
    }

    public EarthStompTower(int damage, int range, int cost, int totalCost, int fireRate, IntCoord tileLocation, BufferedImage image, Projectile projectileType, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, int earthStompTimer, ArrayList<String> towerUpgradePaths) {
        super(damage, range, cost, totalCost, fireRate, tileLocation, image, projectileType, camoHittable, magicProofHittable, armourHittable, towerUpgradePaths);
        this.earthStompTimer = earthStompTimer;
        this.tempTimer = earthStompTimer;
        this.alpha = 1f;
        this.isEarthStomping = false;
    }

    /* Used to override the update method to check the earth stomp damage each frame
     * Pre: Takes in an arraylist of all the enemies
     * Post: Doesn't return anything
     */
    @Override
    public void update(ArrayList<Enemy> enemies) {
        // Calls the super update method
        super.update(enemies);
        // Calls the earth stomp damage each frame to check when to do the earth stomp
        earthStompDamage(enemies);

        // Checks if the tower is doing the earth stomp and if so, subtract from the alpha variable each time
        if(isEarthStomping) {
            alpha -= 0.1f;
            if (alpha <= 0f) {
                // Checks if the alpha has completely become transparent to set the boolean to false and reset the alpha
                this.isEarthStomping = false;
                alpha = 1f;
            }
        }
    }

    /* Used to paint the tower, projectiles and it's range when necessary
     * Pre: Takes in a Graphics2D object which paints things
     * Post: Doesn't return anything
     */
    @Override
    public void paint(Graphics2D g) {
        // Creates the offset so the earth stomp circle will be centered with the tower's center
        double offset = Map.TILE_SIZE / 2.0 - this.range;
        if(isEarthStomping) {
            // Draws the earth stomp circle which has a brown colour that fades out using the alpha variable
            Ellipse2D earthStomp = new Ellipse2D.Double(this.tileLocation.x * Map.TILE_SIZE + offset, this.tileLocation.y * Map.TILE_SIZE + offset, this.range * 2, this.range * 2);
            g.setPaint(new Color(0.3f, 0.2f, 0.2f, alpha));
            g.draw(earthStomp);
            g.fill(earthStomp);
        }
        paint(g, false, false);
    }

    /* Used to make a copy of the current earth stomp tower
     * Pre: Takes in an int coordinate which is the tile location and a double which is the money multiplier to increase or decrease cost
     * Post: Returns the new copy of the earth stomp tower
     */
    @Override
    public EarthStompTower copy(IntCoord tileLocation, double moneyMultiplier){
        return new EarthStompTower(this.damage, this.range, (int) Math.round(this.cost * moneyMultiplier), (int) Math.round(this.totalCost * moneyMultiplier), this.fireRate, tileLocation, this.sprite, this.projectileType, this.camoHittable, this.magicProofHittable, this.armourHittable, this.earthStompTimer, this.towerUpgradePaths);
    }

    /* Used to check if there are any enemies in range and then do the earth stomp
     * Pre: Takes in an arraylist of all the enemies
     * Post: Doesn't return anything
     */
    private void earthStompDamage(ArrayList<Enemy> enemies) {
        if(tempTimer <= 0) {
            // Checks if the timer for earth stomp is at 0 and if it is then it will see if there are any enemies in the range
            for(Enemy enemy : enemies) {
                if(this.collision(enemy)) {
                    // All enemies in the range will take damage and sets the boolean to true to set off the visual
                    enemy.takeDamage(this.damage);
                    isEarthStomping = true;
                }
            }
            // Resets the timer to the original earth stomp timer
            this.tempTimer = earthStompTimer;
        }
        else {
            // If the timer hasn't reached 0 yet then it will count down
            tempTimer--;
        }
    }
}
