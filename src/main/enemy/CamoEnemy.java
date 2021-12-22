package main.enemy;

import main.DoubleCoord;

public class CamoEnemy extends Enemy{

    public CamoEnemy(int maxHealth, double movementSpeed, DoubleCoord position, Direction direction, int size) {
        super(maxHealth, movementSpeed, position, direction, size);
    }
}
