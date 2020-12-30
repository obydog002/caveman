package editor;

import java.awt.*;
import javax.swing.*;

public class EditorMain
{
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
	
	public static void create_and_show_GUI(String[] args)
	{
		JFrame frame = new JFrame("Caveman Level Editor");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MainPanel panel = new MainPanel();
		
		JMenuBar menuBar = new JMenuBar();
		
		MenuHandler handler = new MenuHandler(panel);
		
		JMenu file = new JMenu("File");
		file.getAccessibleContext().setAccessibleDescription("File menu");
		menuBar.add(file);
		
		JMenuItem new_item = new JMenuItem("New");
		new_item.getAccessibleContext().setAccessibleDescription("Create new level");
		new_item.setActionCommand("new");
		new_item.addActionListener(handler);
		
		file.add(new_item);
		
		JMenuItem open_item = new JMenuItem("Open");
		new_item.getAccessibleContext().setAccessibleDescription("Open level from file");
		open_item.setActionCommand("open");
		open_item.addActionListener(handler);
		
		file.add(open_item);
		
		JMenuItem save_item = new JMenuItem("Save");
		save_item.getAccessibleContext().setAccessibleDescription("Save level to disk");
		save_item.setActionCommand("save");
		save_item.addActionListener(handler);
		
		file.add(save_item);
		
		frame.setJMenuBar(menuBar);
		
		frame.add(panel);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}