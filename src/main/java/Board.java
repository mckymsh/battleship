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
	}

	protected void setup()
	{

	}

	protected void reset()
	{
		removeAll();
		for(int i = 0; i < cells.length; i++)
		{
			cells[i] = new Cell(i, this);
			add(cells[i]);			
		}

		activate();
		
		// Remember that ship type 0 is no ship
		for (int i = 0; i < ships.length; i++)
		{
			ships[i] = new Ship(i);
			placeShip(i);	
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

	private boolean placeShip(int shipType)
	{
		// if(horizontal)
		// {
		// 	// Is it too long for the space?
		// 	if(((coordinate % 10) + Battleship.SHIP_LENGTHS[shipType]) > Battleship.BOARD_DIMENSION)
		// 	{
		// 		return false;
		// 	}
		// 	// Are there any ships in the way?
		// 	for(int i = 0; i < Battleship.SHIP_LENGTHS[shipType]; i++)
		// 	{
		// 		if(cells[coordinate + i].hasShip)
		// 		{
		// 			return false;
		// 		}
		// 	}
		// 	// Add ship to the relevant cells.
		// 	for(int i = 0; i < Battleship.SHIP_LENGTHS[shipType]; i++)
		// 	{
		// 		cells[coordinate + i].addShip(shipType);
		// 	}
		// }
		// else
		// {
		// 	// Is it too long for the (now, vertical) space?
		// 	if(((coordinate / 10) + Battleship.SHIP_LENGTHS[shipType]) > Battleship.BOARD_DIMENSION)
		// 	{
		// 		return false;
		// 	}
		// 	// Are there any ships in the way?
		// 	for(int i = 0; i < Battleship.SHIP_LENGTHS[shipType]; i++)
		// 	{
		// 		if(cells[coordinate + i].hasShip)
		// 		{
		// 			return false;
		// 		}
		// 	}
			// addShip(shipType, coordinate, horizontal);
		// }
		// If we made it here, all is well.
		return true;
	}
	
	// This method places the designated ship at the designated coordinates.
	protected void addShip(int shipType, int coordinate, boolean horizontal)
	{
		if(horizontal)
		{
			// Add ship to the relevant cells.
			for(int i = 0; i < Battleship.SHIP_LENGTHS[shipType]; i++)
			{
				cells[coordinate + i].addShip(shipType);
			}
		}
		else
		{
			// Add ship to the relevant cells.
			for(int i = 0; i < Battleship.SHIP_LENGTHS[shipType]; i++)
			{
				cells[coordinate + (i*10)].addShip(shipType);
			}
		}
		// If we made it here, all is well.
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
