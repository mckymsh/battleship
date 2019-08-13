package battleship3;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*; // Why, Java

public class Game extends JFrame
{
	public int state;

	JLabel displayPanel;
	Board playerBoard;
	Board computerBoard;

	Game ()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setTitle(Battleship.WINDOW_TITLE);
		setLayout(new BorderLayout());		
		computerBoard = new Board(false, Battleship.COMPUTER_BOARD_NAME, this);
		playerBoard = new Board(true, Battleship.PLAYER_BOARD_NAME, this);
		add(computerBoard, BorderLayout.PAGE_START);
		add(buildControlPanel(), BorderLayout.CENTER);
		add(playerBoard, BorderLayout.PAGE_END);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	protected void run()
	{
		// Game happens here
	}

	private void setup()
	{
		state = Battleship.SETUP;
		Log.debug("Setup!");
		resetBoards();
		displayPanel.setText("Place Your Ships");
	}

	private void resetBoards()
	{
		computerBoard.reset();
		playerBoard.reset();
	}

	private JPanel buildControlPanel()
	{
		JPanel controlPanel = new JPanel();
		controlPanel.setSize(new Dimension(Battleship.BOARD_DIMENSION * 30, Battleship.BOARD_DIMENSION * 10));
		controlPanel.setLayout(new BorderLayout());

		JButton newGameButton = new JButton("New");
		displayPanel = new JLabel("Welcome to Battleship", SwingConstants.CENTER);
		// displayPanel.setBorder(new LineBorder(Color.BLACK));
		JButton fireButton = new JButton("Fire");

		newGameButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setup();
			}
		});

		controlPanel.add(newGameButton, BorderLayout.LINE_START);
		controlPanel.add(displayPanel, BorderLayout.CENTER);
		controlPanel.add(fireButton, BorderLayout.LINE_END);

		return controlPanel;
	}
}
