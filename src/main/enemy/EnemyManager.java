package main.enemy;

import main.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EnemyManager {
    // Creates an array list for both the starting position for the enemies and starting directions for the enemies (array list for maps with multiple starting points)
    private final ArrayList<DoubleCoord> startPos;
    private final ArrayList<Direction> startDirs;
    // Creates an array list for all of the enemies that spawn
    private final ArrayList<Enemy> enemies;
    // Creates a hashmap which is basically an array where you can use other things as indexes like this which has the enemy name which is a string and the actual enemy and its stats
    private final HashMap<String, Enemy> enemyTypes;

    // A timer for separating the spawns of enemies
    private int timer;

    // Creates a gameState which will be passed in through the constructor's parameter
    private final GameState gameState;

    public EnemyManager(GameState gameState){
        // Initializes all of the arraylists and the hashmap
        this.enemies = new ArrayList<>();
        this.startPos = new ArrayList<>();
        startDirs = new ArrayList<>();
        enemyTypes = new HashMap<>();

        // Sets this gameState to the one being passed in
        this.gameState = gameState;

        try {
            // Reads the enemies txt file and basically adds them all the enemyTypes so they can be spawned later
            Path file = Paths.get("assets/enemies.txt");
            List<String> content = Files.readAllLines(file);
            // Sets up a for loop which splits the list of strings into one line at a time (one enemy at a time)
            for (String line : content) {
                // Splits each string using spaces
                String[] param = line.split(" ");
                // Checks whether the enemy type is a regular enemy, a spawner enemy or a death spawner enemy
                if (param[1].equals("Enemy")) {
                    // Taking all of the stats from the line and putting them into the hashmap to create models for each enemy
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[5] + ".png"));
                    enemyTypes.put(param[0], new Enemy(Integer.parseInt(param[2]), Double.parseDouble(param[3]), Integer.parseInt(param[4]), sprite, Boolean.parseBoolean(param[6]), Boolean.parseBoolean(param[7]), Boolean.parseBoolean(param[8]), Integer.parseInt(param[9])));
                }
                if (param[1].equals("SpawnerEnemy")) {
                    // Taking all of the stats from the line and putting them into the hashmap to create models for each enemy
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[5] + ".png"));
                    enemyTypes.put(param[0], new SpawnerEnemy(Integer.parseInt(param[2]), Double.parseDouble(param[3]), Integer.parseInt(param[4]), sprite, Boolean.parseBoolean(param[6]), Boolean.parseBoolean(param[7]), Boolean.parseBoolean(param[8]), Integer.parseInt(param[9]), enemyTypes.get(param[10]), Integer.parseInt(param[11]), Integer.parseInt(param[12])));
                }
                if (param[1].equals("DeathSpawnerEnemy")) {
                    // Taking all of the stats from the line and putting them into the hashmap to create models for each enemy
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[5] + ".png"));
                    enemyTypes.put(param[0], new DeathSpawnerEnemy(Integer.parseInt(param[2]), Double.parseDouble(param[3]), Integer.parseInt(param[4]), sprite, Boolean.parseBoolean(param[6]), Boolean.parseBoolean(param[7]), Boolean.parseBoolean(param[8]), Integer.parseInt(param[9]), enemyTypes.get(param[10]), Integer.parseInt(param[11])));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Used to initialize the variables and can be re-called to change or reset these variables
     * Pre: Takes in a 2D array which is the map
     * Post: Doesn't return anything
     */
    public void init(int[][] map) {
        // Creates a new arraylist which stores where the start tiles of the map is
        ArrayList<IntCoord> startTiles = new ArrayList<>();
        // Clears the start direction and start position arraylist so it can be reset whenever init is called
        startDirs.clear();
        startPos.clear();
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++) {
                if (map[i][j] == Map.START) {
                    // Finds the starts of the map and then adds it to the start tiles arraylist
                    startTiles.add(new IntCoord(i, j));
                }
            }
        }

        // A for loop which will go through all of the start tiles
        for(IntCoord startTile : startTiles) {
            // Checks whether the going up from the start tile will be in the boundaries and then whether it is a path
            if(startTile.y - 1 >= 0 && startTile.y - 1 < Map.MAP_HEIGHT && map[startTile.x][startTile.y - 1] == Map.PATH){
                startDirs.add(Direction.UP);
            }
            // Checks whether the going down from the start tile will be in the boundaries and then whether it is a path
            else if(startTile.y + 1 >= 0 && startTile.y + 1 < Map.MAP_HEIGHT && map[startTile.x][startTile.y + 1] == Map.PATH) {
                startDirs.add(Direction.DOWN);
            }
            // Checks whether the going left from the start tile will be in the boundaries and then whether it is a path
            else if(startTile.x - 1 >= 0 && startTile.x - 1 < Map.MAP_WIDTH && map[startTile.x - 1][startTile.y] == Map.PATH) {
                startDirs.add(Direction.LEFT);
            }
            // Since everything else didn't work, the last possible start direction would be right
            else {
                startDirs.add(Direction.RIGHT);
            }

            // Adds the starting position of the enemies based off of the start tile's x and y position
            this.startPos.add(new DoubleCoord(startTile.x * Map.TILE_SIZE, startTile.y * Map.TILE_SIZE));
        }

        // Resets the timer and clears the enemies array for each init
        this.timer = 0;
        this.enemies.clear();
    }

    /* Used to update all of the enemies' variables and checks if the enemies get killed or get through to the end
     * Pre: Takes in a 2D array which is the map
     * Post: Doesn't return anything
     */
    public void update(int[][] map){
        // Goes through all of the enemies in the array list
        for (int i = 0; i < enemies.size(); i++) {
            // Calls their movement method each update
            enemies.get(i).move(map);
            if(enemies.get(i) instanceof SpawnerEnemy) {
                // If the enemy is a spawner enemy then it will call the spawn enemy method every update
                ((SpawnerEnemy) enemies.get(i)).spawnEnemy(gameState, enemies);
            }
            if(enemies.get(i) instanceof DeathSpawnerEnemy) {
                // If the enemy is a death spawner enemy then it will call the death spawn enemy method every update
                ((DeathSpawnerEnemy) enemies.get(i)).deathSpawnEnemy(gameState, enemies);
            }
        }
        // Removes the enemy if it isn't active anymore
        enemies.removeIf(enemy -> {
            // If the enemy isn't active then this will return true and the enemy will be removed from the array list
            if(!enemy.isActive()) {
                if(enemy.getHealth() <= 0) {
                    // If the health of the enemy is less than or equal 0 then it means they have been killed so the player gains money depending on the enemy's money give
                    this.gameState.addMoney(enemy.getMoneyGive());
                }
                else {
                    // If that's not the case then it will subtract the amount of health that the enemy had left from the player
                    gameState.subtractHealth(enemy.getHealth());
                }
                return true;
            }
            // If the enemy is still active then it will return false and not be removed from the array list
            else {
                return false;
            }
        });

        // Checks whether the wave amount is still greater than 0 and if it is and timer is less than or equal to 0 then it will call the spawn method
        if(gameState.getWaveAmounts() > 0) {
            if (timer <= 0) {
                spawn();
                // Resets the timer to a random number between 0 and 60 frames
                timer = (int) (Math.random() * Game.FPS);
                // Decreases the wave amount each spawn
                gameState.decrementWaveAmount();
            }
            // Counts down the timer if the timer isn't less than or equal to 0 yet
            else {
                timer--;
            }
        }

        // If there is no more enemies that can spawn and there are no enemies in the array list and the current wave isn't the max wave yet then it will move onto the next wave
        if(gameState.getWaveAmounts() == 0 && enemies.size() == 0 && this.gameState.getWave() < this.gameState.getMaxWave()){
            this.gameState.nextWave();
            // Adds money for the round end bonus
            this.gameState.addMoney(this.gameState.getWave() * 100);
        }
        // If there isn't anymore enemies that can spawn and there are no more enemies in the array list but the current wave is the max wave then victory is set to true in gameState
        else if(gameState.getWaveAmounts() == 0 && enemies.size() == 0 && this.gameState.getWave() == this.gameState.getMaxWave()) {
            this.gameState.setVictory(true);
        }
    }

    /* Used to paint all of the enemies
     * Pre: Takes in a Graphics2D object that paints things
     * Post: Doesn't return anything
     */
    public void paint(Graphics2D g){
        // Goes through all of the enemies and paints them so that the graphics will be updates each frame
        for(Enemy enemy : enemies){
            enemy.paint(g);
        }
    }

    /* Used to spawn enemies depending on the wave and the enemyPercentages
     * Pre: Doesn't take in any parameters
     * Post: Doesn't return anything
     */
    public void spawn() {
        // If i is greater than the size of the start position array list and the start direction array list then this will run (mainly for multiple path maps)
        for(int i = 0; i < this.startPos.size() && i < this.startDirs.size(); i++) {
            // Chosen enemy is chosen by passing in a random number to the gameState's get wave enemy method
            String chosenEnemy = gameState.getWaveEnemies((int)(Math.random()*101));
            // Sets enemy to the chosen enemy
            Enemy enemy = enemyTypes.get(chosenEnemy);
            if(enemy != null) {
                // Checks if enemy isn't null and then makes a copy of the enemy which will have the start position and start direction then adds it to the enemies array list
                enemy = enemy.copy(this.startPos.get(i), this.startDirs.get(i), gameState.getMovementMultiplier());
                enemies.add(enemy);
            }
        }
    }

    /* Used to get the enemies array list
     * Pre: Doesn't take in any parameters
     * Post: Returns an array list which is the enemies
     */
    public ArrayList<Enemy> getEnemies(){
        return enemies;
    }

}
