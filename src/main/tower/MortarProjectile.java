package main.tower;

import main.DoubleCoord;
import main.Map;
import main.enemy.Enemy;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MortarProjectile extends Projectile{
    private int stage;
    private DoubleCoord[] stageTarget;
    private BufferedImage explosion;
    private int timer;
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

    @Override
    public void update() {
        if(this.stage == 0 && this.position.y < 0) {
            this.velocity = calculateVelocity(5 * this.speed, this.stageTarget[1]);
            this.stage++;
        }
        else if(this.stage == 1 && (this.stageTarget[0].x >= this.stageTarget[1].x && this.position.x <= this.stageTarget[1].x || this.stageTarget[0].x <= this.stageTarget[1].x && this.position.x >= this.stageTarget[1].x)) {
            this.velocity = calculateVelocity(this.speed, this.stageTarget[2]);
            isFlipped = true;
            this.stage++;
        }
        else if(this.stage == 2 && this.position.y >= this.stageTarget[2].y) {
            this.velocity.x = 0;
            this.velocity.y = 0;
            this.stage++;
        }
        else if(this.stage == 3 && timer <= 0) {
            this.active = false;
        }
        else if (this.stage == 3){
            timer--;
        }
        this.move();
    }

    @Override
    public void paint(Graphics2D g) {
        if(stage == 3) {
            g.drawImage(this.explosion, (int) this.stageTarget[2].x - this.explosion.getWidth() / 2, (int) this.stageTarget[2].y  - this.explosion.getHeight() / 2, null);
        }
        else if(isFlipped){
            g.drawImage(this.sprite, (int)this.position.x - this.size, (int)this.position.y - this.size, sprite.getWidth(), -sprite.getHeight(), null);
        }
        else {
            g.drawImage(this.sprite, (int)this.position.x - this.size, (int)this.position.y - this.size, null);
        }
    }

    @Override
    public boolean collision(Enemy enemy) {
        if(this.stage == 3) {
            return super.collision(enemy);
        }
        else {
            return false;
        }
    }

    @Override
    public void hit(){
        this.hitCount++;
    }

    @Override
    public void postUpdate() {
        if(this.hitCount > 0) {
            this.active = false;
        }
    }

    @Override
    public MortarProjectile copy(DoubleCoord position, DoubleCoord target){
        return new MortarProjectile(this.speed, position, this.size, target, this.sprite, this.explosion, this.timer);
    }
}
