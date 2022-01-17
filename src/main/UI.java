package main;

import main.tower.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

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
    JButton cowButton;
    JButton mortarRetargetButton;
    JButton upgradeButton1;
    JButton upgradeButton2;
    JButton sellButton;
    JButton mainMenuButton;
    JButton restartButton;

    JTextArea upgradeDescription1;
    JTextArea upgradeDescription2;

    private int moneyShowTimer;
    private int moneyGain;

    private BufferedImage victory;
    private BufferedImage defeat;

    public UI(Map map, TowerManager towerManager, GameState gameState) {
        this.towerManager = towerManager;
        this.gameState = gameState;
        this.map = map;
        this.retarget = false;


        try {
            victory = ImageIO.read(new File("assets/sprites/victory.png"));
            defeat = ImageIO.read(new File("assets/sprites/defeat.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

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

        this.cowButton = new JButton();
        this.cowButton.setIcon(new ImageIcon(towerManager.getTower("FarmCow").getSprite()));
        this.cowButton.setBounds(21 * Map.TILE_SIZE - Map.TILE_SIZE / 2,12 * Map.TILE_SIZE,1,1);

        this.mortarRetargetButton = new JButton("Target Mortar");
        this.mortarRetargetButton.setBounds(22 * Map.TILE_SIZE - Map.TILE_SIZE / 2, Map.TILE_SIZE,192,64);

        this.upgradeButton1 = new JButton();
        this.upgradeButton1.setBounds(22 * Map.TILE_SIZE,3 * Map.TILE_SIZE,128,128);

        this.upgradeButton2 = new JButton();
        this.upgradeButton2.setBounds(22 * Map.TILE_SIZE,7 * Map.TILE_SIZE,128,128);

        this.sellButton = new JButton("Sell");
        this.sellButton.setBounds(22 * Map.TILE_SIZE,12 * Map.TILE_SIZE,128,64);

        this.mainMenuButton = new JButton("Main Menu");
        this.mainMenuButton.setBounds(0, 12 * Map.TILE_SIZE, 128, 64);

        this.restartButton = new JButton("Restart");
        this.restartButton.setBounds(14 * Map.TILE_SIZE,9 * Map.TILE_SIZE,128,64);

        this.upgradeDescription1 = new JTextArea();
        this.upgradeDescription1.setBounds(21 * Map.TILE_SIZE + 5,5 * Map.TILE_SIZE + 10, Map.TILE_SIZE * 4 - 10, 60);
        this.upgradeDescription1.setFont(new Font("TimesRoman", Font.PLAIN, 12));

        this.upgradeDescription2 = new JTextArea();
        this.upgradeDescription2.setBounds(21 * Map.TILE_SIZE + 5,9 * Map.TILE_SIZE + 10, Map.TILE_SIZE * 4 - 10, 60);
        this.upgradeDescription2.setFont(new Font("TimesRoman", Font.PLAIN, 12));

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
        this.cowButton.addActionListener(this);
        this.mortarRetargetButton.addActionListener(this);
        this.upgradeButton1.addActionListener(this);
        this.upgradeButton2.addActionListener(this);
        this.sellButton.addActionListener(this);
        this.mainMenuButton.addActionListener(this);
        this.restartButton.addActionListener(this);
        panel.setLayout(null);
        panel.add(this.attackMageButton);
        panel.add(this.supportMageButton);
        panel.add(this.warriorButton);
        panel.add(this.catapultButton);
        panel.add(this.tackShooterButton);
        panel.add(this.mortarButton);
        panel.add(this.farmButton);
        panel.add(this.ballistaButton);
        panel.add(this.cowButton);
        panel.add(this.mortarRetargetButton);
        panel.add(this.upgradeButton1);
        panel.add(this.upgradeButton2);
        panel.add(this.upgradeDescription1);
        panel.add(this.upgradeDescription2);
        panel.add(this.sellButton);
        panel.add(this.mainMenuButton);
        panel.add(this.restartButton);
        this.mortarRetargetButton.setVisible(false);
        this.upgradeButton1.setVisible(false);
        this.upgradeButton2.setVisible(false);
        this.sellButton.setVisible(false);
        this.restartButton.setVisible(false);
        this.upgradeDescription1.setVisible(false);
        this.upgradeDescription2.setVisible(false);
        this.upgradeDescription1.setLineWrap(true);
        this.upgradeDescription2.setLineWrap(true);
    }

    public void removeNotify(JPanel panel) {
        panel.removeMouseMotionListener(this);
        panel.removeMouseListener(this);
        this.attackMageButton.removeActionListener(this);
        this.supportMageButton.removeActionListener(this);
        this.warriorButton.removeActionListener(this);
        this.catapultButton.removeActionListener(this);
        this.tackShooterButton.removeActionListener(this);
        this.mortarButton.removeActionListener(this);
        this.farmButton.removeActionListener(this);
        this.ballistaButton.removeActionListener(this);
        this.cowButton.removeActionListener(this);
        this.mortarRetargetButton.removeActionListener(this);
        this.upgradeButton1.removeActionListener(this);
        this.upgradeButton2.removeActionListener(this);
        this.sellButton.removeActionListener(this);
        this.mainMenuButton.removeActionListener(this);
        this.restartButton.removeActionListener(this);
        panel.remove(this.attackMageButton);
        panel.remove(this.supportMageButton);
        panel.remove(this.warriorButton);
        panel.remove(this.catapultButton);
        panel.remove(this.tackShooterButton);
        panel.remove(this.mortarButton);
        panel.remove(this.farmButton);
        panel.remove(this.ballistaButton);
        panel.remove(this.cowButton);
        panel.remove(this.mortarRetargetButton);
        panel.remove(this.upgradeButton1);
        panel.remove(this.upgradeButton2);
        panel.remove(this.upgradeDescription1);
        panel.remove(this.upgradeDescription2);
        panel.remove(this.sellButton);
        panel.remove(this.mainMenuButton);
        panel.remove(this.restartButton);
    }

    public void init() {
        this.displayTower = null;
        this.selectedTower = null;
        this.isValid = false;
        moneyShowTimer = 0;
        moneyGain = 0;
    }

    public void paint(Graphics2D g) {
        if(displayTower != null) {
            displayTower.paint(g, isValid, true);
        }
        if(selectedTower != null) {
            selectedTower.paint(g, isValid, true);
        }
        Rectangle2D.Double UI = new Rectangle2D.Double(Map.MAP_WIDTH * Map.TILE_SIZE, 0, UI_WIDTH, UI_HEIGHT);
        g.setPaint(Color.DARK_GRAY);
        g.draw(UI);
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
            System.out.println("has");
            g.drawString("+$" + moneyGain, 3 * Map.TILE_SIZE, Map.TILE_SIZE);
            moneyShowTimer--;
        }
        if(selectedTower != null) {
            Rectangle2D.Double towerMenu = new Rectangle2D.Double(Map.MAP_WIDTH * Map.TILE_SIZE, 0, UI_WIDTH, UI_HEIGHT);
            g.setPaint(Color.DARK_GRAY);
            g.draw(towerMenu);
            g.fill(towerMenu);
            this.attackMageButton.setVisible(false);
            this.supportMageButton.setVisible(false);
            this.warriorButton.setVisible(false);
            this.catapultButton.setVisible(false);
            this.tackShooterButton.setVisible(false);
            this.mortarButton.setVisible(false);
            this.farmButton.setVisible(false);
            this.ballistaButton.setVisible(false);
            this.cowButton.setVisible(false);
            this.upgradeButton1.setVisible(true);
            this.upgradeButton2.setVisible(true);
            this.sellButton.setVisible(true);
            this.upgradeDescription1.setVisible(true);
            this.upgradeDescription2.setVisible(true);
            if(selectedTower.getUpgradePath().size() > 1) {
                this.upgradeButton1.setIcon(new ImageIcon(towerManager.getTower(selectedTower.getUpgradePath().get(0)).getSprite()));
                this.upgradeButton2.setIcon(new ImageIcon(towerManager.getTower(selectedTower.getUpgradePath().get(1)).getSprite()));
                g.setPaint(Color.white);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                g.drawString("$" + Math.round(towerManager.getTower(selectedTower.getUpgradePath().get(0)).getCost() * gameState.getMoneyMultiplier()), 23 * Map.TILE_SIZE - 20,3 * Map.TILE_SIZE - 10);
                g.drawString("$" + Math.round(towerManager.getTower(selectedTower.getUpgradePath().get(1)).getCost() * gameState.getMoneyMultiplier()), 23 * Map.TILE_SIZE - 20,7 * Map.TILE_SIZE - 10);
                upgradeDescription1.setText(towerManager.getDescription(selectedTower.getUpgradePath().get(0)));
                upgradeDescription2.setText(towerManager.getDescription(selectedTower.getUpgradePath().get(1)));
                this.upgradeButton1.setVisible(true);
                this.upgradeButton2.setVisible(true);
                this.upgradeDescription1.setVisible(true);
                this.upgradeDescription2.setVisible(true);
            }
            else if(selectedTower.getUpgradePath().size() > 0){
                this.upgradeButton1.setIcon(new ImageIcon(towerManager.getTower(selectedTower.getUpgradePath().get(0)).getSprite()));
                g.setPaint(Color.white);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                g.drawString("$" + Math.round(towerManager.getTower(selectedTower.getUpgradePath().get(0)).getCost() * gameState.getMoneyMultiplier()), 23 * Map.TILE_SIZE - 20,3 * Map.TILE_SIZE - 10);
                upgradeDescription1.setText(towerManager.getDescription(selectedTower.getUpgradePath().get(0)));
                this.upgradeButton1.setVisible(true);
                this.upgradeButton2.setVisible(false);
                this.upgradeDescription1.setVisible(true);
                this.upgradeDescription2.setVisible(false);
            }
            else{
                this.upgradeButton1.setVisible(false);
                this.upgradeButton2.setVisible(false);
                this.upgradeDescription1.setVisible(false);
                this.upgradeDescription2.setVisible(false);
            }
        }
        else{
            this.attackMageButton.setVisible(true);
            this.supportMageButton.setVisible(true);
            this.warriorButton.setVisible(true);
            this.catapultButton.setVisible(true);
            this.tackShooterButton.setVisible(true);
            this.mortarButton.setVisible(true);
            this.farmButton.setVisible(true);
            this.ballistaButton.setVisible(true);
            this.cowButton.setVisible(true);
            this.upgradeButton1.setVisible(false);
            this.upgradeButton2.setVisible(false);
            this.upgradeDescription1.setVisible(false);
            this.upgradeDescription2.setVisible(false);
            this.sellButton.setVisible(false);
        }

        if(gameState.isGameOver()) {
            if(gameState.getHealth() <= 0) {
                this.mainMenuButton.setBounds(5 * Map.TILE_SIZE,9 * Map.TILE_SIZE,128,64);
                this.restartButton.setVisible(true);
                Rectangle2D.Double end = new Rectangle2D.Double(3 * Map.TILE_SIZE, 3 * Map.TILE_SIZE, 15 * Map.TILE_SIZE, 7 * Map.TILE_SIZE);
                Rectangle2D.Double round = new Rectangle2D.Double(5 * Map.TILE_SIZE, 5 * Map.TILE_SIZE, 11 * Map.TILE_SIZE, 3 * Map.TILE_SIZE);
                g.setPaint(Color.GRAY);
                g.draw(end);
                g.fill(end);
                g.setPaint(Color.DARK_GRAY);
                g.draw(round);
                g.fill(round);
                g.drawImage(defeat, 8 * Map.TILE_SIZE, 3 * Map.TILE_SIZE + Map.TILE_SIZE / 2, null);
                g.setPaint(Color.white);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 60));
                g.drawString("You lost on: Wave " + gameState.getWave(), 6 * Map.TILE_SIZE, 7 * Map.TILE_SIZE);
            }
            else{
                this.mainMenuButton.setBounds(5 * Map.TILE_SIZE,9 * Map.TILE_SIZE,128,64);
                this.restartButton.setVisible(true);
                Rectangle2D.Double end = new Rectangle2D.Double(3 * Map.TILE_SIZE, 3 * Map.TILE_SIZE, 15 * Map.TILE_SIZE, 7 * Map.TILE_SIZE);
                Rectangle2D.Double round = new Rectangle2D.Double(5 * Map.TILE_SIZE, 5 * Map.TILE_SIZE, 11 * Map.TILE_SIZE, 3 * Map.TILE_SIZE);
                g.setPaint(Color.GRAY);
                g.draw(end);
                g.fill(end);
                g.setPaint(Color.DARK_GRAY);
                g.draw(round);
                g.fill(round);
                g.drawImage(victory, 7 * Map.TILE_SIZE + 30, 3 * Map.TILE_SIZE + Map.TILE_SIZE / 2, null);
                g.setPaint(Color.white);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 60));
                g.drawString("Congratulation! You won!", 5 * Map.TILE_SIZE + 10, 7 * Map.TILE_SIZE);
            }
        }
        else {
            this.mainMenuButton.setBounds(0, 12 * Map.TILE_SIZE, 128, 64);
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
        else if(e.getSource() == this.cowButton) {
            temp = towerManager.getTower("FarmCow");
        }

        if(temp != null && this.gameState.getMoney() >= Math.round(temp.getCost() * gameState.getMoneyMultiplier())) {
            this.displayTower = temp.copy(new IntCoord(Map.MAP_WIDTH * Map.TILE_SIZE, 0), gameState.getMoneyMultiplier());
            this.selectedTower = null;
        }

        if(e.getSource() == this.mortarRetargetButton) {
            this.retarget = !this.retarget;
        }
        mortarRetargetButton.setVisible(this.selectedTower instanceof MortarTower);

        if(e.getSource() == this.upgradeButton1) {
            if(selectedTower != null && this.gameState.getMoney() >= Math.round(towerManager.getTower(selectedTower.getUpgradePath().get(0)).getCost() * gameState.getMoneyMultiplier())) {
                towerManager.upgrade(selectedTower, 0, gameState.getMoneyMultiplier());
                this.gameState.subtractMoney((int) Math.round(towerManager.getTower(selectedTower.getUpgradePath().get(0)).getCost() * gameState.getMoneyMultiplier()));
                selectedTower = null;
            }
        }
        if(e.getSource() == this.upgradeButton2) {
            if(selectedTower != null && selectedTower.getUpgradePath().size() > 1) {
                if(this.gameState.getMoney() >= Math.round(towerManager.getTower(selectedTower.getUpgradePath().get(1)).getCost() * gameState.getMoneyMultiplier())) {
                    towerManager.upgrade(selectedTower, 1, gameState.getMoneyMultiplier());
                    this.gameState.subtractMoney((int) Math.round(towerManager.getTower(selectedTower.getUpgradePath().get(1)).getCost() * gameState.getMoneyMultiplier()));
                    selectedTower = null;
                }
            }
        }

        if(e.getSource() == this.sellButton) {
            map.getMap()[selectedTower.getTileLocation().x][selectedTower.getTileLocation().y] = 0;
            towerManager.sell(selectedTower);
            gameState.addMoney((int)Math.round(selectedTower.getTotalCost() * 0.7));
            selectedTower = null;
        }

        if(e.getSource() == this.mainMenuButton) {
            gameState.setGameOver(false);
            gameState.setState(0);
        }
        if(e.getSource() == this.restartButton) {
            gameState.setGameOver(false);
            gameState.setState(1);
        }
    }
}
