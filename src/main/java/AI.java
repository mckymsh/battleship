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

	// Returns a coordinate to the game,
	// which it adds to the board for firing.
	protected int getFiringCoordinate()
	{
		// AKA if this is our first shot.
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
			Log.debug("This should not happen.");
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

	// Places ships on the provided board and returns it.
	// In the future, I may pre-make some boards in some way.
	// Maybe even some abstract boards where the ships are 
	// all broken up or spell something.
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
			// This is a low-priority fix since
			// it doesn't really affect anything.
			do
			{
				coordinate = random.nextInt(100);
				orientation = random.nextInt(4) + 1; // i.e. 1-4, not zero
				Log.debug("Attempting ShipType " + i + " at " 
					+ coordinate + " pointing " + Battleship.ORIENTATIONS[orientation]);
			}
			while(!board.placeShip(coordinate, orientation));
			Log.debug("Ship " + i + " placed successfully.");
		}	
		
		// Returns board, now with ships.
		return board;
	}
}