package main.enemy;

public enum Direction {
    UP(0),
    RIGHT(1),
    LEFT(2),
    DOWN(3);

    public final int index;

    Direction(int index) {
        this.index = index;
    }
}
