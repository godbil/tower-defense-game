package main;

import main.enemy.Enemy;
import main.enemy.EnemyManager;
import main.tower.Tower;
import main.tower.TowerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class Game extends JPanel implements ActionListener, KeyListener, MouseListener {
    public static final int WIDTH = Map.MAP_WIDTH * Map.TILE_SIZE + UI.UI_WIDTH;
    public static final int HEIGHT = Math.max(Map.MAP_HEIGHT * Map.TILE_SIZE, UI.UI_HEIGHT);

    private static final int FPS = 60;
    private static final int TIMER = 1000 / FPS;

    private Map map;
    private EnemyManager enemyManager;
    private TowerManager towerManager;

    public Game(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);
        this.requestFocus();

        this.map = new Map();
        this.enemyManager = new EnemyManager();
        this.towerManager = new TowerManager();
    }

    private void init() {
        try {
            this.map.loadMap("AdvMap");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.enemyManager.init(this.map.getMap());
    }

    @Override
    public void addNotify() {
        super.addNotify();

        this.addKeyListener(this);
        this.addMouseListener(this);

        // Initialize a timer which calls actionPerformed method every TIMER amount of milliseconds
        Timer timer = new Timer(TIMER, this);
        timer.start();
        this.init();
    }

    private void input() {

    }

    private void update() {
        this.enemyManager.update(this.map.getMap());
        this.towerManager.update(this.map.getMap(), this.enemyManager.getEnemies());
    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        Rectangle2D.Double background = new Rectangle2D.Double(0, 0, 1344, HEIGHT);
        g2.draw(background);
        g2.setPaint(Color.black);
        g2.fill(background);
        for(int i = 0; i < 1600; i += 64){
            Line2D.Double line = new Line2D.Double(i, 0, i, 832);
            Line2D.Double line2 = new Line2D.Double(0, i, 1600, i);
            g2.setPaint(Color.white);
            g2.draw(line);
            g2.draw(line2);
        }

        map.paint(g2);
        towerManager.paint(g2);
        enemyManager.paint(g2);
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

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        towerManager.place(map.getMap(), e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
