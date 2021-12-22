package main;

public class IntCoord {
    public int x;
    public int y;

    public IntCoord(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof IntCoord) {
            return x == ((IntCoord) other).x && y == ((IntCoord) other).y;
        }
        else {
            return false;
        }
    }

    @Override
    public String toString(){
        return x + ", " + y;
    }
}
