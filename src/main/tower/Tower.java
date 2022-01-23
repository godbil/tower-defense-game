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
    // Stats for the towers including the damage, range, cost and total cost
    protected int damage;
    protected int range;
    protected int cost;
    protected int totalCost;

    // Fire rate of the tower and the timer for counting down the fire rate
    protected int fireRate;
    protected int timer;

    // The coordinates of the tower in terms of the map grid
    protected IntCoord tileLocation;

    // An array list that stores all of the projectiles and the type of projectile that this tower shoots
    protected final ArrayList<Projectile> projectiles;
    protected Projectile projectileType;

    // The sprite of the tower and whether the tower is flipped or not
    protected BufferedImage sprite;
    protected boolean isFlipped;

    // Three booleans for whether this tower can hit these type of enemies
    protected boolean camoHittable;
    protected boolean magicProofHittable;
    protected boolean armourHittable;

    // An arraylist with the name of the tower's upgrade(s)
    protected final ArrayList<String> towerUpgradePaths;

    public Tower(int damage, int range, int cost, int totalCost, int fireRate, BufferedImage image, Projectile projectileType, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, ArrayList<String> towerUpgradePaths) {
        this.damage = damage;
        this.range = range;
        this.cost = cost;
        this.totalCost = totalCost;
        this.fireRate = fireRate;
        this.timer = 0;

        this.projectiles = new ArrayList<>();
        this.projectileType = projectileType;

        this.sprite = image;
        this.isFlipped = false;

        this.camoHittable = camoHittable;
        this.magicProofHittable = magicProofHittable;
        this.armourHittable = armourHittable;

        this.towerUpgradePaths = towerUpgradePaths;
    }

    public Tower(int damage, int range, int cost, int totalCost, int fireRate, IntCoord tileLocation, BufferedImage image, Projectile projectileType, boolean camoHittable, boolean magicProofHittable, boolean armourHittable, ArrayList<String> towerUpgradePaths) {
        this.damage = damage;
        this.range = range;
        this.cost = cost;
        this.totalCost = totalCost;
        this.fireRate = fireRate;
        this.timer = 0;
        this.tileLocation = tileLocation;

        this.projectiles = new ArrayList<>();
        this.projectileType = projectileType;

        this.sprite = image;
        this.isFlipped = false;

        this.camoHittable = camoHittable;
        this.magicProofHittable = magicProofHittable;
        this.armourHittable = armourHittable;

        this.towerUpgradePaths = towerUpgradePaths;
    }

    /* Used to update all of the tower's variables and calls their other methods which needs to be checked every frame
     * Pre: Takes in an arraylist which is the enemies
     * Post: Doesn't return anything
     */
    public void update(ArrayList<Enemy> enemies) {
        // Removes the projectile from the array list if the projectile is no longer active
        projectiles.removeIf(projectile -> !projectile.isActive());
        // Runs the fire method
        fire(enemies);
        for (Projectile projectile : projectiles) {
            // Calls the projectile's update method
            projectile.update();
            for(Enemy enemy : enemies) {
                // Checks if the projectile is still active and if it has collided with an enemy
                if (projectile.isActive() && projectile.collision(enemy)) {
                    // Checks if this tower can hit the enemy and if it can't then the projectile is set to inactive to be removed in the next update
                    if((enemy.isArmoured() && !this.armourHittable) || (enemy.isMagicProof() && !this.magicProofHittable) || (enemy.isCamo() && !this.camoHittable)) {
                        projectile.setActive(false);
                        break;
                    }
                    else {
                        // If the tower can hit the enemy then the enemy's take damage method will be called and then the projectile's hit command will be called
                        enemy.takeDamage(this.damage);
                        projectile.hit();
                    }
                }
            }
            // Calls the projectile's post update method for updates that need to occur after all of the other updates
            projectile.postUpdate();
        }
    }

    /* Used to check whether the tower can fire yet and call the collision and fire projectile methods
     * Pre: Takes in an arraylist which is the enemies
     * Post: Doesn't return anything
     */
    public void fire(ArrayList<Enemy> enemies) {
        // Check if the tower can shoot yet (fire rate)
        if(timer <= 0) {
            for (Enemy enemy : enemies) {
                // Goes through every single enemy and see if any of them are inside this tower's range
                if (this.collision(enemy)) {
                    // Calls the fire projectile method which will return true if it created a projectile causing this to break from the for loop
                    if(this.fireProjectile(enemy)) {
                        break;
                    }
                }
            }
            // Resets the timer to equal to fire rate of this tower
            timer = this.fireRate;
        }
        else{
            // If the tower cannot shoot yet then it will count down the timer
            timer--;
        }
    }

    /* Used to check whether there is an enemy inside the tower's range
     * Pre: Takes in an object which is a specific enemy
     * Post: Returns a boolean which is true if the tower does see this enemy in the range or false if it doesn't
     */
    public boolean collision(Enemy enemy) {
        // First checks if the tower can see camo enemies and if not then there is no collision
        if(enemy.isCamo() && !this.camoHittable) {
            return false;
        }
        else {
            // Calculates the distance between the enemy's center and this tower's center
            double distance = Math.pow(enemy.getCenter().x - this.getCenter().x, 2) + Math.pow(enemy.getCenter().y - this.getCenter().y, 2);
            // Checks if the distance is less than the enemy's radius and the radius of the tower's range circle to the power of two
            return distance < Math.pow(enemy.getRadius() + range, 2);
        }
    }

    /* Used to paint things, calling the more complicated paint method
     * Pre: Takes in a Graphics2D object which paints things
     * Post: Doesn't return anything
     */
    public void paint(Graphics2D g) {
        paint(g, false, false);
    }

    /* Used to paint the tower, projectiles and it's range when necessary
     * Pre: Takes in a Graphics2D object which paints things, a boolean for if the tower placement is valid and a boolean for if the range should be shown
     * Post: Doesn't return anything
     */
    public void paint(Graphics2D g, boolean isValid, boolean isRange) {
        // Offset used to paint the range circle to be centered with the tower
        double offset = Map.TILE_SIZE / 2.0 - this.range;
        // Goes through all of the projectiles and calls all of their paint methods
        for (Projectile projectile : projectiles) {
            projectile.paint(g);
        }
        if(isFlipped){
            // Draws the sprite of the tower flipped if it is shooting to the right
            g.drawImage(sprite, this.tileLocation.x * Map.TILE_SIZE + sprite.getWidth(), this.tileLocation.y * Map.TILE_SIZE, -sprite.getWidth(), sprite.getHeight(), null);
        }
        else {
            // Draws the original sprite of the tower if it is shooting to the left
            g.drawImage(sprite, this.tileLocation.x * Map.TILE_SIZE, this.tileLocation.y * Map.TILE_SIZE, null);
        }

        if(isRange) {
            // If the range should be shown then a circle is drawn using the tower's tile location and the offset to center the range
            Ellipse2D circle = new Ellipse2D.Double(this.tileLocation.x * Map.TILE_SIZE + offset, this.tileLocation.y * Map.TILE_SIZE + offset, this.range * 2, this.range * 2);
            Color rangeColor;
            if(isValid) {
                // Makes the range colour into a transparent gray if the tower placement is valid
                rangeColor = new Color(0.6f, 0.6f, 0.6f, .4f);
            }
            else{
                // Makes the range colour into a transparent red if the tower placement isn't valid
                rangeColor = new Color(1f, 0.0f, 0.0f, .4f);
            }
            // Draws and fills the range with the chosen range colour
            g.setPaint(rangeColor);
            g.draw(circle);
            g.fill(circle);
        }
    }

    /* Used to set the tile location of the tower
     * Pre: Takes in an int coordinate which is the new tile location
     * Post: Doesn't return anything
     */
    public void setTileLocation(IntCoord tileLocation) {
        this.tileLocation = tileLocation;
    }

    /* Used to get the cost of this tower
     * Pre: Doesn't take in any parameters
     * Post: Returns an int which is the cost of the tower
     */
    public int getCost() {
        return this.cost;
    }

    /* Used to get the total cost of this tower for sell back money
     * Pre: Doesn't take in any parameters
     * Post: Returns an int which is the total cost of the tower
     */
    public int getTotalCost() {
        return this.totalCost;
    }

    /* Used to get the tile location of this tower
     * Pre: Doesn't take in any parameters
     * Post: Returns an int coordinate which is the tile location
     */
    public IntCoord getTileLocation() {
        return this.tileLocation;
    }

    /* Used to make a copy of the current tower
     * Pre: Takes in an int coordinate which is the tile location and a double which is the money multiplier to increase or decrease cost
     * Post: Returns the new copy of the tower
     */
    public Tower copy(IntCoord tileLocation, double moneyMultiplier){
        return new Tower(this.damage, this.range, (int) Math.round(this.cost * moneyMultiplier), (int) Math.round(this.totalCost * moneyMultiplier), this.fireRate, tileLocation, this.sprite, this.projectileType, this.camoHittable, this.magicProofHittable, this.armourHittable, this.towerUpgradePaths);
    }

    /* Used to get the sprite of the tower
     * Pre: Doesn't take in any parameters
     * Post: Returns a buffered image which is the sprite
     */
    public BufferedImage getSprite() {
        return this.sprite;
    }

    /* Used to create a projectile and also checks whether the tower should be flipped, depending on the enemy's position
     * Pre: Takes in the enemy that the tower will fire the projectile at
     * Post: Returns a boolean which tells the fire method to break after creating one projectile
     */
    protected boolean fireProjectile(Enemy enemy) {
        this.isFlipped = this.getCenter().x < enemy.getCenter().x;
        Projectile projectile = this.projectileType.copy(this.getCenter(), enemy.getCenter());
        projectiles.add(projectile);
        return true;
    }

    /* Used to get the upgrade paths of this specific tower
     * Pre: Doesn't take in any parameters
     * Post: Returns a string array list which is the tower's upgrade paths
     */
    public ArrayList<String> getUpgradePath(){
        return this.towerUpgradePaths;
    }

    /* Used to get the center of this tower
     * Pre: Doesn't take in any parameters
     * Post: Returns a double coordinate which is the center of the tower's position
     */
    protected DoubleCoord getCenter(){
        return new DoubleCoord(this.tileLocation.x * Map.TILE_SIZE + Map.TILE_SIZE / 2.0, this.tileLocation.y * Map.TILE_SIZE + Map.TILE_SIZE / 2.0);
    }

}
