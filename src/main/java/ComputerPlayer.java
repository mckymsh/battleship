package battleship;

class ComputerPlayer implements Player
{
	int firingCoordinate;

	ComputerPlayer()
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