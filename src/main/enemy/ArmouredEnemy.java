package main.enemy;

import main.DoubleCoord;

import java.awt.image.BufferedImage;

public class ArmouredEnemy extends Enemy{

    public ArmouredEnemy(int maxHealth, double movementSpeed, DoubleCoord position, Direction direction, int size, BufferedImage image) {
        super(maxHealth, movementSpeed, position, direction, size, image);
    }
}
