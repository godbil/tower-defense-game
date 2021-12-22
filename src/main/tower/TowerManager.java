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

    public void place(int[][] map, MouseEvent e){
        Point p = e.getPoint();
        int x = p.x / Map.TILE_SIZE;
        int y = p.y / Map.TILE_SIZE;
        if(x >= 0 && x < Map.MAP_WIDTH && y >= 0 && y < Map.MAP_HEIGHT && map[x][y] == Map.PLACEABLE){
            map[x][y] = Map.TOWER;
            Tower tower = new Tower(1, 2, 1, new IntCoord(x, y));
            towers.add(tower);
        }
    }

}
