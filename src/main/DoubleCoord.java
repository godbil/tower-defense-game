package main;

public class DoubleCoord {
    // Final variable called epsilon which is basically just a very small number
    private static final double EPSILON = 0.01;

    // The x and y double variable coordinates of this double coordinate
    public double x;
    public double y;

    public DoubleCoord(double x, double y){
        this.x = x;
        this.y = y;
    }

    /* Used to check whether another double coordinate is equal to this coordinate
     * Pre: Takes in an object which is used to compare to the current double coordinate
     * Post: Returns a boolean that is true if the two coordinates are equal and false if they aren't
     */
    @Override
    public boolean equals(Object other){
        // If the passed in object is a double coordinate, it's x value will be subtracted to the current coordinate's x value and the same for the y to see if the difference is less than epsilon
        if(other instanceof DoubleCoord) {
            return Math.abs(x - ((DoubleCoord) other).x) < EPSILON && Math.abs(y - ((DoubleCoord) other).y) < EPSILON;
        }
        else {
            return false;
        }
    }

    /* Used to get a string of all the object's instance variables
     * Pre: Doesn't take in any parameters
     * Post: Returns a string with the x and y coordinate of the double coordinate
     */
    @Override
    public String toString(){
        return x + ", " + y;
    }

    /* Used to convert a double coordinate into an int coordinate
     * Pre: Doesn't take in any parameters
     * Post: Returns the converted integer coordinate
     */
    public IntCoord convertToInt(){
        int newX = (int)Math.round(this.x);
        int newY = (int)Math.round(this.y);
        return new IntCoord(newX, newY);
    }

    /* Used to make a copy of the current double coordinate
     * Pre: Doesn't take in any parameters
     * Post: Returns the copy of the double coordinate
     */
    public DoubleCoord copy(){
        return new DoubleCoord(this.x, this.y);
    }
}
