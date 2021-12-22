package main.enemy;

import main.DoubleCoord;

public class ArmouredEnemy extends Enemy{

    public ArmouredEnemy(int maxHealth, double movementSpeed, DoubleCoord position, Direction direction, int size) {
        super(maxHealth, movementSpeed, position, direction, size);
    }
}
