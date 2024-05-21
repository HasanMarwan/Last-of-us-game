package views;
import java.awt.FlowLayout;

import javax.swing.*;

import engine.Game;


public class CloseWindow {
	JFrame closeScreen = new JFrame();

	public CloseWindow(){
		closeScreen.setTitle("LAST OF US");
		closeScreen.setLayout(new FlowLayout(FlowLayout.CENTER,20,20));
		closeScreen.setSize(500,500);
		closeScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		closeScreen.setLocation(500,100);
		JLabel label = new JLabel();
				 label.setText(winOrLose());
				 closeScreen.add(label);
				 closeScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				 closeScreen.setVisible(true);
	}
	public static String winOrLose(){
		if(Game.checkWin())
			return "you win! :) ";
		else
			return "game over :( ";
	}

	
}
