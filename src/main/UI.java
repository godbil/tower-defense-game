package main;

import main.tower.Tower;
import main.tower.TowerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

public class UI implements ActionListener, MouseMotionListener, MouseListener {
    public static final int UI_WIDTH = 256;
    public static final int UI_HEIGHT = 832;

    private Tower displayTower;
    private Tower selectedTower;
    private boolean isValid;

    private final TowerManager towerManager;
    private final GameState gameState;
    private final Map map;

    JButton attackMageButton;

    public UI(Map map, TowerManager towerManager, GameState gameState) {
        this.displayTower = null;
        this.isValid = true;

        this.towerManager = towerManager;
        this.gameState = gameState;
        this.map = map;

        this.attackMageButton = new JButton();
        this.attackMageButton.setIcon(new ImageIcon(towerManager.getImage()));
        this.attackMageButton.setBounds(22 * Map.TILE_SIZE - Map.TILE_SIZE / 2,3 * Map.TILE_SIZE,64,64);

    }

    public void addNotify(JPanel panel) {
        panel.addMouseMotionListener(this);
        panel.addMouseListener(this);
        this.attackMageButton.addActionListener(this);
        panel.setLayout(null);
        panel.add(this.attackMageButton);
    }

    public void paint(Graphics2D g) {
        Rectangle2D.Double UI = new Rectangle2D.Double(Map.MAP_WIDTH * Map.TILE_SIZE, 0, UI_WIDTH, UI_HEIGHT);
        g.draw(UI);
        g.setPaint(Color.DARK_GRAY);
        g.fill(UI);

        g.setPaint(Color.white);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.drawString("Health: " + this.gameState.getHealth(), Map.TILE_SIZE / 4, Map.TILE_SIZE / 2);
        g.drawString("Money: " + this.gameState.getMoney(), 2 * Map.TILE_SIZE, Map.TILE_SIZE / 2);
        g.drawString("Wave: " + gameState.getWave() + " / " + gameState.MAX_WAVE, 19 * Map.TILE_SIZE, Map.TILE_SIZE / 2);

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
        if(e.getButton() == MouseEvent.BUTTON3) {
            displayTower = null;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(displayTower != null && this.isValid) {
            towerManager.place(map.getMap(), displayTower);
            this.gameState.subtractMoney(120);
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
        if(e.getSource() == this.attackMageButton) {
            if(this.gameState.getMoney() >= displayTower) {
                this.displayTower = new Tower(1, 120, 120, 60, new IntCoord(Map.MAP_WIDTH * Map.TILE_SIZE, 0), towerManager.getImage());
                this.selectedTower = null;
            }
        }
    }
}
