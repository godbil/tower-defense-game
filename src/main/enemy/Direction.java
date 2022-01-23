package main.enemy;

// Creating an enum which is a group of constants like this which is a group of directions
public enum Direction {
    UP(0),
    RIGHT(1),
    LEFT(2),
    DOWN(3);

    // Makes an index which is then set to each direction
    public final int index;

    Direction(int index) {
        this.index = index;
    }
}
