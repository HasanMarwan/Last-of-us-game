package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;
import model.characters.Direction;
import model.characters.Explorer;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;
import model.characters.Zombie;
import model.collectibles.Supply;
import model.collectibles.Vaccine;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;

public class GameWindow implements ActionListener {

    JFrame mapScreen = new JFrame();
    JPanel controlUnit = new JPanel();
    JPanel remHeroes = new JPanel();
    JPanel target = new JPanel();
    JPanel world = new JPanel();
    JLabel t = new JLabel(selectedTargetDetails(Game.heroes.get(0)));
    JLabel h = new JLabel(selectedHeroDetails(Game.heroes.get(0)));
    JButton[][] worldButtons = new JButton[15][15];
    JButton AttackButton = new JButton();
    JButton UpButton = new JButton();
    JButton DownButton = new JButton();
    JButton RightButton = new JButton();
    JButton LeftButton = new JButton();
    JButton CureButton = new JButton();
    JButton UseSpecialButton = new JButton();
    JButton EndTurnButton = new JButton();
    JButton resetButton = new JButton();
    JLabel MoveLabel = new JLabel();
    JLabel ActionLabel = new JLabel();
    static boolean isSelectedHero = false;
    boolean isTarget = false;
    boolean isAttacking = false;
    boolean isCuring = false;
    boolean usingSpecial = false;
    Hero selectedHero;
    String tut = "<html><p>How to play: "
            + "<p>1-select a hero by clicking on him/her"
            + "<p>2-click on the  button with the desired action"
            + "<p>3-click on a target on the map if the action you chose requires"
            + "<p>if you want to alternate between heros press on change hero button";

    public GameWindow() {
        mapScreen.setTitle("LAST OF US");
        mapScreen.setLayout(new BorderLayout());
        mapScreen.setSize(1000, 600);
        mapScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mapScreen.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mapScreen.setLocation(500, 100);
        mapScreen.setVisible(true);

        controlUnit.setBackground(Color.gray);
        remHeroes.setBackground(Color.gray);
        target.setBackground(Color.gray);
        world.setBackground(Color.orange);

        controlUnit.setPreferredSize(new Dimension(200, 200));
        remHeroes.setPreferredSize(new Dimension(100, 115));
        target.setPreferredSize(new Dimension(100, 100));
        world.setPreferredSize(new Dimension(100, 100));

        world.setLayout(new GridLayout(15, 15));
        target.add(t);
        remHeroes.add(h);

        for (int i = 0; i < worldButtons.length; i++) {
            for (int j = 0; j < worldButtons.length; j++) {
                worldButtons[i][j] = new JButton();
                worldButtons[i][j].addActionListener(this);
                world.add(worldButtons[i][j]);
            }
        }

        mapScreen.add(controlUnit, BorderLayout.WEST);
        mapScreen.add(remHeroes, BorderLayout.NORTH);
        mapScreen.add(target, BorderLayout.SOUTH);
        mapScreen.add(world, BorderLayout.CENTER);
        updateWorldButtons();
        //ActionLabel.setLayout(new GridLayout(0,1,0,10));
        //MoveLabel.setLayout(new BorderLayout());

        UpButton.setText("UP");
        DownButton.setText("DOWN");
        RightButton.setText("RIGHT");
        LeftButton.setText("LEFT");
        CureButton.setText("CURE");
        UseSpecialButton.setText("USE SPECIAL");
        EndTurnButton.setText("END TURN");
        AttackButton.setText("ATTACK");
        resetButton.setText("CHANGE HERO");

        UpButton.addActionListener(this);
        DownButton.addActionListener(this);
        RightButton.addActionListener(this);
        LeftButton.addActionListener(this);
        CureButton.addActionListener(this);
        UseSpecialButton.addActionListener(this);
        EndTurnButton.addActionListener(this);
        AttackButton.addActionListener(this);
        resetButton.addActionListener(this);

        controlUnit.setLayout(new FlowLayout());
        controlUnit.add(UpButton, BorderLayout.NORTH);
        controlUnit.add(DownButton, BorderLayout.SOUTH);
        controlUnit.add(RightButton, BorderLayout.EAST);
        controlUnit.add(LeftButton, BorderLayout.WEST);
        controlUnit.add(AttackButton);
        controlUnit.add(CureButton);
        controlUnit.add(UseSpecialButton);
        controlUnit.add(EndTurnButton);
        controlUnit.add(AttackButton);
        controlUnit.add(resetButton);

        JOptionPane.showMessageDialog(mapScreen, tut);

    }

    public static String selectedHeroDetails(Hero h) {

        if (isSelectedHero == true) {
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
            String s = "<html><p>Name: " + h.getName()
                    + "<p>Type: " + type
                    + "<p>Hp: " + h.getCurrentHp()
                    + "<p>AttackDmg: " + h.getAttackDmg()
                    + "<p>ActionsPoints: " + h.getActionsAvailable()
                    + "<p>Supplies: " + h.getSupplyInventory().size()
                    + "<p>Vaccines: " + h.getVaccineInventory().size();
            return s;
        } else {
            return "Selected Hero:  none.";
        }
    }

    public static String selectedTargetDetails(Hero h) {
        String s = "Selected target: none.";
        if (h.getTarget() != null) {
            if (h.getTarget() instanceof Hero) {
                String type = "";
                if (h.getTarget() instanceof Medic) {
                    type = "Medic";
                }
                if (h.getTarget() instanceof Fighter) {
                    type = "Fighter";
                }
                if (h.getTarget() instanceof Explorer) {
                    type = "Explorer";
                }
                s = "<html><p>Name: " + h.getTarget().getName()
                        + "<p>Type: " + type
                        + "<p>Hp: " + h.getTarget().getCurrentHp()
                        + "<p>AttackDmg: " + h.getTarget().getAttackDmg()
                        + "<p>ActionsPoints: " + ((Hero) h.getTarget()).getActionsAvailable()
                        + "<p>Supplies: " + ((Hero) h.getTarget()).getSupplyInventory().size()
                        + "<p>Vaccines: " + ((Hero) h.getTarget()).getVaccineInventory().size();
            } else {
                s = "<html><p> " + h.getTarget().getName()
                        + "<p>Hp: " + h.getTarget().getCurrentHp()
                        + "<p>AttackDmg: " + h.getTarget().getAttackDmg();
            }

            return s;
        } else {
            return s;
        }
    }

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

    public void updateWorldButtons() {
        if (Game.checkWin()) {
            mapScreen.dispose();
            CloseWindow c = new CloseWindow();
        }
        if (Game.checkGameOver()) {
            mapScreen.dispose();
            CloseWindow c = new CloseWindow();
        }
        for (int i = 0; i < Game.map.length; i++) {
            for (int j = 0; j < Game.map.length; j++) {

                if (Game.map[i][j] instanceof CharacterCell) {
                    if (((CharacterCell) Game.map[i][j]).getCharacter() != null) {
                        if (((CharacterCell) Game.map[i][j]).getCharacter() instanceof Hero) {
                            worldButtons[i][j].setText(((CharacterCell) Game.map[i][j]).getCharacter().getName());
                            worldButtons[i][j].setForeground(Color.orange);
                        }
                        if (((CharacterCell) Game.map[i][j]).getCharacter() instanceof Zombie) {
                            worldButtons[i][j].setText(((CharacterCell) Game.map[i][j]).getCharacter().getName());
                            worldButtons[i][j].setForeground(Color.orange);
                        }
                    } else {
                        worldButtons[i][j].setText("");

                    }
                }
                if (Game.map[i][j] instanceof CollectibleCell) {
                    if (((CollectibleCell) Game.map[i][j]).getCollectible() instanceof Vaccine) {
                        worldButtons[i][j].setText("Vaccine");
                        worldButtons[i][j].setForeground(Color.orange);
                    }
                    if (((CollectibleCell) Game.map[i][j]).getCollectible() instanceof Supply) {
                        worldButtons[i][j].setText("Supply");
                        worldButtons[i][j].setForeground(Color.orange);
                    }

                }
                if (Game.map[i][j] instanceof TrapCell) {
                    worldButtons[i][j].setText("trap");
                    worldButtons[i][j].setForeground(Color.orange);
                }
                if (Game.map[i][j].isVisible()) {
                    worldButtons[i][j].setBackground(Color.white);
                }
                if (!(Game.map[i][j].isVisible())) {
                    worldButtons[i][j].setBackground(Color.orange);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < Game.map.length; i++) {
            for (int j = 0; j < Game.map.length; j++) {
                if (e.getSource() == worldButtons[i][j]) {
                    if (Game.map[i][j] instanceof CharacterCell) {
                        if (((CharacterCell) Game.map[i][j]).getCharacter() != null) {
                            if (((CharacterCell) Game.map[i][j]).getCharacter() instanceof Hero) {
                                if (isSelectedHero == false) {
                                    selectedHero = (Hero) ((CharacterCell) Game.map[i][j]).getCharacter();

                                    t.setText(selectedTargetDetails(selectedHero));
                                    isSelectedHero = true;
                                    h.setText(selectedHeroDetails(selectedHero));
                                    return;
                                }

                            }

                        }
                        if (isSelectedHero == true) {
                            if (isAttacking) {
                                try {
                                    selectedHero.setTarget(((CharacterCell) Game.map[i][j]).getCharacter());
                                    t.setText(selectedTargetDetails(selectedHero));
                                    selectedHero.attack();
                                    h.setText(selectedHeroDetails(selectedHero));
                                    t.setText(selectedTargetDetails(selectedHero));
                                    updateWorldButtons();
                                    //isSelectedHero=false;
                                    isAttacking = false;
                                } catch (NotEnoughActionsException | InvalidTargetException ex) {
                                    JOptionPane.showMessageDialog(mapScreen, ex.getMessage());
                                    isAttacking = false;
                                }
                            }
                            if (isCuring) {
                                try {
                                    selectedHero.setTarget(((CharacterCell) Game.map[i][j]).getCharacter());
                                    t.setText(selectedTargetDetails(selectedHero));
                                    selectedHero.cure();
                                    h.setText(selectedHeroDetails(selectedHero));
                                    updateWorldButtons();
                                    t.setText(selectedTargetDetails(selectedHero));
                                    //isSelectedHero=false;
                                    isCuring = false;
                                } catch (NoAvailableResourcesException | InvalidTargetException | NotEnoughActionsException ex) {
                                    JOptionPane.showMessageDialog(mapScreen, ex.getMessage());
                                    isCuring = false;
                                }
                            }
                            if (usingSpecial) {
                                try {
                                    selectedHero.setTarget(((CharacterCell) Game.map[i][j]).getCharacter());
                                    t.setText(selectedTargetDetails(selectedHero));
                                    selectedHero.useSpecial();
                                    t.setText(selectedTargetDetails(selectedHero));
                                    h.setText(selectedHeroDetails(selectedHero));
                                    updateWorldButtons();
                                    //isSelectedHero=false;
                                    usingSpecial = false;
                                } catch (NoAvailableResourcesException | InvalidTargetException ex) {
                                    JOptionPane.showMessageDialog(mapScreen, ex.getMessage());
                                    usingSpecial = false;
                                }

                            }

                        }
                    }

                }
            }
        }
        if (e.getSource() == AttackButton) {
            if (isSelectedHero == true) {
                JOptionPane.showMessageDialog(mapScreen, "choose a target to be attacked");
                isAttacking = true;
            }
        }
        if (e.getSource() == CureButton) {
            if (isSelectedHero == true) {
                JOptionPane.showMessageDialog(mapScreen, "choose a target to be cured");
                isCuring = true;
            }
        }
        if (e.getSource() == UseSpecialButton) {
            if (isSelectedHero == true) {
                if (selectedHero instanceof Fighter || selectedHero instanceof Explorer) {
                    try {
                        selectedHero.useSpecial();
                        updateWorldButtons();
                        h.setText(selectedHeroDetails(selectedHero));
                        //isSelectedHero=false;
                    } catch (NoAvailableResourcesException | InvalidTargetException ex) {
                        JOptionPane.showMessageDialog(mapScreen, ex.getMessage());
                    }
                }
                if (selectedHero instanceof Medic) {
                    JOptionPane.showMessageDialog(mapScreen, "choose a target to be healed");
                    usingSpecial = true;

                }
            }
        }
        if (e.getSource() == UpButton) {
            if (isSelectedHero == true) {
                try {
                    selectedHero.move(Direction.DOWN);
                    h.setText(selectedHeroDetails(selectedHero));
                    updateWorldButtons();
                } catch (MovementException | NotEnoughActionsException ex) {
                    JOptionPane.showMessageDialog(mapScreen, ex.getMessage());
                }
            }

        }
        if (e.getSource() == DownButton) {
            if (isSelectedHero == true) {
                try {
                    selectedHero.move(Direction.UP);
                    h.setText(selectedHeroDetails(selectedHero));
                    updateWorldButtons();
                } catch (MovementException | NotEnoughActionsException ex) {
                    JOptionPane.showMessageDialog(mapScreen, ex.getMessage());
                }
            }

        }
        if (e.getSource() == LeftButton) {
            if (isSelectedHero == true) {
                try {
                    selectedHero.move(Direction.LEFT);
                    h.setText(selectedHeroDetails(selectedHero));
                    updateWorldButtons();
                } catch (MovementException | NotEnoughActionsException ex) {
                    JOptionPane.showMessageDialog(mapScreen, ex.getMessage());
                }
            }

        }
        if (e.getSource() == RightButton) {
            if (isSelectedHero == true) {
                try {
                    selectedHero.move(Direction.RIGHT);
                    h.setText(selectedHeroDetails(selectedHero));
                    updateWorldButtons();
                } catch (MovementException | NotEnoughActionsException ex) {
                    JOptionPane.showMessageDialog(mapScreen, ex.getMessage());
                }
            }

        }
        if (e.getSource() == EndTurnButton) {
            try {

                Game.endTurn();
                isSelectedHero = false;
                h.setText(selectedHeroDetails(selectedHero));
                t.setText("Selected target:  none.");
                updateWorldButtons();

            } catch (NotEnoughActionsException | InvalidTargetException ex) {
                JOptionPane.showMessageDialog(mapScreen, ex.getMessage());
            }
        }

        if (e.getSource() == resetButton) {
            isSelectedHero = false;
            JOptionPane.showMessageDialog(mapScreen, "choose desired hero");
        }

    }

}
