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
	
	public static BufferedImage player = loadImage(ENTITIES_PATH, "player.png");
	public static BufferedImage rock = loadImage(ENTITIES_PATH, "rock.png");
	public static BufferedImage rock2 = loadImage(ENTITIES_PATH, "rock2.png");
	public static BufferedImage red = loadImage(ENTITIES_PATH, "red.png");
	public static BufferedImage green = loadImage(ENTITIES_PATH, "green.png");
	public static BufferedImage yellow = loadImage(ENTITIES_PATH, "yellow.png");
	public static BufferedImage exit = loadImage(ENTITIES_PATH, "exit3.png");
	public static BufferedImage club = loadImage(ENTITIES_PATH, "club.png");
	
	public static BufferedImage boulder = loadImage(ENTITIES_PATH, "boulder.png");
	public static BufferedImage boulderl = loadImage(ENTITIES_PATH, "boulderl.png");
	public static BufferedImage boulderu = loadImage(ENTITIES_PATH, "boulderu.png");
	public static BufferedImage boulderr = loadImage(ENTITIES_PATH, "boulderr.png");
	public static BufferedImage boulderd = loadImage(ENTITIES_PATH, "boulderd.png");
	public static BufferedImage boulderlr = loadImage(ENTITIES_PATH, "boulderlr.png");
	public static BufferedImage boulderud = loadImage(ENTITIES_PATH, "boulderud.png");
	public static BufferedImage boulderlurd = loadImage(ENTITIES_PATH, "boulderlurd.png");
	
	// background
	public static BufferedImage rockBackground = loadImage(BACKGROUND_PATH, "rockBackground.png");
	
	public static BufferedImage toolBar = loadImage(BACKGROUND_PATH, "toolbar.png");
	
	public static Font font = new Font(loadImage(RES_PATH, "alphabet.png"));

	public final static int IgnoreCol = (0x00FF00FF << 8);
	public final static int IgnoreCol2 = (0x00FFFF00 << 8);
	public static void write_image_transparency(BufferedImage img)
	{
		int width = img.getWidth();
		int height = img.getHeight();
		int pixels[] = new int[width * height];
		img.getRGB(0, 0, width, height, pixels, 0, width);
		for (int i = 0; i < pixels.length; i++)
		{
			int shifted = pixels[i] << 8;
			if (shifted == IgnoreCol || shifted == IgnoreCol2)
			{
				pixels[i] = 0x00000000;
			}
		}
		img.setRGB(0, 0, width, height, pixels, 0, width);
	}

	public static BufferedImage loadImage(String path, String filename)
	{
		try
		{
			BufferedImage img = ImageIO.read(new File(path + filename));
			write_image_transparency(img);
			return img;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}