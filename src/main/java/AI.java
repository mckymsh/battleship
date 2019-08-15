package battleship3;

import java.util.Random;

public class AI
{
	private int firingCoordinate;
	private int lastCoordinate;
	private Game game;
	private Random random;
	private int[] results;

	public AI(Game game)
	{
		game = game;
		firingCoordinate = -1;
		lastCoordinate = firingCoordinate;
		random = new Random();
		results = new int[Battleship.BOARD_DIMENSION * Battleship.BOARD_DIMENSION];
	}

	protected int getFiringCoordinate()
	{
		if(firingCoordinate < 0)
		{
			lastCoordinate = 0;
		}
		else
		{
			lastCoordinate = firingCoordinate;
		}
		
		if(results[lastCoordinate] == 2)
		{
			Log.debug("AI's last shot hit.");
		}
		else if(results[lastCoordinate] == 1)
		{
			Log.debug("AI's last shot missed");
		}
		else
		{
			Log.debug("This is AI's first shot.");
		}

		// Easy peasy mode
		do
		{
			firingCoordinate = random.nextInt(100);
		}
		while(results[firingCoordinate] != 0);

		return firingCoordinate;
	}

	// Logs the result of a volley as reported through the fire() method in Game.
	protected void logResult(boolean success)
	{
		results[firingCoordinate] = success ? 2 : 1;
	}

	protected Board placeShips(Board board)
	{
		int coordinate = -1;
		int orientation = Battleship.NONE;
		boolean success = false;

		// For all ships
		for(int i = 1; i < board.ships.length; i++)
		{			
			// Brute force.
			// Randomly tries ship locations until
			// it finds something that works.
			do
			{
				coordinate = random.nextInt(100);
				orientation = random.nextInt(4) + 1; // i.e. 1-4, not zero
				Log.debug("ShipType " + i + ", " + coordinate + "->" + orientation);
			}
			while(!board.placeShip(coordinate, orientation));
			Log.debug("Ship " + i + " placed successfully.");
		}	
		// board.placeShip(0, 2);
		// board.placeShip(10, 2);
		// board.placeShip(20, 2);
		// board.placeShip(30, 2);
		// board.placeShip(40, 2);
		// Returns board, now with ships.
		return board;
	}
}