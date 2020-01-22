package battleship;

class ComputerPlayer extends Player
{
	private AI ai;

	ComputerPlayer()
	{
		ai = new AI();
	}
}