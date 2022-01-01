package main;

import main.tower.*;

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
    private boolean retarget;

    private final TowerManager towerManager;
    private final GameState gameState;
    private final Map map;

    JButton attackMageButton;
    JButton supportMageButton;
    JButton warriorButton;
    JButton catapultButton;
    JButton tackShooterButton;
    JButton mortarButton;
    JButton farmButton;
    JButton ballistaButton;
    JButton mortarRetargetButton;

    private int moneyShowTimer;
    private int moneyGain;

    public UI(Map map, TowerManager towerManager, GameState gameState) {
        this.towerManager = towerManager;
        this.gameState = gameState;
        this.map = map;
        this.retarget = false;

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

        this.farmButton = new JButton();
        this.farmButton.setIcon(new ImageIcon(towerManager.getTower("Farm").getSprite()));
        this.farmButton.setBounds(22 * Map.TILE_SIZE - Map.TILE_SIZE / 2,9 * Map.TILE_SIZE,64,64);

        this.ballistaButton = new JButton();
        this.ballistaButton.setIcon(new ImageIcon(towerManager.getTower("Ballista").getSprite()));
        this.ballistaButton.setBounds(24 * Map.TILE_SIZE - Map.TILE_SIZE / 2,9 * Map.TILE_SIZE,64,64);

        this.mortarRetargetButton = new JButton("Target Mortar");
        this.mortarRetargetButton.setBounds(22 * Map.TILE_SIZE - Map.TILE_SIZE / 2,11 * Map.TILE_SIZE,192,64);

        this.moneyShowTimer = 0;
        this.moneyGain = 0;
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
        this.farmButton.addActionListener(this);
        this.ballistaButton.addActionListener(this);
        this.mortarRetargetButton.addActionListener(this);
        panel.setLayout(null);
        panel.add(this.attackMageButton);
        panel.add(this.supportMageButton);
        panel.add(this.warriorButton);
        panel.add(this.catapultButton);
        panel.add(this.tackShooterButton);
        panel.add(this.mortarButton);
        panel.add(this.farmButton);
        panel.add(this.ballistaButton);
        panel.add(this.mortarRetargetButton);
        mortarRetargetButton.setVisible(false);
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
        g.drawString("Money: $" + this.gameState.getMoney(), 2 * Map.TILE_SIZE, Map.TILE_SIZE / 2);
        g.drawString("Wave: " + gameState.getWave() + " / " + gameState.MAX_WAVE, 19 * Map.TILE_SIZE, Map.TILE_SIZE / 2);
        g.drawString("$" + towerManager.getTower("AttackMage").getCost(), 22 * Map.TILE_SIZE - 25,3 * Map.TILE_SIZE - 5);
        g.drawString("$" + towerManager.getTower("SupportMage").getCost(), 24 * Map.TILE_SIZE - 20,3 * Map.TILE_SIZE - 5);
        g.drawString("$" + towerManager.getTower("Warrior").getCost(), 22 * Map.TILE_SIZE - 20,5 * Map.TILE_SIZE - 5);
        g.drawString("$" + towerManager.getTower("Catapult").getCost(), 24 * Map.TILE_SIZE - 20,5 * Map.TILE_SIZE - 5);
        g.drawString("$" + towerManager.getTower("TackShooter").getCost(), 22 * Map.TILE_SIZE - 20,7 * Map.TILE_SIZE - 5);
        g.drawString("$" + towerManager.getTower("Mortar").getCost(), 24 * Map.TILE_SIZE - 20,7 * Map.TILE_SIZE - 5);
        g.drawString("$" + towerManager.getTower("Farm").getCost(), 22 * Map.TILE_SIZE - 20,9 * Map.TILE_SIZE - 5);
        g.drawString("$" + towerManager.getTower("Ballista").getCost(), 24 * Map.TILE_SIZE - 20,9 * Map.TILE_SIZE - 5);
        if(moneyShowTimer > 0 && moneyGain > 0) {
            g.drawString("+$" + moneyGain, 3 * Map.TILE_SIZE, Map.TILE_SIZE);
            moneyShowTimer--;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (e.getX() / Map.TILE_SIZE >= 0 && e.getX() / Map.TILE_SIZE < Map.MAP_WIDTH && e.getY() / Map.TILE_SIZE >= 0 && e.getY() / Map.TILE_SIZE < Map.MAP_HEIGHT) {
            if(displayTower != null) {
                isValid = map.getMap()[e.getX() / Map.TILE_SIZE][e.getY() / Map.TILE_SIZE] == 0;
                displayTower.setTileLocation(new IntCoord(e.getX() / Map.TILE_SIZE, e.getY() / Map.TILE_SIZE));
            }
            this.towerManager.setMousePosition(e);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX() / Map.TILE_SIZE >= 0 && e.getX() / Map.TILE_SIZE < Map.MAP_WIDTH && e.getY() / Map.TILE_SIZE >= 0 && e.getY() / Map.TILE_SIZE < Map.MAP_HEIGHT && this.displayTower == null) {
            if(selectedTower instanceof MortarTower && retarget) {
                ((MortarTower) selectedTower).setTargetPosition(new DoubleCoord(e.getX(), e.getY()));
            }
            else {
                selectedTower = towerManager.findSelectedTower(new IntCoord(e.getX() / Map.TILE_SIZE, e.getY() / Map.TILE_SIZE));
                if(selectedTower instanceof MortarTower) {
                    mortarRetargetButton.setVisible(true);
                    retarget = false;
                }
                else if(selectedTower instanceof FarmTower) {
                    gameState.addMoney(((FarmTower) selectedTower).getStoredMoney());
                    moneyGain = ((FarmTower) selectedTower).getStoredMoney();
                    ((FarmTower) selectedTower).resetStoredMoney();
                    moneyShowTimer = Game.FPS;
                    mortarRetargetButton.setVisible(false);
                }
                else {
                    mortarRetargetButton.setVisible(false);
                }
            }
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

        if(e.getSource() == this.farmButton) {
            Tower temp = towerManager.getTower("Farm");
            if(this.gameState.getMoney() >= temp.getCost()) {
                this.displayTower = temp.copy(new IntCoord(Map.MAP_WIDTH * Map.TILE_SIZE, 0));
                this.selectedTower = null;
            }
        }

        if(e.getSource() == this.ballistaButton) {
            Tower temp = towerManager.getTower("Ballista");
            if(this.gameState.getMoney() >= temp.getCost()) {
                this.displayTower = temp.copy(new IntCoord(Map.MAP_WIDTH * Map.TILE_SIZE, 0));
                this.selectedTower = null;
            }
        }

        if(e.getSource() == this.mortarRetargetButton) {
            this.retarget = !this.retarget;
        }
        mortarRetargetButton.setVisible(this.selectedTower instanceof MortarTower);
    }
}
