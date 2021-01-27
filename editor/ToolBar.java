package editor;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

public class ToolBar extends JToolBar
{
	public ToolBar()
	{
		super("Editor Bar");
		add_buttons();
	}
	
	private void add_buttons()
	{
		JButton test = new JButton("test");
		this.add(test);
	}
}