package main.tower;

import main.DoubleCoord;
import main.Game;
import main.Map;
import main.enemy.Enemy;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Projectile {
    private DoubleCoord position;
    private DoubleCoord velocity;
    private int size;
    private boolean active;

    public Projectile(double speed, DoubleCoord position, int size, DoubleCoord target) {
        this.position = position;
        this.size = size;
        this.active = true;
        this.velocity = calculateVelocity(speed, target);
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
            Ellipse2D circle = new Ellipse2D.Double(this.position.x - this.size, this.position.y - this.size, this.size * 2, this.size * 2);
            g.setPaint(Color.RED);
            g.draw(circle);
            g.fill(circle);
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

    private DoubleCoord calculateVelocity(double speed, DoubleCoord target) {
        double width = target.x - this.position.x;
        double height = target.y - this.position.y;
        double distance = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
        return new DoubleCoord(speed * width / distance, speed * height / distance);
    }

}
