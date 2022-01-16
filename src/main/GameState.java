package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GameState {
    private int health;
    private int money;
    public int maxWave;
    private int wave;
    public double movementMultiplier;
    public double moneyMultiplier;

    private String[][] waveEnemies;
    private int[][] wavePercentages;
    private int[] waveAmounts;

    public int gameState;
    private boolean gameStateChange;

    public GameState() {
        this.gameState = 0;
    }

    public void init(String difficulty) {
        try {
            Path file = Path.of("assets/" + difficulty + ".txt");
            List<String> content = Files.readAllLines(file);
            String[] param = content.get(0).split(" ");
            this.health = Integer.parseInt(param[0]);
            this.money = Integer.parseInt(param[1]);
            this.maxWave = Integer.parseInt(param[2]);
            this.movementMultiplier = Double.parseDouble(param[3]);
            this.moneyMultiplier = Double.parseDouble(param[4]);

            this.waveEnemies = new String[maxWave + 1][];
            this.wavePercentages = new int[maxWave + 1][];
            this.waveAmounts = new int[maxWave + 1];

            for(int i = 1; i < content.size(); i++) {
                param = content.get(i).split(" ");
                wavePercentages[i] = new int[param.length / 2];
                waveEnemies[i] = new String[param.length / 2];
                waveAmounts[i] = Integer.parseInt(param[0]);
                for(int j = 1; j < param.length; j++) {
                    if(j % 2 == 0) {
                        wavePercentages[i][j / 2 - 1] = Integer.parseInt(param[j]);
                    }
                    else {
                        waveEnemies[i][j / 2] = param[j];
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        this.wave = 1;
    }

    public int getMaxWave() {
        return this.maxWave;
    }

    public int getHealth() {
        return this.health;
    }

    public int getMoney() {
        return this.money;
    }

    public int getWave(){
        return this.wave;
    }

    public int getWaveAmounts() {
        return this.waveAmounts[wave];
    }

    public double getMovementMultiplier() {
        return this.movementMultiplier;
    }

    public double getMoneyMultiplier() {
        return this.moneyMultiplier;
    }

    public int getGameState() {
        return this.gameState;
    }

    public void nextState() {
        this.gameStateChange = true;
        this.gameState++;
    }

    public void setGameStateChange(boolean gameStateChange) {
        this.gameStateChange = gameStateChange;
    }

    public boolean IsGameStateChange() {
        return gameStateChange;
    }

    public void decrementWaveAmount() {
        waveAmounts[wave]--;
    }

    public String getWaveEnemies(int random) {
        int percent = 0;
        for(int i = 0; i < waveEnemies[wave].length; i++) {
            percent += wavePercentages[wave][i];
            if(random <= percent) {
                return waveEnemies[wave][i];
            }
        }
        return null;
    }

    public void subtractHealth(int healthDecrease) {
        this.health -= healthDecrease;
        if(this.health < 0) {
            this.health = 0;
        }
    }

    public void subtractMoney(int moneyDecrease) {
        this.money -= moneyDecrease;
    }

    public void addMoney(int moneyIncrease) {
        this.money += moneyIncrease;
    }

    public void nextWave() {
        this.wave++;
    }

}
