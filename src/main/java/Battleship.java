package battleship3;

import java.awt.Color;

public class Battleship
{
	// Lots n lots of constants used throughout the game.
	// I like it this way in case I want to change something.
	
	// Theoretically, you could change this without breaking things,
	// but that's risky, since it was made by me.
	protected static final int BOARD_DIMENSION = 10;

	protected static final String WINDOW_TITLE = "Battleship by Mickey";
	protected static final String COMPUTER_BOARD_NAME = "Computer Board";
	protected static final String PLAYER_BOARD_NAME = "Player Board";
	
	// Constants for the various types of ship. Alphabetical order.
	//										Ship types:		0		1			2				3			4				5
	protected static final String[] SHIP_NAMES 			= {	"None",	"Carrier", 	"Battleship", 	"Cruiser", 	"Destroyer", 	"Submarine"	};
	protected static final String[] SHIP_ABBREVIATIONS	= {	" - ",	"CAR", 		"BAT", 			"CRU", 		"DES", 			"SUB"		};
	protected static final String[] SHIP_LETTERS		= {	"-",	"A", 		"B", 			"C", 		"D", 			"S"			};
	protected static final int[] 	SHIP_LENGTHS 		= {	0,		5, 			4, 				3, 			2, 				3			};
	protected static final Color 		SHIP_COLOR			= Color.DARK_GRAY;
	
	// Symbols that show in the console.
	protected static final String BLANK_SYMBOL			= "";
	protected static final Color TARGET_COLOR			= Color.YELLOW;
	protected static final String MISS_SYMBOL			= "O";
	protected static final Color MISS_COLOR				= Color.GREEN;
	protected static final String HIT_SYMBOL			= "X";
	protected static final Color HIT_COLOR				= Color.RED;
	protected static final String SUNK_SYMBOL			= "#";
	protected static final Color SUNK_COLOR				= Color.BLACK;

	// Ship placement orientations for code readability
	protected static final int NONE 	= 0;
	protected static final int NORTH 	= 1;
	protected static final int EAST 	= 2;
	protected static final int SOUTH 	= 3;
	protected static final int WEST 	= 4;
	
	public static void main(String[] args)
	{
			Game game = new Game();
			game.run();
	}
}
