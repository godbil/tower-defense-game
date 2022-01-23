package main.tower;

import main.DoubleCoord;
import main.enemy.Enemy;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MortarProjectile extends Projectile{
    // The stage of which the projectile is in
    private int stage;
    // The target for each stage of the mortar
    private DoubleCoord[] stageTarget;
    // Sprite of the explosion
    private BufferedImage explosion;
    // Timer for the explosion
    private int timer;
    // Boolean for whether the sprite is flipped or not
    private boolean isFlipped;

    public MortarProjectile(double speed, int size, BufferedImage sprite, BufferedImage explosion, int timer) {
        super(speed, size, sprite);
        this.explosion = explosion;
        this.stage = 0;
        this.timer = timer;
        isFlipped = false;
    }

    public MortarProjectile(double speed, DoubleCoord position, int size, DoubleCoord target, BufferedImage sprite, BufferedImage explosion, int timer) {
        super(speed, size, sprite);
        this.explosion = explosion;
        this.timer = timer;
        isFlipped = false;
        this.position = position.copy();
        this.stageTarget = new DoubleCoord[]{new DoubleCoord(this.position.x, this.position.y - 1000), new DoubleCoord(target.x, this.position.y - 1000), target};
        this.stage = 0;
        this.velocity = calculateVelocity(this.speed, this.stageTarget[this.stage]);
    }

    /* Used to update the projectile's movement
     * Pre: Doesn't take in any parameters
     * Post: Doesn't return anything
     */
    @Override
    public void update() {
        // If the stage is 0 then it is going up so it checks if the projectile position is less than 0 before moving onto the next stage
        if(this.stage == 0 && this.position.y < 0) {
            this.velocity = calculateVelocity(5 * this.speed, this.stageTarget[1]);
            this.stage++;
        }
        // If the stage is 1 then it is going to the side so it checks whether the projectile position is less than or greater than the target before going onto the next stage
        else if(this.stage == 1 && (this.stageTarget[0].x >= this.stageTarget[1].x && this.position.x <= this.stageTarget[1].x || this.stageTarget[0].x <= this.stageTarget[1].x && this.position.x >= this.stageTarget[1].x)) {
            this.velocity = calculateVelocity(this.speed, this.stageTarget[2]);
            isFlipped = true;
            this.stage++;
        }
        // If the stage is 2 then it is going back down so it checks if the projectile position is greater or equal to the target
        else if(this.stage == 2 && this.position.y >= this.stageTarget[2].y) {
            this.velocity.x = 0;
            this.velocity.y = 0;
            this.stage++;
        }
        // If the stage is 3 then it landed and the explosion will appear
        else if(this.stage == 3 && timer <= 0) {
            this.active = false;
        }
        // Finally it will count down the timer
        else if (this.stage == 3){
            timer--;
        }
        this.move();
    }

    /* Used to paint the projectile sprite
     * Pre: Takes in a Graphics2D object that paints things
     * Post: Doesn't return anything
     */
    @Override
    public void paint(Graphics2D g) {
        // If the projectile is in stage 3 (landed) then it will paint the explosion sprite
        if(stage == 3) {
            g.drawImage(this.explosion, (int) this.stageTarget[2].x - this.explosion.getWidth() / 2, (int) this.stageTarget[2].y  - this.explosion.getHeight() / 2, null);
        }
        else if(isFlipped){
            // Paints the sprite flipped if it is coming down
            g.drawImage(this.sprite, (int)Math.round(this.position.x - this.sprite.getWidth() / 2.0), (int)Math.round(this.position.y - this.sprite.getHeight() / 2.0), sprite.getWidth(), -sprite.getHeight(), null);
        }
        else {
            // Normal sprite paint
            g.drawImage(this.sprite, (int)Math.round(this.position.x - this.sprite.getWidth() / 2.0), (int)Math.round(this.position.y - this.sprite.getHeight() / 2.0), null);
        }
    }

    /* Used to check whether the projectile collides with an enemy
     * Pre: Takes in an enemy object
     * Post: Returns a boolean for whether the projectile collides or not
     */
    @Override
    public boolean collision(Enemy enemy) {
        // Only checks collision with enemy if the mortar projectile is in the 3rd stage which is when it lands
        if(this.stage == 3) {
            return super.collision(enemy);
        }
        // If it's not in the 3rd stage then it will always be not colliding with enemies
        else {
            return false;
        }
    }

    /* Used to increase the hit count of the projectile
     * Pre: Doesn't take in any parameters
     * Post: Doesn't return anything
     */
    @Override
    public void hit(){
        // Sets the projectile to inactive later so it can hit multiple enemies before becoming inactive
        this.hitCount++;
    }

    /* Used to update things after everything else
     * Pre: Doesn't take in any parameters
     * Post: Doesn't return anything
     */
    @Override
    public void postUpdate() {
        // Checks if the projectile hits enemies before setting it to false so that it will splash
        if(this.hitCount > 0) {
            this.active = false;
        }
    }

    /* Used to make a copy of the current mortar projectile
     * Pre: Takes in a double coordinate which is the position of the projectile and another double coordinate which is the target of the projectile
     * Post: Returns the new copy of the mortar projectile
     */
    @Override
    public MortarProjectile copy(DoubleCoord position, DoubleCoord target){
        return new MortarProjectile(this.speed, position, this.size, target, this.sprite, this.explosion, this.timer);
    }
}
