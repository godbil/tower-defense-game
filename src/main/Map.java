package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Map {
    //dimensions
    public static final int TILE_SIZE = 64;
    public static final int MAP_WIDTH = 21;
    public static final int MAP_HEIGHT = 13;

    public static final int PLACEABLE = 0;
    public static final int PATH = 1;
    public static final int START = 2;
    public static final int TOWER = 3;
    public static final int OBSTACLE = 4;

    private BufferedImage mapSprite;
    private BufferedImage mapSpriteOverlay;

    private int[][] map;

    public Map(){
        this.map = new int[MAP_WIDTH][MAP_HEIGHT];
    }

    public void loadMap(String path) throws IOException {
        mapSprite = ImageIO.read(new File("assets/sprites/" + path + ".png"));
        mapSpriteOverlay = ImageIO.read(new File("assets/sprites/" + path + "Overlay.png"));
        Path file = Path.of("assets/maps/" + path + ".txt");
        List<String> content = Files.readAllLines(file);
        for(int i = 0; i < map[0].length; i++){
            for(int j = 0; j < map.length; j++){
                String temp = content.get(i);
                map[j][i] = temp.charAt(j*2) - 48;
            }
        }
    }

    public void paint(Graphics2D g){
        g.drawImage(mapSprite, 0, 0, null);
    }

    public void postDraw(Graphics2D g) {
        g.drawImage(mapSpriteOverlay, 0, 0, null);
    }

    public int[][] getMap() {
        return this.map;
    }

}
