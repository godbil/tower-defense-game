package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Map {
    // Dimensions for the map portion of the game
    public static final int TILE_SIZE = 64; // The size of each tile in the game
    public static final int MAP_WIDTH = 21; // Amount of tiles for the map's width
    public static final int MAP_HEIGHT = 13; // Amount of tiles for the map's height

    // The number that that corresponds to each thing in the map's 2D Array grid
    public static final int PLACEABLE = 0; // Area which is able to have towers placed
    public static final int PATH = 1; // Area where there is a path that enemies walk on
    public static final int START = 2; // The start of the path where enemies come out from
    public static final int TOWER = 3; // Area where there is a tower placed in the tile
    public static final int OBSTACLE = 4; // Area where there are obstacles where towers can't be placed

    private BufferedImage mapSprite; // The sprite for the map
    private BufferedImage mapSpriteOverlay; // The overlay of the map which includes the rock that the enemies come out of

    private int[][] map; // 2D Array for the Map

    public Map(){
        this.map = new int[MAP_WIDTH][MAP_HEIGHT];
    }

    /* Reads the specific map txt file and sprite name and then puts them into the map's 2D Array
     * Pre: Takes in a string which is the name of the map's txt file
     * Post: Doesn't return anything
     */
    public void loadMap(String path) throws IOException {
        // Getting the sprite for the map and overlay
        mapSprite = ImageIO.read(new File("assets/sprites/" + path + ".png"));
        mapSpriteOverlay = ImageIO.read(new File("assets/sprites/" + path + "Overlay.png"));
        // Gets the txt file and puts them into a list of strings, separated by new lines
        Path file = Paths.get("assets/maps/" + path + ".txt");
        List<String> content = Files.readAllLines(file);
        for(int i = 0; i < map[0].length; i++){
            for(int j = 0; j < map.length; j++){
                // Gets the index from the list depending on the first for loop
                String temp = content.get(i);
                // Gets the ascii code of the character from the string from the list which is separated by spaces in the txt file so it's multiplied by 2 and then subtracted by 48 to get the actual character
                map[j][i] = temp.charAt(j*2) - 48;
            }
        }
    }

    /* Used to draw things
     * Pre: Takes in a Graphics2D object, used to paint images
     * Post: Doesn't return anything
     */
    public void paint(Graphics2D g){
        // Draws the sprite from 0, 0 (top left of screen)
        g.drawImage(mapSprite, 0, 0, null);
    }

    /* Used to draw things after everything else is drawn so it will appear above all enemies
     * Pre: Takes in a Graphics2D object, used to paint images
     * Post: Doesn't return anything
     */
    public void postDraw(Graphics2D g) {
        // Draws the overlay from 0, 0 (top left of screen)
        g.drawImage(mapSpriteOverlay, 0, 0, null);
    }

    /* Used to get the 2D array map
     * Pre: Doesn't take in any parameters
     * Post: Returns a 2D array which is the map
     */
    public int[][] getMap() {
        return this.map;
    }

}
