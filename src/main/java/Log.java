package battleship3;

/* 
 * Brazenly copied from Julio Cesar Bahamon at UNCC.
 * He probably has a copyright, but what he doesn't know won't hurt him. 
 */
public final class Log
{
	private static final boolean DEBUG_ON = false;
	
	public static void debug(String message)
	{
		if (DEBUG_ON)
		{
			System.out.printf("[DEBUG] %s\n",message);
		}
	}
}

