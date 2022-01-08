package main.tower;

import main.DoubleCoord;
import main.IntCoord;
import main.Map;
import main.enemy.Enemy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TowerManager {
    private final ArrayList<Tower> towers;
    private final HashMap<String, Tower> towerTypes;
    private final HashMap<String, Projectile> projectileTypes;
    private final DoubleCoord mousePosition;

    public TowerManager(){
        towers = new ArrayList<>();
        towerTypes = new HashMap<>();
        projectileTypes = new HashMap<>();
        mousePosition = new DoubleCoord(0, 0);

        try {
            Path file = Path.of("assets/projectiles.txt");
            List<String> content = Files.readAllLines(file);
            for(String line : content) {
                String[] param = line.split(" ");
                if(param[1].equals("Projectile")) {
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[4] + ".png"));
                    projectileTypes.put(param[0], new Projectile(Double.parseDouble(param[2]), Integer.parseInt(param[3]), sprite));
                }
                else if(param[1].equals("SplashProjectile")) {
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[4] + ".png"));
                    projectileTypes.put(param[0], new SplashProjectile(Double.parseDouble(param[2]), Integer.parseInt(param[3]), sprite));
                }
                else if(param[1].equals("PierceProjectile")) {
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[4] + ".png"));
                    projectileTypes.put(param[0], new PierceProjectile(Double.parseDouble(param[2]), Integer.parseInt(param[3]), sprite, Integer.parseInt(param[5])));
                }
                else if(param[1].equals("RotatableProjectile")) {
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[4] + ".png"));
                    projectileTypes.put(param[0], new RotatableProjectile(Double.parseDouble(param[2]), Integer.parseInt(param[3]), sprite));
                }
                else if(param[1].equals("MortarProjectile")) {
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[4] + ".png"));
                    BufferedImage explosion = ImageIO.read(new File("assets/sprites/" + param[5] + ".png"));
                    projectileTypes.put(param[0], new MortarProjectile(Double.parseDouble(param[2]), Integer.parseInt(param[3]), sprite, explosion, Integer.parseInt(param[6])));
                }
            }
            file = Path.of("assets/towers.txt");
            content = Files.readAllLines(file);
            for(String line : content) {
                String[] param = line.split(" ");
                if(param[1].equals("Tower")) {
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[6] + ".png"));
                    ArrayList<String> towerUpgradePaths = new ArrayList<>();
                    for(int i = 0; i < Integer.parseInt(param[11]); i++){
                        towerUpgradePaths.add(param[i + 12]);
                    }
                    towerTypes.put(param[0], new Tower(Integer.parseInt(param[2]), Integer.parseInt(param[3]), Integer.parseInt(param[4]), Integer.parseInt(param[5]), new IntCoord(0,0), sprite, projectileTypes.get(param[7]), Boolean.parseBoolean(param[8]), Boolean.parseBoolean(param[9]), Boolean.parseBoolean(param[10]), towerUpgradePaths));
                }
                else if(param[1].equals("CircularTower")) {
                    ArrayList<String> towerUpgradePaths = new ArrayList<>();
                    for(int i = 0; i < Integer.parseInt(param[12]); i++){
                        towerUpgradePaths.add(param[i + 13]);
                    }
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[6] + ".png"));
                    towerTypes.put(param[0], new CircularTower(Integer.parseInt(param[2]), Integer.parseInt(param[3]), Integer.parseInt(param[4]), Integer.parseInt(param[5]), new IntCoord(0,0), sprite, projectileTypes.get(param[7]), Integer.parseInt(param[8]), Boolean.parseBoolean(param[9]), Boolean.parseBoolean(param[10]), Boolean.parseBoolean(param[11]), towerUpgradePaths));
                }
                else if(param[1].equals("MortarTower")) {
                    ArrayList<String> towerUpgradePaths = new ArrayList<>();
                    for(int i = 0; i < Integer.parseInt(param[12]); i++){
                        towerUpgradePaths.add(param[i + 13]);
                    }
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[6] + ".png"));
                    BufferedImage target = ImageIO.read(new File("assets/sprites/" + param[8] + ".png"));
                    towerTypes.put(param[0], new MortarTower(Integer.parseInt(param[2]), Integer.parseInt(param[3]), Integer.parseInt(param[4]), Integer.parseInt(param[5]), new IntCoord(0,0), sprite, projectileTypes.get(param[7]), target, Boolean.parseBoolean(param[9]), Boolean.parseBoolean(param[10]), Boolean.parseBoolean(param[11]), towerUpgradePaths));
                }
                else if(param[1].equals("BallistaTower")) {
                    ArrayList<String> towerUpgradePaths = new ArrayList<>();
                    for(int i = 0; i < Integer.parseInt(param[11]); i++){
                        towerUpgradePaths.add(param[i + 12]);
                    }
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[6] + ".png"));
                    towerTypes.put(param[0], new BallistaTower(Integer.parseInt(param[2]), Integer.parseInt(param[3]), Integer.parseInt(param[4]), Integer.parseInt(param[5]), new IntCoord(0,0), sprite, projectileTypes.get(param[7]), Boolean.parseBoolean(param[8]), Boolean.parseBoolean(param[9]), Boolean.parseBoolean(param[10]), towerUpgradePaths));
                }
                else if(param[1].equals("FarmTower")) {
                    ArrayList<String> towerUpgradePaths = new ArrayList<>();
                    for(int i = 0; i < Integer.parseInt(param[13]); i++){
                        towerUpgradePaths.add(param[i + 14]);
                    }
                    BufferedImage sprite = ImageIO.read(new File("assets/sprites/" + param[6] + ".png"));
                    towerTypes.put(param[0], new FarmTower(Integer.parseInt(param[2]), Integer.parseInt(param[3]), Integer.parseInt(param[4]), Integer.parseInt(param[5]), new IntCoord(0,0), sprite, projectileTypes.get(param[7]), Integer.parseInt(param[8]), Integer.parseInt(param[9]), Boolean.parseBoolean(param[10]), Boolean.parseBoolean(param[11]), Boolean.parseBoolean(param[12]), towerUpgradePaths));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        this.towers.clear();
    }

    public void update(ArrayList<Enemy> enemies){
        for(Tower tower : towers){
            tower.update(enemies);
            if(tower instanceof BallistaTower) {
                ((BallistaTower) tower).setTargetPosition(mousePosition);
            }
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

    public Tower getTower(String towerName) {
        return towerTypes.get(towerName);
    }

    public Tower findSelectedTower(IntCoord selectedPosition) {
        for(Tower tower : towers) {
            if(tower.getTileLocation().equals(selectedPosition)){
                return tower;
            }
        }
        return null;
    }

    public void setMousePosition(MouseEvent e) {
        this.mousePosition.x = e.getX();
        this.mousePosition.y = e.getY();
    }
}
