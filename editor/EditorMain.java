package editor;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class EditorMain extends JPanel implements ActionListener
{
	public EditorMain()
	{
		super(new BorderLayout());
		
		ToolBar editor_bar = new ToolBar();
		MainPanel panel = new MainPanel();
		
		add(editor_bar, BorderLayout.LINE_START);
		add(panel, BorderLayout.CENTER);
	}
	
	
	public static void create_menu(EditorMain editor, JFrame frame)
	{
		JMenuBar menuBar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		file.getAccessibleContext().setAccessibleDescription("File menu");
		menuBar.add(file);
		
		JMenuItem new_item = new JMenuItem("New");
		new_item.getAccessibleContext().setAccessibleDescription("Create new level");
		new_item.setActionCommand("new");
		new_item.addActionListener(editor);
		
		file.add(new_item);
		
		JMenuItem open_item = new JMenuItem("Open");
		new_item.getAccessibleContext().setAccessibleDescription("Open level from file");
		open_item.setActionCommand("open");
		open_item.addActionListener(editor);
		
		file.add(open_item);
		
		JMenuItem save_item = new JMenuItem("Save");
		save_item.getAccessibleContext().setAccessibleDescription("Save level to disk");
		save_item.setActionCommand("save");
		save_item.addActionListener(editor);
		
		file.add(save_item);
		
		frame.setJMenuBar(menuBar);
	}
	
	public static void create_and_show_GUI(String[] args)
	{
		JFrame frame = new JFrame("Caveman Level Editor");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		EditorMain editor = new EditorMain();
		create_menu(editor, frame);
		
		frame.add(editor);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String act = e.getActionCommand();
		
		if (act == "new")
		{
			System.out.println("new");
		}
		else if (act == "open")
		{
			System.out.println("open");
		}
		else if (act == "save")
		{
			System.out.println("save");
		}
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