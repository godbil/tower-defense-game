package main.tower;

import main.DoubleCoord;
import main.Game;
import main.enemy.Enemy;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Projectile {
    // Current position for the projectile
    protected DoubleCoord position;
    // Velocity for helping the projectile move
    protected DoubleCoord velocity;
    // Speed of the projectile
    protected double speed;
    // The size of the projectile's hit box
    protected int size;
    // The amount of enemies that the projectile hits (Useful for splash and pierce projectiles)
    protected int hitCount;
    // A boolean for whether the projectile is active or not
    protected boolean active;

    protected BufferedImage sprite;

    public Projectile(double speed, int size, BufferedImage sprite) {
        this.speed = speed;
        this.size = size;
        this.sprite = sprite;
        this.hitCount = 0;
        this.active = true;
    }

    public Projectile(double speed, DoubleCoord position, int size, DoubleCoord target, BufferedImage sprite) {
        this.position = position.copy();
        this.velocity = calculateVelocity(speed, target);
        this.size = size;
        this.sprite = sprite;
        this.hitCount = 0;
        this.active = true;
    }

    /* Used to update the projectile's movement
     * Pre: Doesn't take in any parameters
     * Post: Doesn't return anything
     */
    public void update() {
        // Calls the move method every frame
        this.move();
        // Checks if the projectile goes off the map and then sets it to inactive so it can be removed from the arraylist
        if(position.x < 0 || position.x >= Game.WIDTH || position.y < 0 || position.y >= Game.HEIGHT){
            this.active = false;
        }
    }

    /* Used to move the projectile by adding the velocity
     * Pre: Doesn't take in any parameters
     * Post: Doesn't return anything
     */
    public void move() {
        position.x += velocity.x;
        position.y += velocity.y;
    }

    /* Used to paint the projectile sprite
     * Pre: Takes in a Graphics2D object that paints things
     * Post: Doesn't return anything
     */
    public void paint(Graphics2D g) {
        g.drawImage(this.sprite, (int)Math.round(this.position.x - this.sprite.getWidth() / 2.0), (int)Math.round(this.position.y - this.sprite.getHeight() / 2.0), null);
    }

    /* Used to get whether the projectile hits an enemy or not
     * Pre: Takes in an enemy object to check if it gets collided with
     * Post: Returns the boolean which is if it collides or not
     */
    public boolean collision(Enemy enemy) {
        // Determines the distance between the enemy's center and this sprite's size
        double distance = Math.pow(enemy.getCenter().x - this.position.x - this.sprite.getWidth() / 2.0, 2) + Math.pow(enemy.getCenter().y - this.position.y - this.sprite.getHeight() / 2.0, 2);
        // Checks if the distance is less than then enemy's radius and the projectile's size to the power of 2
        return distance < Math.pow(enemy.getRadius() + size, 2);
    }

    /* Used to set the projectile to inactive once it hits an enemy
     * Pre: Doesn't take in any parameters
     * Post: Doesn't return anything
     */
    public void hit(){
        this.hitCount++;
        this.active = false;
    }

    /* Used to update things after everything else (mainly so other projectiles can override this)
     * Pre: Doesn't take in any parameters
     * Post: Doesn't return anything
     */
    public void postUpdate() {

    }

    /* Used to get whether the projectile is active or not
     * Pre: Doesn't take in any parameters
     * Post: Returns the boolean which is isActive
     */
    public boolean isActive(){
        return this.active;
    }

    /* Used to set the isActive boolean of the projectile
     * Pre: Takes in a boolean which is the new isActive
     * Post: Doesn't return anything
     */
    public void setActive(boolean temp) {
        this.active = temp;
    }

    /* Used to make a copy of the current projectile
     * Pre: Takes in a double coordinate which is the position of the projectile and another double coordinate which is the target of the projectile
     * Post: Returns the new copy of the projectile
     */
    public Projectile copy(DoubleCoord position, DoubleCoord target){
        return new Projectile(this.speed, position, this.size, target, this.sprite);
    }

    /* Used to rotate the sprite of the projectiles so they look better
     * Pre: Takes in a double which is the amount of rotation that is wanted for the projectile
     * Post: Doesn't return anything
     */
    public void rotate(double rotation) {
        // Gets the dimensions of the sprite
        int width = this.sprite.getWidth();
        int height = this.sprite.getHeight();
        // Makes a new sprite with the same dimensions
        BufferedImage newSprite = new BufferedImage(this.sprite.getWidth(), this.sprite.getHeight(), this.sprite.getType());
        //Creates a new Graphics2D which can be used to draw into this image
        Graphics2D g = newSprite.createGraphics();

        // Calls the rotate method using the rotation and then set this sprite as the new sprite
        g.rotate(rotation, width / 2.0, height / 2.0);
        g.drawImage(this.sprite, null, 0, 0);
        this.sprite = newSprite;
    }

    /* Used to calculate the velocity of the projectile
     * Pre: Takes in a double which is the speed that the projectile travels at and a double coordinate which is the target at which the projectile will fly towards
     * Post: Returns a double coordinate of the vector
     */
    protected DoubleCoord calculateVelocity(double speed, DoubleCoord target) {
        // Finds the width and height of a triangle between the projectile's initial position and target's position
        double width = target.x - this.position.x;
        double height = target.y - this.position.y;
        // Calculates the distance between the target and the projectile position (basically finding the hypotenuse of the triangle above)
        double distance = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
        // Returns how much should be added to the position each time by taking the speed and width divided by the distance
        return new DoubleCoord(speed * width / distance, speed * height / distance);
    }

}
