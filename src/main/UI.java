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

public class UI implements ActionListener, MouseMotionListener, MouseListener {
    // The dimensions for the UI in pixels
    public static final int UI_WIDTH = 256;
    public static final int UI_HEIGHT = 832;

    // Creates a display tower for when the player clicks the tower button
    private Tower displayTower;
    // The tower which is selected by the player
    private Tower selectedTower;
    // Boolean which checks whether the player's placement is valid
    private boolean isValid;
    // Checks whether the player is retargeting their mortar
    private boolean retarget;

    // Creates towerManager, gameState and Map objects which gets passed in from the constructor
    private final TowerManager towerManager;
    private final GameState gameState;
    private final Map map;

    // All of the buttons that are needed for the tower shop
    JButton attackMageButton;
    JButton supportMageButton;
    JButton warriorButton;
    JButton catapultButton;
    JButton tackShooterButton;
    JButton mortarButton;
    JButton farmButton;
    JButton ballistaButton;
    JButton cowButton;

    // All of the buttons that are in the tower's ui like upgrades, sell and the mortar's retarget button
    JButton mortarRetargetButton;
    JButton upgradeButton1;
    JButton upgradeButton2;
    JButton sellButton;

    // Buttons for the end screen which either replays the game or sends the user back to the main menu
    JButton mainMenuButton;
    JButton restartButton;

    // Text areas for holding the descriptions of each upgrade
    JTextArea upgradeDescription1;
    JTextArea upgradeDescription2;

    // Integers for the farm tower which is the timer of how long the visual should be shown for and the amount of money that is collected
    private int moneyShowTimer;
    private int moneyGain;

    // The images for the custom font victory or defeat title
    private BufferedImage victory;
    private BufferedImage defeat;

    public UI(Map map, TowerManager towerManager, GameState gameState) {
        // Initializes the towerManager, gameState and map from the parameter
        this.towerManager = towerManager;
        this.gameState = gameState;
        this.map = map;
        // Initializes retarget to false
        this.retarget = false;


        try {
            // Gets the two images for the victory and defeat titles
            victory = ImageIO.read(new File("assets/sprites/victory.png"));
            defeat = ImageIO.read(new File("assets/sprites/defeat.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // Initializes all of the buttons that are needed, either setting their icon or giving their text and then setting their position
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

    }

    /* Used to add listeners and to add the buttons to the actual panel
     * Pre: Takes in a JPanel object to add buttons to
     * Post: Doesn't return anything
     */
    public void addNotify(JPanel panel) {
        // Adds a mouseListener for mouse clicking and mouseMotionListener for mouse movement
        panel.addMouseMotionListener(this);
        panel.addMouseListener(this);

        // Adding actionListeners for all of my buttons
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

        // Sets the layout to null so that we can use setBounds
        panel.setLayout(null);

        // Adds all of the buttons to the panel that is passed in
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

        // Setting the buttons that don't need to be seen right away to false
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

    /* Used to remove listeners, mainly so that actionListeners are not added twice and to remove the buttons to the actual panel
     * Pre: Takes in a JPanel object to remove buttons from
     * Post: Doesn't return anything
     */
    public void removeNotify(JPanel panel) {
        // Removing mouseMotionListener and mouseListener so there won't be multiple which would cause double clicking and other problems
        panel.removeMouseMotionListener(this);
        panel.removeMouseListener(this);

        // Removing actionListeners from all of the buttons
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

        // Removing buttons from the panel that is passed in
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

    /* Used to initialize variables and objects
     * Pre: Doesn't take in any parameters
     * Post: Doesn't return anything
     */
    public void init() {
        this.displayTower = null;
        this.selectedTower = null;
        this.isValid = false;
        moneyShowTimer = 0;
        moneyGain = 0;
    }

    /* Used to paint images, shapes and strings
     * Pre: Takes in a Graphics2D object which is used to paint things
     * Post: Doesn't return anything
     */
    public void paint(Graphics2D g) {
        // If the display tower isn't null then the display tower is painted
        if(displayTower != null) {
            displayTower.paint(g, isValid, true);
        }
        // If the selected tower isn't null then it's painted
        if(selectedTower != null) {
            selectedTower.paint(g, isValid, true);
        }
        // Creates, draws and fills the main UI background
        Rectangle2D.Double UI = new Rectangle2D.Double(Map.MAP_WIDTH * Map.TILE_SIZE, 0, UI_WIDTH, UI_HEIGHT);
        g.setPaint(Color.DARK_GRAY);
        g.draw(UI);
        g.fill(UI);

        // Sets the colour and font to draw the strings that are needed in the UI
        g.setPaint(Color.white);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        // The health, money and wave that the player has
        g.drawString("Health: " + this.gameState.getHealth(), Map.TILE_SIZE / 4, Map.TILE_SIZE / 2);
        g.drawString("Money: $" + this.gameState.getMoney(), 2 * Map.TILE_SIZE, Map.TILE_SIZE / 2);
        g.drawString("Wave: " + gameState.getWave() + " / " + gameState.getMaxWave(), 19 * Map.TILE_SIZE, Map.TILE_SIZE / 2);
        // The prices of every tower
        g.drawString("$" + Math.round(towerManager.getTower("AttackMage").getCost() * gameState.getMoneyMultiplier()), 22 * Map.TILE_SIZE - 25,3 * Map.TILE_SIZE - 5);
        g.drawString("$" + Math.round(towerManager.getTower("SupportMage").getCost() * gameState.getMoneyMultiplier()), 24 * Map.TILE_SIZE - 20,3 * Map.TILE_SIZE - 5);
        g.drawString("$" + Math.round(towerManager.getTower("Warrior").getCost() * gameState.getMoneyMultiplier()), 22 * Map.TILE_SIZE - 20,5 * Map.TILE_SIZE - 5);
        g.drawString("$" + Math.round(towerManager.getTower("Catapult").getCost() * gameState.getMoneyMultiplier()), 24 * Map.TILE_SIZE - 20,5 * Map.TILE_SIZE - 5);
        g.drawString("$" + Math.round(towerManager.getTower("TackShooter").getCost() * gameState.getMoneyMultiplier()), 22 * Map.TILE_SIZE - 20,7 * Map.TILE_SIZE - 5);
        g.drawString("$" + Math.round(towerManager.getTower("Mortar").getCost() * gameState.getMoneyMultiplier()), 24 * Map.TILE_SIZE - 20,7 * Map.TILE_SIZE - 5);
        g.drawString("$" + Math.round(towerManager.getTower("Farm").getCost() * gameState.getMoneyMultiplier()), 22 * Map.TILE_SIZE - 20,9 * Map.TILE_SIZE - 5);
        g.drawString("$" + Math.round(towerManager.getTower("Ballista").getCost() * gameState.getMoneyMultiplier()), 24 * Map.TILE_SIZE - 20,9 * Map.TILE_SIZE - 5);

        // For the farm tower, if the money is being collected then it will paint this string
        if(moneyShowTimer > 0 && moneyGain > 0) {
            g.drawString("+$" + moneyGain, 3 * Map.TILE_SIZE, Map.TILE_SIZE);
            moneyShowTimer--;
        }
        // If the selected tower isn't null then it will draw the tower's upgrades menu
        if(selectedTower != null) {
            // Creates, draws and fills the tower's menu
            Rectangle2D.Double towerMenu = new Rectangle2D.Double(Map.MAP_WIDTH * Map.TILE_SIZE, 0, UI_WIDTH, UI_HEIGHT);
            g.setPaint(Color.DARK_GRAY);
            g.draw(towerMenu);
            g.fill(towerMenu);
            // Sets all of the tower purchasing buttons to false
            this.attackMageButton.setVisible(false);
            this.supportMageButton.setVisible(false);
            this.warriorButton.setVisible(false);
            this.catapultButton.setVisible(false);
            this.tackShooterButton.setVisible(false);
            this.mortarButton.setVisible(false);
            this.farmButton.setVisible(false);
            this.ballistaButton.setVisible(false);
            this.cowButton.setVisible(false);
            // Sets the buttons that can be seen in the tower's menu to true
            this.upgradeButton1.setVisible(true);
            this.upgradeButton2.setVisible(true);
            this.sellButton.setVisible(true);
            this.upgradeDescription1.setVisible(true);
            this.upgradeDescription2.setVisible(true);
            // If there is more than one possible upgrade path for the selected tower then it will show both buttons with their icons, costs and descriptions
            if(selectedTower.getUpgradePath().size() > 1) {
                // Setting the icons for both upgrade buttons
                this.upgradeButton1.setIcon(new ImageIcon(towerManager.getTower(selectedTower.getUpgradePath().get(0)).getSprite()));
                this.upgradeButton2.setIcon(new ImageIcon(towerManager.getTower(selectedTower.getUpgradePath().get(1)).getSprite()));
                // Setting font for the upgrade costs
                g.setPaint(Color.white);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                // Drawing both upgrade path's cost
                g.drawString("$" + Math.round(towerManager.getTower(selectedTower.getUpgradePath().get(0)).getCost() * gameState.getMoneyMultiplier()), 23 * Map.TILE_SIZE - 20,3 * Map.TILE_SIZE - 10);
                g.drawString("$" + Math.round(towerManager.getTower(selectedTower.getUpgradePath().get(1)).getCost() * gameState.getMoneyMultiplier()), 23 * Map.TILE_SIZE - 20,7 * Map.TILE_SIZE - 10);
                // Sets the text to the text area to both upgrades descriptions
                upgradeDescription1.setText(towerManager.getDescription(selectedTower.getUpgradePath().get(0)));
                upgradeDescription2.setText(towerManager.getDescription(selectedTower.getUpgradePath().get(1)));
                // Setting both buttons and text areas to visible
                this.upgradeButton1.setVisible(true);
                this.upgradeButton2.setVisible(true);
                this.upgradeDescription1.setVisible(true);
                this.upgradeDescription2.setVisible(true);
            }
            // If there is only one possible path for the selected tower then it will only show one button with its icon, cost and description
            else if(selectedTower.getUpgradePath().size() > 0){
                // Setting the icon for upgrade button
                this.upgradeButton1.setIcon(new ImageIcon(towerManager.getTower(selectedTower.getUpgradePath().get(0)).getSprite()));
                // Setting font for the upgrade cost
                g.setPaint(Color.white);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                // Drawing upgrade path's cost
                g.drawString("$" + Math.round(towerManager.getTower(selectedTower.getUpgradePath().get(0)).getCost() * gameState.getMoneyMultiplier()), 23 * Map.TILE_SIZE - 20,3 * Map.TILE_SIZE - 10);
                // Sets the text to the text area to the upgrade's description
                upgradeDescription1.setText(towerManager.getDescription(selectedTower.getUpgradePath().get(0)));
                // Setting only one text area and button to visible while the other is false
                this.upgradeButton1.setVisible(true);
                this.upgradeButton2.setVisible(false);
                this.upgradeDescription1.setVisible(true);
                this.upgradeDescription2.setVisible(false);
            }
            else{
                // If there are no available upgrade paths then everything's visibility is false
                this.upgradeButton1.setVisible(false);
                this.upgradeButton2.setVisible(false);
                this.upgradeDescription1.setVisible(false);
                this.upgradeDescription2.setVisible(false);
            }
        }
        else{
            // If selected tower is null then the tower purchase button will be set to visible again and the upgrades won't be visible
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

        // If the game is over then it will check if the health is less than 0, meaning defeat or if it's not less than 0 then it means victory
        if(gameState.isGameOver()) {
            // Sets the position of the main menu button to fit the end screen and sets the restart button to visible
            this.mainMenuButton.setBounds(5 * Map.TILE_SIZE,9 * Map.TILE_SIZE,128,64);
            this.restartButton.setVisible(true);
            // Draws two rectangles which make up the end screen window
            Rectangle2D.Double end = new Rectangle2D.Double(3 * Map.TILE_SIZE, 3 * Map.TILE_SIZE, 15 * Map.TILE_SIZE, 7 * Map.TILE_SIZE);
            Rectangle2D.Double round = new Rectangle2D.Double(5 * Map.TILE_SIZE, 5 * Map.TILE_SIZE, 11 * Map.TILE_SIZE, 3 * Map.TILE_SIZE);
            g.setPaint(Color.GRAY);
            g.draw(end);
            g.fill(end);
            g.setPaint(Color.DARK_GRAY);
            g.draw(round);
            g.fill(round);
            // Happens if the user lost
            if(gameState.getHealth() <= 0) {
                // Draws the defeat title for the end screen
                g.drawImage(defeat, 8 * Map.TILE_SIZE, 3 * Map.TILE_SIZE + Map.TILE_SIZE / 2, null);
                // Sets font and then draws the string telling the player which wave they lost on
                g.setPaint(Color.white);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 60));
                g.drawString("You lost on: Wave " + gameState.getWave(), 6 * Map.TILE_SIZE, 7 * Map.TILE_SIZE);
            }
            // Happens if the user won
            else{
                // Draws the victory title for the end screen
                g.drawImage(victory, 7 * Map.TILE_SIZE + 30, 3 * Map.TILE_SIZE + Map.TILE_SIZE / 2, null);
                // Sets font and then draws the string congratulating the player
                g.setPaint(Color.white);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 60));
                g.drawString("Congratulation! You won!", 5 * Map.TILE_SIZE + 10, 7 * Map.TILE_SIZE);
            }
        }
        else {
            // Resets the main menu button back to the bottom left corner
            this.mainMenuButton.setBounds(0, 12 * Map.TILE_SIZE, 128, 64);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    /* Used to check whenever the mouse is moved
     * Pre: Takes in a MouseEvent object
     * Post: Doesn't return anything
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        // Checks whether the mouse's position are in the map's boundaries and not in the UI
        if (e.getX() / Map.TILE_SIZE >= 0 && e.getX() / Map.TILE_SIZE < Map.MAP_WIDTH && e.getY() / Map.TILE_SIZE >= 0 && e.getY() / Map.TILE_SIZE < Map.MAP_HEIGHT) {
            if(displayTower != null) {
                // Changes isValid depending on if the current mouse position is placeable
                isValid = map.getMap()[e.getX() / Map.TILE_SIZE][e.getY() / Map.TILE_SIZE] == Map.PLACEABLE;
                // Sets the display tower's tile location each time the mouse is moved
                displayTower.setTileLocation(new IntCoord(e.getX() / Map.TILE_SIZE, e.getY() / Map.TILE_SIZE));
            }
            // Sets the mouse position for tower manager which is used for the ballista tower
            this.towerManager.setMousePosition(e);
        }
        else if(displayTower != null) {
            isValid = false;
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /* Used to check whether the mouse has been pressed
     * Pre: Takes in a MouseEvent object
     * Post: Doesn't return anything
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // Checks if the mouse position is in the boundaries and if there is no display tower
        if (e.getX() / Map.TILE_SIZE >= 0 && e.getX() / Map.TILE_SIZE < Map.MAP_WIDTH && e.getY() / Map.TILE_SIZE >= 0 && e.getY() / Map.TILE_SIZE < Map.MAP_HEIGHT && this.displayTower == null) {
            if(selectedTower instanceof MortarTower && retarget) {
                // If the selected tower is a mortar tower and the player has clicked the retarget button then the new mortar's target position is set to wherever the user clicks
                ((MortarTower) selectedTower).setTargetPosition(new DoubleCoord(e.getX(), e.getY()));
            }
            else {
                // Sets the selected tower to whichever tower the player has clicked on (could be null)
                selectedTower = towerManager.findSelectedTower(new IntCoord(e.getX() / Map.TILE_SIZE, e.getY() / Map.TILE_SIZE));
                if(selectedTower instanceof MortarTower) {
                    // If the selected tower is mortar but no retarget has been activated then it sets the retarget button to true
                    mortarRetargetButton.setVisible(true);
                    retarget = false;
                }
                else if(selectedTower instanceof FarmTower) {
                    // If the selected tower is farm then it will add the farm's stored money to the player's money
                    gameState.addMoney(((FarmTower) selectedTower).getStoredMoney());
                    // Sets the money gain to how much money the farm has so that it can be painted to show the amount the player gained
                    moneyGain = ((FarmTower) selectedTower).getStoredMoney();
                    // Resets the stored money
                    ((FarmTower) selectedTower).resetStoredMoney();
                    // Sets timer to 60 frames so the money gain will show for a second
                    moneyShowTimer = Game.FPS;
                    // Makes sure that the mortar button is false
                    mortarRetargetButton.setVisible(false);
                }
                else {
                    // Makes sure that the mortar button is false
                    mortarRetargetButton.setVisible(false);
                }
            }
        }
        // If the player clicks mouse button 3 then it cancels their purchase and display tower becomes null
        if(e.getButton() == MouseEvent.BUTTON3) {
            displayTower = null;
            isValid = true;
        }
    }

    /* Used to check when the player releases their mouse
     * Pre: Takes in a MouseEvent object
     * Post: Doesn't return anything
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if(displayTower != null && this.isValid) {
            // If there's a display tower and the player's mouse placement is valid then it places the display tower
            towerManager.place(map.getMap(), displayTower);
            // Subtracts the tower cost only if they actually place the tower
            this.gameState.subtractMoney(displayTower.getCost());
            // Sets display tower back to null after being placed
            displayTower = null;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /* Used to check whether the buttons have been pressed
     * Pre: Takes in a MouseEvent object
     * Post: Doesn't return anything
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Creates a temporary tower which display tower will be set to later
        Tower temp = null;
        // Checks whether the button press was from any of the tower purchase buttons and if so, set the temporary tower to the specifically clicked tower
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

        // Checks whether display tower is null or not and if the player has enough money (with the money multiplier)
        if(temp != null && this.gameState.getMoney() >= Math.round(temp.getCost() * gameState.getMoneyMultiplier())) {
            // Sets display tower to a copy of the temporary tower and makes the selected tower null if the player clicks one of those buttons
            this.displayTower = temp.copy(new IntCoord(Map.MAP_WIDTH * Map.TILE_SIZE, 0), gameState.getMoneyMultiplier());
            this.selectedTower = null;
        }

        // Checks if the button press is the mortar retarget button and if so, then it will set the retarget boolean to the opposite so if it was true then it is false and if it was false then it is true
        if(e.getSource() == this.mortarRetargetButton) {
            this.retarget = !this.retarget;
        }
        // Sets the mortar button to visible if the selected tower is a mortar
        mortarRetargetButton.setVisible(this.selectedTower instanceof MortarTower);

        // Checks if the button press is one of the upgrade buttons
        if(e.getSource() == this.upgradeButton1) {
            // If selected isn't null and the player has enough money then it will call the upgrade method from tower manager of path 0
            if(selectedTower != null && this.gameState.getMoney() >= Math.round(towerManager.getTower(selectedTower.getUpgradePath().get(0)).getCost() * gameState.getMoneyMultiplier())) {
                towerManager.upgrade(selectedTower, 0, gameState.getMoneyMultiplier());
                // Subtracts the money that is spend and then sets selected tower back to null
                this.gameState.subtractMoney((int) Math.round(towerManager.getTower(selectedTower.getUpgradePath().get(0)).getCost() * gameState.getMoneyMultiplier()));
                selectedTower = null;
            }
        }
        if(e.getSource() == this.upgradeButton2) {
            if(selectedTower != null && selectedTower.getUpgradePath().size() > 1) {
                // If selected isn't null and the player has enough money then it will call the upgrade method from tower manager of path 1
                if(this.gameState.getMoney() >= Math.round(towerManager.getTower(selectedTower.getUpgradePath().get(1)).getCost() * gameState.getMoneyMultiplier())) {
                    towerManager.upgrade(selectedTower, 1, gameState.getMoneyMultiplier());
                    // Subtracts the money that is spend and then sets selected tower back to null
                    this.gameState.subtractMoney((int) Math.round(towerManager.getTower(selectedTower.getUpgradePath().get(1)).getCost() * gameState.getMoneyMultiplier()));
                    selectedTower = null;
                }
            }
        }

        // Checks if the button press is one of the sell button
        if(e.getSource() == this.sellButton) {
            // Sets the map position of the selected tower to placeable and then calls the sell method from towerManager
            map.getMap()[selectedTower.getTileLocation().x][selectedTower.getTileLocation().y] = Map.PLACEABLE;
            towerManager.sell(selectedTower);
            // Adds 70% of the money back into the player's money count and sets selected tower back to null
            gameState.addMoney((int)Math.round(selectedTower.getTotalCost() * 0.7));
            selectedTower = null;
        }

        // Checks if the button press is main menu and if so, it sets the gameOver back to false and changes the state to 0 (menu)
        if(e.getSource() == this.mainMenuButton) {
            gameState.setGameOver(false);
            gameState.setState(0);
        }
        // Checks if the button press is replay and if so, it sets the gameOver back to false and changes the state to 1 (game) which will re-init everything
        if(e.getSource() == this.restartButton) {
            gameState.setGameOver(false);
            gameState.setState(1);
        }
    }
}
