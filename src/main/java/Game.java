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

		run();
	}
	
	protected void run()
	{
		// Game happens here
		newGame();
	}

	private void volley()
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
			end();
		}

		playerBoard.setFiringCoordinate(computer.getFiringCoordinate(playerBoard));
		computer.logResult(playerBoard.fire());

		if(!playerBoard.hasShips())
		{
			end();
		}
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

	private void newGame()
	{
		Log.debug("Setup");
		resetBoards();
		computerBoard = computer.placeShips(computerBoard);
		computerBoard.revalidate();
		computerBoard.repaint();
		// Start Computer setup
		playerBoard.activate();
		showMessage("Place Your Ships");
	}

	private void resetBoards()
	{
		computerBoard.reset();
		playerBoard.reset();
	}

	private void deactivate()
	{
		controlPanel.deactivateFireButton();
		computerBoard.deactivate();
		playerBoard.deactivate();
	}

	protected void setupComplete()
	{
		playerBoard.deactivate();
		computerBoard.activate();
		controlPanel.activateFireButton();
	}

	protected void showMessage(String message)
	{
		controlPanel.displayPanel.setText(message);
	}

	private class ControlPanel extends JPanel
	{
		JButton newGameButton;
		JLabel displayPanel;
		JButton fireButton;
		ActionListener fireButtonListener;
		ControlPanel()
		{
			setSize(new Dimension(Battleship.BOARD_DIMENSION * 30, Battleship.BOARD_DIMENSION * 10));
			setLayout(new BorderLayout());

			newGameButton = new JButton("New");
			displayPanel = new JLabel("Welcome to Battleship", SwingConstants.CENTER);
			fireButton = new JButton("Fire");

			newGameButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					newGame();
				}
			});
			fireButtonListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					Log.debug("Fire Clicked");
					volley();
				}
			};

			add(newGameButton, BorderLayout.LINE_START);
			add(displayPanel, BorderLayout.CENTER);
			add(fireButton, BorderLayout.LINE_END);
		}

		protected void deactivateFireButton()
		{
			fireButton.removeActionListener(fireButtonListener);
		}

		protected void activateFireButton()
		{
			Log.debug("Adding fireButtonListener");
			fireButton.addActionListener(fireButtonListener);
		}
	}
}
