package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
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

    private ImageObserver comp;
    private BufferedImage EasyMap1;

    private int[][] map;

    public Map(){
        this.map = new int[MAP_WIDTH][MAP_HEIGHT];

        try {
            EasyMap1 = ImageIO.read(new File("assets/sprites/easymap1.png"));
        }
        catch (IOException ex) {
        }

        comp = new JComponent() {};
    }

    public void loadMap(String path) throws IOException {
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
        g.drawImage(EasyMap1, 0, 0, comp);
    }

    public int[][] getMap() {
        return this.map;
    }

}
