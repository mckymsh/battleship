package battleship3;

// import java.awt.*;
import javax.swing.*;

public class Game extends JFrame
{	
	Game ()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,800);
		setLocationRelativeTo(null);
		setTitle("Battleship by Mickey");
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(new Board("AI Board"));
		add(new JPanel());
		add(new Board("Player Board"));

		setVisible(true);
	}
	
	protected void run()
	{

	}
}
