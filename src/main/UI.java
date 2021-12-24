package main;

import main.enemy.EnemyManager;
import main.tower.Tower;
import main.tower.TowerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class UI implements ActionListener, MouseMotionListener, MouseListener {
    public static final int UI_WIDTH = 256;
    public static final int UI_HEIGHT = 832;

    private Tower displayTower;
    private Tower selectedTower;
    private boolean isValid;

    private int health;
    private int money;

    private final TowerManager towerManager;
    private final EnemyManager enemyManager;
    private final Map map;

    JButton button;

    public UI(Map map, TowerManager towerManager, EnemyManager enemyManager) {
        this.displayTower = null;
        this.isValid = true;

        this.towerManager = towerManager;
        this.enemyManager = enemyManager;
        this.map = map;

        this.health = 150;
        this.money = 650;

        this.button = new JButton();
        this.button.setIcon(new ImageIcon(towerManager.getImage()));
        this.button.setBounds(22 * Map.TILE_SIZE - Map.TILE_SIZE / 2,3 * Map.TILE_SIZE,64,64);

    }

    public void addNotify(JPanel panel) {
        panel.addMouseMotionListener(this);
        panel.addMouseListener(this);
        this.button.addActionListener(this);
        panel.setLayout(null);
        panel.add(this.button);
    }

    public void paint(Graphics2D g) {
        Rectangle2D.Double UI = new Rectangle2D.Double(Map.MAP_WIDTH * Map.TILE_SIZE, 0, UI_WIDTH, UI_HEIGHT);
        g.draw(UI);
        g.setPaint(Color.DARK_GRAY);
        g.fill(UI);

        g.setPaint(Color.white);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.drawString("Wave: " + enemyManager.getWave() + " / " + enemyManager.MAX_WAVE, 19 * Map.TILE_SIZE, Map.TILE_SIZE / 2);

        if(displayTower != null) {
            displayTower.paint(g, isValid, true);
        }
        if(selectedTower != null) {
            selectedTower.paint(g, isValid, true);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(displayTower != null) {
            if (e.getX() / Map.TILE_SIZE >= 0 && e.getX() / Map.TILE_SIZE < Map.MAP_WIDTH && e.getY() / Map.TILE_SIZE >= 0 && e.getY() / Map.TILE_SIZE < Map.MAP_HEIGHT) {
                isValid = map.getMap()[e.getX() / Map.TILE_SIZE][e.getY() / Map.TILE_SIZE] == 0;
                displayTower.setTileLocation(new IntCoord(e.getX() / Map.TILE_SIZE, e.getY() / Map.TILE_SIZE));
            }
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX() / Map.TILE_SIZE >= 0 && e.getX() / Map.TILE_SIZE < Map.MAP_WIDTH && e.getY() / Map.TILE_SIZE >= 0 && e.getY() / Map.TILE_SIZE < Map.MAP_HEIGHT && this.displayTower == null) {
            selectedTower = towerManager.findSelectedTower(new IntCoord(e.getX() / Map.TILE_SIZE, e.getY() / Map.TILE_SIZE));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(displayTower != null && this.isValid) {
            towerManager.place(map.getMap(), displayTower);
            displayTower = null;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.button) {
            this.displayTower = new Tower(1, 2, 30, new IntCoord(Map.MAP_WIDTH * Map.TILE_SIZE, 0), towerManager.getImage());
        }
    }
}
