package editor;

import java.awt.event.*;

// class to handle menu optionality
public class MenuHandler implements ActionListener
{
	// to connect with main panel
	private MainPanel panel;
	
	public MenuHandler(MainPanel panel)
	{
		this.panel = panel;
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
}