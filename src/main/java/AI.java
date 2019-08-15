package battleship3;

import java.util.Random;

public class AI
{
	private int firingCoordinate;
	private Game game;
	private Random random;
	private Board resultBoard;

	AI(Game game)
	{
		firingCoordinate = -1;
		game = game;
		random = new Random();
		resultBoard = new Board(false, "Result Board", this.game);
	}

	protected int getFiringCoordinate(Board board)
	{
		firingCoordinate = random.nextInt(100);
		return firingCoordinate;
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

	// Logs the result of a volley as reported through the fire() method in Game.
	protected void logResult(boolean success)
	{
		resultBoard.cells[firingCoordinate].state = success ? Battleship.HIT_SYMBOL : Battleship.MISS_SYMBOL;
	}
}