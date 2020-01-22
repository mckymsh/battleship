package battleship;

abstract class Player
{
	int firingCoordinate;

	Player()
	{
		firingCoordinate = -1;
	}

	protected int getFiringCoordinate()
	{
		return this.firingCoordinate;
	}

	protected boolean fireAt(int coordinate)
	{
		return true;
	}
}