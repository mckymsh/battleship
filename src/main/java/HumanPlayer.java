package battleship;

class HumanPlayer implements Player
{
	int firingCoordinate;

	HumanPlayer()
	{
		firingCoordinate = -1;
	}

	int getFiringCoordinate()
	{
		return this.firingCoordinate;
	}

	boolean fireAt(int coordinate)
	{
		return true;
	}
}