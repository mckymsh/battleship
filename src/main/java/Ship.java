package battleship3;

public class Ship
{
	protected int type;
	protected int length;	
	protected String name;
	protected String abbreviation;
	
	protected int hits;
	protected boolean isSunk;
	protected String status;
	
	// Each ship builds itself based off of the constants in "Battleship"
	Ship(int shipType)
	{
		this.type = shipType;
		
		this.length = Battleship.SHIP_LENGTHS[type];
		this.name = Battleship.SHIP_NAMES[type];
		this.abbreviation = Battleship.SHIP_ABBREVIATIONS[type];
		
		hits = 0;
		isSunk = false;
		status = "✓";
	}
	
	// Adds a hit to the ship
	// returns true if this hit has sunk the ship.
	protected boolean addHit()
	{
		hits++;
		
		Log.debug(name + " has " + hits + " hits.");
		
		isSunk = (hits < length) ? false : true;
		status = (isSunk) ? "✗" : "✓";

		return isSunk;
	}
}

