package views;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;

import engine.Game;
import model.characters.Explorer;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;

public class StartWindow implements ActionListener {

    JFrame startScreen = new JFrame();

    public static String heroDetails(Hero h) {
        String type = "";
        if (h instanceof Medic) {
            type = "Medic";
        }
        if (h instanceof Fighter) {
            type = "Fighter";
        }
        if (h instanceof Explorer) {
            type = "Explorer";
        }
        String s = "<html><p>Name: " + h.getName() + "<p>Type: " + type + "<p>Hp: " + h.getMaxHp() + "<p>AttackDmg: " + h.getAttackDmg() + "<p>MaxActions: " + h.getMaxActions();
        return s;
    }
    static JButton[] heroButtons = new JButton[8];

    public StartWindow() {
        try {
            Game.loadHeroes("Heroes.csv");
        } catch (IOException e) {
            System.out.println("no heroes found");
        }
        // JLabel label = new JLabel();
        // label.setText(heroDetails(Game.availableHeroes));
        //	  label.setBackground(Color.cyan);    
        //label.setOpaque(true);	   
        startScreen.setTitle("LAST OF US");
        startScreen.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        startScreen.setSize(500, 500);
        startScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startScreen.setLocation(500, 100);

        for (int i = 0; i < 8; i++) {
            heroButtons[i] = (new JButton(heroDetails(Game.availableHeroes.get(i))));
            startScreen.add(heroButtons[i]);
            heroButtons[i].setFocusable(false);
            heroButtons[i].addActionListener(this);
        }

        startScreen.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < Game.availableHeroes.size(); i++) {
            if (e.getSource() == (heroButtons[i])) {
                Game.startGame(Game.availableHeroes.get(i));
                startScreen.dispose();
                GameWindow g = new GameWindow();
                return;
            }
        }

    }

}
