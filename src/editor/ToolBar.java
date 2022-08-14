package src.editor;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

public class ToolBar extends JToolBar
{
	// for ActionListener stuff
	MainPanel main_panel;
	
	public ToolBar(MainPanel main_panel)
	{
		super("Editor Bar");
		
		this.main_panel = main_panel;
		
		GridLayout layout = new GridLayout(0, 2);
		
		JPanel panel = new JPanel();
		
		//Dimension pref_size = new Dimension();
		panel.setLayout(layout);
		add_buttons(panel);
		
		panel.setVisible(true);
		add(panel);
	}
	
	private void add_buttons(JPanel panel)
	{
		for (int i = 0; i < MainPanel.RES.length; i++)
		{
			ImageIcon icon = new ImageIcon(MainPanel.RES[i]);
			JButton button = new JButton(icon);
			button.addActionListener(main_panel);
			button.setActionCommand((i + 1) + "");
			panel.add(button);
		}
	}
}