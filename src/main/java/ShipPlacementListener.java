package battleship3;

import java.awt.*;
// import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*; // Why, Java

public class ShipPlacementListener implements MouseListener
{
	public void ShipPlacementListener()
	{
		// ??
	}

	public void mouseClicked(MouseEvent e)
	{
		Cell cell = (Cell) e.getSource();
		Log.debug("Player clicked cell #" + cell.coordinate);
		cell.sendShipPlacementRequest();
	}
	

    public void mouseEntered(MouseEvent e)
    {
    	Cell cell = (Cell) e.getSource();
    	cell.setBorder(new LineBorder(Color.BLACK));
    }
    public void mouseExited(MouseEvent e)
    {
    	Cell cell = (Cell) e.getSource();
    	cell.setBorder(BorderFactory.createEmptyBorder());
    }

    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
}