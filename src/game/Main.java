package src.game;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Main
{
	static JFrame frame;
	
	public static void create_and_show_GUI(String[] args)
	{
		/*if (arg.length > 0)
		{
			level = Integer.parseInt(arg[0]);
		}*/
		
		// can add argument passing later, and other options
		GameMain main = new GameMain(); 
		frame = new JFrame("Caveman");
		
		Dimension pref = Toolkit.getDefaultToolkit().getScreenSize();
		
		double sc_width = (3.5/4)*pref.getWidth();
		double sc_height = (3.5/4)*pref.getHeight();
		
		Dimension dim = new Dimension((int)sc_width, (int)sc_height);
		
		frame.setSize(dim);
		frame.setPreferredSize(dim);
		//frame.setMinimumSize(dim);
		//frame.setMaximumSize(dim);
		
		frame.add(main);
		frame.pack();
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		main.start();
	}
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				create_and_show_GUI(args);
			}
		});
	}
}