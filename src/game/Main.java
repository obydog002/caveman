package src.game;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.Dimension;
import java.awt.Toolkit;

import src.menu.MainMenu;

public class Main
{
	static JFrame frame;
	
	public static void create_and_show_game_GUI(String[] args) {	
		frame = new JFrame("Caveman"); 
		
		Dimension pref = Toolkit.getDefaultToolkit().getScreenSize();
		
		double sc_width = pref.getWidth();
		double sc_height = pref.getHeight();
		
		int width = (int)sc_width;
		int height = (int)sc_height;
		Dimension dim = new Dimension(width, height);
		
		frame.setSize(dim);
		frame.setPreferredSize(dim);
		
		GameMain main = new GameMain(frame);
		MainMenu menu = new MainMenu(main, main.getInput(), width, height);
		main.add_process(menu);
		
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
				create_and_show_game_GUI(args);
			}
		});
	}
}
