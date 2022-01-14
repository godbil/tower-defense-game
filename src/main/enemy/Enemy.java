package main.enemy;

import main.DoubleCoord;
import main.IntCoord;
import main.Map;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Enemy {
    private static final Direction[][] DIRECTION_CHOICE = {
            {Direction.UP, Direction.RIGHT, Direction.LEFT},
            {Direction.RIGHT, Direction.UP, Direction.DOWN},
            {Direction.LEFT, Direction.UP, Direction.DOWN},
            {Direction.DOWN, Direction.RIGHT, Direction.LEFT}
    };
    private static final int HEALTH_HEIGHT = 10;
    private static final int HEALTH_OFFSET = 20;
    private static final double SQRT_TWO = Math.sqrt(2);

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

    protected boolean camo;
    protected boolean magicProof;
    protected boolean armoured;
    protected boolean isFlipped;

    protected double slowPercent;
    protected int slowTimer;
    protected int freezeTime;
    protected boolean isFrozen;

    public Enemy(int maxHealth, double movementSpeed, int size, BufferedImage image, boolean camo, boolean magicProof, boolean armoured, int moneyGive) {
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

    public void takeDamage(int damage){
        this.health -= damage;
        if(this.health <= 0){
            this.active = false;
        }
    }

    public void setSlowPercent(double slowPercent){
        this.slowPercent = slowPercent;
    }

    public void setFreezeTime(int freezeTime) {
        this.freezeTime = freezeTime;
        this.isFrozen = true;
    }

    public void move(int[][] map) {
        if(!isFrozen) {
            this.setTarget(map);
            this.target.x *= Map.TILE_SIZE;
            this.target.y *= Map.TILE_SIZE;

            if (this.position.x > this.target.x) {
                this.position.x = Math.max(this.position.x - this.movementSpeed * this.slowPercent, this.target.x);
            } else if (this.position.x < this.target.x) {
                this.position.x = Math.min(this.position.x + this.movementSpeed * this.slowPercent, this.target.x);
            }

            if (this.position.y > this.target.y) {
                this.position.y = Math.max(this.position.y - this.movementSpeed * this.slowPercent, this.target.y);
            } else if (this.position.y < this.target.y) {
                this.position.y = Math.min(this.position.y + this.movementSpeed * this.slowPercent, this.target.y);
            }
            if (this.direction == Direction.RIGHT) {
                isFlipped = false;
            } else if (this.direction == Direction.LEFT) {
                isFlipped = true;
            }
        }
        else {
            freezeTime--;
            if(freezeTime <= 0) {
                isFrozen = false;
            }
        }
        if(slowPercent != 1) {
            slowTimer--;
            if(slowTimer <= 0) {
                slowPercent = 1;
                slowTimer = 192;
            }
        }
    }

    public void paint(Graphics2D g){
        if(this.active) {
            double heightOffset = Map.TILE_SIZE / 2.0 - this.sprite.getHeight() / 2.0;
            double widthOffset = Map.TILE_SIZE / 2.0 - this.sprite.getWidth() / 2.0;

            if(isFlipped){
                if(slowPercent != 1) {
                    g.drawImage(dye(sprite, new Color(0f, 0f, 0.6f, 0.4f)), (int) Math.round(position.x + widthOffset) + sprite.getWidth(), (int) Math.round(position.y + heightOffset), -sprite.getWidth(), sprite.getHeight(), null);
                }
                else if(isFrozen) {
                    g.drawImage(dye(sprite, new Color(0f, 1f, 1f, 0.4f)), (int) Math.round(position.x + widthOffset) + sprite.getWidth(), (int) Math.round(position.y + heightOffset), -sprite.getWidth(), sprite.getHeight(), null);
                }
                else {
                    g.drawImage(sprite, (int) Math.round(position.x + widthOffset) + sprite.getWidth(), (int) Math.round(position.y + heightOffset), -sprite.getWidth(), sprite.getHeight(), null);
                }
            }
            else {
                if(slowPercent != 1) {
                    g.drawImage(dye(sprite, new Color(0f, 0f, 0.6f, 0.4f)), (int) Math.round(position.x + widthOffset), (int) Math.round(position.y + heightOffset), null);
                }
                else if(isFrozen) {

                    g.drawImage(dye(sprite, new Color(0f, 1f, 1f, 0.4f)), (int) Math.round(position.x + widthOffset), (int) Math.round(position.y + heightOffset), null);
                }
                else {
                    g.drawImage(this.sprite, (int) Math.round(position.x + widthOffset), (int) Math.round(position.y + heightOffset), null);
                }
            }


            Rectangle2D rect2 = new Rectangle((int) Math.round(position.x), (int) Math.round(position.y - HEALTH_OFFSET + heightOffset), Map.TILE_SIZE, HEALTH_HEIGHT);
            g.setPaint(Color.red);
            g.draw(rect2);
            g.fill(rect2);

            Rectangle2D rect3 = new Rectangle((int) Math.round(position.x), (int) Math.round(position.y - HEALTH_OFFSET + heightOffset), Map.TILE_SIZE * this.health / this.maxHealth, HEALTH_HEIGHT);
            g.setPaint(Color.green);
            g.draw(rect3);
            g.fill(rect3);
        }
    }

    private void setTarget(int[][] map) {
        IntCoord candidate;
        for(Direction dir : DIRECTION_CHOICE[this.direction.index]) {
            candidate = this.tileLocation();

            switch (dir) {
                case UP -> {
                    candidate.y--;
                }
                case DOWN -> {
                    candidate.y++;
                }
                case LEFT -> {
                    candidate.x--;
                }
                case RIGHT -> {
                    candidate.x++;
                }
            }
            DoubleCoord tileCoord = new DoubleCoord(tileLocation().x * Map.TILE_SIZE, tileLocation().y * Map.TILE_SIZE);

            if(direction != dir){
                if(!position.equals(tileCoord)){
                    target = tileLocation();
                    break;
                }
            }

            if(candidate.x >= 0 && candidate.x < Map.MAP_WIDTH && candidate.y >= 0 && candidate.y < Map.MAP_HEIGHT){
                if(map[candidate.x][candidate.y] == Map.PATH || map[candidate.x][candidate.y] == Map.START){
                    target = candidate;
                    direction = dir;
                    break;
                }
            }
            else if(position.equals(tileCoord)){
                this.active = false;
            }
        }
    }

    public Enemy copy(DoubleCoord position, Direction direction, double movementMultiplier){
        return new Enemy(this.maxHealth, this.movementSpeed * movementMultiplier, position, direction, this.size, this.sprite, this.camo, this.magicProof, this.armoured, this.moneyGive);
    }

    public int getHealth() {
        return health;
    }

    public boolean isCamo() {
        return this.camo;
    }

    public boolean isMagicProof() {
        return this.magicProof;
    }

    public boolean isArmoured() {
        return this.armoured;
    }

    public DoubleCoord getCenter() {
        return new DoubleCoord(position.x + Map.TILE_SIZE / 2.0, position.y + Map.TILE_SIZE / 2.0);
    }

    public double getRadius() {
        return this.size / SQRT_TWO;
    }

    public int getMoneyGive() {
        return this.moneyGive;
    }

    public boolean isActive() {
        return active;
    }

    private IntCoord tileLocation() {
        return new IntCoord(position.convertToInt().x / Map.TILE_SIZE, position.convertToInt().y / Map.TILE_SIZE);
    }

    private BufferedImage dye(BufferedImage image, Color color) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage dyed = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dyed.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.setComposite(AlphaComposite.SrcAtop);
        g.setColor(color);
        g.fillRect(0, 0, w, h);
        g.dispose();
        return dyed;
    }
}
