package main;

import main.enemy.EnemyManager;
import main.tower.TowerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Game extends JPanel implements ActionListener {
    // Full canvas' width
    public static final int WIDTH = Map.MAP_WIDTH * Map.TILE_SIZE + UI.UI_WIDTH;
    // Full canvas' height
    public static final int HEIGHT = Math.max(Map.MAP_HEIGHT * Map.TILE_SIZE, UI.UI_HEIGHT);

    // The frames per second that my game will run with
    public static final int FPS = 60;
    // Divides 1000 milliseconds or 1 second by the amount of frames per second so the timer will know how many milliseconds there are between frames
    public static final int TIMER = 1000 / FPS;
    // Initialize a timer which calls the actionListener's actionPerformed method every TIMER amount of milliseconds
    private Timer timer;

    // Creates all of the objects needed for the game which are the map, enemy manager, tower manager, ui, game state and menu
    private final Map map;
    private final EnemyManager enemyManager;
    private final TowerManager towerManager;
    private final UI ui;
    private final GameState gameState;
    private final Menu menu;

    public Game(){
        // Sets up the preferred size of the JPanel using the width and height
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);
        this.requestFocus();

        // Initializes the timer and all of the objects that are needed for the game
        this.timer = new Timer(TIMER, this);

        this.map = new Map();
        this.towerManager = new TowerManager();
        this.gameState = new GameState();
        this.menu = new Menu(gameState);
        this.enemyManager = new EnemyManager(this.gameState);
        this.ui = new UI(this.map, this.towerManager, this.gameState);
    }

    /* Calls all of the init methods when the game state is 1 and is also used to reset the game each time the player wants to replay or select a new map
     * Pre: Doesn't take in any parameters
     * Post: Doesn't return anything
     */
    private void init() {
        // Checks if the game is in the "game" state and then it will call the load map method using the player's map selection from the menu
        if(gameState.getGameState() == 1) {
            try {
                this.map.loadMap(menu.getMap());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Initializes all of the objects' variables by calling their respective init methods
            this.gameState.init(this.menu.getDifficulty());
            this.enemyManager.init(this.map.getMap());
            this.towerManager.init();
            this.ui.init();
        }
    }

    /* Called automatically when Game is created from main frame
     * Pre: Doesn't take in any parameters
     * Post: Doesn't return anything
     */
    @Override
    public void addNotify() {
        super.addNotify();

        // Calls the menu's addNotify method when the game state is 0 (menu) and the ui's addNotify when the game state is 0 (game)
        if(gameState.getGameState() == 0) {
            this.menu.addNotify(this);
        }
        if(gameState.getGameState() == 1) {
            this.ui.addNotify(this);
        }

        // Starts the timer and then calls init to initialize all of the objects' variables so the game can function
        timer.start();
        this.init();
    }

    /* Calls all of the objects' update methods
     * Pre: Doesn't take in any parameters
     * Post: Doesn't return anything
     */
    private void update() {
        // Checks whether the game state changed
        if(gameState.isGameStateChange()) {
            // Resets the gameStateChange boolean to false
            gameState.setGameStateChange(false);
            // Calls the removeNotify method so that they don't add multiple actionListeners to the buttons or mouse clicks
            this.ui.removeNotify(this);
            this.menu.removeNotify(this);
            // Calls addNotify to check which game state it's in and to add actionListeners and JButtons to this JPanel class
            this.addNotify();
        }
        // If the game state is 1 (game) and the player's health is greater than 0 then it will call the enemy manager and tower manager's update methods
        if(gameState.getGameState() == 1 && gameState.getHealth() > 0) {
            this.enemyManager.update(this.map.getMap());
            this.towerManager.update(this.enemyManager.getEnemies());
            // Calls the gameState's checkGameOver method to check if the game is over each update
            gameState.checkGameOver();
        }
    }

    /* This method is used to call all of the objects' paint methods and is called each time in actionPerformed in the form of repaint()
     * Pre: Takes in a Graphics object, used to draw graphics
     * Post: Doesn't return anything
     */
    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        // If the game state is 0 (menu) then it will paint the menu but if it's 1 (game), it will paint everything needed for the actual game portion
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

    /* This method is called by the timer which then calls the update and repaint methods so the game gets update each frame
     * Pre: Takes in a ActionEvent object
     * Post: Doesn't return anything
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.update();
        this.repaint();
    }
}
