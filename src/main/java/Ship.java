package battleship3;

public class Ship
{
	protected int type;
	protected int length;	
	protected String name;
	protected String letter;
	protected String abbreviation;
	
	protected int hits;
	protected boolean isSunk;
	protected String status;
	
	// Each ship builds itself based off of the constants in "Battleship"
	Ship(int shipType)
	{
		this.type = shipType;
		
		this.length = 		Battleship.SHIP_LENGTHS[type];
		this.name = 		Battleship.SHIP_NAMES[type];
		this.letter = 		Battleship.SHIP_LETTERS[type];
		this.abbreviation = Battleship.SHIP_ABBREVIATIONS[type];
		
		hits = 0;
		isSunk = false;
		status = "✓";

		if(shipType == 0)
		{
			// Log.debug("Creating 'None' Ship");
			hits = 1;
			isSunk = true;
			status = "✗";
		}
	}
	
	// Adds a hit to the ship
	// returns true if this hit has sunk the ship.
	protected boolean addHit()
	{
		hits++;
		
		Log.debug(name + " has " + hits + " hits.");
		
		isSunk = (hits < length) ? false : true;
		status = (isSunk) ? "✗" : "✓";

		if(isSunk)
		{
			Log.debug(name + " has sunk.");
		}

		return isSunk;
	}
}

