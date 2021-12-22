package main.enemy;

import main.DoubleCoord;
import main.IntCoord;
import main.Map;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

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

    private int health;
    private int maxHealth;
    private int size;
    private boolean active;

    private double movementSpeed;
    private DoubleCoord position;
    private IntCoord target;

    private Direction direction;

    public Enemy(int maxHealth, double movementSpeed, DoubleCoord position, Direction direction, int size) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.movementSpeed = movementSpeed;
        this.position = position.copy();
        this.direction = direction;
        this.size = size;

        this.active = true;
    }

    public void takeDamage(int damage){
        this.health -= damage;
        if(this.health <= 0){
            this.active = false;
        }
    }

    public void move(int[][] map) {
        this.setTarget(map);
        this.target.x *= Map.TILE_SIZE;
        this.target.y *= Map.TILE_SIZE;

        if(this.position.x > this.target.x){
            this.position.x = Math.max(this.position.x - this.movementSpeed, this.target.x);
        }
        else if(this.position.x < this.target.x){
            this.position.x = Math.min(this.position.x + this.movementSpeed, this.target.x);
        }

        if(this.position.y > this.target.y){
            this.position.y = Math.max(this.position.y - this.movementSpeed, this.target.y);
        }
        else if(this.position.y < this.target.y){
            this.position.y = Math.min(this.position.y + this.movementSpeed, this.target.y);
        }
    }

    public void paint(Graphics2D g){
        if(this.active) {
            double offset = Map.TILE_SIZE / 2.0 - this.size / 2.0;

            Rectangle2D rect = new Rectangle((int) Math.round(position.x + offset), (int) Math.round(position.y + offset), this.size, this.size);
            g.setPaint(Color.MAGENTA);
            g.draw(rect);
            g.fill(rect);

            Rectangle2D rect2 = new Rectangle((int) Math.round(position.x), (int) Math.round(position.y - HEALTH_OFFSET + offset), Map.TILE_SIZE, HEALTH_HEIGHT);
            g.setPaint(Color.red);
            g.draw(rect2);
            g.fill(rect2);

            Rectangle2D rect3 = new Rectangle((int) Math.round(position.x), (int) Math.round(position.y - HEALTH_OFFSET + offset), Map.TILE_SIZE * this.health / this.maxHealth, HEALTH_HEIGHT);
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

    public DoubleCoord getCenter() {
        return new DoubleCoord(position.x + Map.TILE_SIZE / 2.0, position.y + Map.TILE_SIZE / 2.0);
    }

    public double getRadius() {
        return this.size / SQRT_TWO;
    }

    public boolean isActive() {
        return active;
    }

    private IntCoord tileLocation() {
        return new IntCoord(position.convertToInt().x / Map.TILE_SIZE, position.convertToInt().y / Map.TILE_SIZE);
    }
}
