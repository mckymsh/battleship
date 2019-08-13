package battleship3;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*; // Why, Java

public class Board extends JPanel
{
	protected boolean isPlayerBoard;
	protected String name;
	protected Game game;
	
	protected Cell[] cells;
	protected Ship[] ships;

	private boolean shipPlacementInProgress;
	private int startCoordinate;
	private int currentShipType;
		
	Board(boolean isPlayerBoard, String name, Game game)
	{
		this.isPlayerBoard = isPlayerBoard;
		this.name = name;
		this.game = game;
		
		cells = new Cell[Battleship.BOARD_DIMENSION * Battleship.BOARD_DIMENSION];
		ships = new Ship[Battleship.SHIP_NAMES.length];
		
		setPreferredSize(new Dimension(Battleship.BOARD_DIMENSION * 30, Battleship.BOARD_DIMENSION * 30));
		setLayout(new GridLayout(Battleship.BOARD_DIMENSION, Battleship.BOARD_DIMENSION));
		for(int i = 0; i < cells.length; i++)
		{
			cells[i] = new Cell(i, this);
			add(cells[i]);
		}
		
		// Remember that ship type 0 is no ship
		for (int i = 0; i < ships.length; i++)
		{
			ships[i] = new Ship(i);			
		}

		shipPlacementInProgress = false;
		currentShipType = 1;
	}

	protected void reset()
	{
		removeAll();
		for(int i = 0; i < cells.length; i++)
		{
			cells[i] = new Cell(i, this);
			add(cells[i]);			
		}

		if(isPlayerBoard)
		{
			activate();	
		} 
		
		currentShipType = 1;
		// Remember that ship type 0 is no ship
		ships[0] = new Ship(0);
		for (int i = 1; i < ships.length; i++)
		{
			ships[i] = new Ship(i);
		}

		revalidate();
		repaint();
	}

	protected void activate()
	{
		for(Cell cell : cells)
		{
			cell.activate();
		}
	}

	protected boolean placeShip(int selectedCoordinate)
	{
		// If this is our first click for this ship.
		if(!shipPlacementInProgress)
		{
			if(cells[selectedCoordinate].hasShip)
			{
				Log.debug("Cell Already Has Ship");
				return false;
			}
			cells[selectedCoordinate].addShip(currentShipType);
			shipPlacementInProgress = true;
			startCoordinate = selectedCoordinate;
			return true;
		}

		// If the square is clicked again before the second selectedCoordinate,
		// it cancels the placement.
		if(selectedCoordinate == startCoordinate)
		{
			cells[selectedCoordinate].removeShip();
			shipPlacementInProgress = false;
			return false;
		}



		int shipOrientation = Battleship.NONE;
		if((selectedCoordinate / Battleship.BOARD_DIMENSION) == (startCoordinate / Battleship.BOARD_DIMENSION))
		{
			if((selectedCoordinate > startCoordinate))
			{
				shipOrientation = Battleship.EAST;
			}
			else
			{
				shipOrientation = Battleship.WEST;
			}
		}
		else if((selectedCoordinate % Battleship.BOARD_DIMENSION) == (startCoordinate % Battleship.BOARD_DIMENSION))
		{
			if((selectedCoordinate > startCoordinate))
			{
				shipOrientation = Battleship.SOUTH;
			}
			else
			{
				shipOrientation = Battleship.NORTH;
			}
		}
		else
		{
			Log.debug("Cells Not Aligned");
			return false;
		}

		if(shipOrientation == Battleship.NORTH)
		{
			// Check North
			if(((startCoordinate / 10) - (Battleship.SHIP_LENGTHS[currentShipType]-1)) < 0)
			{
				return false;
			}
			// Are there any ships in the way?
			for(int i = 1; i < Battleship.SHIP_LENGTHS[currentShipType]; i++)
			{
				if(cells[startCoordinate - (i * Battleship.BOARD_DIMENSION)].hasShip)
				{
					return false;
				}
			}
			for(int i = 0; i < Battleship.SHIP_LENGTHS[currentShipType]; i++)
			{
				cells[startCoordinate - (i*10)].addShip(currentShipType);
			}
		}
		else if (shipOrientation == Battleship.EAST)
		{
			// Is it too long for the space?
			if(((startCoordinate % 10) + Battleship.SHIP_LENGTHS[currentShipType]) > Battleship.BOARD_DIMENSION)
			{
				return false;
			}
			// Are there any ships in the way?
			for(int i = 1; i < Battleship.SHIP_LENGTHS[currentShipType]; i++)
			{
				if(cells[startCoordinate + i].hasShip)
				{
					return false;
				}
			}

			for(int i = 0; i < Battleship.SHIP_LENGTHS[currentShipType]; i++)
			{
				cells[startCoordinate + i].addShip(currentShipType);
			}
		}
		else if(shipOrientation == Battleship.SOUTH)
		{
			// Is it too long for the (now, vertical) space?
			if(((startCoordinate / 10) + Battleship.SHIP_LENGTHS[currentShipType]) > Battleship.BOARD_DIMENSION)
			{
				return false;
			}
			// Are there any ships in the way?
			for(int i = 1; i < Battleship.SHIP_LENGTHS[currentShipType]; i++)
			{
				if(cells[startCoordinate + (i * Battleship.BOARD_DIMENSION)].hasShip)
				{
					return false;
				}
			}
			for(int i = 0; i < Battleship.SHIP_LENGTHS[currentShipType]; i++)
			{
				cells[startCoordinate + (i*10)].addShip(currentShipType);
			}
		}
		else if(shipOrientation == Battleship.WEST)
		{
			// Is it too long for the space?
			if(((startCoordinate % 10) - (Battleship.SHIP_LENGTHS[currentShipType]-1)) < 0)
			{
				return false;
			}
			// Are there any ships in the way?
			for(int i = 1; i < Battleship.SHIP_LENGTHS[currentShipType]; i++)
			{
				if(cells[startCoordinate - i].hasShip)
				{
					return false;
				}
			}

			for(int i = 0; i < Battleship.SHIP_LENGTHS[currentShipType]; i++)
			{
				cells[startCoordinate - i].addShip(currentShipType);
			}
		}
		else
		{
			// If this happens, something has gone horribly wrong.
			Log.debug("Ship Has No Orientation");
			return false;
		}

		// If we made it here, all is well.
		shipPlacementInProgress = false;
		Log.debug("Current shipType before increment = " + currentShipType);
		currentShipType++;
		if (!(currentShipType < ships.length))
		{
			allShipsPlaced();
		}
		return true;
	}
	
	// This method places the designated ship at the designated coordinates.
	protected void addShip(int currentShipType, int coordinate, boolean horizontal)
	{
		if(horizontal)
		{
			// Add ship to the relevant cells.
			for(int i = 0; i < Battleship.SHIP_LENGTHS[currentShipType]; i++)
			{
				cells[coordinate + i].addShip(currentShipType);
			}
		}
		else
		{
			// Add ship to the relevant cells.
			for(int i = 0; i < Battleship.SHIP_LENGTHS[currentShipType]; i++)
			{
				cells[coordinate + (i*10)].addShip(currentShipType);
			}
		}
		// If we made it here, all is well.
	}

	private void allShipsPlaced()
	{
		for(Cell cell : cells)
		{
			cell.deactivate();
		}
		game.setupComplete();
	}


	// Checks to see if any ships are afloat.
	protected boolean hasShips()
	{
		for(Ship s : ships)
		{
			// If any aren't sunk
			if(!s.isSunk)
			{
				// Hooray, we have ships
				return true;
			}
		}
		// Boo, no ships. We lose.
		return false;
	}
	
	// Adds a hit to the cell and any ship that's on it.
	protected boolean addHit(int coordinate)
	{
		if(cells[coordinate].state == Battleship.MISS_SYMBOL)
		{
			Log.debug("You've already fired there.");
			return false;
		}
			
		// If there's a ship there...
		if(cells[coordinate].hasShip)
		{
			// Let 'em know
			Log.debug("That's a hit!");
			
			// If we've already been hit here:
			// * Don't add hits to the ship
			// * Count it as a miss?
			if(		(cells[coordinate].state == Battleship.HIT_SYMBOL)
				||	(cells[coordinate].state == Battleship.SUNK_SYMBOL))
			{
				Log.debug("... but you've already shot there!");
				return true;
			}
			
			// If this hit sank the ship
			if(ships[cells[coordinate].shipId].addHit())
			{
				// You know the commercial-- ya gotta say it if ya can!
				if(cells[coordinate].shipId == 2)
				{
					Log.debug("You sunk my Battleship!");
				}
				else
				{
					Log.debug(Battleship.SHIP_NAMES[cells[coordinate].shipId] + " has been sunk!");
				}
				
				// Update the cell
				cells[coordinate].state = Battleship.SUNK_SYMBOL;
				
				// If it's sunk, make sure all of the cells match.
				// No longer necessary in this version, but it does no harm.
				for(int i = 0; i < cells.length; i++)
				{
					if(cells[i].shipId == cells[coordinate].shipId)
					{
						cells[i].state = Battleship.SUNK_SYMBOL;
					}
				}
			}
			else
			{
				// If it's hit but not sunk.
				cells[coordinate].state = Battleship.HIT_SYMBOL;
			}		
			
			// Finally, return a hit.
			return true;	
		}
		else
		{
			// *Sad trombones*
			Log.debug("That's a miss! :(");
			cells[coordinate].state = Battleship.MISS_SYMBOL;
			return false;
		}	
	}
}
