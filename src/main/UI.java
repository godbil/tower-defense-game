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
    JButton supportMageButton;
    JButton warriorButton;
    JButton catapultButton;
    JButton tackShooterButton;
    JButton mortarButton;

    public UI(Map map, TowerManager towerManager, GameState gameState) {
        this.towerManager = towerManager;
        this.gameState = gameState;
        this.map = map;

        this.attackMageButton = new JButton();
        this.attackMageButton.setIcon(new ImageIcon(towerManager.getTower("AttackMage").getSprite()));
        this.attackMageButton.setBounds(22 * Map.TILE_SIZE - Map.TILE_SIZE / 2,3 * Map.TILE_SIZE,64,64);

        this.supportMageButton = new JButton();
        this.supportMageButton.setIcon(new ImageIcon(towerManager.getTower("SupportMage").getSprite()));
        this.supportMageButton.setBounds(24 * Map.TILE_SIZE - Map.TILE_SIZE / 2,3 * Map.TILE_SIZE,64,64);

        this.warriorButton = new JButton();
        this.warriorButton.setIcon(new ImageIcon(towerManager.getTower("Warrior").getSprite()));
        this.warriorButton.setBounds(22 * Map.TILE_SIZE - Map.TILE_SIZE / 2,5 * Map.TILE_SIZE,64,64);

        this.catapultButton = new JButton();
        this.catapultButton.setIcon(new ImageIcon(towerManager.getTower("Catapult").getSprite()));
        this.catapultButton.setBounds(24 * Map.TILE_SIZE - Map.TILE_SIZE / 2,5 * Map.TILE_SIZE,64,64);

        this.tackShooterButton = new JButton();
        this.tackShooterButton.setIcon(new ImageIcon(towerManager.getTower("TackShooter").getSprite()));
        this.tackShooterButton.setBounds(22 * Map.TILE_SIZE - Map.TILE_SIZE / 2,7 * Map.TILE_SIZE,64,64);

        this.mortarButton = new JButton();
        this.mortarButton.setIcon(new ImageIcon(towerManager.getTower("Mortar").getSprite()));
        this.mortarButton.setBounds(24 * Map.TILE_SIZE - Map.TILE_SIZE / 2,7 * Map.TILE_SIZE,64,64);
    }

    public void addNotify(JPanel panel) {
        panel.addMouseMotionListener(this);
        panel.addMouseListener(this);
        this.attackMageButton.addActionListener(this);
        this.supportMageButton.addActionListener(this);
        this.warriorButton.addActionListener(this);
        this.catapultButton.addActionListener(this);
        this.tackShooterButton.addActionListener(this);
        this.mortarButton.addActionListener(this);
        panel.setLayout(null);
        panel.add(this.attackMageButton);
        panel.add(this.supportMageButton);
        panel.add(this.warriorButton);
        panel.add(this.catapultButton);
        panel.add(this.tackShooterButton);
        panel.add(this.mortarButton);
    }

    public void init() {
        this.displayTower = null;
        this.selectedTower = null;
        this.isValid = true;
    }

    public void paint(Graphics2D g) {
        if(displayTower != null) {
            displayTower.paint(g, isValid, true);
        }
        if(selectedTower != null) {
            selectedTower.paint(g, isValid, true);
        }
        Rectangle2D.Double UI = new Rectangle2D.Double(Map.MAP_WIDTH * Map.TILE_SIZE, 0, UI_WIDTH, UI_HEIGHT);
        g.draw(UI);
        g.setPaint(Color.DARK_GRAY);
        g.fill(UI);

        g.setPaint(Color.white);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.drawString("Health: " + this.gameState.getHealth(), Map.TILE_SIZE / 4, Map.TILE_SIZE / 2);
        g.drawString("Money: " + this.gameState.getMoney(), 2 * Map.TILE_SIZE, Map.TILE_SIZE / 2);
        g.drawString("Wave: " + gameState.getWave() + " / " + gameState.MAX_WAVE, 19 * Map.TILE_SIZE, Map.TILE_SIZE / 2);
        g.drawString("$" + towerManager.getTower("AttackMage").getCost(), 22 * Map.TILE_SIZE - 25,3 * Map.TILE_SIZE - 5);
        g.drawString("$" + towerManager.getTower("SupportMage").getCost(), 24 * Map.TILE_SIZE - 20,3 * Map.TILE_SIZE - 5);
        g.drawString("$" + towerManager.getTower("Warrior").getCost(), 22 * Map.TILE_SIZE - 20,5 * Map.TILE_SIZE - 5);
        g.drawString("$" + towerManager.getTower("Catapult").getCost(), 24 * Map.TILE_SIZE - 20,5 * Map.TILE_SIZE - 5);
        g.drawString("$" + towerManager.getTower("TackShooter").getCost(), 22 * Map.TILE_SIZE - 20,7 * Map.TILE_SIZE - 5);
        g.drawString("$" + towerManager.getTower("Mortar").getCost(), 24 * Map.TILE_SIZE - 20,7 * Map.TILE_SIZE - 5);
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
            this.gameState.subtractMoney(displayTower.getCost());
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
            Tower temp = towerManager.getTower("AttackMage");
            if(this.gameState.getMoney() >= temp.getCost()) {
                this.displayTower = temp.copy(new IntCoord(Map.MAP_WIDTH * Map.TILE_SIZE, 0));
                this.selectedTower = null;
            }
        }

        if(e.getSource() == this.supportMageButton) {
            Tower temp = towerManager.getTower("SupportMage");
            if(this.gameState.getMoney() >= temp.getCost()) {
                this.displayTower = temp.copy(new IntCoord(Map.MAP_WIDTH * Map.TILE_SIZE, 0));
                this.selectedTower = null;
            }
        }

        if(e.getSource() == this.warriorButton) {
            Tower temp = towerManager.getTower("Warrior");
            if(this.gameState.getMoney() >= temp.getCost()) {
                this.displayTower = temp.copy(new IntCoord(Map.MAP_WIDTH * Map.TILE_SIZE, 0));
                this.selectedTower = null;
            }
        }

        if(e.getSource() == this.catapultButton) {
            Tower temp = towerManager.getTower("Catapult");
            if(this.gameState.getMoney() >= temp.getCost()) {
                this.displayTower = temp.copy(new IntCoord(Map.MAP_WIDTH * Map.TILE_SIZE, 0));
                this.selectedTower = null;
            }
        }

        if(e.getSource() == this.tackShooterButton) {
            Tower temp = towerManager.getTower("TackShooter");
            if(this.gameState.getMoney() >= temp.getCost()) {
                this.displayTower = temp.copy(new IntCoord(Map.MAP_WIDTH * Map.TILE_SIZE, 0));
                this.selectedTower = null;
            }
        }

        if(e.getSource() == this.mortarButton) {
            Tower temp = towerManager.getTower("Mortar");
            if(this.gameState.getMoney() >= temp.getCost()) {
                this.displayTower = temp.copy(new IntCoord(Map.MAP_WIDTH * Map.TILE_SIZE, 0));
                this.selectedTower = null;
            }
        }
    }
}
