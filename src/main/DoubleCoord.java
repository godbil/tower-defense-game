package main;

public class DoubleCoord {
    private static final double EPSILON = 0.01;

    public double x;
    public double y;

    public DoubleCoord(double x, double y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof DoubleCoord) {
            return Math.abs(x - ((DoubleCoord) other).x) < EPSILON && Math.abs(y - ((DoubleCoord) other).y) < EPSILON;
        }
        else {
            return false;
        }
    }

    @Override
    public String toString(){
        return x + ", " + y;
    }

    public IntCoord convertToInt(){
        int newX = (int)Math.round(this.x);
        int newY = (int)Math.round(this.y);
        return new IntCoord(newX, newY);
    }

    public DoubleCoord copy(){
        return new DoubleCoord(this.x, this.y);
    }

}
