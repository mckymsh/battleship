package battleship3;

import java.util.Random;
import java.util.Stack;
import java.util.LinkedList;

public class AI
{
	private int firingCoordinate;
	private int lastCoordinate;
	private Game game;
	private Random random;
	
	private Board resultsBoard;
	private Stack<Integer> history;
	private LinkedList<Integer> candidateList;

	public AI(Game game)
	{
		game = game;
		firingCoordinate = -1;
		lastCoordinate = firingCoordinate;
		random = new Random();
		resultsBoard = new Board("Computer Results Board");
		history = new Stack<Integer>();
		candidateList = new LinkedList<Integer>();
	}

	// Returns a coordinate to the game,
	// which it adds to the board for firing.
	protected int getFiringCoordinate()
	{
		// Easy peasy mode
		// do
		// {
		// 	firingCoordinate = random.nextInt(100);
		// }
		// while(results[firingCoordinate] != 0);

		if(candidateList.peekFirst() == null)
		{
			do
			{
				firingCoordinate = random.nextInt(100);
			}
			while(history.search(new Integer(firingCoordinate)) >= 0);
		}
		else
		{
			firingCoordinate = candidateList.pop().intValue();
		}

		history.push(new Integer(firingCoordinate));
		return firingCoordinate;
	}

	// Logs the result of a volley as reported through the fire() method in Game.
	protected void logResult(boolean success)
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

		if(success)
		{
			resultsBoard.cells[lastCoordinate].state = Battleship.HIT_SYMBOL;
			addAllAdjacentCells(lastCoordinate);
		}
		else
		{
			resultsBoard.cells[lastCoordinate].state = Battleship.MISS_SYMBOL;
		}
	}

	private void addAllAdjacentCells(int coordinate)
	{
		int checkCoordinate = coordinate - Battleship.BOARD_DIMENSION;
		if(!((coordinate / 10) - 1 < 0))
		{
			// NOT Out of bounds NORTH
			if(resultsBoard.cells[checkCoordinate].state == Battleship.BLANK_SYMBOL
				&& history.search(new Integer(checkCoordinate)) < 0)
			{
				candidateList.addLast(new Integer(checkCoordinate));
			}
		}

		checkCoordinate = coordinate + 1;
		if(!((coordinate % 10) + 1 > Battleship.BOARD_DIMENSION - 1))
		{
			// NOT Out of bounds EAST
			if(resultsBoard.cells[checkCoordinate].state == Battleship.BLANK_SYMBOL
				&& history.search(new Integer(checkCoordinate)) < 0)
			{
				candidateList.addLast(new Integer(checkCoordinate));
			}
		}

		checkCoordinate = coordinate + Battleship.BOARD_DIMENSION;
		if(!((coordinate / 10) + 1 > Battleship.BOARD_DIMENSION - 1))
		{
			// NOT Out of bounds SOUTH
			if(resultsBoard.cells[checkCoordinate].state == Battleship.BLANK_SYMBOL
				&& history.search(new Integer(checkCoordinate)) < 0)
			{
				candidateList.addLast(new Integer(checkCoordinate));
			}
		}

		checkCoordinate = coordinate - 1;
		if(!((coordinate % Battleship.BOARD_DIMENSION) - 1 < 0))
		{
			// NOT Out of bounds WEST
			if(resultsBoard.cells[checkCoordinate].state == Battleship.BLANK_SYMBOL
				&& history.search(new Integer(checkCoordinate)) < 0)
			{
				candidateList.addLast(new Integer(checkCoordinate));
			}
		}
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