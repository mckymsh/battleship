package battleship3;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*; // Why, Java

public class Board extends JPanel
{
	protected String name;
	
	protected Cell[] cells;
	protected Ship[] ships;
		
	Board(String name)
	{
		Border border = BorderFactory.createLineBorder(Color.black);
		this.name = name;
		
		cells = new Cell[Battleship.BOARD_DIMENSION * Battleship.BOARD_DIMENSION];
		ships = new Ship[Battleship.SHIP_NAMES.length];
		
		JPanel cellPanel = new JPanel();
		cellPanel.setBorder(border);
		cellPanel.setLayout(new GridLayout(Battleship.BOARD_DIMENSION, Battleship.BOARD_DIMENSION));
		for(int i = 0; i < cells.length; i++)
		{
			cells[i] = new Cell();
			cellPanel.add(cells[i]);
		}

		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));

		// Remember that ship type 0 is no ship,
		// so it doesn't need to be on the panel.
		ships[0] = new Ship(0);
		for (int i = 1; i < ships.length; i++)
		{
			ships[i] = new Ship(i);
			statusPanel.add(new JLabel(ships[i].status));
		}

		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(cellPanel);
		this.add(statusPanel);
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
	
	// This method places the designated ship at the designated coordinates.
	// It checks for problems before actually changing the board.
	protected boolean addShip(int shipType, int coordinate, boolean horizontal)
	{
		if(horizontal)
		{
			// Is it too long for the space?
			if(((coordinate % 10) + Battleship.SHIP_LENGTHS[shipType]) > Battleship.BOARD_DIMENSION)
			{
				return false;
			}
			// Are there any ships in the way?
			for(int i = 0; i < Battleship.SHIP_LENGTHS[shipType]; i++)
			{
				if(cells[coordinate + i].hasShip)
				{
					return false;
				}
			}
			// Add ship to the relevant cells.
			for(int i = 0; i < Battleship.SHIP_LENGTHS[shipType]; i++)
			{
				cells[coordinate + i].addShip(shipType);
			}
		}
		else
		{
			// Is it too long for the (now, vertical) space?
			if(((coordinate / 10) + Battleship.SHIP_LENGTHS[shipType]) > Battleship.BOARD_DIMENSION)
			{
				return false;
			}
			// Are there any ships in the way?
			for(int i = 0; i < Battleship.SHIP_LENGTHS[shipType]; i++)
			{
				if(cells[coordinate + i].hasShip)
				{
					return false;
				}
			}
			// Add ship to the relevant cells.
			for(int i = 0; i < Battleship.SHIP_LENGTHS[shipType]; i++)
			{
				cells[coordinate + (i*10)].addShip(shipType);
			}
		}
		// If we made it here, all is well.
		return true;
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
			if(ships[cells[coordinate].shipType].addHit())
			{
				// You know the commercial-- ya gotta say it if ya can!
				if(cells[coordinate].shipType == 2)
				{
					Log.debug("You sunk my Battleship!");
				}
				else
				{
					Log.debug(Battleship.SHIP_NAMES[cells[coordinate].shipType] + " has been sunk!");
				}
				
				// Update the cell
				cells[coordinate].state = Battleship.SUNK_SYMBOL;
				
				// If it's sunk, make sure all of the cells match.
				// No longer necessary in this version, but it does no harm.
				for(int i = 0; i < cells.length; i++)
				{
					if(cells[i].shipType == cells[coordinate].shipType)
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
