package main.tower;

import main.DoubleCoord;
import main.IntCoord;
import main.Map;
import main.enemy.Enemy;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class TowerManager {
    private final ArrayList<Tower> towers;

    public TowerManager(){
        towers = new ArrayList<>();
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

}
