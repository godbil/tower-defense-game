package main.enemy;

import main.DoubleCoord;
import main.IntCoord;
import main.Map;

import java.awt.*;
import java.util.ArrayList;

public class EnemyManager {
    private final ArrayList<DoubleCoord> startPos;
    private final ArrayList<Direction> startDirs;
    private final ArrayList<Enemy> enemies;

    public EnemyManager(){
        enemies = new ArrayList<>();
        startPos = new ArrayList<>();
        startDirs = new ArrayList<>();
    }

    public void init(int[][] map) {
        ArrayList<IntCoord> startTiles = new ArrayList<>();
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++) {
                if (map[i][j] == Map.START) {
                    startTiles.add(new IntCoord(i, j));
                }
            }
        }

        for(IntCoord startTile : startTiles) {
            if(startTile.y - 1 >= 0 && startTile.y - 1 < Map.MAP_HEIGHT && map[startTile.x][startTile.y - 1] == Map.PATH){
                startDirs.add(Direction.UP);
            }
            else if(startTile.y + 1 >= 0 && startTile.y + 1 < Map.MAP_HEIGHT && map[startTile.x][startTile.y + 1] == Map.PATH) {
                startDirs.add(Direction.DOWN);
            }
            else if(startTile.x - 1 >= 0 && startTile.x - 1 < Map.MAP_WIDTH && map[startTile.x - 1][startTile.y] == Map.PATH) {
                startDirs.add(Direction.LEFT);
            }
            else {
                startDirs.add(Direction.RIGHT);
            }

            this.startPos.add(new DoubleCoord(startTile.x * Map.TILE_SIZE, startTile.y * Map.TILE_SIZE));
        }

        for(int i = 0; i < this.startPos.size() && i < this.startDirs.size(); i++) {
            Enemy zombie = new Enemy(10000,2, this.startPos.get(i), this.startDirs.get(i), Map.TILE_SIZE / 2);
            enemies.add(zombie);
        }
    }

    public void update(int[][] map){
        for(Enemy enemy : enemies){
            enemy.move(map);
        }
        enemies.removeIf(enemy -> !enemy.isActive());
    }

    public void paint(Graphics2D g){
        for(Enemy enemy : enemies){
            enemy.paint(g);
        }
    }

    public ArrayList<Enemy> getEnemies(){
        return enemies;
    }

}
