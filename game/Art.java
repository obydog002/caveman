package game;

import java.io.File;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Art
{
	public static final String PATH = Art.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	
	public static Bitmap player = loadBitmap("res/entities/player.png");
	public static Bitmap rock = loadBitmap("res/entities/rock.png");
	public static Bitmap rock2 = loadBitmap("res/entities/rock2.png");
	public static Bitmap red = loadBitmap("res/entities/red.png");
	public static Bitmap green = loadBitmap("res/entities/green.png");
	public static Bitmap yellow = loadBitmap("res/entities/yellow.png");
	public static Bitmap exit = loadBitmap("res/entities/exit2.png");
	
	public static Bitmap boulder = loadBitmap("res/entities/boulder.png");
	public static Bitmap boulderl = loadBitmap("res/entities/boulderl.png");
	public static Bitmap boulderu = loadBitmap("res/entities/boulderu.png");
	public static Bitmap boulderr = loadBitmap("res/entities/boulderr.png");
	public static Bitmap boulderd = loadBitmap("res/entities/boulderd.png");
	public static Bitmap boulderlr = loadBitmap("res/entities/boulderlr.png");
	public static Bitmap boulderud = loadBitmap("res/entities/boulderud.png");
	public static Bitmap boulderlurd = loadBitmap("res/entities/boulderlurd.png");
	
	public static Bitmap rockBackground = loadBitmap("res/backgrounds/rockBackground.png");
	public static Bitmap toolBar = loadBitmap("res/backgrounds/toolBar.png");
	public static Bitmap club = loadBitmap("res/entities/club.png");
	
	public static Bitmap alphabet = loadBitmap("res/alphabet.png");
	
	public static Bitmap loadBitmap(String filename)
	{
		try
		{
			BufferedImage img = ImageIO.read(new File(PATH + "/" + filename));
			int w = img.getWidth();
			int h = img.getHeight();
			Bitmap b = new Bitmap(w,h);
			img.getRGB(0,0,w,h,b.pixels,0,w);
			return b;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}