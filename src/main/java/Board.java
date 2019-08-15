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
	private int currentCoordinate;
	private int currentShipType;

	protected int firingCoordinate;
		
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

		firingCoordinate = -1;
	}

	protected void reset()
	{
		removeAll();
		for(int i = 0; i < cells.length; i++)
		{
			cells[i] = new Cell(i, this);
			add(cells[i]);			
		}
		
		currentShipType = 1;
		// Remember that ship type 0 is no ship
		for (int i = 0; i < ships.length; i++)
		{
			ships[i] = new Ship(i);
		}

		revalidate();
		repaint();
	}

	protected void activate()
	{
		if(isPlayerBoard)
		{
			for(Cell cell : cells)
			{
				cell.activatePlaceShips();
			}
		}
		else
		{
			Log.debug("Should be activating computer board");
			for(Cell cell : cells)
			{
				cell.activateSelectCoordinates();
			}
		}
	}

	protected void deactivate()
	{
		for(Cell cell : cells)
		{
			cell.deactivate();
		}
	}

	protected void setFiringCoordinate(int currentCoordinate)
	{
		if(currentCoordinate == firingCoordinate) // Clicking again should cancel
		{
			cells[currentCoordinate].defaultBackground();
			if(!isPlayerBoard) game.showMessage("Targeting Cancelled");
			firingCoordinate = -1;
			return;
		}

		if(!(firingCoordinate < 0))
		{
			cells[firingCoordinate].defaultBackground(); // Unused cell back to blank
		}
		firingCoordinate = currentCoordinate;

		if(!cells[firingCoordinate].state.equals(Battleship.BLANK_SYMBOL))
		{
			if(!isPlayerBoard) game.showMessage("You've already fired there.");
			firingCoordinate = -1;
			return;
		}
		
		cells[firingCoordinate].setBackground(Battleship.TARGET_COLOR);
		if(!isPlayerBoard) game.showMessage("Targeting Cell " + firingCoordinate);
	}

	// This method is an absolute disaster. Fix this.
	protected boolean addCoordinate(int coordinate)
	{
		currentCoordinate = coordinate;
		// If this is our first click for this ship.
		if(!shipPlacementInProgress)
		{
			Log.debug("currentShipType = " + currentShipType);
			if(cells[currentCoordinate].hasShip)
			{
				if(!isPlayerBoard) game.showMessage("Cell Already Has Ship");
				return false;
			}
			if(!isPlayerBoard) game.showMessage("Placing " + Battleship.SHIP_NAMES[currentShipType]);
			if(isPlayerBoard) cells[currentCoordinate].setBackground(Battleship.SHIP_COLOR);
			shipPlacementInProgress = true;
			startCoordinate = currentCoordinate;
			return true;
		}

		// If the square is clicked again before the second currentCoordinate,
		// it cancels the placement.
		if(currentCoordinate == startCoordinate)
		{
			cells[currentCoordinate].defaultBackground();
			shipPlacementInProgress = false;
			cancelPlacement("Placement Cancelled");
			return false;
		}

		int shipOrientation = getShipOrientation();

		if(!placeShip(startCoordinate, shipOrientation))
		{
			return false;
		}

		// If we made it here, all is well.
		if(!isPlayerBoard) game.showMessage("Ship Placed!");
		shipPlacementInProgress = false;
		if (!(currentShipType < ships.length))
		{
			game.setupComplete();
		}
		return true;
	}

	protected boolean placeShip(int coordinate, int shipOrientation)
	{
		if(cells[coordinate].hasShip)
		{
			return false;
		}
		if(shipOrientation == Battleship.NORTH)
		{
			// Check North
			if(((coordinate / 10) - (Battleship.SHIP_LENGTHS[currentShipType]-1)) < 0)
			{
				cancelPlacement("Out of Bounds");
				return false;
			}
			// Are there any ships in the way?
			for(int i = 1; i < Battleship.SHIP_LENGTHS[currentShipType]; i++)
			{
				int checkCoord = coordinate - (i * Battleship.BOARD_DIMENSION);
				if(cells[checkCoord].hasShip)
				{
					cancelPlacement("Blocked by " + Battleship.SHIP_NAMES[cells[checkCoord].shipId]);
					return false;
				}
			}
			for(int i = 0; i < Battleship.SHIP_LENGTHS[currentShipType]; i++)
			{
				cells[coordinate - (i*10)].addShip(currentShipType);
				if(isPlayerBoard) cells[coordinate - (i*10)].setBackground(Battleship.SHIP_COLOR);
			}
		}
		else if (shipOrientation == Battleship.EAST)
		{
			// Is it too long for the space?
			if(((coordinate % 10) + Battleship.SHIP_LENGTHS[currentShipType]) > Battleship.BOARD_DIMENSION)
			{
				cancelPlacement("Out of Bounds");
				return false;
			}
			// Are there any ships in the way?
			for(int i = 1; i < Battleship.SHIP_LENGTHS[currentShipType]; i++)
			{
				int checkCoord = coordinate + i;
				if(cells[checkCoord].hasShip)
				{
					cancelPlacement("Blocked by " + Battleship.SHIP_NAMES[cells[checkCoord].shipId]);
					return false;
				}
			}

			for(int i = 0; i < Battleship.SHIP_LENGTHS[currentShipType]; i++)
			{
				cells[coordinate + i].addShip(currentShipType);
				if(isPlayerBoard) cells[coordinate + i].setBackground(Battleship.SHIP_COLOR);
			}
		}
		else if(shipOrientation == Battleship.SOUTH)
		{
			// Is it too long for the (now, vertical) space?
			if(((coordinate / 10) + Battleship.SHIP_LENGTHS[currentShipType]) > Battleship.BOARD_DIMENSION)
			{
				cancelPlacement("Out of Bounds");
				return false;
			}
			// Are there any ships in the way?
			for(int i = 1; i < Battleship.SHIP_LENGTHS[currentShipType]; i++)
			{
				int checkCoord = coordinate + (i * Battleship.BOARD_DIMENSION);
				if(cells[checkCoord].hasShip)
				{
					cancelPlacement("Blocked by " + Battleship.SHIP_NAMES[cells[checkCoord].shipId]);
					return false;
				}
			}
			for(int i = 0; i < Battleship.SHIP_LENGTHS[currentShipType]; i++)
			{
				cells[coordinate + (i*10)].addShip(currentShipType);
				if(isPlayerBoard) cells[coordinate + (i*10)].setBackground(Battleship.SHIP_COLOR);
			}
		}
		else if(shipOrientation == Battleship.WEST)
		{
			// Is it too long for the space?
			if(((coordinate % 10) - (Battleship.SHIP_LENGTHS[currentShipType]-1)) < 0)
			{
				cancelPlacement("Out of Bounds");
				return false;
			}
			// Are there any ships in the way?
			for(int i = 1; i < Battleship.SHIP_LENGTHS[currentShipType]; i++)
			{
				int checkCoord = coordinate - i;
				if(cells[checkCoord].hasShip)
				{
					cancelPlacement("Blocked by " + Battleship.SHIP_NAMES[cells[checkCoord].shipId]);
					return false;
				}
			}

			for(int i = 0; i < Battleship.SHIP_LENGTHS[currentShipType]; i++)
			{
				cells[coordinate - i].addShip(currentShipType);
				if(isPlayerBoard) cells[coordinate - i].setBackground(Battleship.SHIP_COLOR);
			}
		}
		else
		{
			cancelPlacement("Cells Not Aligned");
			return false;
		}

		currentShipType++;

		// Did we make it?
		return true;
	}

	private int getShipOrientation()
	{
		if((currentCoordinate / Battleship.BOARD_DIMENSION) == (startCoordinate / Battleship.BOARD_DIMENSION))
		{
			if((currentCoordinate > startCoordinate))
			{
				return Battleship.EAST;
			}
			else
			{
				return Battleship.WEST;
			}
		}
		else if((currentCoordinate % Battleship.BOARD_DIMENSION) == (startCoordinate % Battleship.BOARD_DIMENSION))
		{
			if((currentCoordinate > startCoordinate))
			{
				return Battleship.SOUTH;
			}
			else
			{
				return Battleship.NORTH;
			}
		}
		else
		{
			return Battleship.NONE;
		}
	}

	protected void cancelPlacement(String reason)
	{
		Log.debug(startCoordinate + " " + currentCoordinate + " " + reason);
		if(!isPlayerBoard) game.showMessage(reason);
		cells[startCoordinate].defaultBackground();
		cells[currentCoordinate].defaultBackground();
		shipPlacementInProgress = false;
	}

	// Checks to see if any ships are afloat.
	protected boolean hasShips()
	{
		Log.debug("Do we have ships?");
		for(Ship s : ships)
		{
			Log.debug(s.name + " has " + s.hits + " hits and isSunk is " + s.isSunk);
			// If any aren't sunk
			if(!s.isSunk)
			{
				// Hooray, we have ships
				Log.debug("Yes.");
				return true;
			}
		}
		Log.debug("No.");
		// Boo, no ships. We lose.
		return false;
	}
	
	protected boolean fire()
	{
		if(firingCoordinate < 0)
		{
			if(!isPlayerBoard) game.showMessage("Must Select Coordinate");
			return false;
		}
		boolean result = addHit();
		cells[firingCoordinate].deactivate();
		firingCoordinate = -1;
		return result;
	}

	// Adds a hit to the cell and any ship that's on it.
	protected boolean addHit()
	{
		if(cells[firingCoordinate].state == Battleship.MISS_SYMBOL)
		{
			if(!isPlayerBoard) game.showMessage("You've already fired there.");
			cells[firingCoordinate].setBackground(Battleship.MISS_COLOR);
			cells[firingCoordinate].state = Battleship.MISS_SYMBOL;
			return false;
		}
			
		// If there's a ship there...
		if(cells[firingCoordinate].hasShip)
		{
			cells[firingCoordinate].setBackground(Battleship.HIT_COLOR);
			// Let 'em know
			String message = "Hit!";
			
			// If we've already been hit here:
			// * Don't add hits to the ship
			// * Count it as a miss?
			if(		(cells[firingCoordinate].state == Battleship.HIT_SYMBOL)
				||	(cells[firingCoordinate].state == Battleship.SUNK_SYMBOL))
			{
				message += " ... again.";
				if(!isPlayerBoard) game.showMessage(message);
				return true;
			}
			else
			{
				if(!isPlayerBoard) game.showMessage(message);
			}
			
			// If this hit sank the ship
			if(ships[cells[firingCoordinate].shipId].addHit())
			{
				cells[firingCoordinate].setBackground(Battleship.SUNK_COLOR);
				// You know the commercial-- ya gotta say it if ya can!
				if(cells[firingCoordinate].shipId == 2)
				{
					if(!isPlayerBoard) game.showMessage("You sunk my Battleship!");
				}
				else
				{
					if(!isPlayerBoard) game.showMessage(Battleship.SHIP_NAMES[cells[firingCoordinate].shipId] + " has sunk!");
				}
				
				// Update the cell
				cells[firingCoordinate].state = Battleship.SUNK_SYMBOL;
				
				// If it's sunk, make sure all of the cells match.
				// No longer necessary in this version, but it does no harm.
				for(int i = 0; i < cells.length; i++)
				{
					if(cells[i].shipId == cells[firingCoordinate].shipId)
					{
						cells[i].setBackground(Battleship.SUNK_COLOR);
						cells[i].state = Battleship.SUNK_SYMBOL;
					}
				}
				if(!hasShips())
				{
					Log.debug("Game should be over.");
					game.end();
				}
			}
			else
			{
				// If it's hit but not sunk.
				cells[firingCoordinate].setBackground(Battleship.HIT_COLOR);
				cells[firingCoordinate].state = Battleship.HIT_SYMBOL;
			}		
			
			// Finally, return a hit.
			return true;	
		}
		else
		{
			// *Sad trombones*
			if(!isPlayerBoard) game.showMessage("That's a miss! :(");
			cells[firingCoordinate].setBackground(Battleship.MISS_COLOR);
			cells[firingCoordinate].state = Battleship.MISS_SYMBOL;
			return false;
			}	
	}
}