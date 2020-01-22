package battleship;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Battleship extends JFrame
{
	Controller controller;

	Board computerBoard;
	// ControlPanel controlPanel;
	Board playerBoard;

	public static void main(String[] args)
	{
			Battleship battleship = new Battleship();
	}

	public Battleship()
	{
		controller = new Controller();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setTitle(Constants.WINDOW_TITLE);
		setLayout(new BorderLayout());		
		computerBoard = new Board(false, Constants.COMPUTER_BOARD_NAME, controller);
		// controlPanel = new ControlPanel();
		playerBoard = new Board(true, Constants.PLAYER_BOARD_NAME, controller);
		add(computerBoard, BorderLayout.PAGE_START);
		// add(controlPanel, BorderLayout.CENTER);
		add(playerBoard, BorderLayout.PAGE_END);

		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	// private class ControlPanel extends JPanel
	// {
	// 	JButton newGameButton;
	// 	JLabel displayPanel;
	// 	JButton levelsButton;
	// 	ControlPanel()
	// 	{
	// 		setSize(new Dimension(Constants.BOARD_DIMENSION * 30, Constants.BOARD_DIMENSION * 10));
	// 		setLayout(new BorderLayout());

	// 		newGameButton = new JButton("New");
	// 		displayPanel = new JLabel(Constants.BLANK_SYMBOL, SwingConstants.CENTER);
	// 		levelsButton = new JButton("Lvl");

	// 		newGameButton.addActionListener(new ActionListener()
	// 		{
	// 			public void actionPerformed(ActionEvent e)
	// 			{
	// 				startGame();
	// 			}
	// 		});
	// 		levelsButton.addActionListener(new ActionListener()
	// 		{
	// 			public void actionPerformed(ActionEvent e)
	// 			{
	// 				changeLevel();
	// 			}
	// 		});

	// 		add(newGameButton, BorderLayout.LINE_START);
	// 		add(displayPanel, BorderLayout.CENTER);
	// 		add(levelsButton, BorderLayout.LINE_END);
	// 	}
	// }

	// private void changeLevel()
	// {
	// 	Log.debug("Changing Level...");
	// 	// JOptionPane.showOptionDialog(this,"Hello, this is a dialogue box.");  

	// 	Object[] levels = {
	// 		battleship.Constants.Level.EASY,
	// 		battleship.Constants.Level.NORMAL,
	// 		battleship.Constants.Level.HARD
	// 	};
	// 	int result = JOptionPane.showOptionDialog(this, 
	// 		battleship.Constants.LEVEL_MESSAGE + computer.getLevel().toString(),
	// 		battleship.Constants.LEVEL_TITLE,
	// 		JOptionPane.YES_NO_CANCEL_OPTION,
	// 	    JOptionPane.PLAIN_MESSAGE,
	//         null, 
	//         levels, 
	//         computer.getLevel()
	//         );
	// 	if(result == 2)
	// 	{
	// 		Log.debug("HARD has not yet been implemented.");
	// 	}
	// 	else if(result >= 0)
	// 	{
	// 		Log.debug("Setting level to "
	// 			+ levels[result].toString());
	// 		computer.setLevel((battleship.Constants.Level) levels[result]);
	// 		startGame();
	// 	}
	// 	else
	// 	{
	// 		Log.debug("Level change cancelled.");
	// 	}
	// }
}
