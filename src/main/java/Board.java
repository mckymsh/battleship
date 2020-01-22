package battleship;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*; // Why, Java

public class Board extends JPanel
{
	protected boolean isPlayerBoard;
	protected String name;
	protected Controller game;
	
	protected Cell[] cells;
	protected Ship[] ships;

	private boolean shipPlacementInProgress;
	private int startCoordinate;
	private int currentCoordinate;
	private int currentShipType;

	protected int firingCoordinate;
		
	Board(boolean isPlayerBoard, String name, Controller game)
	{
		this.isPlayerBoard = isPlayerBoard;
		this.name = name;
		this.game = game;
		
		cells = new Cell[battleship.Constants.BOARD_DIMENSION * battleship.Constants.BOARD_DIMENSION];
		ships = new Ship[battleship.Constants.SHIP_NAMES.length];
		
		setPreferredSize(new Dimension(battleship.Constants.BOARD_DIMENSION * 30, battleship.Constants.BOARD_DIMENSION * 30));
		setLayout(new GridLayout(battleship.Constants.BOARD_DIMENSION, battleship.Constants.BOARD_DIMENSION));
		
		// reset();
	}

	// This constructor is only used by the AI for record-keeping.
	Board(String name)
	{
		this.name = name;
		
		cells = new Cell[battleship.Constants.BOARD_DIMENSION * battleship.Constants.BOARD_DIMENSION];
		ships = new Ship[battleship.Constants.SHIP_NAMES.length];

		for(int i = 0; i < cells.length; i++)
		{
			cells[i] = new Cell(i, this);
			add(cells[i]);			
		}
		
		// Remember that ship type 0 is no ship
		for (int i = 0; i < ships.length; i++)
		{
			ships[i] = new Ship(0);
		}
	}

	protected void reset()
	{
		shipPlacementInProgress = false;
		currentShipType = 1;
		firingCoordinate = -1;

		removeAll();
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

		revalidate();
		repaint();
	}

	// For these two methods, it might be better to just have a 'mask' panel appear over top,
	// rather than changing all of the cells individually. Food for thought.
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
			Log.debug("Activating computer board");
			for(Cell cell : cells)
			{
				cell.activateSelectCoordinates();
			}
		}
	}

	protected void deactivate()
	{
		Log.debug("Deactivating computer board");
		for(Cell cell : cells)
		{
			cell.deactivate();
		}
	}

	protected void setFiringCoordinate(int currentCoordinate)
	{
		// If the same cell is clicked a second time
		if(currentCoordinate == firingCoordinate)
		{
			// shoot
			return;
		}

		// If firing coordinate has been set
		if(firingCoordinate > -1)
		{
			cells[firingCoordinate].defaultBackground(); // Unused cell back to blank
		}
		firingCoordinate = currentCoordinate;

		if(cells[firingCoordinate].state != battleship.Constants.State.BLANK)
		{
			// if(!isPlayerBoard) game.showMessage("You've already fired there.");
			firingCoordinate = -1;
			return;
		}
		
		cells[firingCoordinate].setBackground(battleship.Constants.TARGET_COLOR);
		// if(!isPlayerBoard) game.showMessage("Press Again to Fire");
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
				// if(!isPlayerBoard) game.showMessage("Cell Already Has Ship");
				return false;
			}
			// if(isPlayerBoard) game.showMessage("Placing " + battleship.Constants.SHIP_NAMES[currentShipType]);
			if(isPlayerBoard) cells[currentCoordinate].setBackground(battleship.Constants.SHIP_COLOR);
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
		// if(isPlayerBoard) game.showMessage("Ship Placed!");
		shipPlacementInProgress = false;
		if (!(currentShipType < ships.length))
		{
			// game.setupComplete();
		}
		return true;
	}

	protected boolean placeShip(int coordinate, int shipOrientation)
	{
		if(cells[coordinate].hasShip)
		{
			return false;
		}
		if(shipOrientation == battleship.Constants.NORTH)
		{
			// Check North
			if(((coordinate / 10) - (battleship.Constants.SHIP_LENGTHS[currentShipType]-1)) < 0)
			{
				cancelPlacement("Out of Bounds");
				return false;
			}
			// Are there any ships in the way?
			for(int i = 1; i < battleship.Constants.SHIP_LENGTHS[currentShipType]; i++)
			{
				int checkCoord = coordinate - (i * battleship.Constants.BOARD_DIMENSION);
				if(cells[checkCoord].hasShip)
				{
					cancelPlacement("Blocked by " + battleship.Constants.SHIP_NAMES[cells[checkCoord].shipId]);
					return false;
				}
			}
			for(int i = 0; i < battleship.Constants.SHIP_LENGTHS[currentShipType]; i++)
			{
				cells[coordinate - (i*10)].addShip(currentShipType);
				if(isPlayerBoard) cells[coordinate - (i*10)].setBackground(battleship.Constants.SHIP_COLOR);
			}
		}
		else if (shipOrientation == battleship.Constants.EAST)
		{
			// Is it too long for the space?
			if(((coordinate % 10) + battleship.Constants.SHIP_LENGTHS[currentShipType]) > battleship.Constants.BOARD_DIMENSION)
			{
				cancelPlacement("Out of Bounds");
				return false;
			}
			// Are there any ships in the way?
			for(int i = 1; i < battleship.Constants.SHIP_LENGTHS[currentShipType]; i++)
			{
				int checkCoord = coordinate + i;
				if(cells[checkCoord].hasShip)
				{
					cancelPlacement("Blocked by " + battleship.Constants.SHIP_NAMES[cells[checkCoord].shipId]);
					return false;
				}
			}

			for(int i = 0; i < battleship.Constants.SHIP_LENGTHS[currentShipType]; i++)
			{
				cells[coordinate + i].addShip(currentShipType);
				if(isPlayerBoard) cells[coordinate + i].setBackground(battleship.Constants.SHIP_COLOR);
			}
		}
		else if(shipOrientation == battleship.Constants.SOUTH)
		{
			// Is it too long for the (now, vertical) space?
			if(((coordinate / 10) + battleship.Constants.SHIP_LENGTHS[currentShipType]) > battleship.Constants.BOARD_DIMENSION)
			{
				cancelPlacement("Out of Bounds");
				return false;
			}
			// Are there any ships in the way?
			for(int i = 1; i < battleship.Constants.SHIP_LENGTHS[currentShipType]; i++)
			{
				int checkCoord = coordinate + (i * battleship.Constants.BOARD_DIMENSION);
				if(cells[checkCoord].hasShip)
				{
					cancelPlacement("Blocked by " + battleship.Constants.SHIP_NAMES[cells[checkCoord].shipId]);
					return false;
				}
			}
			for(int i = 0; i < battleship.Constants.SHIP_LENGTHS[currentShipType]; i++)
			{
				cells[coordinate + (i*10)].addShip(currentShipType);
				if(isPlayerBoard) cells[coordinate + (i*10)].setBackground(battleship.Constants.SHIP_COLOR);
			}
		}
		else if(shipOrientation == battleship.Constants.WEST)
		{
			// Is it too long for the space?
			if(((coordinate % 10) - (battleship.Constants.SHIP_LENGTHS[currentShipType]-1)) < 0)
			{
				cancelPlacement("Out of Bounds");
				return false;
			}
			// Are there any ships in the way?
			for(int i = 1; i < battleship.Constants.SHIP_LENGTHS[currentShipType]; i++)
			{
				int checkCoord = coordinate - i;
				if(cells[checkCoord].hasShip)
				{
					cancelPlacement("Blocked by " + battleship.Constants.SHIP_NAMES[cells[checkCoord].shipId]);
					return false;
				}
			}

			for(int i = 0; i < battleship.Constants.SHIP_LENGTHS[currentShipType]; i++)
			{
				cells[coordinate - i].addShip(currentShipType);
				if(isPlayerBoard) cells[coordinate - i].setBackground(battleship.Constants.SHIP_COLOR);
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
		if((currentCoordinate / battleship.Constants.BOARD_DIMENSION) == (startCoordinate / battleship.Constants.BOARD_DIMENSION))
		{
			if((currentCoordinate > startCoordinate))
			{
				return battleship.Constants.EAST;
			}
			else
			{
				return battleship.Constants.WEST;
			}
		}
		else if((currentCoordinate % battleship.Constants.BOARD_DIMENSION) == (startCoordinate % battleship.Constants.BOARD_DIMENSION))
		{
			if((currentCoordinate > startCoordinate))
			{
				return battleship.Constants.SOUTH;
			}
			else
			{
				return battleship.Constants.NORTH;
			}
		}
		else
		{
			return battleship.Constants.NONE;
		}
	}

	protected void cancelPlacement(String reason)
	{
		Log.debug(reason);
		cells[startCoordinate].defaultBackground();
		cells[currentCoordinate].defaultBackground();
		shipPlacementInProgress = false;
	}

	// Checks to see if any ships are afloat.
	protected boolean hasShips()
	{
		Log.debug("Does " + name + " have ships?");
		for(Ship s : ships)
		{
			// Log.debug(s.name + " has " + s.hits + " hits and isSunk is " + s.isSunk);
			// If any aren't sunk
			if(!s.isSunk)
			{
				// Hooray, we have ships
				Log.debug("Well, it has a " + s.name + ", at least.");
				return true;
			}
		}
		Log.debug("No.");
		// Boo, no ships. We lose.
		return false;
	}
	
	protected boolean fire()
	{
		boolean result = addHit();
		cells[firingCoordinate].deactivate();
		firingCoordinate = -1;
		return result;
	}

	// Adds a hit to the cell and any ship that's on it.
	protected boolean addHit()
	{			
		// If there's a ship there...
		if(cells[firingCoordinate].hasShip)
		{
			cells[firingCoordinate].setBackground(battleship.Constants.HIT_COLOR);

			// Let 'em know
			
			// If this hit sank the ship
			if(ships[cells[firingCoordinate].shipId].addHit())
			{
				cells[firingCoordinate].setBackground(battleship.Constants.SUNK_COLOR);
				// You know the commercial-- ya gotta say it if ya can!
				if(cells[firingCoordinate].shipId == 2)
				{
					// if(!isPlayerBoard) game.showMessage("You sunk my Battleship!");
				}
				else
				{
					// if(!isPlayerBoard) game.showMessage(battleship.Constants.SHIP_NAMES[cells[firingCoordinate].shipId] + " has sunk!");
				}
				// Tell computer what sank here
				
				// Update the cell
				cells[firingCoordinate].state = battleship.Constants.State.SUNK;
				
				// If it's sunk, make sure all of the cells match.
				// No longer necessary in this version, but it does no harm.
				for(int i = 0; i < cells.length; i++)
				{
					if(cells[i].shipId == cells[firingCoordinate].shipId)
					{
						cells[i].setBackground(battleship.Constants.SUNK_COLOR);
						cells[i].state = battleship.Constants.State.SUNK;
					}
				}
				if(!hasShips())
				{
					Log.debug("Game should be over.");
				}
			}
			else
			{
				// If it's hit but not sunk.
				cells[firingCoordinate].setBackground(battleship.Constants.HIT_COLOR);
				cells[firingCoordinate].state = battleship.Constants.State.HIT;
			}		
			
			// Finally, return a hit.
			return true;	
		}
		else
		{
			// *Sad trombones*
			cells[firingCoordinate].setBackground(battleship.Constants.MISS_COLOR);
			cells[firingCoordinate].state = battleship.Constants.State.MISS;
			return false;
		}	
	}
}