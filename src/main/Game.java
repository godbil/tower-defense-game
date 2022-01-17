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
    // Initialize a timer which calls actionPerformed method every TIMER amount of milliseconds
    private Timer timer;

    private final Map map;
    private final EnemyManager enemyManager;
    private final TowerManager towerManager;
    private final UI ui;
    private final GameState gameState;
    private final Menu menu;

    public Game(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);
        this.requestFocus();

        this.timer = new Timer(TIMER, this);

        this.map = new Map();
        this.towerManager = new TowerManager();
        this.gameState = new GameState();
        this.menu = new Menu(gameState);
        this.enemyManager = new EnemyManager(this.gameState);
        this.ui = new UI(this.map, this.towerManager, this.gameState);
    }

    private void init() {
        if(gameState.getGameState() == 1) {
            try {
                this.map.loadMap(menu.getMap());
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.gameState.init(this.menu.getDifficulty());
            this.enemyManager.init(this.map.getMap());
            this.towerManager.init();
            this.ui.init();
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();

        if(gameState.getGameState() == 0) {
            this.menu.addNotify(this);
        }
        if(gameState.getGameState() == 1) {
            this.ui.addNotify(this);
        }
        timer.start();
        this.init();
    }

    private void update() {
        if(gameState.isGameStateChange()) {
            gameState.setGameStateChange(false);
            this.ui.removeNotify(this);
            this.menu.removeNotify(this);
            this.addNotify();
        }
        if(gameState.getGameState() == 1 && gameState.getHealth() > 0) {
            this.enemyManager.update(this.map.getMap());
            this.towerManager.update(this.enemyManager.getEnemies());
            gameState.checkGameOver();
        }
    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        if(gameState.getGameState() == 0) {
            menu.paint(g2);
        }
        else if(gameState.getGameState() == 1) {
            map.paint(g2);
            towerManager.paint(g2);
            enemyManager.paint(g2);
            ui.paint(g2);
            map.postDraw(g2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.update();
        this.repaint();
    }
}
