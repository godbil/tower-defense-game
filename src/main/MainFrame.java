package main;

import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame(){
        this.setLocationRelativeTo(null);
        this.setTitle("Medieval Tower Defense");
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void run(){
        this.setContentPane(new Game());
        this.setVisible(true);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public static void main(String[] args){
        MainFrame mainFrame = new MainFrame();
        mainFrame.run();
    }
}
