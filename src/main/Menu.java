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
    private final GameState gameState;

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


    private boolean isInInstructions;
    private boolean isInMapSelect;
    private boolean isInDifficultySelect;

    private BufferedImage background;
    private BufferedImage instructionTitleSprite;
    private BufferedImage mapSelectionTitleSprite;
    private BufferedImage difficultySelectionTitleSprite;
    private BufferedImage easyMap1Sprite;
    private BufferedImage easyMap2Sprite;
    private BufferedImage intMapSprite;
    private BufferedImage advMapSprite;

    private String map;
    private String difficulty;

    public Menu(GameState gameState) {
        try {
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
        this.gameState = gameState;
        this.map = "";
        this.difficulty = "";

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

        this.isInInstructions = false;
        this.isInMapSelect = false;
        this.isInDifficultySelect = false;
    }

    public void addNotify(JPanel panel) {
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
        panel.setLayout(null);
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

    public void removeNotify(JPanel panel) {
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

    public void paint(Graphics2D g) {
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
        Rectangle2D.Double back = new Rectangle2D.Double(0, 0, Map.MAP_WIDTH * Map.TILE_SIZE + UI.UI_WIDTH, Map.MAP_HEIGHT * Map.TILE_SIZE);
        g.setPaint(Color.white);
        g.draw(back);
        g.fill(back);
        g.drawImage(background, 0, 0, null);
        if(isInInstructions || isInMapSelect || isInDifficultySelect) {
            Rectangle2D.Double instructions = new Rectangle2D.Double(Map.TILE_SIZE, Map.TILE_SIZE, Map.MAP_WIDTH * Map.TILE_SIZE + Map.TILE_SIZE * 2, Map.MAP_HEIGHT * Map.TILE_SIZE - Map.TILE_SIZE * 2);
            g.setPaint(Color.DARK_GRAY);
            g.draw(instructions);
            g.fill(instructions);
            this.play.setVisible(false);
            this.instructions.setVisible(false);
            this.back.setVisible(true);
        }
        if(isInInstructions && !isInMapSelect && !isInDifficultySelect) {
            g.drawImage(instructionTitleSprite, Map.TILE_SIZE + 32, Map.TILE_SIZE + 10, null);
            g.setPaint(Color.white);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g.drawString("Welcome to Medieval Tower Defense!", Map.TILE_SIZE * 2 - Map.TILE_SIZE / 2, Map.TILE_SIZE * 3);
            g.drawString("In this game, you will work with your towers to defeat all of the enemies trying to defeat you.", Map.TILE_SIZE * 2 - Map.TILE_SIZE / 2, Map.TILE_SIZE * 4);
            g.drawString("Build and upgrade towers to survive the onslaught of enemies and survive all of the waves to beat the game.", Map.TILE_SIZE * 2 - Map.TILE_SIZE / 2, Map.TILE_SIZE * 5);
            g.drawString("You can select towers to see their range or upgrade them.", Map.TILE_SIZE * 2 - Map.TILE_SIZE / 2, Map.TILE_SIZE * 6);
            g.drawString("A few things to look out for though, the mortar tower can be retargeted using the target mortar button and the ballista tower shoots at where your mouse is.", Map.TILE_SIZE * 2 - Map.TILE_SIZE / 2, Map.TILE_SIZE * 7);
            g.drawString("To collect money from the farms, just click on the farm tower.", Map.TILE_SIZE * 2 - Map.TILE_SIZE / 2, Map.TILE_SIZE * 8);
            g.drawString("Some enemies will have special properties like camo, armoured and magic-proof. Make sure you get enemies which can hit all of these types of enemies.", Map.TILE_SIZE * 2 - Map.TILE_SIZE / 2, Map.TILE_SIZE * 9);
        }
        if(!isInInstructions && isInMapSelect && !isInDifficultySelect) {
            this.easyMap1.setVisible(true);
            this.easyMap2.setVisible(true);
            this.intMap.setVisible(true);
            this.advMap.setVisible(true);
            g.drawImage(mapSelectionTitleSprite, Map.TILE_SIZE + 32, Map.TILE_SIZE + 10, null);
            g.setPaint(Color.white);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g.drawString("Medieval Meadow", 4 * Map.TILE_SIZE - 10,8 * Map.TILE_SIZE - Map.TILE_SIZE / 2);
            g.drawString("Easy Map", 4 * Map.TILE_SIZE + 20,8 * Map.TILE_SIZE);
            g.drawString("Village Path", 9 * Map.TILE_SIZE + 20,8 * Map.TILE_SIZE - Map.TILE_SIZE / 2);
            g.drawString("Easy Map", 9 * Map.TILE_SIZE + 28,8 * Map.TILE_SIZE);
            g.drawString("Off The Coast", 14 * Map.TILE_SIZE + 5,8 * Map.TILE_SIZE - Map.TILE_SIZE / 2);
            g.drawString("Intermediate Map", 14 * Map.TILE_SIZE - 5,8 * Map.TILE_SIZE);
            g.drawString("Double Trouble", 19 * Map.TILE_SIZE + 5,8 * Map.TILE_SIZE - Map.TILE_SIZE / 2);
            g.drawString("Advanced Map", 19 * Map.TILE_SIZE + 8,8 * Map.TILE_SIZE);
        }
        if(!isInInstructions && !isInMapSelect && isInDifficultySelect) {
            g.drawImage(difficultySelectionTitleSprite, Map.TILE_SIZE + 32, Map.TILE_SIZE + 10, null);
            this.easy.setVisible(true);
            this.normal.setVisible(true);
            this.hard.setVisible(true);
            this.impossible.setVisible(true);
            g.setPaint(Color.white);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
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

    public String getMap() {
        return this.map;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.play){
            this.isInMapSelect = true;
        }
        if(e.getSource() == this.instructions){
            this.isInInstructions = true;
        }
        if(e.getSource() == this.back){
            if(this.isInInstructions) {
                this.isInInstructions = false;
            }
            if(this.isInMapSelect) {
                this.isInMapSelect = false;
            }
            if(this.isInDifficultySelect) {
                this.isInDifficultySelect = false;
                this.isInMapSelect = true;
            }
        }

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
