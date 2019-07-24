package battleship3;

public class Battleship
{
	// Lots n lots of constants used throughout the game.
	// I like it this way in case I want to change something.
	
	// Theoretically, you could change this without breaking things,
	// but that's risky since I made it.
	protected static final int BOARD_DIMENSION = 10;
	
	// Constants for the various types of ship. Alphabetical order.
	//										Ship types:		0		1			2				3			4				5
	protected static final String[] SHIP_NAMES 			= {	"None",	"Carrier", 	"Battleship", 	"Cruiser", 	"Destroyer", 	"Submarine"	};
	protected static final String[] SHIP_ABBREVIATIONS	= {	"_",	"A", 		"B", 			"C", 		"D", 			"S"			};
	protected static final int[] 	SHIP_LENGTHS 		= {	0,		5, 			4, 				3, 			2, 				3			};
	
	// Symbols that show in the console.
	protected static final String MISS_SYMBOL			= "O";
	protected static final String HIT_SYMBOL			= "X";
	protected static final String SUNK_SYMBOL			= "#";
	
	
	public static void main(String[] args)
	{
			Game game = new Game();
			game.run();
	}
}
