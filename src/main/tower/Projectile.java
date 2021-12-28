package main.tower;

import main.DoubleCoord;
import main.Game;
import main.enemy.Enemy;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Projectile {
    private DoubleCoord position;
    private DoubleCoord velocity;
    private double speed;
    private int size;
    private boolean active;

    private BufferedImage sprite;

    public Projectile(double speed, int size, BufferedImage sprite) {
        this.speed = speed;
        this.size = size;
        this.sprite = sprite;
        this.active = true;
    }

    public Projectile(double speed, DoubleCoord position, int size, DoubleCoord target, BufferedImage sprite) {
        this.position = position.copy();
        this.velocity = calculateVelocity(speed, target);
        this.size = size;
        this.sprite = sprite;
        this.active = true;
    }

    public void update() {
        this.move();
        if(position.x < 0 || position.x >= Game.WIDTH || position.y < 0 || position.y >= Game.HEIGHT){
            this.active = false;
        }
    }

    public void move() {
        position.x += velocity.x;
        position.y += velocity.y;
    }

    public void paint(Graphics2D g) {
        if(this.active) {
            g.drawImage(this.sprite, (int)this.position.x - this.size, (int)this.position.y - this.size, null);
        }
    }

    public boolean collision(Enemy enemy) {
        double distance = Math.pow(enemy.getCenter().x - this.position.x, 2) + Math.pow(enemy.getCenter().y - this.position.y, 2);
        return distance < Math.pow(enemy.getRadius() + size, 2);
    }

    public void hit(){
        this.active = false;
    }

    public boolean isActive(){
        return this.active;
    }

    public Projectile copy(DoubleCoord position, DoubleCoord target){
        return new Projectile(this.speed, position, this.size, target, this.sprite);
    }

    private DoubleCoord calculateVelocity(double speed, DoubleCoord target) {
        double width = target.x - this.position.x;
        double height = target.y - this.position.y;
        double distance = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
        return new DoubleCoord(speed * width / distance, speed * height / distance);
    }

}
