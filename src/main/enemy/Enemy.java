package main.enemy;

import main.DoubleCoord;
import main.IntCoord;
import main.Map;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Enemy {
    // The four different types of direction choice that can be chosen when an enemy chooses the next tile (Can never go backwards)
    private static final Direction[][] DIRECTION_CHOICE = {
            {Direction.UP, Direction.RIGHT, Direction.LEFT},
            {Direction.RIGHT, Direction.UP, Direction.DOWN},
            {Direction.LEFT, Direction.UP, Direction.DOWN},
            {Direction.DOWN, Direction.RIGHT, Direction.LEFT}
    };
    // The height and offset for the enemies health bar graphics
    private static final int HEALTH_HEIGHT = 10;
    private static final int HEALTH_OFFSET = 20;
    // A final variable which is the square root of 2 that is used to calculate the radius of the enemies circular hit box
    private static final double SQRT_TWO = Math.sqrt(2);

    // Protected variables which can be used for enemy subclasses
    protected int health;
    protected int maxHealth;
    protected int size;
    protected boolean active;
    protected int moneyGive;

    protected double movementSpeed;
    protected DoubleCoord position;
    protected IntCoord target;
    protected Direction direction;

    protected BufferedImage sprite;

    // Booleans of whether the enemy has any of these properties
    protected boolean camo;
    protected boolean magicProof;
    protected boolean armoured;

    // Boolean for graphics to flip when walking in another direction
    protected boolean isFlipped;

    // How slow the enemy is (1 is original speed) and the timer for how long it's slowed for
    protected double slowPercent;
    protected int slowTimer;
    // The amount of frames that an enemy is frozen for and the boolean for whether an enemy is frozen
    protected int freezeTime;
    protected boolean isFrozen;

    public Enemy(int maxHealth, double movementSpeed, int size, BufferedImage image, boolean camo, boolean magicProof, boolean armoured, int moneyGive) {
        // Initializing all of the instance variables
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.movementSpeed = movementSpeed;
        this.size = size;
        this.moneyGive = moneyGive;

        this.active = true;

        this.sprite = image;

        this.camo = camo;
        this.magicProof = magicProof;
        this.armoured = armoured;
        this.isFlipped = false;

        this.slowPercent = 1;
        this.slowTimer = 192;
        this.freezeTime = 0;
        this.isFrozen = false;
    }

    public Enemy(int maxHealth, double movementSpeed, DoubleCoord position, Direction direction, int size, BufferedImage image, boolean camo, boolean magicProof, boolean armoured, int moneyGive) {
        // Initializing all of the instance variables
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.movementSpeed = movementSpeed;
        this.position = position.copy();
        this.direction = direction;
        this.size = size;
        this.moneyGive = moneyGive;

        this.active = true;

        this.sprite = image;

        this.camo = camo;
        this.magicProof = magicProof;
        this.armoured = armoured;
        this.isFlipped = false;

        this.slowPercent = 1;
        this.slowTimer = 192;
        this.freezeTime = 0;
        this.isFrozen = false;
    }

    /* Used to subtract health from the enemy
     * Pre: Takes in an int which is the damage that the enemy takes
     * Post: Doesn't return anything
     */
    public void takeDamage(int damage){
        // Subtracts damage from health
        this.health -= damage;
        if(this.health <= 0){
            // Makes the enemy's active into false if they die
            this.active = false;
        }
    }

    /* Used to set the slow percent of the enemy
     * Pre: Takes in a double which is the new slow percent
     * Post: Doesn't return anything
     */
    public void setSlowPercent(double slowPercent){
        this.slowPercent = slowPercent;
    }

    /* Used to set the amount of time that an enemy is frozen for
     * Pre: Takes in an int which is the new freeze time
     * Post: Doesn't return anything
     */
    public void setFreezeTime(int freezeTime) {
        this.freezeTime = freezeTime;
        // Sets the enemy to frozen
        this.isFrozen = true;
    }

    /* Used to move the enemy
     * Pre: Takes in a 2D array which is the map
     * Post: Doesn't return anything
     */
    public void move(int[][] map) {
        // First checks if the enemy is not frozen to run the movement portion
        if(!isFrozen) {
            // Calls the set target method to set the target position of the enemy
            this.setTarget(map);
            // Multiplies by the tile size to get the coordinate in terms of pixels
            this.target.x *= Map.TILE_SIZE;
            this.target.y *= Map.TILE_SIZE;

            // For the x coordinate
            if (this.position.x > this.target.x) {
                // If the enemies current position is greater than the target then it will set the enemy's position to the max of the current position minus movement speed (times slow percent) or the target, basically making sure the enemy position never goes past the target
                this.position.x = Math.max(this.position.x - this.movementSpeed * this.slowPercent, this.target.x);
            }
            else if (this.position.x < this.target.x) {
                // If the enemies current position is less than the target then it will set the enemy's position to the min of the current position minus movement speed (times slow percent) or the target, basically making sure the enemy position never goes past the target
                this.position.x = Math.min(this.position.x + this.movementSpeed * this.slowPercent, this.target.x);
            }

            // For the y coordinate, does the same thing as the x coordinate
            if (this.position.y > this.target.y) {
                this.position.y = Math.max(this.position.y - this.movementSpeed * this.slowPercent, this.target.y);
            }
            else if (this.position.y < this.target.y) {
                this.position.y = Math.min(this.position.y + this.movementSpeed * this.slowPercent, this.target.y);
            }

            // Checks if the direction is right or left and sets isFlipped accordingly which will be used in the paint method
            if (this.direction == Direction.RIGHT) {
                isFlipped = false;
            } else if (this.direction == Direction.LEFT) {
                isFlipped = true;
            }
        }
        // If the enemy is frozen
        else {
            // Counts down the freeze timer
            freezeTime--;
            if(freezeTime <= 0) {
                // If the freeze timer finally reaches 0 then the enemy is no longer frozen
                isFrozen = false;
            }
        }
        // If the slow percent isn't 1, it means it isn't the original percent so the slow timer gets counted down
        if(slowPercent != 1) {
            slowTimer--;
            if(slowTimer <= 0) {
                // If the slow timer reaches 0 then the slow percent is reset and the timer is also reset
                slowPercent = 1;
                slowTimer = 192;
            }
        }
    }

    /* Used to paint all of the things that enemies need
     * Pre: Takes in a Graphics2D object to paint things
     * Post: Doesn't return anything
     */
    public void paint(Graphics2D g){
        // Checks whether this enemy is active
        if(this.active) {
            // Sets offsets for the sprite of the enemy
            double heightOffset = Map.TILE_SIZE / 2.0 - this.sprite.getHeight() / 2.0;
            double widthOffset = Map.TILE_SIZE / 2.0 - this.sprite.getWidth() / 2.0;

            // These images are for if the enemy is flipped
            if(isFlipped){
                if(slowPercent != 1) {
                    // If the enemy is slowed then they will have a dark blue tint using the dye method
                    g.drawImage(dye(sprite, new Color(0f, 0f, 0.6f, 0.4f)), (int) Math.round(position.x + widthOffset) + sprite.getWidth(), (int) Math.round(position.y + heightOffset), -sprite.getWidth(), sprite.getHeight(), null);
                }
                else if(isFrozen) {
                    // If the enemy is frozen then they will have a light blue tint using the dye method
                    g.drawImage(dye(sprite, new Color(0f, 1f, 1f, 0.4f)), (int) Math.round(position.x + widthOffset) + sprite.getWidth(), (int) Math.round(position.y + heightOffset), -sprite.getWidth(), sprite.getHeight(), null);
                }
                else {
                    // If there is no changes to the enemy then it will be painted as is
                    g.drawImage(sprite, (int) Math.round(position.x + widthOffset) + sprite.getWidth(), (int) Math.round(position.y + heightOffset), -sprite.getWidth(), sprite.getHeight(), null);
                }
            }
            // These images are for if the enemy isn't flipped
            else {
                if(slowPercent != 1) {
                    // If the enemy is slowed then they will have a dark blue tint using the dye method
                    g.drawImage(dye(sprite, new Color(0f, 0f, 0.6f, 0.4f)), (int) Math.round(position.x + widthOffset), (int) Math.round(position.y + heightOffset), null);
                }
                else if(isFrozen) {
                    // If the enemy is frozen then they will have a light blue tint using the dye method
                    g.drawImage(dye(sprite, new Color(0f, 1f, 1f, 0.4f)), (int) Math.round(position.x + widthOffset), (int) Math.round(position.y + heightOffset), null);
                }
                else {
                    // If there is no changes to the enemy then it will be painted as is
                    g.drawImage(this.sprite, (int) Math.round(position.x + widthOffset), (int) Math.round(position.y + heightOffset), null);
                }
            }

            // Drawing the red portion of the health bar which is painted underneath the green health bar
            Rectangle2D rect2 = new Rectangle((int) Math.round(position.x), (int) Math.round(position.y - HEALTH_OFFSET + heightOffset), Map.TILE_SIZE, HEALTH_HEIGHT);
            g.setPaint(Color.red);
            g.draw(rect2);
            g.fill(rect2);

            // Drawing the green portion of the health bar which is painted and then shrinks as the enemy's health gets lowered
            Rectangle2D rect3 = new Rectangle((int) Math.round(position.x), (int) Math.round(position.y - HEALTH_OFFSET + heightOffset), Map.TILE_SIZE * this.health / this.maxHealth, HEALTH_HEIGHT);
            g.setPaint(Color.green);
            g.draw(rect3);
            g.fill(rect3);
        }
    }

    /* Used to set the target tile for the enemy
     * Pre: Takes in a 2D array which is the map
     * Post: Doesn't return anything
     */
    private void setTarget(int[][] map) {
        // Makes an int coordinate for the candidate of the direction choice
        IntCoord candidate;
        // Goes through each direction choice depending on the enemies current direction
        for(Direction dir : DIRECTION_CHOICE[this.direction.index]) {
            candidate = this.tileLocation();

            // Goes through each case in the direction choice and sees which direction has a path
                if(dir == Direction.UP) {
                    candidate.y--;
                }
                else if(dir == Direction.DOWN) {
                    candidate.y++;
                }
                 else if(dir == Direction.LEFT) {
                    candidate.x--;
                }
                else if(dir == Direction.RIGHT) {
                    candidate.x++;
                }
            // Makes a double coordinate which is corresponding to the top left of tile location of the enemy
            DoubleCoord tileCoord = new DoubleCoord(tileLocation().x * Map.TILE_SIZE, tileLocation().y * Map.TILE_SIZE);

            // Checks if the enemy is trying to change direction
            if(direction != dir){
                // Checks if the enemy's current position is equal to the enemy's current tile's center and if not, it will keep the target as the middle of the current tile and break until it moves into the center
                if(!position.equals(tileCoord)){
                    target = tileLocation();
                    break;
                }
            }

            // Checks if the candidate for the target tile is within the boundaries and if it is a path or starting tile on the map
            if(candidate.x >= 0 && candidate.x < Map.MAP_WIDTH && candidate.y >= 0 && candidate.y < Map.MAP_HEIGHT){
                if(map[candidate.x][candidate.y] == Map.PATH || map[candidate.x][candidate.y] == Map.START){
                    // Sets the target to candidate and direction to dir and then breaks out of the for loop
                    target = candidate;
                    direction = dir;
                    break;
                }
            }
            // Checks if the position is already equal to
            else if(position.equals(tileCoord)){
                this.active = false;
            }
        }
    }

    /* Used to make a copy of the current enemy
     * Pre: Takes in a double coordinate which is the enemies position, a direction enum and a double which is the movement multiplier of the enemy
     * Post: Returns the copied enemy
     */
    public Enemy copy(DoubleCoord position, Direction direction, double movementMultiplier){
        return new Enemy(this.maxHealth, this.movementSpeed * movementMultiplier, position, direction, this.size, this.sprite, this.camo, this.magicProof, this.armoured, this.moneyGive);
    }

    /* Used to get the health of the enemy
     * Pre: Doesn't take in any parameters
     * Post: Returns the health of the enemy
     */
    public int getHealth() {
        return health;
    }

    /* Used to check whether the enemy is camo
     * Pre: Doesn't take in any parameters
     * Post: Returns a boolean which is whether it is camo or not
     */
    public boolean isCamo() {
        return this.camo;
    }

    /* Used to check whether the enemy is magic proof
     * Pre: Doesn't take in any parameters
     * Post: Returns a boolean which is whether it is magic proof or not
     */
    public boolean isMagicProof() {
        return this.magicProof;
    }

    /* Used to check whether the enemy is armoured
     * Pre: Doesn't take in any parameters
     * Post: Returns a boolean which is whether it is armoured or not
     */
    public boolean isArmoured() {
        return this.armoured;
    }

    /* Used to get the center coordinate of the enemy which is used for collision
     * Pre: Doesn't take in any parameters
     * Post: Returns a double coordinate which is the center of the enemy
     */
    public DoubleCoord getCenter() {
        return new DoubleCoord(position.x + Map.TILE_SIZE / 2.0, position.y + Map.TILE_SIZE / 2.0);
    }

    /* Used to get the radius of the enemy which is used for collision
     * Pre: Doesn't take in any parameters
     * Post: Returns a double which is the radius of the enemy
     */
    public double getRadius() {
        return this.size / SQRT_TWO;
    }

    /* Used to get how much money the enemy gives
     * Pre: Doesn't take in any parameters
     * Post: Returns an int which is the amount of money given
     */
    public int getMoneyGive() {
        return this.moneyGive;
    }

    /* Used to check whether the enemy is active
     * Pre: Doesn't take in any parameters
     * Post: Returns a boolean which is whether it is active or not
     */
    public boolean isActive() {
        return active;
    }

    /* Used to check the enemy's tile location by converting the position into an int coordinate
     * Pre: Doesn't take in any parameters
     * Post: Returns an int coordinate which is the tile location
     */
    private IntCoord tileLocation() {
        return new IntCoord(position.convertToInt().x / Map.TILE_SIZE, position.convertToInt().y / Map.TILE_SIZE);
    }

    /* Used to dye an image to a specific colour
     * Pre: Takes in a buffered image which is the sprite and a color object which is the colour that the sprite will be dyed as
     * Post: Returns the new dyed buffered image of the sprite
     */
    private BufferedImage dye(BufferedImage image, Color color) {
        // Gets the image's dimensions
        int w = image.getWidth();
        int h = image.getHeight();
        // Creates a new buffered image that will be returned
        BufferedImage dyed = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        //Creates a new Graphics2D which can be used to draw into this image
        Graphics2D g = dyed.createGraphics();
        // Draws the original image into the dyed image
        g.drawImage(image, 0, 0, null);
        // Sets the composite of the image to implement the opaque SRC_ATOP rule with an alpha of 1.0f
        g.setComposite(AlphaComposite.SrcAtop);
        // Sets the color to the passed in colour
        g.setColor(color);
        // Fill the graphic and then return the final dyed image
        g.fillRect(0, 0, w, h);
        g.dispose();
        return dyed;
    }
}
