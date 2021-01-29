package editor;

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
	
	// width of the level (in entity width)
	private int level_width = game.Main.WIDTH;
	
	// height of level
	// -1 for item hack
	private int level_height = game.Main.HEIGHT - 1;
	
	// array for level entities and resources
	// -1 - nought
	// 0 - unbreakable wall1
	// 1 - unbreakable wall2
	// 2 - boulder
	// 3 - boulder left
	// 4 - boulder up
	// 5 - boulder right
	// 6 - boulder down
	// 7 - boulder left-right
	// 8 - boulder up-down
	// 9 - boulder all
	// 10 - player spawn
	// 11 - exit location
	// 12 - club
	// 13 - green mammoth
	// 14 - yellow mammoth
	// 15 - red mammoth
	private int[][] level_entities;
	
	// for mouse drawing
	private int mouse_x;
	private int mouse_y;
	
	boolean draw_entity_on_mouse = true;
	private int entity_choice;
	
	public static final BufferedImage[] RES = new BufferedImage[16];
	
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
		
		entity_choice = -1;
		
		level_entities = new int[level_width][level_height];
		
		for (int i = 0; i < level_width; i++)
		{
			for (int j = 0; j < level_height; j++)
			{
				level_entities[i][j] = -1;
			}
		}
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		
		Dimension pref = new Dimension(3*screen.width/4, 3*screen.height/4);
		this.setPreferredSize(pref);
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
		double box_width = width/level_width;
		double box_height = height/level_height;
		
		// background brown
		g.setColor(new Color(0xff602618));
		g.fillRect(0,0,(int)width,(int)height);
		
		g.setColor(Color.BLACK);
		
		for (int xl = 0; xl < level_width; xl++)
		{
			int x_pos = (int)(xl*box_width);
			g.drawLine(x_pos, 0, x_pos, (int)height);
		}
		
		for (int yl = 0; yl < level_height; yl++)
		{
			int y_pos = (int)(yl*box_height);
			g.drawLine(0, y_pos, (int)width, y_pos);
		}
		
		for (int x = 0; x < level_width; x++)
		{
			for (int y = 0; y < level_height; y++)
			{
				if (level_entities[x][y] != -1)
				{
					int xx = (int)(box_width * x);
					int yy = (int)(box_height * y);
					
					g.drawImage(RES[level_entities[x][y]], xx, yy, (int)box_width, (int)box_height, null, null);
				}
			}
		}
	}
	
	// draw the current entity selected on mouse location
	public void draw_mouse(Graphics2D g, double width, double height)
	{
		double box_width = width/level_width;
		double box_height = height/level_height;
		
		if (draw_entity_on_mouse && entity_choice > -1)
		{
			int xl = mouse_x - (int)box_width;
			int yt = mouse_y - (int)box_height;
			g.drawImage(RES[entity_choice], xl, yt, (int)box_width, (int)box_height, null, null);
		}
	}
	
	// mouse motion listener methods
	
	public void mouseDragged(MouseEvent e)
	{
		// empty
	}
	
	public void mouseMoved(MouseEvent e)
	{
		mouse_x = e.getX();
		mouse_y = e.getY();
	}
	
	// mouse listener methods
	public void mouseClicked(MouseEvent e)
	{
		
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
		
	}
	
}