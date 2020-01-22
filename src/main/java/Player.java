package battleship;

interface Player
{
	int getFiringCoordinate();
	boolean fireAt(int coordinate);
}