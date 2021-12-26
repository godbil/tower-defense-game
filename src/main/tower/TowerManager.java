package main.tower;

import main.IntCoord;
import main.Map;
import main.enemy.Enemy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TowerManager {
    private final ArrayList<Tower> towers;

    BufferedImage attackMage;

    public TowerManager(){
        towers = new ArrayList<>();

        try {
            attackMage = ImageIO.read(new File("assets/attackmage1.png"));
        }
        catch (IOException ignored) {

        }
    }

    public void init() {
        this.towers.clear();
    }

    public void update(int[][] map, ArrayList<Enemy> enemies){
        for(Tower tower : towers){
            tower.update(enemies);
        }
    }

    public void paint(Graphics2D g){
        for(Tower tower : towers){
            tower.paint(g);
        }
    }

    public void place(int[][] map, Tower tower){
        if(tower.getTileLocation().x >= 0 && tower.getTileLocation().x < Map.MAP_WIDTH && tower.getTileLocation().y >= 0 && tower.getTileLocation().y < Map.MAP_HEIGHT && map[tower.getTileLocation().x][tower.getTileLocation().y] == Map.PLACEABLE){
            map[tower.getTileLocation().x][tower.getTileLocation().y] = Map.TOWER;
            towers.add(tower);
        }
    }

    public Tower findSelectedTower(IntCoord selectedPosition) {
        for(Tower tower : towers) {
            if(tower.getTileLocation().equals(selectedPosition)){
                return tower;
            }
        }
        return null;
    }

    public BufferedImage getImage() {
        return attackMage;
    }

}
