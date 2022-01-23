package main;

public class IntCoord {
    // The x and y integer variable coordinates of this int coordinate
    public int x;
    public int y;

    public IntCoord(int x, int y){
        this.x = x;
        this.y = y;
    }

    /* Used to check whether another int coordinate is equal to this coordinate
     * Pre: Takes in an object which is used to compare to the current int coordinate
     * Post: Returns a boolean that is true if the two coordinates are equal and false if they aren't
     */
    @Override
    public boolean equals(Object other){
        // If the object is an int coordinate then it compares the current x to the object's x and same for the y
        if(other instanceof IntCoord) {
            return x == ((IntCoord) other).x && y == ((IntCoord) other).y;
        }
        else {
            return false;
        }
    }

    /* Used to get a string of all the object's instance variables
     * Pre: Doesn't take in any parameters
     * Post: Returns a string with the x and y coordinate of the int coordinate
     */
    @Override
    public String toString(){
        return x + ", " + y;
    }
}
