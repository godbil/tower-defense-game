package main.enemy;

import main.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class EnemyManager {
    private final ArrayList<DoubleCoord> startPos;
    private final ArrayList<Direction> startDirs;
    private final ArrayList<Enemy> enemies;
    private final HashMap<String, Enemy> enemyTypes;

    private int timer;
    private int enemyQuota;

    private final GameState gameState;

    public EnemyManager(GameState gameState){
        enemies = new ArrayList<>();
        startPos = new ArrayList<>();
        startDirs = new ArrayList<>();
        enemyTypes = new HashMap<>();

        this.gameState = gameState;

        try {
            Path file = Path.of("assets/enemies.txt");
            java.util.List<String> content = Files.readAllLines(file);
            for(String line : content) {
                String[] param = line.split(" ");
                if(param[1].equals("Enemy")) {
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[5] + ".png"));
                    enemyTypes.put(param[0], new Enemy(Integer.parseInt(param[2]), Double.parseDouble(param[3]), Integer.parseInt(param[4]), sprite));
                }
            }
        }
        catch (IOException ignored) {
        }
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

        this.timer = 0;
        this.enemyQuota = this.gameState.getWave() * 20;
        this.enemies.clear();
    }

    public void update(int[][] map){
        for (Enemy enemy : enemies) {
            enemy.move(map);
        }
        enemies.removeIf(enemy -> {
            if(!enemy.isActive()) {
                if(enemy.getHealth() <= 0) {
                    this.gameState.addMoney(this.gameState.getWave() * 20);
                }
                else {
                    gameState.subtractHealth(enemy.getHealth());
                }
                return true;
            }
            else {
                return false;
            }
        });

        if(enemyQuota > 0) {
            if (timer <= 0) {
                spawn();
                timer = (int) (Math.random() * Game.FPS);
                enemyQuota--;
            }
            else {
                timer--;
            }
        }

        if(enemyQuota == 0 && enemies.size() == 0 && this.gameState.getWave() < this.gameState.MAX_WAVE){
            this.gameState.nextWave();
            enemyQuota = this.gameState.getWave() * 10;
            this.gameState.addMoney(this.gameState.getWave() * 120);
        }
    }

    public void paint(Graphics2D g){
        for(Enemy enemy : enemies){
            enemy.paint(g);
        }
    }

    public void spawn() {
        for(int i = 0; i < this.startPos.size() && i < this.startDirs.size(); i++) {
            int random = (int)(Math.random()*3+1);
            if(random == 1) {
                Enemy smallGoblin = enemyTypes.get("SmallGoblin").copy(this.startPos.get(i), this.startDirs.get(i));
                enemies.add(smallGoblin);
            }
            else if(random == 2) {
                Enemy largeGoblin = enemyTypes.get("LargeGoblin").copy(this.startPos.get(i), this.startDirs.get(i));
                enemies.add(largeGoblin);
            }
            else if(random == 3) {
                Enemy splunky = enemyTypes.get("Splunky").copy(this.startPos.get(i), this.startDirs.get(i));
                enemies.add(splunky);
            }
        }
    }

    public ArrayList<Enemy> getEnemies(){
        return enemies;
    }

}
