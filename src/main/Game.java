package main;

import main.enemy.EnemyManager;
import main.tower.TowerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Game extends JPanel implements ActionListener {
    public static final int WIDTH = Map.MAP_WIDTH * Map.TILE_SIZE + UI.UI_WIDTH;
    public static final int HEIGHT = Math.max(Map.MAP_HEIGHT * Map.TILE_SIZE, UI.UI_HEIGHT);

    public static final int FPS = 60;
    public static final int TIMER = 1000 / FPS;

    private final Map map;
    private final EnemyManager enemyManager;
    private final TowerManager towerManager;
    private final UI ui;
    private final GameState gameState;

    public Game(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);
        this.requestFocus();

        this.map = new Map();
        this.towerManager = new TowerManager();
        this.gameState = new GameState();
        this.enemyManager = new EnemyManager(this.gameState);
        this.ui = new UI(this.map, this.towerManager, this.gameState);

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
        this.ui.addNotify(this);

        // Initialize a timer which calls actionPerformed method every TIMER amount of milliseconds
        Timer timer = new Timer(TIMER, this);
        timer.start();
        this.init();
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
        map.postDraw(g2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.update();
        this.repaint();
    }
}
