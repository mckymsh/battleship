package battleship3;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*; // Why, Java

public class Game extends JFrame
{
	Board playerBoard;
	ControlPanel controlPanel;
	Board computerBoard;
	AI computer;

	Game ()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setTitle(Battleship.WINDOW_TITLE);
		setLayout(new BorderLayout());		
		computerBoard = new Board(false, Battleship.COMPUTER_BOARD_NAME, this);
		controlPanel = new ControlPanel();
		playerBoard = new Board(true, Battleship.PLAYER_BOARD_NAME, this);
		add(computerBoard, BorderLayout.PAGE_START);
		add(controlPanel, BorderLayout.CENTER);
		add(playerBoard, BorderLayout.PAGE_END);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);

		computer = new AI(this);

		startGame();
	}

	private void startGame()
	{
		Log.debug("############################################################");
		Log.debug("Setup");
		resetGame();
		computerBoard = computer.placeShips(computerBoard);
		computerBoard.revalidate();
		computerBoard.repaint();
		playerBoard.activate();
		showMessage("Place Your Ships");	
	}

	private void resetGame()
	{
		computer.reset();
		computerBoard.reset();
		playerBoard.reset();
	}

	protected void volley()
	{
		Log.debug("Volley");
		if(computerBoard.firingCoordinate < 0) // Player fires
		{
			showMessage("No Coordinate Selected");
			return;
		}
		computerBoard.fire();

		if(!computerBoard.hasShips())
		{
			Log.debug("Player Wins!");
			end();
		}
		Log.debug("Computer's board still has ships.");

		playerBoard.setFiringCoordinate(computer.getFiringCoordinate());
		computer.logResult(playerBoard.fire());

		if(!playerBoard.hasShips())
		{
			Log.debug("Computer Wins!");
			end();
		}
		Log.debug("Player's board still has ships.");
	}

	protected void end()
	{
		if(!computerBoard.hasShips())
		{
			showMessage("Player Wins!");
		}
		else
		{
			showMessage("Computer Wins!");
		}
		deactivate();
	}

	private void deactivate()
	{
		computerBoard.deactivate();
		playerBoard.deactivate();
	}

	protected void setupComplete()
	{
		showMessage("Fire Away!");
		playerBoard.deactivate();
		computerBoard.activate();
	}

	protected void showMessage(String message)
	{
		controlPanel.displayPanel.setText(message);
	}

	private void displayOptions()
	{
		Log.debug("Displaying Options");
	}

	private class ControlPanel extends JPanel
	{
		JButton newGameButton;
		JLabel displayPanel;
		JButton optionsButton;
		ControlPanel()
		{
			setSize(new Dimension(Battleship.BOARD_DIMENSION * 30, Battleship.BOARD_DIMENSION * 10));
			setLayout(new BorderLayout());

			newGameButton = new JButton("New");
			displayPanel = new JLabel(Battleship.BLANK_SYMBOL, SwingConstants.CENTER);
			optionsButton = new JButton("Options");

			newGameButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					startGame();
				}
			});
			optionsButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					displayOptions();
				}
			});

			add(newGameButton, BorderLayout.LINE_START);
			add(displayPanel, BorderLayout.CENTER);
			add(optionsButton, BorderLayout.LINE_END);
		}
	}
}
