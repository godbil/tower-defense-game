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
        this.isValid = false;
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
        g.drawString("Wave: " + gameState.getWave() + " / " + gameState.getMaxWave(), 19 * Map.TILE_SIZE, Map.TILE_SIZE / 2);
        g.drawString("$" + Math.round(towerManager.getTower("AttackMage").getCost() * gameState.getMoneyMultiplier()), 22 * Map.TILE_SIZE - 25,3 * Map.TILE_SIZE - 5);
        g.drawString("$" + Math.round(towerManager.getTower("SupportMage").getCost() * gameState.getMoneyMultiplier()), 24 * Map.TILE_SIZE - 20,3 * Map.TILE_SIZE - 5);
        g.drawString("$" + Math.round(towerManager.getTower("Warrior").getCost() * gameState.getMoneyMultiplier()), 22 * Map.TILE_SIZE - 20,5 * Map.TILE_SIZE - 5);
        g.drawString("$" + Math.round(towerManager.getTower("Catapult").getCost() * gameState.getMoneyMultiplier()), 24 * Map.TILE_SIZE - 20,5 * Map.TILE_SIZE - 5);
        g.drawString("$" + Math.round(towerManager.getTower("TackShooter").getCost() * gameState.getMoneyMultiplier()), 22 * Map.TILE_SIZE - 20,7 * Map.TILE_SIZE - 5);
        g.drawString("$" + Math.round(towerManager.getTower("Mortar").getCost() * gameState.getMoneyMultiplier()), 24 * Map.TILE_SIZE - 20,7 * Map.TILE_SIZE - 5);
        g.drawString("$" + Math.round(towerManager.getTower("Farm").getCost() * gameState.getMoneyMultiplier()), 22 * Map.TILE_SIZE - 20,9 * Map.TILE_SIZE - 5);
        g.drawString("$" + Math.round(towerManager.getTower("Ballista").getCost() * gameState.getMoneyMultiplier()), 24 * Map.TILE_SIZE - 20,9 * Map.TILE_SIZE - 5);
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
        else if(displayTower != null) {
            isValid = false;
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
            isValid = true;
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
        Tower temp = null;
        if(e.getSource() == this.attackMageButton) {
            temp = towerManager.getTower("AttackMage");
        }
        else if(e.getSource() == this.supportMageButton) {
            temp = towerManager.getTower("SupportMage");
        }
        else if(e.getSource() == this.warriorButton) {
            temp = towerManager.getTower("Warrior");
        }
        else if(e.getSource() == this.catapultButton) {
            temp = towerManager.getTower("Catapult");
        }
        else if(e.getSource() == this.tackShooterButton) {
            temp = towerManager.getTower("TackShooter");
        }
        else if(e.getSource() == this.mortarButton) {
            temp = towerManager.getTower("Mortar");
        }
        else if(e.getSource() == this.farmButton) {
            temp = towerManager.getTower("Farm");
        }
        else if(e.getSource() == this.ballistaButton) {
            temp = towerManager.getTower("Ballista");
        }

        if(temp != null && this.gameState.getMoney() >= Math.round(temp.getCost() * gameState.getMoneyMultiplier())) {
            this.displayTower = temp.copy(new IntCoord(Map.MAP_WIDTH * Map.TILE_SIZE, 0), gameState.getMoneyMultiplier());
            this.selectedTower = null;
        }

        if(e.getSource() == this.mortarRetargetButton) {
            this.retarget = !this.retarget;
        }
        mortarRetargetButton.setVisible(this.selectedTower instanceof MortarTower);
    }
}
