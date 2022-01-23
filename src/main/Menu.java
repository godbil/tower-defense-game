package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Menu implements ActionListener {
    // Makes a gameState object
    private final GameState gameState;

    // Makes all of the buttons that are needed for the menu
    private JButton play;
    private JButton instructions;
    private JButton back;
    private JButton easyMap1;
    private JButton easyMap2;
    private JButton intMap;
    private JButton advMap;
    private JButton easy;
    private JButton normal;
    private JButton hard;
    private JButton impossible;

    // Booleans that are used to see which window they are in
    private boolean isInInstructions;
    private boolean isInMapSelect;
    private boolean isInDifficultySelect;

    // Images for the custom font words and map icons
    private BufferedImage background;
    private BufferedImage instructionTitleSprite;
    private BufferedImage mapSelectionTitleSprite;
    private BufferedImage difficultySelectionTitleSprite;
    private BufferedImage easyMap1Sprite;
    private BufferedImage easyMap2Sprite;
    private BufferedImage intMapSprite;
    private BufferedImage advMapSprite;

    // Strings that can be gotten and used to see which map or difficulty to play
    private String map;
    private String difficulty;

    public Menu(GameState gameState) {
        try {
            // Gets all of the images from the assets folder
            background = ImageIO.read(new File("assets/sprites/mainmenubackground.png"));
            instructionTitleSprite = ImageIO.read(new File("assets/sprites/instructions.png"));
            mapSelectionTitleSprite = ImageIO.read(new File("assets/sprites/mapselection.png"));
            difficultySelectionTitleSprite = ImageIO.read(new File("assets/sprites/difficultyselection.png"));
            easyMap1Sprite = ImageIO.read(new File("assets/sprites/easymap1icon.png"));
            easyMap2Sprite = ImageIO.read(new File("assets/sprites/easymap2icon.png"));
            intMapSprite = ImageIO.read(new File("assets/sprites/intmapicon.png"));
            advMapSprite = ImageIO.read(new File("assets/sprites/advmapicon.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // Constructor takes in a gameState object as a parameter
        this.gameState = gameState;

        // Sets the map and difficulty to empty strings
        this.map = "";
        this.difficulty = "";

        // Initializes all of the buttons with their respective text or icon and sets their position
        this.play = new JButton("PLAY");
        this.play.setBounds(12 * Map.TILE_SIZE - Map.TILE_SIZE / 2,8 * Map.TILE_SIZE,192,64);

        this.instructions = new JButton("INSTRUCTIONS");
        this.instructions.setBounds(12 * Map.TILE_SIZE - Map.TILE_SIZE / 2,10 * Map.TILE_SIZE,192,64);

        this.back = new JButton("BACK");
        this.back.setBounds(Map.TILE_SIZE * 22, Map.TILE_SIZE,128,32);

        this.easyMap1 = new JButton();
        this.easyMap1.setIcon(new ImageIcon(easyMap1Sprite));
        this.easyMap1.setBounds(4 * Map.TILE_SIZE - Map.TILE_SIZE / 2,5 * Map.TILE_SIZE,210,130);

        this.easyMap2 = new JButton();
        this.easyMap2.setIcon(new ImageIcon(easyMap2Sprite));
        this.easyMap2.setBounds(9 * Map.TILE_SIZE - Map.TILE_SIZE / 2,5 * Map.TILE_SIZE,210,130);

        this.intMap = new JButton();
        this.intMap.setIcon(new ImageIcon(intMapSprite));
        this.intMap.setBounds(14 * Map.TILE_SIZE - Map.TILE_SIZE / 2,5 * Map.TILE_SIZE,210,130);

        this.advMap = new JButton();
        this.advMap.setIcon(new ImageIcon(advMapSprite));
        this.advMap.setBounds(19 * Map.TILE_SIZE - Map.TILE_SIZE / 2,5 * Map.TILE_SIZE,210,130);

        this.easy = new JButton("EASY");
        this.easy.setBounds(5 * Map.TILE_SIZE - Map.TILE_SIZE / 2,5 * Map.TILE_SIZE,128,64);

        this.normal = new JButton("NORMAL");
        this.normal.setBounds(10 * Map.TILE_SIZE - Map.TILE_SIZE / 2,5 * Map.TILE_SIZE,128,64);

        this.hard = new JButton("HARD");
        this.hard.setBounds(15 * Map.TILE_SIZE - Map.TILE_SIZE / 2,5 * Map.TILE_SIZE,128,64);

        this.impossible = new JButton("IMPOSSIBLE");
        this.impossible.setBounds(20 * Map.TILE_SIZE - Map.TILE_SIZE / 2,5 * Map.TILE_SIZE,128,64);

        // Sets all of the booleans to false because the user is just in the main menu at first
        this.isInInstructions = false;
        this.isInMapSelect = false;
        this.isInDifficultySelect = false;
    }

    /* Used to add listeners and to add the buttons to the actual panel
     * Pre: Takes in a JPanel object to add buttons to
     * Post: Doesn't return anything
     */
    public void addNotify(JPanel panel) {
        // Adding actionListeners for all of my buttons
        this.play.addActionListener(this);
        this.instructions.addActionListener(this);
        this.back.addActionListener(this);
        this.easyMap1.addActionListener(this);
        this.easyMap2.addActionListener(this);
        this.intMap.addActionListener(this);
        this.advMap.addActionListener(this);
        this.easy.addActionListener(this);
        this.normal.addActionListener(this);
        this.hard.addActionListener(this);
        this.impossible.addActionListener(this);

        // Sets the layout to null so that we can use setBounds
        panel.setLayout(null);

        // Adds all of the buttons to the panel that is passed in
        panel.add(play);
        panel.add(instructions);
        panel.add(back);
        panel.add(easyMap1);
        panel.add(easyMap2);
        panel.add(intMap);
        panel.add(advMap);
        panel.add(easy);
        panel.add(normal);
        panel.add(hard);
        panel.add(impossible);

        // Setting the buttons that don't need to be seen right away to false
        this.back.setVisible(false);
        this.easyMap1.setVisible(false);
        this.easyMap2.setVisible(false);
        this.intMap.setVisible(false);
        this.advMap.setVisible(false);
        this.easy.setVisible(false);
        this.normal.setVisible(false);
        this.hard.setVisible(false);
        this.impossible.setVisible(false);
    }

    /* Used to remove listeners, mainly so that actionListeners are not added twice and to remove the buttons to the actual panel
     * Pre: Takes in a JPanel object to remove buttons from
     * Post: Doesn't return anything
     */
    public void removeNotify(JPanel panel) {
        // Removing actionListeners from all of the buttons
        this.play.removeActionListener(this);
        this.instructions.removeActionListener(this);
        this.back.removeActionListener(this);
        this.easyMap1.removeActionListener(this);
        this.easyMap2.removeActionListener(this);
        this.intMap.removeActionListener(this);
        this.advMap.removeActionListener(this);
        this.easy.removeActionListener(this);
        this.normal.removeActionListener(this);
        this.hard.removeActionListener(this);
        this.impossible.removeActionListener(this);

        // Removing buttons from the panel that is passed in
        panel.remove(play);
        panel.remove(instructions);
        panel.remove(back);
        panel.remove(easyMap1);
        panel.remove(easyMap2);
        panel.remove(intMap);
        panel.remove(advMap);
        panel.remove(easy);
        panel.remove(normal);
        panel.remove(hard);
        panel.remove(impossible);
    }

    /* Used to paint everything that is needed by the main menu
     * Pre: Takes in a Graphics2D object, used to paint images
     * Post: Doesn't return anything
     */
    public void paint(Graphics2D g) {
        // Sets and resets the visibility of each button
        this.play.setVisible(true);
        this.instructions.setVisible(true);
        this.back.setVisible(false);
        this.easyMap1.setVisible(false);
        this.easyMap2.setVisible(false);
        this.intMap.setVisible(false);
        this.advMap.setVisible(false);
        this.easy.setVisible(false);
        this.easy.setVisible(false);
        this.normal.setVisible(false);
        this.hard.setVisible(false);
        this.impossible.setVisible(false);

        // Creates, draws and fills a rectangle that is used as the background of the main menu
        Rectangle2D.Double back = new Rectangle2D.Double(0, 0, Map.MAP_WIDTH * Map.TILE_SIZE + UI.UI_WIDTH, Map.MAP_HEIGHT * Map.TILE_SIZE);
        g.setPaint(Color.white);
        g.draw(back);
        g.fill(back);
        g.drawImage(background, 0, 0, null);

        // If the player is in any of these windows, it will create a rectangle as the background of the window
        if(isInInstructions || isInMapSelect || isInDifficultySelect) {
            // Creating, drawing and filling the rectangle
            Rectangle2D.Double windowBackground = new Rectangle2D.Double(Map.TILE_SIZE, Map.TILE_SIZE, Map.MAP_WIDTH * Map.TILE_SIZE + Map.TILE_SIZE * 2, Map.MAP_HEIGHT * Map.TILE_SIZE - Map.TILE_SIZE * 2);
            g.setPaint(Color.DARK_GRAY);
            g.draw(windowBackground);
            g.fill(windowBackground);

            // Setting the two buttons' visibility from the main menu to false and setting the back button's visibility to true
            this.play.setVisible(false);
            this.instructions.setVisible(false);
            this.back.setVisible(true);
        }
        // If the player is in the instructions tab, it will paint the instruction title image and draw the instructions
        if(isInInstructions && !isInMapSelect && !isInDifficultySelect) {
            // Drawing the instruction title image
            g.drawImage(instructionTitleSprite, Map.TILE_SIZE + 32, Map.TILE_SIZE + 10, null);
            // Setting paint colour and font for drawing the instructions
            g.setPaint(Color.white);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            // Instructions
            g.drawString("Welcome to Medieval Tower Defense!", Map.TILE_SIZE * 2 - Map.TILE_SIZE / 2, Map.TILE_SIZE * 3);
            g.drawString("In this game, you will work with your towers to defeat all of the enemies trying to defeat you.", Map.TILE_SIZE * 2 - Map.TILE_SIZE / 2, Map.TILE_SIZE * 4);
            g.drawString("Build and upgrade towers to survive the onslaught of enemies and survive all of the waves to beat the game.", Map.TILE_SIZE * 2 - Map.TILE_SIZE / 2, Map.TILE_SIZE * 5);
            g.drawString("You can select towers to see their range or upgrade them.", Map.TILE_SIZE * 2 - Map.TILE_SIZE / 2, Map.TILE_SIZE * 6);
            g.drawString("A few things to look out for though, the mortar tower can be retargeted using the target mortar button and the ballista tower shoots at where your mouse is.", Map.TILE_SIZE * 2 - Map.TILE_SIZE / 2, Map.TILE_SIZE * 7);
            g.drawString("To collect money from the farms, just click on the farm tower.", Map.TILE_SIZE * 2 - Map.TILE_SIZE / 2, Map.TILE_SIZE * 8);
            g.drawString("Some enemies will have special properties like camo, armoured and magic-proof. Make sure you get enemies which can hit all of these types of enemies.", Map.TILE_SIZE * 2 - Map.TILE_SIZE / 2, Map.TILE_SIZE * 9);
        }
        // If the player is in the map selection tab, it will paint the things needed for the map selection tab
        if(!isInInstructions && isInMapSelect && !isInDifficultySelect) {
            // Setting the four map buttons to true
            this.easyMap1.setVisible(true);
            this.easyMap2.setVisible(true);
            this.intMap.setVisible(true);
            this.advMap.setVisible(true);
            // Drawing the map selection title image
            g.drawImage(mapSelectionTitleSprite, Map.TILE_SIZE + 32, Map.TILE_SIZE + 10, null);
            // Setting paint colour and font for drawing the map selection names and difficulty
            g.setPaint(Color.white);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            // Draws the map's name and difficulty of each map
            g.drawString("Medieval Meadow", 4 * Map.TILE_SIZE - 10,8 * Map.TILE_SIZE - Map.TILE_SIZE / 2);
            g.drawString("Easy Map", 4 * Map.TILE_SIZE + 20,8 * Map.TILE_SIZE);
            g.drawString("Village Path", 9 * Map.TILE_SIZE + 20,8 * Map.TILE_SIZE - Map.TILE_SIZE / 2);
            g.drawString("Easy Map", 9 * Map.TILE_SIZE + 28,8 * Map.TILE_SIZE);
            g.drawString("Off The Coast", 14 * Map.TILE_SIZE + 5,8 * Map.TILE_SIZE - Map.TILE_SIZE / 2);
            g.drawString("Intermediate Map", 14 * Map.TILE_SIZE - 5,8 * Map.TILE_SIZE);
            g.drawString("Double Trouble", 19 * Map.TILE_SIZE + 5,8 * Map.TILE_SIZE - Map.TILE_SIZE / 2);
            g.drawString("Advanced Map", 19 * Map.TILE_SIZE + 8,8 * Map.TILE_SIZE);
        }
        // If the player is in the difficulty selection tab, it will paint the things needed for the difficulty selection tab
        if(!isInInstructions && !isInMapSelect && isInDifficultySelect) {
            // Setting the four difficulty buttons to true
            this.easy.setVisible(true);
            this.normal.setVisible(true);
            this.hard.setVisible(true);
            this.impossible.setVisible(true);
            // Drawing the difficulty selection title image
            g.drawImage(difficultySelectionTitleSprite, Map.TILE_SIZE + 32, Map.TILE_SIZE + 10, null);
            // Setting paint colour and font for drawing the map selection names and difficulty
            g.setPaint(Color.white);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            // Drawing the settings that each difficulty has
            g.drawString("100 Health", 5 * Map.TILE_SIZE - 15,7 * Map.TILE_SIZE - Map.TILE_SIZE / 2);
            g.drawString("1000 Starting Money", 5 * Map.TILE_SIZE - Map.TILE_SIZE,7 * Map.TILE_SIZE);
            g.drawString("Slower Enemies", 5 * Map.TILE_SIZE - 40,8 * Map.TILE_SIZE - Map.TILE_SIZE / 2);
            g.drawString("Cheaper towers and upgrades", 5 * Map.TILE_SIZE - 100,8 * Map.TILE_SIZE);
            g.drawString("75 Health", 10 * Map.TILE_SIZE - 10,7 * Map.TILE_SIZE - Map.TILE_SIZE / 2);
            g.drawString("900 Starting Money", 10 * Map.TILE_SIZE - 50,7 * Map.TILE_SIZE);
            g.drawString("50 Health", 15 * Map.TILE_SIZE - 10,7 * Map.TILE_SIZE - Map.TILE_SIZE / 2);
            g.drawString("800 Starting Money", 15 * Map.TILE_SIZE - 50,7 * Map.TILE_SIZE);
            g.drawString("Faster Enemies", 15 * Map.TILE_SIZE - 40,8 * Map.TILE_SIZE - Map.TILE_SIZE / 2);
            g.drawString("More expensive towers and upgrades", 15 * Map.TILE_SIZE - Map.TILE_SIZE * 2,8 * Map.TILE_SIZE);
            g.drawString("DO NOT ATTEMPT!", 20 * Map.TILE_SIZE - 50,7 * Map.TILE_SIZE - Map.TILE_SIZE / 2);
        }
    }

    /* Used to get the map string which tells which map txt document to load
     * Pre: Doesn't take in any parameters
     * Post: Returns the map string
     */
    public String getMap() {
        return this.map;
    }

    /* Used to get the difficulty string which tells which difficulty txt document to load
     * Pre: Doesn't take in any parameters
     * Post: Returns the difficulty string
     */
    public String getDifficulty() {
        return this.difficulty;
    }

    /* Used to check whether any of the buttons were clicked
     * Pre: Takes in an ActionEvent object which checks which object the action occurred from
     * Post: Doesn't return anything
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // If the button clicked is play then it moves onto the map selection tab
        if(e.getSource() == this.play){
            this.isInMapSelect = true;
        }
        // If the button clicked is instructions then it will move onto the instruction tab
        if(e.getSource() == this.instructions){
            this.isInInstructions = true;
        }
        // If the map is back, depending on which tab the player is in, it will send them back one tab
        if(e.getSource() == this.back){
            // Sets the boolean to false causing them to go back to the main tab
            if(this.isInInstructions) {
                this.isInInstructions = false;
            }
            // Sets the boolean to false causing them to go back to the main tab
            if(this.isInMapSelect) {
                this.isInMapSelect = false;
            }
            // Sets the boolean to false but map select back true causing them to go back to map select tab
            if(this.isInDifficultySelect) {
                this.isInDifficultySelect = false;
                this.isInMapSelect = true;
            }
        }

        // Checks which map is clicked then it will go onto the difficulty select tab and set the map string to the name of the map
        if(e.getSource() == this.easyMap1){
            this.isInMapSelect = false;
            this.isInDifficultySelect = true;
            this.map = "EasyMap1";
        }
        if(e.getSource() == this.easyMap2){
            this.isInMapSelect = false;
            this.isInDifficultySelect = true;
            this.map = "EasyMap2";
        }
        if(e.getSource() == this.intMap){
            this.isInMapSelect = false;
            this.isInDifficultySelect = true;
            this.map = "IntMap";
        }
        if(e.getSource() == this.advMap){
            this.isInMapSelect = false;
            this.isInDifficultySelect = true;
            this.map = "AdvMap";
        }

        // Checks which difficulty is clicked then sets the difficulty string to the difficulty and then it will change the gameState's state to 1 (game)
        if(e.getSource() == this.easy) {
            this.difficulty = "easymode";
            this.isInMapSelect = false;
            this.isInDifficultySelect = false;
            this.isInInstructions = false;
            gameState.setState(1);
        }
        if(e.getSource() == this.normal) {
            this.difficulty = "normalmode";
            this.isInMapSelect = false;
            this.isInDifficultySelect = false;
            this.isInInstructions = false;
            gameState.setState(1);
        }
        if(e.getSource() == this.hard) {
            this.difficulty = "hardmode";
            this.isInMapSelect = false;
            this.isInDifficultySelect = false;
            this.isInInstructions = false;
            gameState.setState(1);
        }
        if(e.getSource() == this.impossible) {
            this.difficulty = "impossiblemode";
            this.isInMapSelect = false;
            this.isInDifficultySelect = false;
            this.isInInstructions = false;
            gameState.setState(1);
        }
    }
}
