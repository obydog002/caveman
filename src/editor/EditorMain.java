package src.editor;

import src.file.*;

import java.io.File;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class EditorMain extends JPanel implements ActionListener
{
	MainPanel panel;
	
	final JFileChooser fc;
	
	public final static String PATH = EditorMain.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	
	public EditorMain()
	{
		super(new BorderLayout());
		
		Dimension pref = Toolkit.getDefaultToolkit().getScreenSize();
		
		double width = 3*pref.getWidth()/4;
		double height = 3*pref.getHeight()/4;
		
		setSize(new Dimension((int)width, (int)height));
		
		MainPanel panel = new MainPanel();
		ToolBar editor_bar = new ToolBar(panel);
		
		fc = new JFileChooser(PATH + "/res/maps/user");
		
		this.panel = panel;
		
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
		
		JMenu options_submenu = new JMenu("Options");
		options_submenu.getAccessibleContext().setAccessibleDescription("Options menu");
		
		JMenuItem display_info_item = new JMenuItem("display info");
		display_info_item.getAccessibleContext().setAccessibleDescription("display level info");
		display_info_item.setActionCommand("display_info");
		display_info_item.addActionListener(editor);
		options_submenu.add(display_info_item);
		
		JMenuItem change_name_item = new JMenuItem("Level name");
		change_name_item.getAccessibleContext().setAccessibleDescription("Change the current level name");
		change_name_item.setActionCommand("change_level_name");
		change_name_item.addActionListener(editor);
		options_submenu.add(change_name_item);
		
		JMenuItem change_level_dims_item = new JMenuItem("Level width/height");
		change_level_dims_item.getAccessibleContext().setAccessibleDescription("Change the current level width or height");
		change_level_dims_item.setActionCommand("change_level_dims");
		change_level_dims_item.addActionListener(editor);
		options_submenu.add(change_level_dims_item);
		
		file.add(options_submenu);
		
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
			JPanel dialog_panel = new JPanel();
			dialog_panel.setLayout(new BoxLayout(dialog_panel, BoxLayout.PAGE_AXIS));
			
			dialog_panel.add(new JLabel("level name:"));
			JTextField name_field = new JTextField("base");
			dialog_panel.add(name_field);
			
			dialog_panel.add(new JLabel("width:"));
			JTextField width_field = new JTextField("30");
			dialog_panel.add(width_field);
			
			dialog_panel.add(new JLabel("height:"));
			JTextField height_field = new JTextField("20");
			dialog_panel.add(height_field);
			
			int res = JOptionPane.showConfirmDialog(null, dialog_panel, "New level", JOptionPane.OK_CANCEL_OPTION);
			
			if (res == JOptionPane.OK_OPTION)
			{
				boolean ok = true;
				String error = "";
				
				String level_name = name_field.getText().toLowerCase();
				int width = 0, height = 0;
				try
				{
					width = Integer.parseInt(width_field.getText());
					
					if (width < 1)
					{
						error += "Width must be above 0!\n";
						ok = false;
					}
					else if (width > 255)
					{
						error += "Width cannot be above 255!\n";
						ok = false;
					}
				}
				catch (Exception exe)
				{
					error += "Width must be a valid numeric entry!\n";
					ok = false;
				}
					
				try
				{
					height = Integer.parseInt(height_field.getText());
					
					if (height < 1)
					{
						error += "Height must be above 0!\n";
						ok = false;
					}
					else if (height > 255)
					{
						error += "Height cannot be above 255!\n";
						ok = false;
					}
				}
				catch (Exception exe)
				{
					error += "Height must be a valid numeric entry!\n";
					ok = false;
				}
				
				if (!ok)
				{
					JOptionPane.showMessageDialog(panel, error, "Error", JOptionPane.ERROR_MESSAGE);
				}
				else // all good
				{
					ok = true;
					// see if its been changed since last save
					// warn user if so
					if (panel.check_changed())
					{	
						res = JOptionPane.showOptionDialog(panel, "Unsaved changes will be lost! Press Confirm to continue.", 
						"Unsaved changes", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, 
						null, null, null);
						
						ok = !(res == JOptionPane.CANCEL_OPTION);
					}
					
					if (ok)
						panel.init_template_level(width, height, level_name);
				}
			}
		}
		else if (act == "open")
		{
			int return_val = fc.showOpenDialog(panel);
			
			if (return_val == JFileChooser.APPROVE_OPTION)
			{
				File file = fc.getSelectedFile();
				
				Level level = FileManager.read_cv_level(file);
				
				// something went wrong!
				if (level == null)
				{
					JOptionPane.showMessageDialog(panel, "Could not read file as caveman level file!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					// check for unsaved changes
					boolean ok = true;
					if (panel.check_changed())
					{	
						Object[] options = {"Confirm", "Cancel"};
						int res = JOptionPane.showOptionDialog(panel, "Unsaved changes will be lost! Press Confirm to continue.", 
						"Unsaved changes", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, 
						null, options, options[0]);
						
						ok = !(res == 1);
					}
					
					if (ok)
						panel.set_level(level);
				}
			}
		}
		else if (act == "save")
		{
			int return_val = fc.showSaveDialog(panel);
			
			if (return_val == JFileChooser.APPROVE_OPTION)
			{
				File file = fc.getSelectedFile();
				
				// check the file extensions is correct, modify it to be correct (force save of .CVL file)
				String str = file.toString();
				
				boolean append_CVL = false;
				if (str.length() > 4)
				{
					int str_len = str.length();
					String end = str.substring(str_len-4, str_len);
					
					if (!end.equals(".CVL"))
						append_CVL = true;
				}
				else
					append_CVL = true;
				
				if (append_CVL)
					file = new File(str + ".CVL");
				
				Level level = panel.get_level();
				
				// remember widths and heights can only be upto 255! although thats already massively huge
				
				// smaller still is the number of entity types, which may go upto 127
				byte byte_ents[] = new byte[level.entities.length];
				
				for (int i = 0; i < level.entities.length; i++)
				{
					byte b = (byte)level.entities[i];
					if (b < 0) // conversion error for some reason
					{
						throw new RuntimeException("Overflow in level entities on saving!");
					}
					
					byte_ents[i] = b;
				}
				
				FileManager.write_cv_level(file, level.width, level.height, byte_ents, level.name);
				
				panel.set_prev();
			}
		}
		else if (act == "display_info")
		{
			Level level = panel.get_level();
			
			JOptionPane.showMessageDialog(panel, ("Name: " + level.name + "\nWidth: " + level.width + "\nHeight: " + level.height),
			"Level Info", JOptionPane.INFORMATION_MESSAGE);
		}
		else if (act == "change_level_name")
		{
			Level level = panel.get_level();
			
			JPanel dialog_panel = new JPanel();
			dialog_panel.setLayout(new BoxLayout(dialog_panel, BoxLayout.PAGE_AXIS));
			
			dialog_panel.add(new JLabel("level name:"));
			JTextField name_field = new JTextField(level.name);
			dialog_panel.add(name_field);
			
			int res = JOptionPane.showConfirmDialog(null, dialog_panel, "Change level name", JOptionPane.OK_CANCEL_OPTION);
			if (res == JOptionPane.OK_OPTION)
			{
				level.name = name_field.getText().toLowerCase();
				panel.set_level(level);
			}
		}
		else if (act == "change_level_dims")
		{
			JPanel dialog_panel = new JPanel();
			dialog_panel.setLayout(new BoxLayout(dialog_panel, BoxLayout.PAGE_AXIS));
			
			Level level = panel.get_level();
			
			dialog_panel.add(new JLabel("width:"));
			JTextField width_field = new JTextField(level.width + "");
			dialog_panel.add(width_field);
			
			dialog_panel.add(new JLabel("height:"));
			JTextField height_field = new JTextField(level.height + "");
			dialog_panel.add(height_field);
			
			int res = JOptionPane.showConfirmDialog(null, dialog_panel, "Change level width/height", JOptionPane.OK_CANCEL_OPTION);
			if (res == JOptionPane.OK_OPTION)
			{
				boolean ok = true;
				String error = "";
				
				int width = 0, height = 0;
				try
				{
					width = Integer.parseInt(width_field.getText());
					
					if (width < 1)
					{
						error += "Width must be above 0!\n";
						ok = false;
					}
					else if (width > 255)
					{
						error += "Width cannot be above 255!\n";
						ok = false;
					}
				}
				catch (Exception exe)
				{
					error += "Width must be a valid numeric entry!\n";
					ok = false;
				}
					
				try
				{
					height = Integer.parseInt(height_field.getText());
					
					if (height < 1)
					{
						error += "Height must be above 0!\n";
						ok = false;
					}
					else if (height > 255)
					{
						error += "Height cannot be above 255!\n";
						ok = false;
					}
				}
				catch (Exception exe)
				{
					error += "Height must be a valid numeric entry!\n";
					ok = false;
				}
				
				if (!ok)
				{
					JOptionPane.showMessageDialog(panel, error, "Error", JOptionPane.ERROR_MESSAGE);
				}
				else // all good
				{
					boolean warning = false;
					// if the width and or height is lower than current dims, then warn the user
					// that the sides will be compressed (loss of data)
					
					error = "";
					if (width < level.width)
					{
						error += "New width is less than current level width!\n";
						warning = true;
					}
					
					if (height < level.height)
					{
						error += "New height is less than current level height!\n";
						warning = true;
					}
					
					if (warning)
					{
						error += "The level will be resized right-to-left, bottom-to-top.\nEntities along the right and bottom sides may be lost.\nDo you wish to continue?";
						
						res = JOptionPane.showOptionDialog(panel, error, "Resize warning", 
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
						
						if (res == JOptionPane.OK_OPTION)
						{
							panel.resize_level(width, height);
						}
					}
					else
					{
						panel.resize_level(width, height);
					}
				}
			}
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