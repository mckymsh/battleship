package battleship3;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class Cell extends JLabel implements MouseListener
{
	protected boolean hasShip;
	protected boolean active;
	protected int shipId;
	protected int coordinate;
	protected String state;
	protected Board parent;
	
	// All cells initialize blank.
	Cell(int coordinate, Board parent)
	{
		this.parent = parent;

		// Keep in mind ship type 0 is no ship.
		addShip(0);
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.CENTER);
		
		setOpaque(true);

		state = "";
		this.coordinate = coordinate;
		if ((coordinate / 10) % 2 == 0) // Even row
		{
			if (coordinate % 2 == 0) // Even column
			{
				setBackground(Color.LIGHT_GRAY);
			}
			else // Odd column
			{
				setBackground(Color.CYAN);
			}
		}
		else // Odd row
		{
			if (coordinate % 2 == 0) // Even column
			{
				setBackground(Color.CYAN);
			}
			else // Odd Column
			{
				setBackground(Color.LIGHT_GRAY);
			}
		}		
	}

	protected void activate()
	{
		active = true;
		addMouseListener(this);
	}

	protected void deactivate()
	{
		active = false;
		removeMouseListener(this);
	}
	
	// Adds a ship.
	protected void addShip(int shipType)
	{	
		this.shipId = shipType;	
		hasShip = (shipId == 0) ? false : true;
		if(parent.isPlayerBoard)
		{
			setBackground(Color.DARK_GRAY); // I dunno, battleship color?
		}
	}

	public void mouseClicked(MouseEvent e)
	{
		Cell cell = (Cell) e.getSource();
		Log.debug("Player clicked cell #" + cell.coordinate);
		if(!active)
		{
			return;
		}
		if (cell.state == Battleship.MISS_SYMBOL)
		{
			cell.state = Battleship.HIT_SYMBOL;
			cell.setBackground(Color.RED);
		}
		else if (cell.state == Battleship.HIT_SYMBOL)
		{
			cell.state = Battleship.MISS_SYMBOL;
			cell.setBackground(Color.ORANGE);
		}
		else
		{
			cell.state = Battleship.HIT_SYMBOL;
			cell.setBackground(Color.RED);
		}
	}

    public void mouseEntered(MouseEvent e)
    {
    	if(parent.isPlayerBoard && parent.game.state == Battleship.SETUP)
    	{
    		setBorder(new LineBorder(Color.BLACK));
    	}
    }

    public void mouseExited(MouseEvent e)
    {
    	if(parent.isPlayerBoard && parent.game.state == Battleship.SETUP)
    	{
	    	setBorder(BorderFactory.createEmptyBorder());
	    }
    }

    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
}
