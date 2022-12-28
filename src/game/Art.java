package src.game;

import src.file.FileManager;

import java.io.File;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Art
{
	private static final String RES_PATH = FileManager.RES_PATH;
	private static final String ENTITIES_PATH = FileManager.RES_PATH + "/entities/";
	private static final String BACKGROUND_PATH = FileManager.RES_PATH + "/backgrounds/";
	
	public static Bitmap player = loadBitmap(ENTITIES_PATH, "player.png");
	public static Bitmap rock = loadBitmap(ENTITIES_PATH, "rock.png");
	public static Bitmap rock2 = loadBitmap(ENTITIES_PATH, "rock2.png");
	public static Bitmap red = loadBitmap(ENTITIES_PATH, "red.png");
	public static Bitmap green = loadBitmap(ENTITIES_PATH, "green.png");
	public static Bitmap yellow = loadBitmap(ENTITIES_PATH, "yellow.png");
	public static Bitmap exit = loadBitmap(ENTITIES_PATH, "exit3.png");
	public static Bitmap club = loadBitmap(ENTITIES_PATH, "club.png");
	
	public static Bitmap boulder = loadBitmap(ENTITIES_PATH, "boulder.png");
	public static Bitmap boulderl = loadBitmap(ENTITIES_PATH, "boulderl.png");
	public static Bitmap boulderu = loadBitmap(ENTITIES_PATH, "boulderu.png");
	public static Bitmap boulderr = loadBitmap(ENTITIES_PATH, "boulderr.png");
	public static Bitmap boulderd = loadBitmap(ENTITIES_PATH, "boulderd.png");
	public static Bitmap boulderlr = loadBitmap(ENTITIES_PATH, "boulderlr.png");
	public static Bitmap boulderud = loadBitmap(ENTITIES_PATH, "boulderud.png");
	public static Bitmap boulderlurd = loadBitmap(ENTITIES_PATH, "boulderlurd.png");
	
	// background
	public static Bitmap rockBackground = loadBitmap(BACKGROUND_PATH, "rockBackground.png");
	
	public static Bitmap toolBar = loadBitmap(BACKGROUND_PATH, "toolbar.png");
	
	public static Bitmap alphabet = loadBitmap(RES_PATH, "alphabet.png");
	
	public static Bitmap loadBitmap(String path, String filename)
	{
		try
		{
			BufferedImage img = ImageIO.read(new File(path + filename));
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