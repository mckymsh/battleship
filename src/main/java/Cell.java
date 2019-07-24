package battleship3;

import java.awt.*;
import javax.swing.*;

public class Cell extends JLabel
{
	protected boolean hasShip;
	protected int shipType;
	protected String state;
	
	// All cells initialize blank.
	Cell()
	{
		hasShip = false;
		// Keep in mind ship type 0 is no ship.
		shipType = 0;
		state = Battleship.SHIP_ABBREVIATIONS[shipType];
		setText(state);
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.CENTER);
	}
	
	// Adds a ship.
	protected void addShip(int shipType)
	{		
		hasShip = true;
		this.shipType = shipType;
		state = Battleship.SHIP_ABBREVIATIONS[shipType];
		setText(state);
	}
}
