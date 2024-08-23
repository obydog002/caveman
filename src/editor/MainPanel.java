package src.editor;

import src.file.Level;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

import javax.imageio.ImageIO;

import java.io.File;

import java.awt.event.*;
// main panel for drawing options on the side 
// and drawing the level itself
public class MainPanel extends JPanel implements MouseMotionListener, MouseListener, ActionListener
{
	// path to file
	public static final String PATH = MainPanel.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	
	// Art paths
	public static final String[] RES_FILES = {"rock.png", "rock2.png", "boulder.png", 
	"boulderl.png", "boulderu.png", "boulderr.png" , "boulderd.png", "boulderlr.png", "boulderud.png",
	"boulderlurd.png", "player.png", "exit2.png", "club.png", "green.png", "yellow.png", "red.png"};
	
	public static BufferedImage load_resource(String path)
	{
		try
		{
			BufferedImage img = ImageIO.read(new File(PATH + "/" + path));
			return img;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}
	
	// Level info
	private Level level;
	
	// prev Level info
	private Level prev_level;
	
	// previous entities to check if there has been changes
	int[] prev_entities;
	
	/*
	// array for level entities and resources
	// 0 - nought
	// 1 - unbreakable wall1
	// 2 - unbreakable wall2
	// 3 - boulder
	// 4 - boulder left
	// 5 - boulder up
	// 6 - boulder right
	// 7 - boulder down
	// 8 - boulder left-right
	// 9 - boulder up-down
	// 10 - boulder all
	// 11 - player spawn - 1 allowed
	// 12 - exit location - 1 allowed
	// 13 - club
	// 14 - green mammoth
	// 15 - yellow mammoth
	// 16 - red mammoth
	*/
	
	// to keep track of where spawn, exit are
	// -1 means they have not been set yet
	private int spawn_loc = -1;
	private int exit_loc = -1;
	
	// for mouse drawing
	private int mouse_x;
	private int mouse_y;
	
	private int entity_choice;
	
	public static final BufferedImage[] RES = new BufferedImage[16];
	
	// include 1 for empty entity
	public static final int ENTITIES_NUM = RES.length + 1;
	
	static 
	{
		for (int i = 0; i < 16; i++)
		{
			RES[i] = load_resource("res/entities/" + RES_FILES[i]);
		}
	}
	
	public MainPanel()
	{
		mouse_x = 0;
		mouse_y = 0;
		
		entity_choice = 0;
		
		init_template_level(30, 20, "base");
		set_prev();
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		
		Dimension pref = new Dimension(3*screen.width/4, 3*screen.height/4);
		this.setPreferredSize(pref);
		
		// mouse listener
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	// template level, 
	// with initial width, height
	// bordered by unbreakable walls
	// sets the level to this new one
	public void init_template_level(int width, int height, String name)
	{
		int[] entities = new int[width * height];
		
		for (int i = 0; i < width; i++)
		{
			entities[i] = 1;
			entities[(height - 1)*width + i] = 1;
		}
		
		for (int j = 0; j < height; j++)
		{
			entities[width*j] = 1;
			entities[width*j + (width - 1)] = 1;
		}
		
		this.level = new Level(width, height, entities, name);
	}
	
	// sets the prev entities to match the current level
	public void set_prev()
	{
		int[] ents = new int[level.width * level.height];
		
		for (int i = 0; i < level.entities.length; i++)
		{
			ents[i] = level.entities[i];
		}
		
		prev_level = new Level(level.width, level.height, ents, level.name);
	}
	
	// method to check if the level has been changed or not
	// false means not changed
	// true means changed.
	public boolean check_changed()
	{
		// widths, heights should be the same
		if (prev_level.width != level.width)
			return true;
		
		if (prev_level.height != level.height)
			return true;
		
		// name should be the same
		if (!prev_level.name.equals(level.name))
			return true;
		
		// the lengths of array should be the same
		if (prev_level.entities.length != level.entities.length)
			return true;
		
		for (int i = 0; i < prev_level.entities.length; i++)
		{
			if (prev_level.entities[i] != level.entities[i])
				return true;
		}
		
		return false;
	}
	
	public Level get_level()
	{
		return level;
	}
	
	public void set_level(Level level)
	{
		this.level = level;
		int ents[] = new int[level.width * level.height];
		
		for (int i = 0; i < level.entities.length; i++)
		{
			ents[i] = level.entities[i];
		}
		
		prev_level = new Level(level.width, level.height, ents, level.name);
	}
	
	// special method to resize the level to new width and height
	// if width/height are smaller, the entities will be cutoff from right-to-left, bottom-to-top
	// if they are bigger empty space will be left
	public void resize_level(int width, int height)
	{
		int[] ents = new int[width*height];

		for (int xx = 0; xx < width; xx++)
		{
			for (int yy = 0; yy < height; yy++)
			{
				// fill with empty
				if (xx >= level.width || yy >= level.height)
					ents[xx + yy*width] = 0;
				else
					ents[xx + yy*width] = level.entities[xx + yy*level.width];
			}
		}
		
		level = new Level(width, height, ents, level.name);
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2D = (Graphics2D)g;

		double width = getWidth();
		double height = getHeight();
		
		draw_level(g2D, width, height);
		draw_mouse(g2D, width, height);
	}
	
	// draws the main level 
	public void draw_level(Graphics2D g, double width, double height)
	{
		double box_width = Math.ceil(width/level.width);
		double box_height = Math.ceil(height/level.height);
		
		// background brown
		g.setColor(new Color(0xff602618));
		g.fillRect(0,0,(int)width,(int)height);
		
		g.setColor(Color.BLACK);
		
		for (int xl = 0; xl < level.width; xl++)
		{
			int x_pos = (int)(xl*box_width);
			g.drawLine(x_pos, 0, x_pos, (int)height);
		}
		
		for (int yl = 0; yl < level.height; yl++)
		{
			int y_pos = (int)(yl*box_height);
			g.drawLine(0, y_pos, (int)width, y_pos);
		}
		
		for (int x = 0; x < level.width; x++)
		{
			for (int y = 0; y < level.height; y++)
			{
				int lev_ent = level.entities[x + level.width*y];
				if (lev_ent != 0)
				{
					int xx = (int)Math.ceil(box_width * x);
					int yy = (int)Math.ceil(box_height * y);
					
					g.drawImage(RES[lev_ent-1], xx, yy, (int)box_width, (int)box_height, null, null);
				}
			}
		}
	}
	
	// draw the current entity selected on mouse location
	public void draw_mouse(Graphics2D g, double width, double height)
	{
		double box_width = Math.ceil(width/level.width);
		double box_height = Math.ceil(height/level.height);
		
		if (entity_choice > 0 && entity_choice < ENTITIES_NUM)
		{
			int xl = (int)Math.ceil(mouse_x - box_width/2);
			int yt = (int)Math.ceil(mouse_y - box_height/2);
			
			g.drawImage(RES[entity_choice - 1], xl, yt, (int)box_width, (int)box_height, null, null);
		}
	}
	
	// since both clicked and dragged have similar functions
	public void handle_click(MouseEvent e)
	{
		mouse_x = e.getX();
		mouse_y = e.getY();
		
		int button_press = e.getButton();
		
		double width = getWidth();
		double height = getHeight();
			
		double box_width = Math.ceil(width/level.width);
		double box_height = Math.ceil(height/level.height);
			
		int ent_x = (int)(mouse_x/box_width);
		int ent_y = (int)(mouse_y/box_height);
		
		// check mouse bounds
		if (ent_x > -1 && ent_x < level.width && ent_y > -1 && ent_y < level.height)
		{
			int index = ent_x + ent_y*level.width;
			
			// left button press
			if (SwingUtilities.isLeftMouseButton(e))
			{	
				if (entity_choice > -1 && entity_choice < ENTITIES_NUM)
				{	
					// need to check if its spawn or exit and remove the last one
					if (entity_choice == 11) // spawn
					{
						if (spawn_loc > -1)
							level.entities[spawn_loc] = 0;
						
						spawn_loc = index;
					}
					else if (entity_choice == 12)
					{
						if (exit_loc > -1)
							level.entities[exit_loc] = 0;
						
						exit_loc = index;
					}
					
					level.entities[index] = entity_choice;
				}
			}
			else // everything else
			{
				if (level.entities[index] == 11) // spawn
					spawn_loc = -1;
				else if (level.entities[index] == 12) // exit
					exit_loc = -1;
				
				level.entities[index] = 0;
			}
		}
		
		repaint();
	}
	
	// mouse motion listener methods
	public void mouseDragged(MouseEvent e)
	{
		handle_click(e);
	}
	
	public void mouseMoved(MouseEvent e)
	{
		mouse_x = e.getX();
		mouse_y = e.getY();
		
		repaint();
	}
	
	// mouse listener methods
	public void mouseClicked(MouseEvent e)
	{
		handle_click(e);
	}
	
	public void mouseEntered(MouseEvent e)
	{
		
	}
	
	public void mouseExited(MouseEvent e)
	{
		
	}
	
	public void mousePressed(MouseEvent e)
	{

	}
	
	public void mouseReleased(MouseEvent e)
	{
	
	}
	
	// actionlistener
	public void actionPerformed(ActionEvent e)
	{
		int res = Integer.parseInt(e.getActionCommand());
		if (res > -1 && res < ENTITIES_NUM)
		{
			entity_choice = res;
		}
	}
	
}
