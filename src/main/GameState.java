package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GameState {
    private int health; // The player's health
    private int money; // The player's money
    public int maxWave; // The max amount of waves that the player has to reach to beat the game
    private int wave; // The current wave that the player is on
    public double movementMultiplier; // The multiplier of the enemies movement speed that depends on difficulty
    public double moneyMultiplier; // The multiplier of the cost of towers that varies on difficulty

    private String[][] waveEnemies; // A 2D string array that holds the specific enemies in each wave
    private int[][] wavePercentages; // A 2D int array that holds the specific percentages of each enemy spawn in each wave and has a corresponding index to the waveEnemies array
    private int[] waveAmounts; // The amount of enemies per wave

    public int gameState; // The state of which the game is in, either 0 which is menu or 1 which is game
    private boolean gameStateChange; // A boolean which tracks whether the game state int has changed
    private boolean gameOver; // A boolean that tracks whether the game has finished yet
    private boolean victory; // A boolean that tracks whether the player has won

    public GameState() {
        // Sets the game state to 0 (menu) at first
        this.gameState = 0;
    }

    /* Used to initialize the variables and can be re-called to change or reset these variables
     * Pre: Takes in a string which is the difficulty that tells which txt file to read
     * Post: Doesn't return anything
     */
    public void init(String difficulty) {
        try {
            // Reads the difficulty txt file that is chosen by the player
            Path file = Paths.get("assets/" + difficulty + ".txt");
            List<String> content = Files.readAllLines(file);
            // Splits the strings by spaces, getting only the first line for now which contains the game settings
            String[] param = content.get(0).split(" ");
            // Sets the health, money, max wave, movement multiplier and money multiplier to the corresponding difficulty's settings
            this.health = Integer.parseInt(param[0]);
            this.money = Integer.parseInt(param[1]);
            this.maxWave = Integer.parseInt(param[2]);
            this.movementMultiplier = Double.parseDouble(param[3]);
            this.moneyMultiplier = Double.parseDouble(param[4]);

            // Initializes the 3 arrays with maxWave + 1 so that it is easier to index the array
            this.waveEnemies = new String[maxWave + 1][];
            this.wavePercentages = new int[maxWave + 1][];
            this.waveAmounts = new int[maxWave + 1];

            // Sets up for loop which starts at index 1 because I added 1 to maxWave and this makes it easier to add values to the arrays
            for(int i = 1; i < content.size(); i++) {
                // Splits param using spaces again
                param = content.get(i).split(" ");
                // Initializes these 2 arrays with the param length divided by 2 because one line of the txt file includes the enemies and their percentages
                wavePercentages[i] = new int[param.length / 2];
                waveEnemies[i] = new String[param.length / 2];
                // Sets the i index of wave amounts to the first index of param which will always be the wave amount in the txt file
                waveAmounts[i] = Integer.parseInt(param[0]);
                // Then sets a for loop which starts at 1 because the first index of param is already used in wave amounts
                for(int j = 1; j < param.length; j++) {
                    if(j % 2 == 0) {
                        // If j is even then that means the string in param is a wave percent so it's added to the array using i as the first index and then divide j by 2 and subtracting 1 to get the correct index
                        wavePercentages[i][j / 2 - 1] = Integer.parseInt(param[j]);
                    }
                    else {
                        // If j is odd then that means the string is the name of an enemy so it's added to the array using i as the first index and then divide j by 2 to get the correct index
                        waveEnemies[i][j / 2] = param[j];
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // Sets wave to 1 and gameOver and victory to false which will reset each time init is called
        this.wave = 1;
        this.gameOver = false;
        this.victory = false;
    }

    /* Used to check whether the game is over by seeing if victory is true or if the player's health is less than or equal to 0
     * Pre: Doesn't take in any parameters
     * Post: Doesn't return anything
     */
    public void checkGameOver() {
        if(this.health <= 0 || victory) {
            this.gameOver = true;
        }
    }

    /* Used to get the maxWave of the current difficulty
     * Pre: Doesn't take in any parameters
     * Post: Returns an int which is the maxWave
     */
    public int getMaxWave() {
        return this.maxWave;
    }

    /* Used to get the health of the player currently
     * Pre: Doesn't take in any parameters
     * Post: Returns an int which is the player's current health
     */
    public int getHealth() {
        return this.health;
    }

    /* Used to get the money of the player currently
     * Pre: Doesn't take in any parameters
     * Post: Returns an int which is the player's money
     */
    public int getMoney() {
        return this.money;
    }

    /* Used to get the wave which the player is currently on
     * Pre: Doesn't take in any parameters
     * Post: Returns an int which is the player's current wave
     */
    public int getWave(){
        return this.wave;
    }

    /* Used to get amount of enemies that can be spawned in the current round
     * Pre: Doesn't take in any parameters
     * Post: Returns an int which is the wave amount of the current round
     */
    public int getWaveAmounts() {
        return this.waveAmounts[wave];
    }

    /* Used to get the movement multiplier which the enemies have depending on the difficulty
     * Pre: Doesn't take in any parameters
     * Post: Returns a double which is the movement multiplier for enemies
     */
    public double getMovementMultiplier() {
        return this.movementMultiplier;
    }

    /* Used to get the money multiplier which the towers have depending on the difficulty
     * Pre: Doesn't take in any parameters
     * Post: Returns a double which is the money multiplier for towers
     */
    public double getMoneyMultiplier() {
        return this.moneyMultiplier;
    }

    /* Used to get the game state that the game is currently in
     * Pre: Doesn't take in any parameters
     * Post: Returns a int which is the game state the game is in
     */
    public int getGameState() {
        return this.gameState;
    }

    /* Used to set the gameState to another gameState and then sets the gameStateChange boolean to true
     * Pre: Takes in an int which is the state that will set the gameState
     * Post: Doesn't return anything
     */
    public void setState(int state) {
        this.gameStateChange = true;
        this.gameState = state;
    }

    /* Used to set the gameOver boolean to passed in boolean
     * Pre: Takes in a boolean which is whether the game is over or not
     * Post: Doesn't return anything
     */
    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }

    /* Used to set the gameStateChange to reset the boolean
     * Pre: Takes in a boolean which is what the gameStateChange should be set to
     * Post: Doesn't return anything
     */
    public void setGameStateChange(boolean gameStateChange) {
        this.gameStateChange = gameStateChange;
    }

    /* Used to get whether the gameStateChange is true or false
     * Pre: Doesn't take in any parameters
     * Post: Returns a boolean which is the gameStateChange
     */
    public boolean isGameStateChange() {
        return gameStateChange;
    }

    /* Used to get whether the gameOver is true or false
     * Pre: Doesn't take in any parameters
     * Post: Returns a boolean which is gameOver
     */
    public boolean isGameOver() {
        return this.gameOver;
    }

    /* Used to set victory to true or false
     * Pre: Takes in a boolean which is used to set the victory boolean
     * Post: Doesn't return anything
     */
    public void setVictory(boolean victory) {
        this.victory = victory;
    }

    /* Used to decrease the waveAmount by one which makes sure the correct amount of enemies are being spawned
     * Pre: Doesn't take in any parameters
     * Post: Doesn't return anything
     */
    public void decrementWaveAmount() {
        waveAmounts[wave]--;
    }

    /* Used to get the specific spawned enemy
     * Pre: Takes in an int which is a randomly generated number
     * Post: Returns the string of the enemy that will get spawned
     */
    public String getWaveEnemies(int random) {
        // Sets percent to 0
        int percent = 0;
        // Sets up a for loop that does everything until i is greater than the length of waveEnemies[wave]
        for(int i = 0; i < waveEnemies[wave].length; i++) {
            // Adds the wavePercent of the index to percent
            percent += wavePercentages[wave][i];
            // Checks whether random is in that range and if it is then the waveEnemy of that index will be returned
            if(random <= percent) {
                return waveEnemies[wave][i];
            }
        }
        return null;
    }

    /* Used to subtract health to the player depending on the health decrease
     * Pre: Takes in an int which is how much health the player loses
     * Post: Doesn't return anything
     */
    public void subtractHealth(int healthDecrease) {
        // Subtracts the amount of healthDecrease from health
        this.health -= healthDecrease;
        // If health is less than 0 then I reset health to 0 for the graphics so it won't show negative numbers
        if(this.health < 0) {
            this.health = 0;
        }
    }

    /* Used to subtract money from the player's amount depending on the money decrease
     * Pre: Takes in an int which is how much money the player loses
     * Post: Doesn't return anything
     */
    public void subtractMoney(int moneyDecrease) {
        this.money -= moneyDecrease;
    }

    /* Used to add money to the player's amount depending on the money increase
     * Pre: Takes in an int which is how much money the player gains
     * Post: Doesn't return anything
     */
    public void addMoney(int moneyIncrease) {
        this.money += moneyIncrease;
    }

    /* Used to move onto the next wave
     * Pre: Doesn't take in any parameters
     * Post: Doesn't return anything
     */
    public void nextWave() {
        this.wave++;
    }

}
