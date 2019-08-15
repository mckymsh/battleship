package battleship3;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class Cell extends JLabel
{
	protected boolean hasShip;
	protected int shipId;
	protected int coordinate;
	protected String state;
	protected Board parentBoard;
	private MouseListener mouseListener;
	private Color originalColor;
	
	// All cells initialize blank.
	Cell(int coordinate, Board parentBoard)
	{
		this.parentBoard = parentBoard;

		// Keep in mind ship type 0 is no ship.
		addShip(0);
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.CENTER);		
		setOpaque(true);

		state = Battleship.BLANK_SYMBOL;
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
		originalColor = this.getBackground();	
	}

	protected void activatePlaceShips()
	{
		mouseListener = new MouseListener()
		{
			public void mouseClicked(MouseEvent e)
			{
				Log.debug("Player clicked cell #" + coordinate);
				sendShipPlacementRequest();
			}

		    public void mouseEntered(MouseEvent e)
		    {
		    	setBorder(new LineBorder(Color.BLACK));
		    }

		    public void mouseExited(MouseEvent e)
		    {
		    	setBorder(BorderFactory.createEmptyBorder());
		    }

		    public void mousePressed(MouseEvent e){}
		    public void mouseReleased(MouseEvent e){}
		};
		addMouseListener(mouseListener);
	}

	protected void activateSelectCoordinates()
	{
		mouseListener = new MouseListener()
		{
			public void mouseClicked(MouseEvent e)
			{
				Log.debug("Player clicked cell #" + coordinate);
				parentBoard.setFiringCoordinate(coordinate);
			}

		    public void mouseEntered(MouseEvent e)
		    {
		    	setBorder(new LineBorder(Color.BLACK));
		    }

		    public void mouseExited(MouseEvent e)
		    {
	    		setBorder(BorderFactory.createEmptyBorder());
		    }

		    public void mousePressed(MouseEvent e){}
		    public void mouseReleased(MouseEvent e){}
		};
		addMouseListener(mouseListener);
	}

	protected void deactivate()
	{
		setBorder(BorderFactory.createEmptyBorder());
		removeMouseListener(mouseListener);
		mouseListener = null;
	}

	protected void sendShipPlacementRequest()
	{
		parentBoard.addCoordinate(this.coordinate);
	}
	
	// Adds a ship.
	protected Cell addShip(int shipType)
	{	
		originalColor = this.getBackground();
		this.shipId = shipType;	
		hasShip = (shipId == 0) ? false : true;
		return this;
	}

	protected void defaultBackground()
	{
		setBackground(originalColor);
	}
}
