package main;

import java.awt.*;
import java.awt.geom.Rectangle2D;
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

    private int[][] map;

    public Map(){
        this.map = new int[MAP_WIDTH][MAP_HEIGHT];
    }

    public void loadMap(String path) throws IOException {
        Path file = Path.of("assets/" + path + ".txt");
        List<String> content = Files.readAllLines(file);
        for(int i = 0; i < map[0].length; i++){
            for(int j = 0; j < map.length; j++){
                String temp = content.get(i);
                map[j][i] = temp.charAt(j*2) - 48;
            }
        }
    }

    public void paint(Graphics2D g){
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                if(map[i][j] == PATH){
                    Rectangle2D rect = new Rectangle(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    g.setPaint(Color.white);
                    g.draw(rect);
                    g.fill(rect);
                }
                else if(map[i][j] == START){
                    Rectangle2D rect = new Rectangle(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    g.setPaint(Color.red);
                    g.draw(rect);
                    g.fill(rect);
                }
            }
        }
    }

    public int[][] getMap() {
        return this.map;
    }

}
