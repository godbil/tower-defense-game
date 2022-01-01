package main;

import main.enemy.EnemyManager;
import main.tower.TowerManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Game extends JPanel implements ActionListener, KeyListener {
    public static final int WIDTH = Map.MAP_WIDTH * Map.TILE_SIZE + UI.UI_WIDTH;
    public static final int HEIGHT = Math.max(Map.MAP_HEIGHT * Map.TILE_SIZE, UI.UI_HEIGHT);

    public static final int FPS = 60;
    public static final int TIMER = 1000 / FPS;

    private Map map;
    private EnemyManager enemyManager;
    private TowerManager towerManager;
    private UI ui;
    private GameState gameState;

    private BufferedImage rock;

    public Game(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);
        this.requestFocus();

        this.map = new Map();
        this.towerManager = new TowerManager();
        this.gameState = new GameState();
        this.enemyManager = new EnemyManager(this.gameState);
        this.ui = new UI(this.map, this.towerManager, this.gameState);

        try {
            rock = ImageIO.read(new File("assets/sprites/rock.png"));
        }
        catch (IOException ignored) {

        }
    }

    private void init() {
        try {
            this.map.loadMap("EasyMap1");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.gameState.init();
        this.enemyManager.init(this.map.getMap());
        this.towerManager.init();
        this.ui.init();
    }

    @Override
    public void addNotify() {
        super.addNotify();

        this.addKeyListener(this);
        this.ui.addNotify(this);

        // Initialize a timer which calls actionPerformed method every TIMER amount of milliseconds
        Timer timer = new Timer(TIMER, this);
        timer.start();
        this.init();
    }

    private void input() {

    }

    private void update() {
        this.enemyManager.update(this.map.getMap());
        this.towerManager.update(this.enemyManager.getEnemies());
        if(this.gameState.getHealth() <= 0) {
            this.init();
        }
    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;

        map.paint(g2);
        towerManager.paint(g2);
        enemyManager.paint(g2);
        ui.paint(g2);

        g.drawImage(rock, -10, 3 * Map.TILE_SIZE, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.input();
        this.update();
        this.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
