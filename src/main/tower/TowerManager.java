package main.tower;

import main.DoubleCoord;
import main.IntCoord;
import main.Map;
import main.enemy.Enemy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TowerManager {
    // An array list of all the placed towers
    private final ArrayList<Tower> towers;
    // A hashmap of all the different types of towers
    private final HashMap<String, Tower> towerTypes;
    // A hashmap for all the different kinds of projectiles
    private final HashMap<String, Projectile> projectileTypes;
    // A hashmap for storing the tower's descriptions
    private final HashMap<String, String> towerDescription;
    // A double coordinate of where the player's mouse is for the ballista tower
    private final DoubleCoord mousePosition;

    public TowerManager(){
        // Initializes all of the instance variables
        towers = new ArrayList<>();
        towerTypes = new HashMap<>();
        projectileTypes = new HashMap<>();
        towerDescription = new HashMap<>();
        mousePosition = new DoubleCoord(0, 0);

        try {
            // Gets the projectile txt file and then stores all of the information into a list of strings
            Path file = Paths.get("assets/projectiles.txt");
            List<String> content = Files.readAllLines(file);
            for(String line : content) {
                // Splits each line of the list into separate strings
                String[] param = line.split(" ");
                // Checks which type (class) of projectile is currently being added
                if(param[1].equals("Projectile")) {
                    // Makes the key of the hashmap into the name of the projectile and then creates a new projectile to be stored in the hashmap
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[4] + ".png"));
                    projectileTypes.put(param[0], new Projectile(Double.parseDouble(param[2]), Integer.parseInt(param[3]), sprite));
                }
                else if(param[1].equals("SplashProjectile")) {
                    // Makes the key of the hashmap into the name of the projectile and then creates a new splash projectile to be stored in the hashmap
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[4] + ".png"));
                    projectileTypes.put(param[0], new SplashProjectile(Double.parseDouble(param[2]), Integer.parseInt(param[3]), sprite, Integer.parseInt(param[5])));
                }
                else if(param[1].equals("PierceProjectile")) {
                    // Makes the key of the hashmap into the name of the projectile and then creates a new pierce projectile to be stored in the hashmap
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[4] + ".png"));
                    projectileTypes.put(param[0], new PierceProjectile(Double.parseDouble(param[2]), Integer.parseInt(param[3]), sprite, Integer.parseInt(param[ 5])));
                }
                else if(param[1].equals("RotatableProjectile")) {
                    // Makes the key of the hashmap into the name of the projectile and then creates a new rotatable projectile to be stored in the hashmap
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[4] + ".png"));
                    projectileTypes.put(param[0], new RotatableProjectile(Double.parseDouble(param[2]), Integer.parseInt(param[3]), sprite));
                }
                else if(param[1].equals("MortarProjectile")) {
                    // Makes the key of the hashmap into the name of the projectile and then creates a new mortar projectile to be stored in the hashmap
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[4] + ".png"));
                    BufferedImage explosion = ImageIO.read(new File("assets/sprites/" + param[5] + ".png"));
                    projectileTypes.put(param[0], new MortarProjectile(Double.parseDouble(param[2]), Integer.parseInt(param[3]), sprite, explosion, Integer.parseInt(param[6])));
                }
            }
            // Gets the tower txt file and then stores all of the information into a list of strings
            file = Paths.get("assets/towers.txt");
            content = Files.readAllLines(file);
            for(String line : content) {
                // Splits each line of the list into separate strings
                String[] param = line.split(" ");
                // Checks which type (class) of tower is currently being added
                if(param[1].equals("Tower")) {
                    // Makes the tower upgrade path for the tower which gets passed in through the parameter
                    ArrayList<String> towerUpgradePaths = new ArrayList<>();
                    for(int i = 0; i < Integer.parseInt(param[12]); i++){
                        towerUpgradePaths.add(param[i + 13]);
                    }
                    // Makes the key of the hashmap into the name of the tower and then creates a new tower to be stored in the hashmap
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[7] + ".png"));
                    towerTypes.put(param[0], new Tower(Integer.parseInt(param[2]), Integer.parseInt(param[3]), Integer.parseInt(param[4]), Integer.parseInt(param[5]), Integer.parseInt(param[6]), new IntCoord(0,0), sprite, projectileTypes.get(param[8]), Boolean.parseBoolean(param[9]), Boolean.parseBoolean(param[10]), Boolean.parseBoolean(param[11]), towerUpgradePaths));
                }
                else if(param[1].equals("CircularTower")) {
                    // Makes the tower upgrade path for the tower which gets passed in through the parameter
                    ArrayList<String> towerUpgradePaths = new ArrayList<>();
                    for(int i = 0; i < Integer.parseInt(param[13]); i++){
                        towerUpgradePaths.add(param[i + 14]);
                    }
                    // Makes the key of the hashmap into the name of the tower and then creates a new circular tower to be stored in the hashmap
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[7] + ".png"));
                    towerTypes.put(param[0], new CircularTower(Integer.parseInt(param[2]), Integer.parseInt(param[3]), Integer.parseInt(param[4]), Integer.parseInt(param[5]), Integer.parseInt(param[6]), new IntCoord(0,0), sprite, projectileTypes.get(param[8]), Integer.parseInt(param[9]), Boolean.parseBoolean(param[10]), Boolean.parseBoolean(param[11]), Boolean.parseBoolean(param[12]), towerUpgradePaths));
                }
                else if(param[1].equals("MortarTower")) {
                    // Makes the tower upgrade path for the tower which gets passed in through the parameter
                    ArrayList<String> towerUpgradePaths = new ArrayList<>();
                    for(int i = 0; i < Integer.parseInt(param[14]); i++){
                        towerUpgradePaths.add(param[i + 15]);
                    }
                    // Makes the key of the hashmap into the name of the tower and then creates a new mortar tower to be stored in the hashmap
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[7] + ".png"));
                    BufferedImage target = ImageIO.read(new File("assets/sprites/" + param[9] + ".png"));
                    towerTypes.put(param[0], new MortarTower(Integer.parseInt(param[2]), Integer.parseInt(param[3]), Integer.parseInt(param[4]), Integer.parseInt(param[5]), Integer.parseInt(param[6]), new IntCoord(0,0), sprite, projectileTypes.get(param[8]), target, Boolean.parseBoolean(param[10]), Boolean.parseBoolean(param[11]), Boolean.parseBoolean(param[12]), Integer.parseInt(param[13]), towerUpgradePaths));
                }
                else if(param[1].equals("BallistaTower")) {
                    // Makes the tower upgrade path for the tower which gets passed in through the parameter
                    ArrayList<String> towerUpgradePaths = new ArrayList<>();
                    for(int i = 0; i < Integer.parseInt(param[12]); i++){
                        towerUpgradePaths.add(param[i + 13]);
                    }
                    // Makes the key of the hashmap into the name of the tower and then creates a new ballista tower to be stored in the hashmap
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[7] + ".png"));
                    towerTypes.put(param[0], new BallistaTower(Integer.parseInt(param[2]), Integer.parseInt(param[3]), Integer.parseInt(param[4]), Integer.parseInt(param[5]), Integer.parseInt(param[6]), new IntCoord(0,0), sprite, projectileTypes.get(param[8]), Boolean.parseBoolean(param[9]), Boolean.parseBoolean(param[10]), Boolean.parseBoolean(param[11]), towerUpgradePaths));
                }
                else if(param[1].equals("FarmTower")) {
                    // Makes the tower upgrade path for the tower which gets passed in through the parameter
                    ArrayList<String> towerUpgradePaths = new ArrayList<>();
                    for(int i = 0; i < Integer.parseInt(param[14]); i++){
                        towerUpgradePaths.add(param[i + 15]);
                    }
                    // Makes the key of the hashmap into the name of the tower and then creates a new farm tower to be stored in the hashmap
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[7] + ".png"));
                    towerTypes.put(param[0], new FarmTower(Integer.parseInt(param[2]), Integer.parseInt(param[3]), Integer.parseInt(param[4]), Integer.parseInt(param[5]), Integer.parseInt(param[6]), new IntCoord(0,0), sprite, projectileTypes.get(param[8]), Integer.parseInt(param[9]), Integer.parseInt(param[10]), Boolean.parseBoolean(param[11]), Boolean.parseBoolean(param[12]), Boolean.parseBoolean(param[13]), towerUpgradePaths));
                }
                else if(param[1].equals("MultiHitTower")) {
                    // Makes the tower upgrade path for the tower which gets passed in through the parameter
                    ArrayList<String> towerUpgradePaths = new ArrayList<>();
                    for(int i = 0; i < Integer.parseInt(param[13]); i++){
                        towerUpgradePaths.add(param[i + 14]);
                    }
                    // Makes the key of the hashmap into the name of the tower and then creates a new multi hit tower to be stored in the hashmap
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[7] + ".png"));
                    towerTypes.put(param[0], new MultiHitTower(Integer.parseInt(param[2]), Integer.parseInt(param[3]), Integer.parseInt(param[4]), Integer.parseInt(param[5]), Integer.parseInt(param[6]), new IntCoord(0,0), sprite, projectileTypes.get(param[8]), Boolean.parseBoolean(param[9]), Boolean.parseBoolean(param[10]), Boolean.parseBoolean(param[11]), Integer.parseInt(param[12]), towerUpgradePaths));
                }
                if(param[1].equals("CircleShootingTower")) {
                    // Makes the tower upgrade path for the tower which gets passed in through the parameter
                    ArrayList<String> towerUpgradePaths = new ArrayList<>();
                    for(int i = 0; i < Integer.parseInt(param[14]); i++){
                        towerUpgradePaths.add(param[i + 15]);
                    }
                    // Makes the key of the hashmap into the name of the tower and then creates a new circular shooting tower to be stored in the hashmap
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[7] + ".png"));
                    towerTypes.put(param[0], new CircleShootingTower(Integer.parseInt(param[2]), Integer.parseInt(param[3]), Integer.parseInt(param[4]), Integer.parseInt(param[5]), Integer.parseInt(param[6]), new IntCoord(0,0), sprite, projectileTypes.get(param[8]), Boolean.parseBoolean(param[9]), Boolean.parseBoolean(param[10]), Boolean.parseBoolean(param[11]), Integer.parseInt(param[12]), Integer.parseInt(param[13]), towerUpgradePaths));
                }
                if(param[1].equals("AquaRingTower")) {
                    // Makes the tower upgrade path for the tower which gets passed in through the parameter
                    ArrayList<String> towerUpgradePaths = new ArrayList<>();
                    for(int i = 0; i < Integer.parseInt(param[14]); i++){
                        towerUpgradePaths.add(param[i + 15]);
                    }
                    // Makes the key of the hashmap into the name of the tower and then creates a new aqua ring tower to be stored in the hashmap
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[7] + ".png"));
                    towerTypes.put(param[0], new AquaRingTower(Integer.parseInt(param[2]), Integer.parseInt(param[3]), Integer.parseInt(param[4]), Integer.parseInt(param[5]), Integer.parseInt(param[6]), new IntCoord(0,0), sprite, projectileTypes.get(param[8]), Boolean.parseBoolean(param[9]), Boolean.parseBoolean(param[10]), Boolean.parseBoolean(param[11]), Integer.parseInt(param[12]), Integer.parseInt(param[13]), towerUpgradePaths));
                }
                if(param[1].equals("EarthStompTower")) {
                    // Makes the tower upgrade path for the tower which gets passed in through the parameter
                    ArrayList<String> towerUpgradePaths = new ArrayList<>();
                    for(int i = 0; i < Integer.parseInt(param[13]); i++){
                        towerUpgradePaths.add(param[i + 14]);
                    }
                    // Makes the key of the hashmap into the name of the tower and then creates a new earth stomp tower to be stored in the hashmap
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[7] + ".png"));
                    towerTypes.put(param[0], new EarthStompTower(Integer.parseInt(param[2]), Integer.parseInt(param[3]), Integer.parseInt(param[4]), Integer.parseInt(param[5]), Integer.parseInt(param[6]), new IntCoord(0,0), sprite, projectileTypes.get(param[8]), Boolean.parseBoolean(param[9]), Boolean.parseBoolean(param[10]), Boolean.parseBoolean(param[11]), Integer.parseInt(param[12]), towerUpgradePaths));
                }
                if(param[1].equals("SlowTower")) {
                    // Makes the tower upgrade path for the tower which gets passed in through the parameter
                    ArrayList<String> towerUpgradePaths = new ArrayList<>();
                    for(int i = 0; i < Integer.parseInt(param[13]); i++){
                        towerUpgradePaths.add(param[i + 14]);
                    }
                    // Makes the key of the hashmap into the name of the tower and then creates a new slow tower to be stored in the hashmap
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[7] + ".png"));
                    towerTypes.put(param[0], new SlowTower(Integer.parseInt(param[2]), Integer.parseInt(param[3]), Integer.parseInt(param[4]), Integer.parseInt(param[5]), Integer.parseInt(param[6]), new IntCoord(0,0), sprite, projectileTypes.get(param[8]), Boolean.parseBoolean(param[9]), Boolean.parseBoolean(param[10]), Boolean.parseBoolean(param[11]), Double.parseDouble(param[12]), towerUpgradePaths));
                }
                if(param[1].equals("FreezeTower")) {
                    // Makes the tower upgrade path for the tower which gets passed in through the parameter
                    ArrayList<String> towerUpgradePaths = new ArrayList<>();
                    for(int i = 0; i < Integer.parseInt(param[13]); i++){
                        towerUpgradePaths.add(param[i + 14]);
                    }
                    // Makes the key of the hashmap into the name of the tower and then creates a new freeze tower to be stored in the hashmap
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[7] + ".png"));
                    towerTypes.put(param[0], new FreezeTower(Integer.parseInt(param[2]), Integer.parseInt(param[3]), Integer.parseInt(param[4]), Integer.parseInt(param[5]), Integer.parseInt(param[6]), new IntCoord(0,0), sprite, projectileTypes.get(param[8]), Boolean.parseBoolean(param[9]), Boolean.parseBoolean(param[10]), Boolean.parseBoolean(param[11]), Integer.parseInt(param[12]), towerUpgradePaths));
                }
            }
            // Gets the tower's description txt file and then stores all of the information into a list of strings
            file = Paths.get("assets/towerdesc.txt");
            content = Files.readAllLines(file);
            for(String line : content) {
                // Splits the string by spaces but limits it by two so that it won't split the description which has spaces
                String[] param = line.split(" ", 2);
                towerDescription.put(param[0], param[1]);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Used to initialize things and clear the tower array list so that it can be reset when init is recalled
     * Pre: Doesn't take in any paramaters
     * Post: Doesn't return anything
     */
    public void init() {
        this.towers.clear();
    }

    /* Used to call the tower's update method and also set the ballista's target position
     * Pre: Takes in an array list which is the enemies
     * Post: Doesn't return anything
     */
    public void update(ArrayList<Enemy> enemies){
        for(Tower tower : towers){
            tower.update(enemies);
            if(tower instanceof BallistaTower) {
                ((BallistaTower) tower).setTargetPosition(mousePosition);
            }
        }
    }

    /* Used to call the tower's paint method for all towers
     * Pre: Takes in a Graphics2D object used for painting things
     * Post: Doesn't return anything
     */
    public void paint(Graphics2D g){
        for(Tower tower : towers){
            tower.paint(g);
        }
    }

    /* Used to place a tower wherever the player chooses
     * Pre: Takes in a 2D array which is the map and then tower that is being placed
     * Post: Doesn't return anything
     */
    public void place(int[][] map, Tower tower){
        // Checks if the location is inside the boundaries and if the area is placeable
        if(tower.getTileLocation().x >= 0 && tower.getTileLocation().x < Map.MAP_WIDTH && tower.getTileLocation().y >= 0 && tower.getTileLocation().y < Map.MAP_HEIGHT && map[tower.getTileLocation().x][tower.getTileLocation().y] == Map.PLACEABLE){
            // Sets that tile on the map to a tower and then adds the tower to the array list of towers
            map[tower.getTileLocation().x][tower.getTileLocation().y] = Map.TOWER;
            towers.add(tower);
        }
    }

    /* Used to replace the tower being upgraded with the new tower
     * Pre: Takes in a tower which is the tower object being upgraded, the int of the upgrade path that is chosen and the double of the money multiplier for the difficulty
     * Post: Doesn't return anything
     */
    public void upgrade(Tower upgradingTower, int path, double moneyMultiplier) {
        // Gets the new tower from the tower types array list using the old tower's upgrade paths
        Tower tower = towerTypes.get(upgradingTower.towerUpgradePaths.get(path)).copy(upgradingTower.getTileLocation(), moneyMultiplier);
        // Removes the old tower from the array list and then adds the new one
        towers.remove(upgradingTower);
        towers.add(tower);
    }

    /* Used to sell the tower by removing it from the array list
     * Pre: Takes in the specific tower object being sold
     * Post: Doesn't return anything
     */
    public void sell(Tower tower) {
        towers.remove(tower);
    }

    /* Used to get a specific tower using it's name
     * Pre: Takes in a string of the tower's name
     * Post: Returns the actual tower object
     */
    public Tower getTower(String towerName) {
        return towerTypes.get(towerName);
    }

    /* Used to get the description of a tower using it's name
     * Pre: Takes in a string of the tower's name
     * Post: Returns the string of the tower's description
     */
    public String getDescription(String towerName) {
        return towerDescription.get(towerName);
    }

    /* Used to find which tower is selected given the position
     * Pre: Takes in an int coordinate of the selected tower
     * Post: Returns the actual tower object of the selected tower
     */
    public Tower findSelectedTower(IntCoord selectedPosition) {
        for(Tower tower : towers) {
            // Goes through all of the towers and check their position to see which matches the selected one
            if(tower.getTileLocation().equals(selectedPosition)){
                return tower;
            }
        }
        return null;
    }

    /* Used to get the mouse position of the player
     * Pre: Takes in a mouseEvent object
     * Post: Doesn't return anything
     */
    public void setMousePosition(MouseEvent e) {
        // Gets the x and y coordinate of the mouse's position
        this.mousePosition.x = e.getX();
        this.mousePosition.y = e.getY();
    }
}
