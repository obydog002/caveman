package editor;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

import javax.imageio.ImageIO;

import java.io.File;

// main panel for drawing options on the side 
// and drawing the level itself
public class MainPanel extends JPanel
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
	private int level_width = 32; 
	
	// height of level
	private int level_height = 27;
	
	// array for level entities and resources
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
	
	private static final BufferedImage[] RES = new BufferedImage[16];
	
	static 
	{
		for (int i = 0; i < 16; i++)
		{
			RES[i] = load_resource("res/entities/" + RES_FILES[i]);
		}
	}
	
	public MainPanel()
	{
		level_entities = new int[level_height][level_height];
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		
		Dimension pref = new Dimension(3*screen.width/4, 3*screen.height/4);
		this.setPreferredSize(pref);
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2D = (Graphics2D)g;
		
		
		//draw_options(g2D);
		draw_level(g2D);
	}
	
	// draw the side left panel
	/*public void draw_options(Graphics2D g)
	{
		double width = ratio*getWidth();
		double height = getHeight();
		
		int icon_width = (int)((1 - ratio)*getWidth()/level_width);
		int icon_height = (int)getHeight()/level_height;
		
		
		
		int x_i = 0;
		int y_i = 0;
		int index = 0;
		
		while (index < RES.length)
		{
			if (x_i == icons_per_row)
			{
				x_i = 0;
				y_i++;
			}
			
			int x = gap_width + gaps_per_icon*gap_width*x_i + gap_width*x_i;
			int y = gap_width + gaps_per_icon*gap_width*y_i + gap_width*y_i;
			
			g.drawImage(RES[index],x,y,gaps_per_icon*gap_width,gaps_per_icon*gap_width, null);
			
			x_i++;
			index++;
		}
	}*/
	
	// draws the main level 
	public void draw_level(Graphics2D g)
	{
		double width = getWidth();
		double height = getHeight();
		
		int x = 0;
		
		int x_line_gap = (int)(width/level_width);
		int y_line_gap = (int)(height/level_height);
		
		g.setColor(new Color(0xff602618));
		g.fillRect(x,0,(int)width,(int)height);
		
		g.setColor(Color.BLACK);
		
		for (int xl = 0; xl < level_width; xl++)
		{
			g.drawLine(x + xl*x_line_gap, 0, x + xl*x_line_gap, (int)height);
		}
		
		for (int yl = 0; yl < level_height; yl++)
		{
			g.drawLine(x, yl*y_line_gap, (int)getWidth(), yl*y_line_gap);
		}
	}
}