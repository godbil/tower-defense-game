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
import java.util.List;

public class EnemyManager {
    private final ArrayList<DoubleCoord> startPos;
    private final ArrayList<Direction> startDirs;
    private final ArrayList<Enemy> enemies;
    private final HashMap<String, Enemy> enemyTypes;

    private int timer;

    private final GameState gameState;

    public EnemyManager(GameState gameState){
        this.enemies = new ArrayList<>();
        this.startPos = new ArrayList<>();
        startDirs = new ArrayList<>();
        enemyTypes = new HashMap<>();

        this.gameState = gameState;

        try {
            Path file = Path.of("assets/enemies.txt");
            List<String> content = Files.readAllLines(file);
            for (String line : content) {
                String[] param = line.split(" ");
                if (param[1].equals("Enemy")) {
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[5] + ".png"));
                    enemyTypes.put(param[0], new Enemy(Integer.parseInt(param[2]), Double.parseDouble(param[3]), Integer.parseInt(param[4]), sprite, Boolean.parseBoolean(param[6]), Boolean.parseBoolean(param[7]), Boolean.parseBoolean(param[8]), Integer.parseInt(param[9])));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(int[][] map) {
        ArrayList<IntCoord> startTiles = new ArrayList<>();
        startDirs.clear();
        startPos.clear();
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
        this.enemies.clear();
    }

    public void update(int[][] map){
        for (Enemy enemy : enemies) {
            enemy.move(map);
        }
        enemies.removeIf(enemy -> {
            if(!enemy.isActive()) {
                if(enemy.getHealth() <= 0) {
                    this.gameState.addMoney(enemy.getMoneyGive());
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

        if(gameState.getWaveAmounts() > 0) {
            if (timer <= 0) {
                spawn();
                timer = (int) (Math.random() * Game.FPS);
                gameState.decrementWaveAmount();
            }
            else {
                timer--;
            }
        }

        if(gameState.getWaveAmounts() == 0 && enemies.size() == 0 && this.gameState.getWave() < this.gameState.getMaxWave()){
            this.gameState.nextWave();
            this.gameState.addMoney(this.gameState.getWave() * 100);
        }
    }

    public void paint(Graphics2D g){
        for(Enemy enemy : enemies){
            enemy.paint(g);
        }
    }

    public void spawn() {
        for(int i = 0; i < this.startPos.size() && i < this.startDirs.size(); i++) {
            String chosenEnemy = gameState.getWaveEnemies((int)(Math.random()*101));
            Enemy enemy = enemyTypes.get(chosenEnemy);
            if(enemy != null) {
                enemy = enemy.copy(this.startPos.get(i), this.startDirs.get(i), gameState.getMovementMultiplier());
                enemies.add(enemy);
            }
        }
    }

    public ArrayList<Enemy> getEnemies(){
        return enemies;
    }

}
